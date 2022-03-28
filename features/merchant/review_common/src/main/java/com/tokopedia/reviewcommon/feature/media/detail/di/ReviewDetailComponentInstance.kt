package com.tokopedia.reviewcommon.feature.media.detail.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.reviewcommon.feature.media.detail.di.component.DaggerReviewDetailComponent
import com.tokopedia.reviewcommon.feature.media.detail.di.component.ReviewDetailComponent
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance

object ReviewDetailComponentInstance {
    private var INSTANCE: ReviewDetailComponent? = null

    @JvmStatic
    fun getInstance(context: Context): ReviewDetailComponent {
        return INSTANCE ?: DaggerReviewDetailComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(context))
            .build()
            .also { INSTANCE = it }
    }
}