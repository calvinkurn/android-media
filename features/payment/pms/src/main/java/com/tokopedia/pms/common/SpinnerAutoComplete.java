package com.tokopedia.pms.common;

import android.content.Context;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Created by nathan on 4/7/17.
 */

public class SpinnerAutoComplete extends AppCompatAutoCompleteTextView {
    public SpinnerAutoComplete(Context context) {
        super(context);
    }

    public SpinnerAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerAutoComplete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        super.performFiltering("", 0);
    }
}
