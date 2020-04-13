package com.tokopedia.talk.feature.report.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.talk.feature.report.presentation.viewmodel.TalkReportViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@TalkReportScope
abstract class TalkReadingViewModelModule {

    @Binds
    @TalkReportScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TalkReportViewModel::class)
    internal abstract fun talkReadingViewModel(viewModel: TalkReportViewModel): ViewModel
}