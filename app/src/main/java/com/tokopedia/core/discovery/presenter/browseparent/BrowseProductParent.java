package com.tokopedia.core.discovery.presenter.browseparent;

import android.content.Context;

import com.tokopedia.core.discovery.model.NetworkParam;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.BrowseProductModel;
import com.tokopedia.core.discovery.view.BrowseProductParentView;
import com.tokopedia.core.session.base.BaseImpl;

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

}
