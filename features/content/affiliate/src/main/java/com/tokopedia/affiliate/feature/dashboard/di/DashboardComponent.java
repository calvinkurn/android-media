package com.tokopedia.affiliate.feature.dashboard.di;

import com.tokopedia.affiliate.common.di.AffiliateComponent;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.CommissionDetailFragment;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.DashboardFragment;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.NewDashboardFragment;

import dagger.Component;

/**
 * @author by yfsx on 13/09/18.
 */
@DashboardScope
@Component(modules =
            {DashboardModule.class,
            GqlRawQueryModule.class,
            ViewModelModule.class},
        dependencies = AffiliateComponent.class)
public interface DashboardComponent {

    void inject(DashboardFragment dashboardFragment);

    void inject(NewDashboardFragment newDashboardFragment);

    void inject(CommissionDetailFragment commissionDetailFragment);
}
