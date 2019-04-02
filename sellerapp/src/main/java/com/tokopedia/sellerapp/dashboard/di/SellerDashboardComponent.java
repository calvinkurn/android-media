package com.tokopedia.sellerapp.dashboard.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.sellerapp.dashboard.view.fragment.DashboardFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@SellerDashboardScope
@Component(modules = SellerDashboardModule.class, dependencies = AppComponent.class)
public interface SellerDashboardComponent {
    void inject(DashboardFragment dashboardFragment);
}
