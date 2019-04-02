package com.tokopedia.home.account.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.account.di.module.AccountSettingModule;
import com.tokopedia.home.account.di.scope.AccountSettingScope;
import com.tokopedia.home.account.presentation.fragment.setting.AccountSettingFragment;

import dagger.Component;

/**
 * @author by alvinatin on 16/11/18.
 */

@Component(modules = {AccountSettingModule.class}, dependencies = {BaseAppComponent.class})
@AccountSettingScope
public interface AccountSettingComponent {
    void inject(AccountSettingFragment fragment);
}
