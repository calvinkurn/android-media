package com.tokopedia.gm.statistic.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.List;

/**
 * Created by normansyahputa on 1/2/17.
 */

public interface GMStatisticDashboardView extends CustomerView {

    void onSuccessLoadTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph, boolean isGoldMerchant);

    void onErrorLoadTransactionGraph(Throwable t);

    void onSuccessLoadProductGraph(GetProductGraph getProductGraph);

    void onErrorLoadProductGraph(Throwable t);

    void onSuccessLoadPopularProduct(GetPopularProduct getPopularProduct);

    void onErrorLoadPopularProduct(Throwable t);

    void onSuccessLoadBuyerGraph(GetBuyerGraph getBuyerGraph);

    void onErrorLoadBuyerGraph(Throwable t);

    void onGetShopCategoryEmpty(boolean goldMerchant);

    void onSuccessGetCategory(String categoryName);

    void onSuccessGetKeyword(List<GetKeyword> getKeywords, boolean isGoldMerchant);

    void onErrorLoadMarketInsight(Throwable t);

    void onErrorLoadShopInfo(Throwable t);

    void onSuccessLoadShopInfo(boolean isGoldMerchant);
}