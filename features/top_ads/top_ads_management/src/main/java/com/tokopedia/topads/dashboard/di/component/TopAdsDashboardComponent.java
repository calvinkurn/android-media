package com.tokopedia.topads.dashboard.di.component;

import com.tokopedia.topads.dashboard.di.module.TopAdsDashboardModule;
import com.tokopedia.topads.dashboard.di.module.TopAdsShopModule;
import com.tokopedia.topads.dashboard.di.scope.TopAdsDashboardScope;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment;

import dagger.Component;

/**
 * Created by hadi.putra on 23/04/18.
 */

@TopAdsDashboardScope
@Component(modules = {TopAdsDashboardModule.class}, dependencies = TopAdsComponent.class)
public interface TopAdsDashboardComponent {

    void inject(TopAdsDashboardFragment topAdsDashboardFragment);
}
