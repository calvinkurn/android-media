package com.tokopedia.settingnotif.usersetting.di

import com.tokopedia.settingnotif.usersetting.presenter.SettingFieldPresenter
import dagger.Module
import dagger.Provides


@UserSettingScope
@Module
class UserSettingModule {

    @UserSettingScope
    @Provides
    fun provideSettingFieldPresenter(): SettingFieldPresenter {
        return SettingFieldPresenter()
    }

}