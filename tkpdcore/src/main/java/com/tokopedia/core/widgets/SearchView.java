package com.tokopedia.core.widgets;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author erry on 24/02/17.
 */

public class SearchView extends androidx.appcompat.widget.SearchView  {

    public SearchView(Context context) {
        super(context);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LinearLayout searchPlate = (LinearLayout) findViewById(com.google.android.material.R.id.search_plate);
        if(searchPlate != null){
            EditText mSearchEditText = (EditText)searchPlate.findViewById(com.google.android.material.R.id.search_src_text);
            if(mSearchEditText != null){
                mSearchEditText.setBackgroundResource(android.R.color.transparent);     // This fixes the keyboard from popping up each time
            }
        }
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.background_dark));
    }
}
