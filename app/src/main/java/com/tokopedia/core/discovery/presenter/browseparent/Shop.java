package com.tokopedia.core.discovery.presenter.browseparent;

import android.content.Context;

import com.tokopedia.core.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.core.discovery.view.ShopView;
import com.tokopedia.core.session.base.BaseImpl;

/**
 * Created by Erry on 6/30/2016.
 */
public abstract class Shop extends BaseImpl<ShopView> {

    public Shop(ShopView view) {
        super(view);
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    public abstract void callNetwork(DiscoveryActivityPresenter discoveryActivityPresenter);

    public abstract void loadMore(Context context);

    public abstract void fetchDynamicAttribut();

}
