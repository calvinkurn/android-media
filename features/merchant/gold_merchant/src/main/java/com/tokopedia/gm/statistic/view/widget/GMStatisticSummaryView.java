package com.tokopedia.gm.statistic.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.gm.R;
import com.tokopedia.gm.statistic.constant.GMStatConstant;

/**
 * Created by nathan on 7/24/17.
 */

public class GMStatisticSummaryView extends BaseCustomView {

    private TextView titleTextView;
    private TextView contentTextView;
    private ArrowPercentageView arrowPercentageView;

    private String titleText;
    private String contentText;
    private double percentage;

    public GMStatisticSummaryView(Context context) {
        super(context);
        init();
    }

    public GMStatisticSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GMStatisticSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GMStatisticSummaryView);
        try {
            titleText = styledAttributes.getString(R.styleable.GMStatisticSummaryView_gm_statistic_summary_title);
            contentText = styledAttributes.getString(R.styleable.GMStatisticSummaryView_gm_statistic_summary_content);
            percentage = styledAttributes.getFloat(R.styleable.GMStatisticSummaryView_gm_statistic_summary_percentage, 0);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(titleText)) {
            titleTextView.setText(titleText);
        }
        if (!TextUtils.isEmpty(contentText)) {
            contentTextView.setText(contentText);
        }
        arrowPercentageView.setPercentage(0);
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_gm_statistic_summary, this);
        titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        contentTextView = (TextView) view.findViewById(R.id.text_view_content);
        arrowPercentageView = (ArrowPercentageView) view.findViewById(R.id.arrow_percentage_view);
    }

    public void setTitleText(String titleText) {
        titleTextView.setText(titleText);
    }

    public void setContentText(String contentText) {
        contentTextView.setText(contentText);
    }

    public void setPercentage(double percentage) {
        arrowPercentageView.setPercentage(percentage);
    }

    public void setNoDataPercentage(){
        arrowPercentageView.setNoDataPercentage();
    }
}