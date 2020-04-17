package com.tokopedia.settingnotif.usersetting.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.settingnotif.usersetting.di.UserSettingScope
import com.tokopedia.settingnotif.usersetting.view.viewmodel.SettingStateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class UserSettingVIewModelModule {

    @UserSettingScope
    @Binds
    internal abstract fun bindViewModelFactory(
            viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @UserSettingScope
    @ViewModelKey(SettingStateViewModel::class)
    internal abstract fun bindSettingStateViewModel(
            viewModel: SettingStateViewModel
    ): ViewModel

}