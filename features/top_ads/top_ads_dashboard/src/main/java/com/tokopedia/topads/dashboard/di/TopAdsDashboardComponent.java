package com.tokopedia.topads.dashboard.di;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAddCreditFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment;

import dagger.Component;

/**
 * Created by hadi.putra on 23/04/18.
 */

@TopAdsDashboardScope
@Component(modules = {TopAdsDashboardModule.class, TopAdsDashboardShopModule.class, TopAdsDashboardNetworkModule.class},
        dependencies = BaseAppComponent.class)
public interface TopAdsDashboardComponent {

    void inject(TopAdsDashboardFragment topAdsDashboardFragment);

    void inject(TopAdsAddCreditFragment topAdsAddCreditFragment);
}
