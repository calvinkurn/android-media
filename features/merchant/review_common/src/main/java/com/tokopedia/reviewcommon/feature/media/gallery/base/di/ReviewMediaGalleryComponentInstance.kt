package com.tokopedia.reviewcommon.feature.media.gallery.base.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.component.DaggerReviewMediaGalleryComponent
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.component.ReviewMediaGalleryComponent
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance

object ReviewMediaGalleryComponentInstance {
    private var INSTANCE: ReviewMediaGalleryComponent? = null

    @JvmStatic
    fun getInstance(context: Context): ReviewMediaGalleryComponent {
        return INSTANCE ?: DaggerReviewMediaGalleryComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(context))
            .build()
            .also { INSTANCE = it }
    }
}