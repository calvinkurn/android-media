package com.tokopedia.affiliate.feature.dashboard.di;

import com.tokopedia.affiliate.common.di.AffiliateComponent;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.DashboardFragment;

import dagger.Component;

/**
 * @author by yfsx on 13/09/18.
 */
@DashboardScope
@Component(modules = DashboardModule.class, dependencies = AffiliateComponent.class)
public interface DashboardComponent {

    void inject(DashboardFragment dashboardFragment);


}
