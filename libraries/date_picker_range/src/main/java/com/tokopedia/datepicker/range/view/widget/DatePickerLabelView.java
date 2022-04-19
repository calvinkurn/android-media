package com.tokopedia.datepicker.range.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.datepicker.range.R;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class DatePickerLabelView extends FrameLayout {

    private TextView titleTextView;
    private TextView contentTextView;
    private View contentLayout;
    private String titleText;
    private String contentText;
    private int colorValue;

    public DatePickerLabelView(Context context) {
        super(context);
        init();
    }

    public DatePickerLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DatePickerLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DatePickerLabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.DatePickerLabelView_date_picker_title);
            contentText = styledAttributes.getString(R.styleable.DatePickerLabelView_date_picker_content);
            colorValue = styledAttributes.getColor(R.styleable.DatePickerLabelView_date_picker_content_color, ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(titleText);
        contentTextView.setText(contentText);
        contentTextView.setTextColor(colorValue);
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_date_picker_custom, this);
        titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        contentTextView = (TextView) view.findViewById(R.id.text_view_content);
        contentLayout = view.findViewById(R.id.layout_content);
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setContent(String textValue) {
        contentTextView.setText(textValue);
        invalidate();
        requestLayout();
    }

    public void setOnContentClick(OnClickListener onClickListener) {
        contentLayout.setOnClickListener(onClickListener);
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public String getValue() {
        return contentTextView.getText().toString();
    }
}