package com.tokopedia.shop.score.view.fragment;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.shop.score.view.model.ShopScoreDetailItemViewModel;
import com.tokopedia.shop.score.view.model.ShopScoreDetailStateEnum;
import com.tokopedia.shop.score.view.model.ShopScoreDetailSummaryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 2/24/17.
 */
public interface ShopScoreDetailView extends CustomerView {

    void renderShopScoreDetail(List<ShopScoreDetailItemViewModel> viewModel);

    void renderShopScoreSummary(ShopScoreDetailSummaryViewModel viewModel);

    void renderShopScoreState(ShopScoreDetailStateEnum shopScoreDetailStateEnum);

    void emptyState();

    void showLoading();

    void dismissLoading();

}
