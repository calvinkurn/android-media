package com.tokopedia.reviewcommon.feature.media.player.video.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewcommon.feature.media.player.video.di.scope.ReviewVideoPlayerScope
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.viewmodel.ReviewVideoPlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewVideoPlayerViewModelModule {
    @Binds
    @ReviewVideoPlayerScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewVideoPlayerViewModel::class)
    internal abstract fun provideReviewVideoPlayerViewModel(viewModel: ReviewVideoPlayerViewModel): ViewModel
}