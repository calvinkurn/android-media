package com.tokopedia.reviewcommon.feature.media.gallery.detailed.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.component.DaggerDetailedReviewMediaGalleryComponent
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.component.DetailedReviewMediaGalleryComponent

object DetailedReviewMediaGalleryComponentInstance {
    private var INSTANCE: DetailedReviewMediaGalleryComponent? = null

    @JvmStatic
    fun getInstance(context: Context): DetailedReviewMediaGalleryComponent {
        return INSTANCE ?: DaggerDetailedReviewMediaGalleryComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .also { INSTANCE = it }
    }
}