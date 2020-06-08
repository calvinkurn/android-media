package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.user.session.UserSession;

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
        UserSession session = new UserSession(context);
        return session.getShopId();
    }
}