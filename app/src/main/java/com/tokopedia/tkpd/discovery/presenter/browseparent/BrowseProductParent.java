package com.tokopedia.tkpd.discovery.presenter.browseparent;

import android.content.Context;

import com.tokopedia.tkpd.discovery.model.NetworkParam;
import com.tokopedia.tkpd.discovery.model.Breadcrumb;
import com.tokopedia.tkpd.discovery.model.BrowseProductModel;
import com.tokopedia.tkpd.discovery.view.BrowseProductParentView;
import com.tokopedia.tkpd.session.base.BaseImpl;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public abstract class BrowseProductParent extends BaseImpl<BrowseProductParentView> {

    public BrowseProductParent(BrowseProductParentView view) {
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

    public abstract BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly);

    public abstract NetworkParam.Product getProductParam();

    public abstract List<Breadcrumb> getBreadCrumb();

    public abstract void fetchDynamicAttribute(Context context, String source);

}
