package com.tokopedia.review.feature.reading.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.reading.presentation.viewmodel.ReadReviewSortFilterViewModel
import com.tokopedia.review.feature.reading.presentation.viewmodel.ReadReviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReadReviewViewModelModule {

    @Binds
    @ReadReviewScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReadReviewViewModel::class)
    internal abstract fun readReviewViewModel(viewModel: ReadReviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReadReviewSortFilterViewModel::class)
    internal abstract fun readReviewSortFilterViewModel(viewModel: ReadReviewSortFilterViewModel): ViewModel
}