package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailListener;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.user.session.UserSession;

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
        UserSession session = new UserSession(context);
        return session.getShopId();
    }

    protected void unsubscribe(){
        topAdsDetailListener = null;
    }
}
