package com.tokopedia.talk.feature.sellersettings.smartreply.settings.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel.TalkSmartReplySettingsViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TalkSmartReplySettingsViewModelModule {

    @Binds
    @TalkSmartReplySettingsScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkSmartReplySettingsViewModel::class)
    internal abstract fun talkSmartReplySettingsViewModel(viewModel: TalkSmartReplySettingsViewModel): ViewModel
}