package com.tokopedia.home.account.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.account.di.module.TestAccountHomeModule;
import com.tokopedia.home.account.di.scope.AccountHomeScope;
import com.tokopedia.home.account.presentation.AccountHome;
import com.tokopedia.home.account.presentation.presenter.AccountHomePresenter;
import com.tokopedia.navigation_common.model.WalletPref;

import dagger.Component;

/**
 * @author okasurya on 7/20/18.
 */
@Component(modules = {TestAccountHomeModule.class}, dependencies = {BaseAppComponent.class})
@AccountHomeScope
public interface TestAccountHomeComponent extends AccountHomeComponent {
    AccountHome.Presenter accountHomePresenter();

    WalletPref walletPref();
}
