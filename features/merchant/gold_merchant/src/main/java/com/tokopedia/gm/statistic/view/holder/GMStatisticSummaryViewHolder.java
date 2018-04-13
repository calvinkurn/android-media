package com.tokopedia.gm.statistic.view.holder;

import android.view.View;

import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.R;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.view.widget.GMStatisticSummaryView;

/**
 * Created by nathan on 7/24/17.
 */

public class GMStatisticSummaryViewHolder implements GMStatisticViewHolder{

    private LoadingStateView successTransactionLoadingStateView;
    private LoadingStateView conversionLoadingStateView;
    private LoadingStateView productSeenLoadingStateView;
    private LoadingStateView productSoldLoadingStateView;
    private GMStatisticSummaryView successTransactionSummaryView;
    private GMStatisticSummaryView conversionSummaryView;
    private GMStatisticSummaryView productSeenSummaryView;
    private GMStatisticSummaryView productSoldSummaryView;
    private String errorValue;

    public GMStatisticSummaryViewHolder(View view) {
        errorValue = view.getContext().getString(R.string.label_empty_value);
        successTransactionLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_success_transaction);
        conversionLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_conversion);
        productSeenLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_product_seen);
        productSoldLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_product_sold);
        successTransactionSummaryView = (GMStatisticSummaryView) view.findViewById(R.id.summary_view_success_transaction);
        conversionSummaryView = (GMStatisticSummaryView) view.findViewById(R.id.summary_view_conversion);
        productSeenSummaryView = (GMStatisticSummaryView) view.findViewById(R.id.summary_view_product_seen);
        productSoldSummaryView = (GMStatisticSummaryView) view.findViewById(R.id.summary_view_product_sold);
    }

    public void setData(GetProductGraph getProductGraph) {
        successTransactionSummaryView.setContentText(KMNumbers.getSummaryString(getProductGraph.getSuccessTrans()));
        successTransactionSummaryView.setPercentage(getProductGraph.getDiffTrans());
        conversionSummaryView.setContentText(KMNumbers.formatToPercentString(getProductGraph.getConversionRate()));
        conversionSummaryView.setPercentage(getProductGraph.getDiffConv());
        productSeenSummaryView.setContentText(KMNumbers.getSummaryString(getProductGraph.getProductView()));
        productSeenSummaryView.setPercentage(getProductGraph.getDiffView());
        productSoldSummaryView.setContentText(KMNumbers.getSummaryString(getProductGraph.getProductSold()));
        productSoldSummaryView.setPercentage(getProductGraph.getDiffSold());
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    @Override
    public void setViewState(int state) {
        if (state == LoadingStateView.VIEW_LOADING) {
            successTransactionLoadingStateView.setViewState(state);
            conversionLoadingStateView.setViewState(state);
            productSeenLoadingStateView.setViewState(state);
            productSoldLoadingStateView.setViewState(state);
            return;
        }
        successTransactionLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        conversionLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        productSeenLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        productSoldLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        switch (state) {
            case LoadingStateView.VIEW_ERROR:
            case LoadingStateView.VIEW_EMPTY:
                successTransactionSummaryView.setContentText(errorValue);
                successTransactionSummaryView.setNoDataPercentage();
                conversionSummaryView.setContentText(errorValue);
                conversionSummaryView.setNoDataPercentage();
                productSeenSummaryView.setContentText(errorValue);
                productSeenSummaryView.setNoDataPercentage();
                productSoldSummaryView.setContentText(errorValue);
                productSoldSummaryView.setNoDataPercentage();
        }
    }
}