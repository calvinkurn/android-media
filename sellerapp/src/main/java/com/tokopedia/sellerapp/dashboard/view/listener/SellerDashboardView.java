package com.tokopedia.sellerapp.dashboard.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus;
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult;
import com.tokopedia.sellerapp.dashboard.model.ShopInfoDashboardModel;
import com.tokopedia.user_identification_common.subscriber.GetApprovalStatusSubscriber;

/**
 * Created by hendry on 9/8/2017.
 */

public interface SellerDashboardView extends CustomerView {

    void onSuccessGetShopInfoAndScore(ShopInfoDashboardModel shopInfoDashboardModel,
                                      GoldGetPmOsStatus goldGetPmOsStatus,
                                      ShopScoreResult shopScoreResult);

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
