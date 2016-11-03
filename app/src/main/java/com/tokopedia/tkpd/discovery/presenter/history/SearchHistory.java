package com.tokopedia.tkpd.discovery.presenter.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.tkpd.discovery.view.history.SearchHistoryView;
import com.tokopedia.tkpd.session.base.BaseImpl;

/**
 * Created by Erry on 6/30/2016.
 */
public abstract class SearchHistory extends BaseImpl<SearchHistoryView> {

    public SearchHistory(SearchHistoryView view) {
        super(view);
    }

    public abstract RecyclerView.Adapter getAdapter();
    public abstract void unregisterBroadcast(Context context);
}
