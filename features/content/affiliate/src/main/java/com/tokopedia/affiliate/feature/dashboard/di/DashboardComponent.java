package com.tokopedia.affiliate.feature.dashboard.di;

import com.tokopedia.affiliate.common.di.AffiliateComponent;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.AffiliateDashboardFragment;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.AffiliateCuratedProductFragment;
import com.tokopedia.affiliate.feature.dashboard.view.fragment.CommissionDetailFragment;

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

    void inject(AffiliateCuratedProductFragment affiliateCuratedProductFragment);

    void inject(AffiliateDashboardFragment dashboardFragment);

    void inject(CommissionDetailFragment commissionDetailFragment);
}
