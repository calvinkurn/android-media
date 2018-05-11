package com.tokopedia.topads.keyword.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

import java.util.List;

/**
 * Created by hadi.putra on 11/05/18.
 */

public interface TopAdsKeywordListView extends CustomerView {
    void showListError(Throwable throwable);

    void onSearchLoaded(List<KeywordAd> data, boolean hasNextData);
}
