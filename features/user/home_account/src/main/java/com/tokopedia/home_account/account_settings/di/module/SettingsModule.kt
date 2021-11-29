package com.tokopedia.home_account.account_settings.di.module

import android.content.Context
import com.tokopedia.home_account.account_settings.di.scope.AccountLogoutScope
import com.tokopedia.home_account.account_settings.domain.SetUserProfileSafeModeUseCase
import com.tokopedia.home_account.account_settings.domain.UserProfileDobUseCase
import com.tokopedia.home_account.account_settings.domain.UserProfileSafeModeUseCase
import com.tokopedia.home_account.account_settings.presentation.presenter.SettingsPresenter
import dagger.Module
import dagger.Provides

@Module
class SettingsModule(private var activityContext: Context?) {

    @AccountLogoutScope
    @Provides
    public fun providesSettingsPresenter(userProfileSafeModeUseCase: UserProfileSafeModeUseCase?,
                                         userProfileDobUseCase: UserProfileDobUseCase?,
                                         setUserProfileSafeModeUseCase: SetUserProfileSafeModeUseCase?): SettingsPresenter {
        return SettingsPresenter(activityContext, userProfileSafeModeUseCase, userProfileDobUseCase, setUserProfileSafeModeUseCase)
    }

}