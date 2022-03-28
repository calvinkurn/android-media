package com.tokopedia.reviewcommon.feature.media.player.controller.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.ReviewMediaGalleryComponentInstance
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.reviewcommon.feature.media.player.controller.di.component.DaggerReviewMediaPlayerControllerComponent
import com.tokopedia.reviewcommon.feature.media.player.controller.di.component.ReviewMediaPlayerControllerComponent

object ReviewMediaPlayerControllerComponentInstance {
    private var INSTANCE: ReviewMediaPlayerControllerComponent? = null

    @JvmStatic
    fun getInstance(context: Context): ReviewMediaPlayerControllerComponent {
        return INSTANCE ?: DaggerReviewMediaPlayerControllerComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .reviewMediaGalleryComponent(ReviewMediaGalleryComponentInstance.getInstance(context))
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(context))
            .build()
            .also { INSTANCE = it }
    }
}