package com.tokopedia.design.text;

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
 * Created by nathan on 04/05/17.
 */

public class TitleGroupView extends BaseCustomView {

    private TextView titleTextView;
    private ImageView iconImageView;

    private String titleText;
    private Drawable iconDrawable;

    public TitleGroupView(Context context) {
        super(context);
        init();
    }

    public TitleGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TitleGroupView);
        try {
            titleText = styledAttributes.getString(R.styleable.TitleGroupView_tgv_title);
            iconDrawable = styledAttributes.getDrawable(R.styleable.TitleGroupView_tgv_icon);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_title_group, this);
        titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        iconImageView = (ImageView) view.findViewById(R.id.image_view_icon);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(titleText)) {
            titleTextView.setText(titleText);
        }
        if (iconDrawable == null) {
            iconImageView.setVisibility(View.GONE);
        } else {
            iconImageView.setVisibility(View.VISIBLE);
            iconImageView.setImageDrawable(iconDrawable);
        }
        invalidate();
        requestLayout();
    }

    public void setTitle(String titleText) {
        this.titleText = titleText;
        invalidate();
        requestLayout();
    }
}