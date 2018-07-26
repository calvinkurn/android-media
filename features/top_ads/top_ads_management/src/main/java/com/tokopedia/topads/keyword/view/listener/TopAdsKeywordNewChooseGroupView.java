package com.tokopedia.topads.keyword.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;

import java.util.List;

/**
 * Created by zulfikarrahman on 5/23/17.
 */

public interface TopAdsKeywordNewChooseGroupView extends CustomerView {
    void onGetGroupAdList(List<GroupAd> groupAds);

    void onGetGroupAdListError(Throwable e);
}
