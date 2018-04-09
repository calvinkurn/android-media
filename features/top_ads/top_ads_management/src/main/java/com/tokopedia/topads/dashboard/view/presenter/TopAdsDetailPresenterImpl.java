package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailListener;
import com.tokopedia.topads.dashboard.view.model.Ad;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public abstract class TopAdsDetailPresenterImpl<V extends Ad> implements TopAdsDetailPresenter {

    protected TopAdsDetailListener<V> topAdsDetailListener;

    private Context context;

    public TopAdsDetailPresenterImpl(Context context, TopAdsDetailListener<V> topAdsDetailListener) {
        this.context = context;
        this.topAdsDetailListener = topAdsDetailListener;
    }

    protected String getShopId() {
        SessionHandler session = new SessionHandler(context);
        return session.getShopID();
    }

    protected void unsubscribe(){
        topAdsDetailListener = null;
    }
}
