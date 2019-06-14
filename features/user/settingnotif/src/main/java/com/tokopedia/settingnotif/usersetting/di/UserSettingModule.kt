package com.tokopedia.settingnotif.usersetting.di

import com.tokopedia.settingnotif.usersetting.domain.usecase.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.presenter.SettingFieldPresenter
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import dagger.Module
import dagger.Provides


@UserSettingScope
@Module
class UserSettingModule {

    @UserSettingScope
    @Provides
    fun provideSettingFieldPresenter(getUserSettingUseCase: GetUserSettingUseCase): SettingFieldContract.Presenter {
        return SettingFieldPresenter(getUserSettingUseCase)
    }

    @UserSettingScope
    @Provides
    fun providesGetUserSettingUseCase(): GetUserSettingUseCase {
        return GetUserSettingUseCase()
    }

}