package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.data.model.data.GroupAd;

/**
 * Created by zulfikarrahman on 1/3/17.
 */
public interface TopAdsDetailGroupPresenter extends TopAdsDetailViewPresenter {
    void getDetailGroup(String adId, final GroupAd groupAd);
}
