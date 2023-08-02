package com.tokopedia.review.feature.media.gallery.base.di.component

import android.graphics.Bitmap
import android.util.LruCache
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.media.gallery.base.di.module.ReviewMediaGalleryModule
import com.tokopedia.review.feature.media.gallery.base.di.module.ReviewMediaGalleryTrackerModule
import com.tokopedia.review.feature.media.gallery.base.di.module.ReviewMediaGalleryViewModelModule
import com.tokopedia.review.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.base.di.scope.ReviewMediaGalleryScope
import com.tokopedia.review.feature.media.gallery.base.presentation.fragment.ReviewMediaGalleryFragment
import com.tokopedia.review.feature.media.gallery.detailed.di.component.DetailedReviewMediaGalleryComponent
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import dagger.Component

@Component(
    modules = [
        ReviewMediaGalleryViewModelModule::class,
        ReviewMediaGalleryModule::class,
        ReviewMediaGalleryTrackerModule::class,
    ],
    dependencies = [
        BaseAppComponent::class,
        DetailedReviewMediaGalleryComponent::class
    ]
)
@ReviewMediaGalleryScope
interface ReviewMediaGalleryComponent {
    fun inject(fragment: ReviewMediaGalleryFragment)

    fun reviewVideoPlayer(): ReviewVideoPlayer

    @ReviewMediaGalleryViewModelFactory
    fun viewModelFactory(): ViewModelProvider.Factory

    fun bitmapCache(): LruCache<String, Bitmap>
}
