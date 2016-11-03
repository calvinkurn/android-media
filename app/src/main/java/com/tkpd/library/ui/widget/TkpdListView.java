package com.tkpd.library.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class TkpdListView extends ListView {

	public TkpdListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	 @Override
	 protected void layoutChildren() {
	     try {
	         super.layoutChildren();
	     } catch (IllegalStateException e) {
	         //ZLog.e(LOG, "This is not realy dangerous problem");
	     }
	 }


}
