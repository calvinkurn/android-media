package com.tokopedia.kyc;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;

import dagger.Component;

@OVOUpgradeScope
@Component(modules = UpgradeOvoModule.class, dependencies = BaseAppComponent.class)
public interface UpgradeOvoComponent {
    void inject(UpgradeToOvoFragment upgradeToOvoFragment);
    void inject(IntroToOvoUpgradeStepsFragment upgradeToOvoFragment);
}
