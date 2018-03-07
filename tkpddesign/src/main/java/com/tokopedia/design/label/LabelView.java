package com.tokopedia.design.label;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class LabelView extends BaseCustomView {

    private TextView titleTextView;
    private TextView contentTextView;
    private ImageView arrow;

    private String titleText;
    @ColorInt
    private int titleColorValue;
    private int contentTextStyleValue;

    private String contentText;
    @ColorInt
    private int contentColorValue;
    private int titleTextStyleValue;

    private boolean showArrow;
    private int maxLines;
    private float textSize;
    private float titleTextSize;
    private float contentTextSize;
    private int minTitleWidth;

    private boolean enabled;

    public LabelView(Context context) {
        super(context);
        init();
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.LabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.LabelView_lv_title);
            titleColorValue = styledAttributes.getColor(R.styleable.LabelView_lv_title_color, ContextCompat.getColor(getContext(), R.color.font_black_primary_70));
            contentText = styledAttributes.getString(R.styleable.LabelView_lv_content);
            contentColorValue = styledAttributes.getColor(R.styleable.LabelView_lv_content_color, ContextCompat.getColor(getContext(), R.color.font_black_secondary_54));
            contentTextStyleValue = styledAttributes.getInt(R.styleable.LabelView_lv_content_text_style, Typeface.NORMAL);
            titleTextStyleValue = styledAttributes.getInt(R.styleable.LabelView_lv_title_text_style, Typeface.NORMAL);
            showArrow = styledAttributes.getBoolean(R.styleable.LabelView_lv_show_arrow, false);
            maxLines = styledAttributes.getInt(R.styleable.LabelView_lv_content_max_lines, 1);
            textSize = styledAttributes.getDimension(R.styleable.LabelView_lv_font_size,
                    getResources().getDimension(com.tokopedia.design.R.dimen.font_title));
            titleTextSize = styledAttributes.getDimension(R.styleable.LabelView_lv_title_text_size, textSize);
            minTitleWidth = styledAttributes.getDimensionPixelSize(R.styleable.LabelView_lv_title_min_width, 0);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_label_view, this);
        titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        contentTextView = (TextView) view.findViewById(R.id.content_text_view);
        arrow = (ImageView) view.findViewById(R.id.arrow_left);

        titleTextView.setText(titleText);
        titleTextView.setTypeface(null, titleTextStyleValue);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        titleTextView.setMinWidth(minTitleWidth);

        setContent(contentText);
        contentTextView.setTextColor(contentColorValue);
        contentTextView.setTypeface(null, contentTextStyleValue);
        contentTextView.setMaxLines(maxLines);
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        setVisibleArrow(showArrow);

        setEnabled(super.isEnabled());
    }

    public void resetContentText() {
        setContent(contentText);
        setVisibleArrow(showArrow);
        invalidate();
        requestLayout();
    }

    public boolean isContentDefault(){
        return contentTextView.getText().equals(contentText);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setClickable(enabled);
        if (enabled) {
            titleTextView.setTextColor(titleColorValue);
            contentTextView.setTextColor(contentColorValue);
            arrow.clearColorFilter();
        } else {
            titleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.font_black_disabled_38));
            contentTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.font_black_disabled_38));
            arrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_400),
                    PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setTitleContentTypeFace(int typeFace) {
        titleTextView.setTypeface(null, typeFace);
        invalidate();
        requestLayout();
    }

    public void setContent(CharSequence textValue) {
        contentTextView.setText(textValue);
        invalidate();
        requestLayout();
    }

    public String getContent() {
        return contentTextView.getText().toString();
    }

    public void setContentTypeface(int typefaceType) {
        contentTextView.setTypeface(null, typefaceType);
        invalidate();
        requestLayout();
    }

    public void setContentColorValue(@ColorInt int colorValue) {
        contentColorValue = colorValue;
        contentTextView.setTextColor(colorValue);
        invalidate();
        requestLayout();
    }

    public void setVisibleArrow(boolean isVisible) {
        if (isVisible) {
            arrow.setVisibility(VISIBLE);
        } else {
            arrow.setVisibility(GONE);
        }
    }
}