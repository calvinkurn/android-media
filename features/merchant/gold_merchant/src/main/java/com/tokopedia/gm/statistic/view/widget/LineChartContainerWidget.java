package com.tokopedia.gm.statistic.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.gm.R;
import com.tokopedia.gm.statistic.view.model.GMGraphViewWithPreviousModel;

/**
 * Created by hendry on 7/10/2017.
 */

public class LineChartContainerWidget extends LinearLayout {

    private TextView tvAmount;
    private ArrowPercentageView arrowPercentageView;
    private TextView tvSubtitle;
    private TextView tvPercentageDesc;
    private GMDateRangeView gmStatisticTransactionRangeMain;
    private GMDateRangeView gmStatisticTransactionRangeCompare;

    private ViewGroup chartInnerContainer;
    private CharSequence subtitle;

    public LineChartContainerWidget(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public LineChartContainerWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public LineChartContainerWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LineChartContainerWidget);
        subtitle = a.getString(R.styleable.LineChartContainerWidget_subtitle);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_line_chart_container, this);

        tvAmount = (TextView) view.findViewById(R.id.tv_amount);
        arrowPercentageView = (ArrowPercentageView) view.findViewById(R.id.view_arrow_percentage);
        tvSubtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        tvPercentageDesc = (TextView) view.findViewById(R.id.tv_percentage_description);

        View vgDateRange = view.findViewById(R.id.vg_date_range);
        gmStatisticTransactionRangeMain = (GMDateRangeView) vgDateRange.findViewById(R.id.gm_statistic_transaction_range_main);
        gmStatisticTransactionRangeCompare = (GMDateRangeView) vgDateRange.findViewById(R.id.gm_statistic_transaction_range_compare);
        gmStatisticTransactionRangeCompare.setDrawable(R.drawable.circle_grey);

        setSubtitle(subtitle);
        tvAmount.setVisibility(View.GONE);
        arrowPercentageView.setVisibility(View.GONE);
        tvPercentageDesc.setVisibility(View.GONE);

        chartInnerContainer = (ViewGroup) view.findViewById(R.id.gm_statistic_transaction_graph_container_inner);

        setAddStatesFromChildren(true);
    }

    public void setTvSubtitleTextColor(@ColorRes int colorRes) {
        tvSubtitle.setTextColor(ContextCompat.getColor(getContext(), colorRes));
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.line_chart_container) {
            // Carry on adding the View...
            super.addView(child, index, params);
        } else {
            // remove the link between child and previous parent before add (if any)
            if (child.getParent()!= null) {
                ViewGroup viewParent = (ViewGroup) child.getParent();
                viewParent.removeView(child);
            }
            chartInnerContainer.addView(child, params);
        }
    }

    public void setMainDate(@Nullable GMGraphViewWithPreviousModel data) {
        if (data == null) {
            gmStatisticTransactionRangeMain.setVisibility(View.GONE);
        } else {
            gmStatisticTransactionRangeMain.bind(data.dateRangeModel);
            gmStatisticTransactionRangeMain.setVisibility(View.VISIBLE);
        }
    }

    public void setCompareDate(@Nullable GMGraphViewWithPreviousModel data) {
        if (data == null) {
            gmStatisticTransactionRangeCompare.setVisibility(View.GONE);
        } else {
            gmStatisticTransactionRangeCompare.bind(data.pDateRangeModel);
            gmStatisticTransactionRangeCompare.setVisibility(View.VISIBLE);
        }
    }

    public void setAmount(CharSequence formattedAmount) {
        if (TextUtils.isEmpty(formattedAmount)) {
            tvAmount.setVisibility(View.GONE);
        } else {
            tvAmount.setText(formattedAmount);
            tvAmount.setVisibility(View.VISIBLE);
        }
    }

    // set percentage 1 for 100%
    public void setPercentage(Double percentage) {
        arrowPercentageView.setPercentage(percentage);
        arrowPercentageView.setVisibility(View.VISIBLE);
    }

    public void hidePercentageView(){
        arrowPercentageView.setVisibility(View.GONE);
    }

    public void setSubtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
        if (TextUtils.isEmpty(subtitle)) {
            tvSubtitle.setVisibility(View.GONE);
        } else {
            tvSubtitle.setText(subtitle);
            tvSubtitle.setVisibility(View.VISIBLE);
        }
    }

    public void setPercentageDesc(CharSequence percentageDesc) {
        if (TextUtils.isEmpty(percentageDesc)) {
            tvPercentageDesc.setVisibility(View.GONE);
        } else {
            tvPercentageDesc.setText(percentageDesc);
            tvPercentageDesc.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}
