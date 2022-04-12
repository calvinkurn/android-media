package com.tokopedia.reviewcommon.feature.media.player.video.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.component.ReviewMediaGalleryComponent
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.component.DetailedReviewMediaGalleryComponent
import com.tokopedia.reviewcommon.feature.media.player.video.di.module.ReviewVideoPlayerViewModelModule
import com.tokopedia.reviewcommon.feature.media.player.video.di.scope.ReviewVideoPlayerScope
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.fragment.ReviewVideoPlayerFragment
import dagger.Component

@Component(
    modules = [ReviewVideoPlayerViewModelModule::class],
    dependencies = [
        BaseAppComponent::class,
        ReviewMediaGalleryComponent::class,
        DetailedReviewMediaGalleryComponent::class
    ]
)
@ReviewVideoPlayerScope
interface ReviewVideoPlayerComponent {
    fun inject(fragment: ReviewVideoPlayerFragment)
}