package com.tokopedia.review.feature.media.player.controller.di.component

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.media.gallery.detailed.di.component.DetailedReviewMediaGalleryComponent
import com.tokopedia.review.feature.media.player.controller.di.module.ReviewMediaPlayerControllerViewModelModule
import com.tokopedia.review.feature.media.player.controller.di.qualifier.ReviewMediaPlayerControllerViewModelFactory
import com.tokopedia.review.feature.media.player.controller.di.scope.ReviewMediaPlayerControllerScope
import com.tokopedia.review.feature.media.player.controller.presentation.fragment.ReviewMediaPlayerControllerFragment
import dagger.Component

@Component(
    modules = [ReviewMediaPlayerControllerViewModelModule::class],
    dependencies = [BaseAppComponent::class, DetailedReviewMediaGalleryComponent::class]
)
@ReviewMediaPlayerControllerScope
interface ReviewMediaPlayerControllerComponent {
    fun inject(fragment: ReviewMediaPlayerControllerFragment)

    @ReviewMediaPlayerControllerViewModelFactory
    fun viewModelProvider(): ViewModelProvider.Factory
}
