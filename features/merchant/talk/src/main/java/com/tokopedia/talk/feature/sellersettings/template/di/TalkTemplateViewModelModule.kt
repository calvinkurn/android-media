package com.tokopedia.talk.feature.sellersettings.template.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.di.TalkSmartReplySettingsScope
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel.TalkSmartReplySettingsViewModel
import com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel.TalkEditTemplateViewModel
import com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel.TalkTemplateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TalkTemplateViewModelModule {

    @Binds
    @TalkSmartReplySettingsScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkTemplateViewModel::class)
    internal abstract fun talkTemplateViewModel(viewModel: TalkTemplateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TalkEditTemplateViewModel::class)
    internal abstract fun talkEditTemplateViewModel(viewModel: TalkEditTemplateViewModel): ViewModel
}