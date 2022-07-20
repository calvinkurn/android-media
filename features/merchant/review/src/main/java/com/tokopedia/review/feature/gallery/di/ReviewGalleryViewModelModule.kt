package com.tokopedia.review.feature.gallery.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.gallery.presentation.viewmodel.ReviewGalleryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewGalleryViewModelModule {

    @Binds
    @ReviewGalleryScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewGalleryViewModel::class)
    internal abstract fun reviewGridGalleryViewModel(viewModel: ReviewGalleryViewModel): ViewModel
}