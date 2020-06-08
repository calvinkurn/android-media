package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsEtalaseListView;

/**
 * Created by hendry on 2/27/17.
 */
public interface TopAdsEtalaseListPresenter extends CustomerPresenter<TopAdsEtalaseListView> {

    void populateEtalaseList(String shopId, String userId, String deviceId);
}
