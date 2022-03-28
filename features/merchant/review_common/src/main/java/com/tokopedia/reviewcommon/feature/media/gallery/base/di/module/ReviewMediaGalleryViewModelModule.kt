package com.tokopedia.reviewcommon.feature.media.gallery.base.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.scope.ReviewMediaGalleryScope
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.viewmodel.ReviewMediaGalleryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewMediaGalleryViewModelModule {
    @Binds
    @ReviewMediaGalleryScope
    @ReviewMediaGalleryViewModelFactory
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewMediaGalleryViewModel::class)
    internal abstract fun provideReviewMediaGalleryViewModel(viewModel: ReviewMediaGalleryViewModel): ViewModel
}