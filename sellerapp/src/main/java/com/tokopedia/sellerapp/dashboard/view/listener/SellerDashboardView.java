package com.tokopedia.sellerapp.dashboard.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.shopinfo.models.shopmodel.Info;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopTxStats;
import com.tokopedia.seller.home.view.ReputationView;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;

/**
 * Created by hendry on 9/8/2017.
 */

public interface SellerDashboardView extends CustomerView {

    void onSuccessGetShopInfoAndScore(ShopModel shopModel,
                                      ShopScoreViewModel shopScoreViewModel);

    void onErrorShopInfoAndScore(Throwable t);

    void onErrorGetTickers(Throwable throwable);

    void onSuccessGetTickers(Ticker.Tickers[] tickers);

    void onErrorGetNotifiction(String message);

    void onSuccessGetNotification(DrawerNotification drawerNotification);
}
