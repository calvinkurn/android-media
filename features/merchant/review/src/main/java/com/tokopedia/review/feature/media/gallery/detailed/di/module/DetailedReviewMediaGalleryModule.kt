package com.tokopedia.review.feature.media.gallery.detailed.di.module

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.feature.media.gallery.detailed.di.scope.DetailedReviewMediaGalleryScope
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class DetailedReviewMediaGalleryModule {

    companion object {
        private const val BITMAP_CACHE_MEMORY_ALLOCATION_PERCENTAGE = 0.2
        private const val BITMAP_CACHE_DEFAULT_MEMORY_ALLOCATION_MB = 20
        private const val BYTES_MULTIPLIER = 1024
    }

    @Provides
    @DetailedReviewMediaGalleryScope
    fun provideTrackingQueue(@ApplicationContext context: Context): TrackingQueue {
        return TrackingQueue(context)
    }

    @Provides
    @DetailedReviewMediaGalleryScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @DetailedReviewMediaGalleryScope
    fun provideReviewVideoPlayer(@ApplicationContext context: Context): ReviewVideoPlayer {
        return ReviewVideoPlayer(context)
    }

    @Provides
    @DetailedReviewMediaGalleryScope
    fun provideBitmapCache(@ApplicationContext context: Context): LruCache<String, Bitmap> {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memoryClass = activityManager?.memoryClass
        val cacheMemoryAllocationMB = memoryClass?.times(BITMAP_CACHE_MEMORY_ALLOCATION_PERCENTAGE)?.toInt() ?: BITMAP_CACHE_DEFAULT_MEMORY_ALLOCATION_MB
        val cacheSize = cacheMemoryAllocationMB * BYTES_MULTIPLIER * BYTES_MULTIPLIER
        return object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return value?.byteCount.orZero()
            }
        }
    }
}
