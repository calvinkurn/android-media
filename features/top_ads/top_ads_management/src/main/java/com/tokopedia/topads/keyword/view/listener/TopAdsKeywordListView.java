package com.tokopedia.topads.keyword.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

import java.util.List;

/**
 * Created by hadi.putra on 11/05/18.
 */

public interface TopAdsKeywordListView extends CustomerView {
    void showListError(Throwable throwable);

    void onSearchLoaded(List<KeywordAd> data, boolean hasNextData);

    void onGetGroupAdListError();

    void onGetGroupAdList(List<GroupAd> groupAds);

    void onBulkActionError(Throwable e);

    void onBulkActionSuccess(PageDataResponse<DataBulkKeyword> adBulkActions);
}
