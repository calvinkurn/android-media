package com.tokopedia.core.discovery.view.history;

import com.tokopedia.core.presenter.BaseView;

/**
 * Created by Erry on 6/30/2016.
 */
public interface SearchHistoryView extends BaseView {
    int FRAGMENT_ID = 192_471;
    void initRecylerView();
    void sendSearchResult(String query);
    void clearSearchQuery();

    void sendHotlistResult(String selected);
}
