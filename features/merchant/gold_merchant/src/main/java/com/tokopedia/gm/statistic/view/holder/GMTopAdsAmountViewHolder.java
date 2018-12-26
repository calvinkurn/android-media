package com.tokopedia.gm.statistic.view.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.card.EmptyCardContentView;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.R;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.db.williamchart.Tools;
import com.db.williamchart.view.LineChartView;
import com.db.williamchart.base.BaseWilliamChartConfig;
import com.db.williamchart.base.BaseWilliamChartModel;
import com.db.williamchart.util.GMStatisticUtil;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.gm.statistic.view.model.GMGraphViewModel;
import com.tokopedia.gm.statistic.view.widget.LineChartContainerWidget;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTopAdsAmountViewHolder implements GMStatisticViewHolder {

    private TitleCardView titleCardView;
    private LineChartView gmStatisticTopAdsGraph;
    private LineChartContainerWidget gmTopAdsLineChartWidget;
    private String[] monthNamesAbrev;
    private OnTopAdsViewHolderListener onTopAdsViewHolderListener;
    public GMTopAdsAmountViewHolder(View view) {
        titleCardView = (TitleCardView) view.findViewById(R.id.topads_statistic_card_view);
        Button buttonTopAds = (Button) titleCardView.findViewById(R.id.button_see_top_ads);
        gmStatisticTopAdsGraph = (LineChartView) view.findViewById(R.id.gm_statistic_topads_graph);
        gmTopAdsLineChartWidget = (LineChartContainerWidget) titleCardView.findViewById(R.id.topads_line_chart_container);
        gmTopAdsLineChartWidget.setTvSubtitleTextColor(R.color.font_black_secondary_54);
        monthNamesAbrev = view.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        buttonTopAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTopAdsViewHolderListener != null) {
                    UnifyTracking.eventClickGMStatManageTopAds(view.getContext());
                    onTopAdsViewHolderListener.onManageTopAdsClicked();
                }
            }
        });
    }

    public void setOnTopAdsViewHolderListener(OnTopAdsViewHolderListener onTopAdsViewHolderListener) {
        this.onTopAdsViewHolderListener = onTopAdsViewHolderListener;
    }

    private void setTopAdsCardView(GMGraphViewModel gmGraphViewModel, boolean isCompareDate) {
        gmTopAdsLineChartWidget.setSubtitle(gmTopAdsLineChartWidget.getContext().getString(R.string.gm_statistic_top_ads_amount_subtitle_text));
        if (isCompareDate) {
            gmTopAdsLineChartWidget.setPercentage(gmGraphViewModel.percentage);
        } else {
            gmTopAdsLineChartWidget.hidePercentageView();
        }
        gmTopAdsLineChartWidget.setAmount(KMNumbers.formatRupiahString(gmGraphViewModel.amount));
    }

    public void bind(@Nullable GMGraphViewModel data, boolean isCompareDate) {
        setTopAdsCardView(data, isCompareDate);
        BaseWilliamChartModel baseWilliamChartModel = GMStatisticUtil.joinDateAndGraph3(data.dates, data.values, monthNamesAbrev);
        // create model for chart
        BaseWilliamChartConfig baseWilliamChartConfig = Tools.getCommonWilliamChartConfig(gmStatisticTopAdsGraph, baseWilliamChartModel);
        baseWilliamChartConfig.buildChart(gmStatisticTopAdsGraph);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    public void bindTopAdsCreditNotUsed(@Nullable GMGraphViewModel data, DataDeposit dataDeposit) {
        EmptyCardContentView emptyCardContentView = (EmptyCardContentView) titleCardView.getEmptyView().findViewById(R.id.empty_card_content_view);
        if (data.amount > 0) {
            emptyCardContentView.setContentText(emptyCardContentView.getContext().getString(R.string.gm_statistic_top_ads_empty_desc_credit_not_used, dataDeposit.getAmountFmt()));
        }
        emptyCardContentView.setActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTopAdsViewHolderListener != null) {
                    onTopAdsViewHolderListener.onManageTopAdsClicked();
                }
            }
        });
        setViewState(LoadingStateView.VIEW_EMPTY);
    }

    @Override
    public void setViewState(int state) {
        titleCardView.setViewState(state);
    }

    public interface OnTopAdsViewHolderListener {

        void onManageTopAdsClicked();
    }
}