package com.tokopedia.topads.group.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;

import java.util.List;

/**
 * Created by hadi.putra on 09/05/18.
 */

public interface TopAdsGroupAdListView extends CustomerView {
    void showListError(Throwable throwable);

    void onSearchLoaded(List<GroupAd> groupAds, boolean hasNextData);

    void onBulkActionError(Throwable throwable);

    void onBulkActionSuccess(GroupAdBulkAction groupAdBulkAction);
}
