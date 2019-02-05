package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.view.model.Ad;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class TopAdsAdListPresenterImpl<T extends Ad> implements TopAdsAdListPresenter<T> {

    protected final BaseListViewListener baseListViewListener;
    protected final Context context;
    protected PagingHandler pagingHandler;

    public TopAdsAdListPresenterImpl(Context context, BaseListViewListener baseListViewListener) {
        this.baseListViewListener = baseListViewListener;
        this.context = context;
        pagingHandler = new PagingHandler();
    }

    protected String getShopId() {
        SessionHandler session = new SessionHandler(context);
        return session.getShopID();
    }
}