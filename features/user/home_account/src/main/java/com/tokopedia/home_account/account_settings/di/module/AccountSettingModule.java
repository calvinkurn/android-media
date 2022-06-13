package com.tokopedia.home_account.account_settings.di.module;

import com.tokopedia.home_account.account_settings.di.scope.AccountSettingScope;
import com.tokopedia.home_account.account_settings.domain.GetAccountSettingConfigUseCase;
import com.tokopedia.home_account.account_settings.presentation.AccountSetting;
import com.tokopedia.home_account.account_settings.presentation.presenter.AccountSettingPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvinatin on 16/11/18.
 */

@Module
public class AccountSettingModule {
    @Provides
    @AccountSettingScope
    AccountSetting.Presenter provideAccountSettingPresenter(GetAccountSettingConfigUseCase getAccountSettingConfigUseCase) {
        return new AccountSettingPresenter(getAccountSettingConfigUseCase);
    }
}
