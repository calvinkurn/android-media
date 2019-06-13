package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

import java.util.Date;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public interface TopAdsDetailPresenter extends RetrofitPresenter {
    void refreshAd(Date startDate, Date endDate, String id);

    void checkAutoAds();
}
