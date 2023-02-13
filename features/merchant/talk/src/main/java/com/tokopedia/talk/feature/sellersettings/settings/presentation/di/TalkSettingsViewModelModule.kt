package com.tokopedia.talk.feature.sellersettings.settings.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.sellersettings.settings.presentation.viewmodel.TalkSettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TalkSettingsViewModelModule {

    @Binds
    @TalkSettingsScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkSettingsViewModel::class)
    internal abstract fun talkTalkSettingsViewModel(viewModel: TalkSettingsViewModel): ViewModel
}
