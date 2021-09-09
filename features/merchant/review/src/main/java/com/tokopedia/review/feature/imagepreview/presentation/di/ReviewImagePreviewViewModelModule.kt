package com.tokopedia.review.feature.imagepreview.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.imagepreview.presentation.viewmodel.ReviewImagePreviewViewModel
import com.tokopedia.review.feature.reading.di.ReadReviewScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewImagePreviewViewModelModule {

    @Binds
    @ReadReviewScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewImagePreviewViewModel::class)
    internal abstract fun reviewGalleryViewModel(viewModel: ReviewImagePreviewViewModel): ViewModel
}