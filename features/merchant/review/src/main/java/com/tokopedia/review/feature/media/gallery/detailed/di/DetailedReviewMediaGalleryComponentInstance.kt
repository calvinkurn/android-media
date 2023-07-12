package com.tokopedia.review.feature.media.gallery.detailed.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.review.feature.media.detail.di.module.ReviewDetailTrackerModule
import com.tokopedia.review.feature.media.gallery.detailed.di.component.DaggerDetailedReviewMediaGalleryComponent
import com.tokopedia.review.feature.media.gallery.detailed.di.component.DetailedReviewMediaGalleryComponent
import com.tokopedia.review.feature.media.gallery.detailed.presentation.util.DetailedReviewMediaGalleryStorage

object DetailedReviewMediaGalleryComponentInstance {

    @JvmStatic
    fun getInstance(context: Context): DetailedReviewMediaGalleryComponent {
        return DaggerDetailedReviewMediaGalleryComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .reviewDetailTrackerModule(ReviewDetailTrackerModule(DetailedReviewMediaGalleryStorage.pageSource))
            .build()
    }
}
