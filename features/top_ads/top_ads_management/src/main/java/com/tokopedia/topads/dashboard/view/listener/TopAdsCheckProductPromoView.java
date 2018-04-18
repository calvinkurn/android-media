package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by nakama on 17/04/18.
 */

public interface TopAdsCheckProductPromoView extends CustomerView {
    void moveToCreateAds();

    void moveToAdsDetail(String adsId);

    void showLoadingProgress();

    void finishLoadingProgress();

    void renderErrorView(String message);

    void renderRetryRefresh();
}
