package com.tokopedia.reviewcommon.feature.media.gallery.base.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.scope.ReviewMediaGalleryScope
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import dagger.Module
import dagger.Provides

@Module
class ReviewMediaGalleryModule {
    @Provides
    @ReviewMediaGalleryScope
    fun provideReviewVideoPlayer(@ApplicationContext context: Context): ReviewVideoPlayer {
        return ReviewVideoPlayer(context)
    }
}