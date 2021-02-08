package com.tokopedia.talk.feature.sellersettings.smartreply.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel.TalkSmartReplyDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TalkSmartReplyDetailViewModelModule {

    @Binds
    @TalkSmartReplyDetailScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkSmartReplyDetailViewModel::class)
    internal abstract fun talkSmartReplyDetailViewModel(viewModel: TalkSmartReplyDetailViewModel): ViewModel
}