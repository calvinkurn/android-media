package com.tokopedia.review.feature.media.player.image.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.media.gallery.base.di.component.ReviewMediaGalleryComponent
import com.tokopedia.review.feature.media.player.image.di.module.ReviewImagePlayerViewModelModule
import com.tokopedia.review.feature.media.player.image.di.scope.ReviewImagePlayerScope
import com.tokopedia.review.feature.media.player.image.presentation.fragment.ReviewImagePlayerFragment
import dagger.Component

@Component(
    modules = [ReviewImagePlayerViewModelModule::class],
    dependencies = [BaseAppComponent::class, ReviewMediaGalleryComponent::class]
)
@ReviewImagePlayerScope
interface ReviewImagePlayerComponent {
    fun inject(fragment: ReviewImagePlayerFragment)
}