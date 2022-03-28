package com.tokopedia.reviewcommon.feature.media.player.image.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewcommon.feature.media.player.image.di.qualifier.ReviewImagePlayerViewModelFactory
import com.tokopedia.reviewcommon.feature.media.player.image.di.scope.ReviewImagePlayerScope
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.viewmodel.ReviewImagePlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewImagePlayerViewModelModule {
    @Binds
    @ReviewImagePlayerScope
    @ReviewImagePlayerViewModelFactory
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewImagePlayerViewModel::class)
    internal abstract fun provideReviewImagePlayerViewModel(viewModel: ReviewImagePlayerViewModel): ViewModel
}