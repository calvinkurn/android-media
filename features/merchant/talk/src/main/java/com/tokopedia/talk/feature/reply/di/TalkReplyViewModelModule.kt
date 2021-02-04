package com.tokopedia.talk.feature.reply.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.reply.presentation.viewmodel.TalkReplyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TalkReplyViewModelModule {

    @Binds
    @TalkReplyScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkReplyViewModel::class)
    internal abstract fun talkReadingViewModel(viewModel: TalkReplyViewModel): ViewModel
}