package com.tokopedia.home.account.di.module

import android.content.Context
import com.tokopedia.home.account.di.scope.AccountLogoutScope
import com.tokopedia.home.account.domain.SetUserProfileSafeModeUseCase
import com.tokopedia.home.account.domain.UserProfileDobUseCase
import com.tokopedia.home.account.domain.UserProfileSafeModeUseCase
import com.tokopedia.home.account.presentation.presenter.SettingsPresenter
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