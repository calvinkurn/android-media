package com.tokopedia.sellerapp.dashboard.view.listener;

import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreViewModel;
import com.tokopedia.user_identification_common.subscriber.GetApprovalStatusSubscriber;

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

    void showLoading();

    void hideLoading();

    void onSuccessOpenShop();

    void onErrorOpenShop();

    Context getContext();

    GetApprovalStatusSubscriber.GetApprovalStatusListener getApprovalStatusListener();
}
