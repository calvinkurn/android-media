package com.tokopedia.review.feature.media.gallery.detailed.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.review.feature.media.detail.di.module.ReviewDetailTrackerModule
import com.tokopedia.review.feature.media.gallery.base.di.module.ReviewMediaGalleryTrackerModule
import com.tokopedia.review.feature.media.gallery.detailed.di.component.DaggerDetailedReviewMediaGalleryComponent
import com.tokopedia.review.feature.media.gallery.detailed.di.component.DetailedReviewMediaGalleryComponent
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter

object DetailedReviewMediaGalleryComponentInstance {

    @JvmStatic
    fun getInstance(context: Context): DetailedReviewMediaGalleryComponent {
        return (context as HasComponent<*>).component as DetailedReviewMediaGalleryComponent
    }

    @JvmStatic
    fun create(
        context: Context,
        @ReviewMediaGalleryRouter.PageSource pageSource: Int
    ): DetailedReviewMediaGalleryComponent {
        return DaggerDetailedReviewMediaGalleryComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .reviewDetailTrackerModule(ReviewDetailTrackerModule(pageSource))
            .reviewMediaGalleryTrackerModule(ReviewMediaGalleryTrackerModule(pageSource))
            .build()
    }
}
