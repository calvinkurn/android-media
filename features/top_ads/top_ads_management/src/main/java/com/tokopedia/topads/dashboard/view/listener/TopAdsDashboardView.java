package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.topads.common.data.model.DataDeposit;

/**
 * Created by hadi.putra on 23/04/18.
 */

public interface TopAdsDashboardView extends CustomerView {
    void onLoadTopAdsShopDepositError(Throwable throwable);

    void onLoadTopAdsShopDepositSuccess(DataDeposit dataDeposit);

    void onErrorGetShopInfo(Throwable throwable);

    void onSuccessGetShopInfo(ShopInfo shopInfo);
}
