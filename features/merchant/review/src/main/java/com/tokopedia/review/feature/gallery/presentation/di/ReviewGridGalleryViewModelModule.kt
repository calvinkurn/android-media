package com.tokopedia.review.feature.gallery.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.gallery.presentation.viewmodel.ReviewGridGalleryViewModel
import com.tokopedia.review.feature.reading.di.ReadReviewScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewGridGalleryViewModelModule {

    @Binds
    @ReadReviewScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewGridGalleryViewModel::class)
    internal abstract fun reviewGridGalleryViewModel(viewModel: ReviewGridGalleryViewModel): ViewModel
}