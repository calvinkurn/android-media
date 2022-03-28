package com.tokopedia.reviewcommon.feature.media.player.controller.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewcommon.feature.media.player.controller.di.qualifier.ReviewMediaPlayerControllerViewModelFactory
import com.tokopedia.reviewcommon.feature.media.player.controller.di.scope.ReviewMediaPlayerControllerScope
import com.tokopedia.reviewcommon.feature.media.player.controller.presentation.viewmodel.ReviewMediaPlayerControllerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewMediaPlayerControllerViewModelModule {
    @Binds
    @ReviewMediaPlayerControllerScope
    @ReviewMediaPlayerControllerViewModelFactory
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewMediaPlayerControllerViewModel::class)
    internal abstract fun provideReviewMediaPlayerControllerViewModel(viewModel: ReviewMediaPlayerControllerViewModel): ViewModel
}