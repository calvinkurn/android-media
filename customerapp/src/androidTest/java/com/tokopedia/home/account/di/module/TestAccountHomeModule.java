package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.analytics.domain.GetUserAttributesUseCase;
import com.tokopedia.home.account.di.scope.AccountHomeScope;
import com.tokopedia.home.account.domain.GetAccountUseCase;
import com.tokopedia.home.account.presentation.AccountHome;
import com.tokopedia.home.account.presentation.presenter.AccountHomePresenter;
import com.tokopedia.navigation_common.model.WalletPref;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * @author okasurya on 7/20/18.
 */
@Module
@AccountHomeScope
public class TestAccountHomeModule {
    private WalletPref walletPref;

    private GetUserAttributesUseCase getUserAttributesUseCase;
    private AccountAnalytics accountAnalytics;

    @Provides
    WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson) {
        return walletPref == null ? (walletPref = mock(WalletPref.class)) : walletPref;
    }

    @Provides
    AccountHome.Presenter provideAccountHomePresenter() {

        if (this.getUserAttributesUseCase == null) {
            this.getUserAttributesUseCase = mock(GetUserAttributesUseCase.class);
        }

        if (this.accountAnalytics == null) {
            this.accountAnalytics = mock(AccountAnalytics.class);
        }
        return new AccountHomePresenter(getUserAttributesUseCase, accountAnalytics);
    }

    public WalletPref getWalletPref() {
        return walletPref;
    }

    public GetUserAttributesUseCase getGetAccountUseCase() {
        return getUserAttributesUseCase;
    }
}
