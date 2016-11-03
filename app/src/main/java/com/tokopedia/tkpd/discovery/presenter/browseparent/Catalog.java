package com.tokopedia.tkpd.discovery.presenter.browseparent;

import android.content.Context;

import com.tokopedia.tkpd.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.tkpd.discovery.view.CatalogView;
import com.tokopedia.tkpd.session.base.BaseImpl;

/**
 * Created by Erry on 6/30/2016.
 */
public abstract class Catalog extends BaseImpl<CatalogView> {

    public Catalog(CatalogView view) {
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
}
