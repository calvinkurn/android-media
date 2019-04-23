package com.tokopedia.search.presentation.presenter;

import android.content.Context;

import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.search.SearchContract;
import com.tokopedia.search.presentation.view.activity.SearchActivity;

public class SearchPresenter extends DiscoveryPresenter<SearchContract.View, SearchActivity>
        implements SearchContract.Presenter {

    public SearchPresenter(Context context) {
        super(context);
    }
}
