package com.tokopedia.sellerapp.dashboard.view;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.shopinfo.models.shopmodel.Info;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopTxStats;
import com.tokopedia.seller.home.view.ReputationView;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;

/**
 * Created by hendry on 9/8/2017.
 */

public interface SellerDashboardView extends CustomerView {
    void onErrorGetShopInfo(Throwable e);
    void onSuccessGetShopInfo (Info shopModelInfo);
    void onSuccessGetShopOpenInfo (boolean isOpen);
    void onSuccessGetShopTransaction (ShopTxStats shopTxStats);
    void onSuccessGetReputation (ReputationView.ReputationViewModel reputationViewModel);
    void renderShopScore(ShopScoreViewModel shopScoreViewModel);
    void onErrorShopScore();
}
