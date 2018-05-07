package com.tokopedia.gm.statistic.di.component;

import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.statistic.di.module.GMStatisticModule;
import com.tokopedia.gm.statistic.di.scope.GMStatisticScope;
import com.tokopedia.gm.statistic.view.activity.GMStatisticDashboardActivity;
import com.tokopedia.gm.statistic.view.fragment.GMStatisticDashboardFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 6/15/17.
 */
@GMStatisticScope
@Component(modules = GMStatisticModule.class, dependencies = GMComponent.class)
public interface GMStatisticDashboardComponent {
    void inject(GMStatisticDashboardActivity gmStatisticDashboardActivity);

    void inject(GMStatisticDashboardFragment gmStatisticDashboardFragment);
}
