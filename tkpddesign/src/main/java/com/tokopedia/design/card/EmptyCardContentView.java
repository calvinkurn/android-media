package com.tokopedia.design.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nathan on 8/1/17.
 */

public class EmptyCardContentView extends BaseCustomView {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView descTextView;
    private TextView contentTextView;
    private TextView actionTextView;

    private Drawable defaultIcon;
    private String defaultTitleText;
    private String defaultDescText;
    private String defaultContentText;
    private String defaultActionText;


    public EmptyCardContentView(Context context) {
        super(context);
        init();
    }

    public EmptyCardContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmptyCardContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.EmptyCardContentView);
        try {
            defaultIcon = styledAttributes.getDrawable(R.styleable.EmptyCardContentView_eccv_icon);
            defaultTitleText = styledAttributes.getString(R.styleable.EmptyCardContentView_eccv_title_text);
            defaultDescText = styledAttributes.getString(R.styleable.EmptyCardContentView_eccv_desc_text);
            defaultContentText = styledAttributes.getString(R.styleable.EmptyCardContentView_eccv_content_text);
            defaultActionText = styledAttributes.getString(R.styleable.EmptyCardContentView_eccv_action_text);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_empty_card_content, this);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        descTextView = (TextView) view.findViewById(R.id.text_view_description);
        contentTextView = (TextView) view.findViewById(R.id.text_view_content);
        actionTextView = (TextView) view.findViewById(R.id.text_view_action);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setIconDrawable(defaultIcon);
        setTitleText(defaultTitleText);
        setDescriptionText(defaultDescText);
        setContentText(defaultContentText);
        setActionText(defaultActionText);
        invalidate();
        requestLayout();
    }

    public void setIconDrawable(Drawable icon) {
        if (icon == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageDrawable(icon);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleText(String text) {
        if (!TextUtils.isEmpty(text)) {
            titleTextView.setText(text);
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.GONE);
        }
    }

    public void setDescriptionText(String text) {
        if (!TextUtils.isEmpty(text)) {
            descTextView.setText(text);
            descTextView.setVisibility(View.VISIBLE);
        } else {
            descTextView.setVisibility(View.GONE);
        }
    }

    public void setContentText(String text) {
        if (!TextUtils.isEmpty(text)) {
            contentTextView.setText(text);
            contentTextView.setVisibility(View.VISIBLE);
        } else {
            contentTextView.setVisibility(View.GONE);
        }
    }

    public void setActionText(String text) {
        if (!TextUtils.isEmpty(text)) {
            actionTextView.setText(text);
            actionTextView.setVisibility(View.VISIBLE);
        } else {
            actionTextView.setVisibility(View.GONE);
        }
    }

    public void setActionClickListener(OnClickListener onClickListener) {
        actionTextView.setOnClickListener(onClickListener);
    }
}