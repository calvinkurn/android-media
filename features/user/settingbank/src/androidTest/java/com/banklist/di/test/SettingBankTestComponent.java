package com.banklist.di.test;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.settingbank.SettingBankActivityTest;
import com.tokopedia.settingbank.banklist.di.SettingBankComponent;
import com.tokopedia.settingbank.banklist.di.SettingBankScope;

import dagger.Component;

@SettingBankScope
@Component(modules = SettingBankTestModule.class, dependencies = BaseAppComponent.class)
public interface SettingBankTestComponent extends SettingBankComponent {
    void inject(SettingBankActivityTest settingBankActivityTest);
}
