package com.dahl.brendan.wordsearch.view.controller;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dahl.brendan.wordsearch.model.Grid;
import com.dahl.brendan.wordsearch.model.Theme;
import com.dahl.brendan.wordsearch.view.R;
import com.dahl.brendan.wordsearch.view.runnables.UpdateLetterBoxLand;
import com.dahl.brendan.wordsearch.view.runnables.UpdateWordList;

public class WordBoxControllerLand extends ArrayAdapter<String> implements WordBoxControllerI {
	private final Set<String> wordsFound = new HashSet<String>();
	private final ListView wordListView;
	private Theme theme;
	private final TextView letterBox;
	public WordBoxControllerLand(Context context, ListView wordList, TextView letterBox) {
		super(context, R.layout.wordlist_text_view);
		this.wordListView = wordList;
		this.wordListView.setClickable(false);
		this.wordListView.setEnabled(false);
		this.wordListView.setAdapter(this);
		this.letterBox = letterBox;
	}

	public void setLetter(CharSequence charSequence) {
		this.wordListView.post(new UpdateLetterBoxLand(charSequence, letterBox));
	}
	
	public int wordFound(String str) {
		this.wordsFound.add(str);
		this.wordListView.post(new UpdateWordList(this, str, null));
		return wordsLeft();
	}

	public void resetWords(Grid grid) {
		this.wordsFound.clear();
		this.wordsFound.addAll(grid.getWordFound());
		this.wordListView.post(new UpdateWordList(this, null, grid));
	}

	public int wordsLeft() {
		return this.getCount()-this.wordsFound.size();
	}

	public void updateTheme(Theme theme) {
		this.theme = theme;
		this.letterBox.setTextColor(theme.picked);
		this.wordListView.post(new UpdateWordList(this, null, null));
	}

	public long getItemId(int position) {
		long id = this.getItem(position).hashCode();
		if (this.wordsFound.contains(getItem(position))) {
			id++;
		}
		if (theme != null) {
			id += theme.hashCode();
		}
		return id;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView)super.getView(position, convertView, parent);
		if (this.wordsFound.contains(this.getItem(position))) {
			view.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			view.setPaintFlags(0);
		}
		if (theme != null) {
			view.setTextColor(theme.normal);
		}
		return view;
	}

}
