package com.tokopedia.gm.statistic.view.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.gm.statistic.view.activity.GMStatisticTransactionActivity;
import com.tokopedia.gm.statistic.view.model.GMGraphViewWithPreviousModel;
import com.tokopedia.gm.statistic.view.widget.ArrowPercentageView;
import com.tokopedia.gm.statistic.view.widget.config.DataTransactionChartConfig;
import com.tokopedia.gm.statistic.view.widget.config.DataTransactionDataSetConfig;
import com.tokopedia.gm.statistic.view.widget.config.EmptyDataTransactionDataSetConfig;
import com.tokopedia.gm.R;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.db.williamchart.base.BaseWilliamChartConfig;
import com.db.williamchart.base.BaseWilliamChartModel;
import com.db.williamchart.util.GMStatisticUtil;
import com.db.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created on 11/10/16.
 *
 * @author normansyahputa
 */

public class GMStatisticTransactionViewHolder implements GMStatisticViewHolder {

    public interface Listener {
        void onViewNotGmClicked();
    }

    private LineChartView transactionChart;

    private TitleCardView transactionDataCardView;
    private TextView tvTransactionCount;
    private ArrowPercentageView arrowPercentageView;
    private View seeDetailView;
    View viewNotGM;

    private String[] monthNamesAbrev;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public GMStatisticTransactionViewHolder(View view) {
        transactionDataCardView = (TitleCardView) view.findViewById(R.id.transaction_data_card_view);
        viewNotGM = transactionDataCardView.findViewById(R.id.transaction_data_container_non_gold_merchant);
        viewNotGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventClickGMStatBuyGMDetailTransaction(view.getContext());
                if (listener != null) {
                    listener.onViewNotGmClicked();
                }
            }
        });
        transactionChart = (LineChartView) transactionDataCardView.findViewById(R.id.transaction_chart);
        tvTransactionCount = (TextView) transactionDataCardView.findViewById(R.id.tv_transaction_count);
        arrowPercentageView = (ArrowPercentageView) transactionDataCardView.findViewById(R.id.view_arrow_percentage);
        seeDetailView = transactionDataCardView.findViewById(R.id.see_detail_container);
        seeDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = transactionDataCardView.getContext();
                // force to move to new statistic
                Intent intent = new Intent(context, GMStatisticTransactionActivity.class);
                context.startActivity(intent);

                UnifyTracking.eventClickGMStatSeeDetailTransaction(view.getContext());
            }
        });
        monthNamesAbrev = transactionDataCardView.getContext().getResources()
                .getStringArray(R.array.lib_date_picker_month_entries);

        TextView titleUpgradeGM = viewNotGM.findViewById(R.id.market_insight_gmsubscribe_text);
        titleUpgradeGM.setText(view.getContext().getString(R.string.gm_statistic_upgrade_to_gold_merchant,
                view.getContext().getString(GMConstant.getGMTitleResource(view.getContext()))));
    }

    public void bindData(GMGraphViewWithPreviousModel totalTransactionModel, boolean isGoldMerchant) {
        if (!isGoldMerchant) {
            setViewNoGM();
            setEmptyStatePercentage();
            seeDetailView.setClickable(false);
            return;
        }

        viewNotGM.setVisibility(View.GONE);

        /* empty state */
        if (totalTransactionModel.values == null || totalTransactionModel.amount == 0) {

            setEmptyStatePercentage();

            tvTransactionCount.setText(KMNumbers.getSummaryString(0));

            displayGraphic(totalTransactionModel.values, totalTransactionModel.dates, true);
            seeDetailView.setVisibility(View.GONE);
            setViewState(LoadingStateView.VIEW_CONTENT);
            return;
        }

        setViewState(LoadingStateView.VIEW_CONTENT);

        seeDetailView.setClickable(true);
        seeDetailView.setVisibility(View.VISIBLE);

        /* non empty state */
        tvTransactionCount.setText(KMNumbers.getSummaryString(totalTransactionModel.amount));

        Double diffSuccessTrans = totalTransactionModel.percentage;
        arrowPercentageView.setPercentage(diffSuccessTrans);

        displayGraphic(totalTransactionModel.values, totalTransactionModel.dates, false);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    private void setViewNoGM() {
        transactionDataCardView.setViewState(LoadingStateView.VIEW_CONTENT);
        viewNotGM.setVisibility(View.VISIBLE);
    }

    private void setEmptyStatePercentage() {
        arrowPercentageView.setNoDataPercentage();
    }

    private void displayGraphic(List<Integer> data, List<Integer> dateGraph, boolean emptyState) {
        BaseWilliamChartModel baseWilliamChartModel
                = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);
        BaseWilliamChartConfig baseWilliamChartConfig = new BaseWilliamChartConfig();
        baseWilliamChartConfig.setBasicGraphConfiguration(new DataTransactionChartConfig(transactionChart.getContext()));
        if (emptyState) {
            baseWilliamChartConfig.addBaseWilliamChartModels(
                    baseWilliamChartModel, new EmptyDataTransactionDataSetConfig());
        } else {
            baseWilliamChartConfig.addBaseWilliamChartModels(
                    baseWilliamChartModel, new DataTransactionDataSetConfig());
        }
        baseWilliamChartConfig.buildChart(transactionChart);
    }

    @Override
    public void setViewState(int viewState) {
        transactionDataCardView.setViewState(viewState);
    }
}
