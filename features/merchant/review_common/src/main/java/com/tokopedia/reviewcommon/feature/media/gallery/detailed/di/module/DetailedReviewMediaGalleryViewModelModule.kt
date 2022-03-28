package com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.scope.DetailedReviewMediaGalleryScope
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DetailedReviewMediaGalleryViewModelModule {
    @Binds
    @DetailedReviewMediaGalleryScope
    @DetailedReviewMediaGalleryViewModelFactory
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SharedReviewMediaGalleryViewModel::class)
    internal abstract fun provideSharedReviewMediaGalleryViewModel(viewModel: SharedReviewMediaGalleryViewModel): ViewModel
}