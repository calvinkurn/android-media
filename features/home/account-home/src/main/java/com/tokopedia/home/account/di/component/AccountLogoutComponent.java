package com.tokopedia.home.account.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.account.di.module.DialogLogoutModule;
import com.tokopedia.home.account.di.scope.AccountLogoutScope;
import com.tokopedia.home.account.presentation.fragment.DialogLogoutFragment;

import dagger.Component;

@AccountLogoutScope
@Component(modules = {DialogLogoutModule.class}, dependencies = {BaseAppComponent.class})
public interface AccountLogoutComponent {
    void inject(DialogLogoutFragment fragment);
}
