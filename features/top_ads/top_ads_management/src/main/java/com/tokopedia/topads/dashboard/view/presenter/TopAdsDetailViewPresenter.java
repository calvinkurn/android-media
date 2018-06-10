package com.tokopedia.topads.dashboard.view.presenter;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public interface TopAdsDetailViewPresenter extends TopAdsDetailPresenter {

    void turnOnAds(String id);

    void turnOffAds(String id);

    void deleteAd(String id);
}
