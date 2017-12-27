package com.tokopedia.design.label;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.utils.DateLabelUtils;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class DateLabelView extends BaseCustomView {

    private TextView dateTextView;
    private TextView comparedDateTextView;
    private ColorStateList defaultTextColor;
    private ColorStateList defaultCompareTextColor;
    private int grayColor;

    public DateLabelView(Context context) {
        super(context);
        init();
    }

    public DateLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DateLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_date_label_view, this);
        dateTextView = (TextView) view.findViewById(R.id.text_view_date);
        comparedDateTextView = (TextView) view.findViewById(R.id.text_view_compared_date);
        defaultTextColor = dateTextView.getTextColors();
        defaultCompareTextColor = comparedDateTextView.getTextColors();
        grayColor = ContextCompat.getColor(getContext(), R.color.grey_400);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    public void setDate(Date startDate, Date endDate) {
        setDate(startDate.getTime(), endDate.getTime());
    }

    public void setDate(long startDate, long endDate) {
        dateTextView.setText(DateLabelUtils.getRangeDateFormatted(dateTextView.getContext(), startDate, endDate));
        invalidate();
        requestLayout();
    }

    public void setComparedDate(long startDate, long endDate) {
        comparedDateTextView.setText(DateLabelUtils.getRangeDateFormatted(dateTextView.getContext(), startDate, endDate));
        invalidate();
        requestLayout();
    }

    public void setComparedDateVisibility(int visibility) {
        comparedDateTextView.setVisibility(visibility);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            dateTextView.setTextColor(defaultTextColor);
            comparedDateTextView.setTextColor(defaultCompareTextColor);
        } else {
            dateTextView.setTextColor(grayColor);
            comparedDateTextView.setTextColor(grayColor);
        }
    }
}