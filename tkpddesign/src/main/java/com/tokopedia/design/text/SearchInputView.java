package com.tokopedia.design.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nathan on 04/05/17.
 */

public class SearchInputView extends BaseCustomView {

    private ImageView searchImageView;
    private EditText searchTextView;
    private ImageButton closeImageButton;

    private Drawable searchDrawable;
    private String searchText;
    private String searchHint;

    public SearchInputView(Context context) {
        super(context);
        init();
    }

    public SearchInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SearchInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SpinnerCounterInputView);
        try {
            searchDrawable = styledAttributes.getDrawable(R.styleable.SearchInputView_siv_search_icon);
            searchText = styledAttributes.getString(R.styleable.SearchInputView_siv_search_text);
            searchHint = styledAttributes.getString(R.styleable.SearchInputView_siv_search_hint);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (searchDrawable != null) {
            searchImageView.setImageDrawable(searchDrawable);
        }
        if (!TextUtils.isEmpty(searchText)) {
            searchTextView.setText(searchText);
        }
        if (!TextUtils.isEmpty(searchHint)) {
            searchTextView.setHint(searchHint);
        }
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_search_input_view, this);
        searchImageView = (ImageView) view.findViewById(R.id.image_view_search);
        searchTextView = (EditText) view.findViewById(R.id.edit_text_search);
        closeImageButton = (ImageButton) view.findViewById(R.id.image_button_close);
    }

    @Override
    public void setEnabled(boolean enabled) {
        searchTextView.setEnabled(enabled);
        closeImageButton.setEnabled(enabled);
    }
}