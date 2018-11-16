package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.home.account.di.scope.AccountSettingScope;
import com.tokopedia.home.account.domain.GetAccountSettingConfigUseCase;
import com.tokopedia.home.account.presentation.AccountSetting;
import com.tokopedia.home.account.presentation.presenter.AccountSettingPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvinatin on 16/11/18.
 */

@Module
public class AccountSettingModule {
    @Provides
    @AccountSettingScope
    AccountSetting.Presenter provideAccountSettingPresenter(GetAccountSettingConfigUseCase getAccountSettingConfigUseCase,
                                                            @ApplicationContext Context context) {
        return new AccountSettingPresenter(getAccountSettingConfigUseCase, context);
    }
}
