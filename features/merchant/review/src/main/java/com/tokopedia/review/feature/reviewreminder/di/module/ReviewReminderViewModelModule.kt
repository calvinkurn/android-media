package com.tokopedia.review.feature.reviewreminder.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.reviewreminder.di.scope.ReviewReminderScope
import com.tokopedia.review.feature.reviewreminder.view.viewmodel.ReminderMessageViewModel
import com.tokopedia.review.feature.reviewreminder.view.viewmodel.ReminderPerformanceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ReviewReminderViewModelModule {

    @ReviewReminderScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReminderPerformanceViewModel::class)
    abstract fun reminderPerformanceViewModel(viewModel: ReminderPerformanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReminderMessageViewModel::class)
    abstract fun reminderMessageViewModel(viewModel: ReminderMessageViewModel): ViewModel
}