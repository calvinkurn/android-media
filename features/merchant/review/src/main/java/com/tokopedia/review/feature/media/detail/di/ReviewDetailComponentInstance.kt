package com.tokopedia.review.feature.media.detail.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.review.feature.media.detail.di.component.DaggerReviewDetailComponent
import com.tokopedia.review.feature.media.detail.di.component.ReviewDetailComponent
import com.tokopedia.review.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance

object ReviewDetailComponentInstance {

    @JvmStatic
    fun getInstance(context: Context): ReviewDetailComponent {
        return DaggerReviewDetailComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(context))
            .build()
    }
}
