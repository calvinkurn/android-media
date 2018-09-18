package com.tokopedia.affiliate.feature.dashboard.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory;

/**
 * @author by yfsx on 18/09/18.
 */
public class EmptyDashboardViewModel implements Visitable<DashboardItemTypeFactory> {

    public EmptyDashboardViewModel() {
    }

    @Override
    public int type(DashboardItemTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
