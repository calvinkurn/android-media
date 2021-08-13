package com.tokopedia.review.feature.createreputation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CreateReviewViewModelModule {

    @Binds
    @CreateReviewScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CreateReviewViewModel::class)
    internal abstract fun createReviewViewModel(viewModel: CreateReviewViewModel): ViewModel
}