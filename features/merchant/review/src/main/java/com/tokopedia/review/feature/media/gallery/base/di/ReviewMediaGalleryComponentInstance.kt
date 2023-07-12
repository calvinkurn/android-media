package com.tokopedia.review.feature.media.gallery.base.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.review.feature.media.gallery.base.di.component.DaggerReviewMediaGalleryComponent
import com.tokopedia.review.feature.media.gallery.base.di.component.ReviewMediaGalleryComponent
import com.tokopedia.review.feature.media.gallery.base.di.module.ReviewMediaGalleryTrackerModule
import com.tokopedia.review.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.gallery.detailed.presentation.util.DetailedReviewMediaGalleryStorage

object ReviewMediaGalleryComponentInstance {

    @JvmStatic
    fun getInstance(context: Context): ReviewMediaGalleryComponent {
        return DaggerReviewMediaGalleryComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .reviewMediaGalleryTrackerModule(ReviewMediaGalleryTrackerModule(DetailedReviewMediaGalleryStorage.pageSource))
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(context))
            .build()
    }
}
