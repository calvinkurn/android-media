package com.tokopedia.review.feature.media.player.controller.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.review.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.player.controller.di.component.DaggerReviewMediaPlayerControllerComponent
import com.tokopedia.review.feature.media.player.controller.di.component.ReviewMediaPlayerControllerComponent

object ReviewMediaPlayerControllerComponentInstance {

    @JvmStatic
    fun getInstance(context: Context): ReviewMediaPlayerControllerComponent {
        return DaggerReviewMediaPlayerControllerComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(context))
            .build()
    }
}
