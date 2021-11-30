package com.tokopedia.talk.feature.reporttalk.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.reply.presentation.viewmodel.TalkReplyViewModel
import com.tokopedia.talk.feature.reporttalk.view.viewmodel.ReportTalkViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReportTalkViewModelModule {

    @Binds
    @ReportTalkScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkReplyViewModel::class)
    internal abstract fun reportTalkViewModel(viewModel: ReportTalkViewModel): ViewModel
}