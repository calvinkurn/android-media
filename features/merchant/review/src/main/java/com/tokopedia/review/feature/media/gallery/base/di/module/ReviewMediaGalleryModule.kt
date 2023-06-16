package com.tokopedia.review.feature.media.gallery.base.di.module

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryGson
import com.tokopedia.review.feature.media.gallery.base.di.scope.ReviewMediaGalleryScope
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.review.feature.media.player.image.presentation.uimodel.ImageMediaItemUiModel
import com.tokopedia.review.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.reviewcommon.util.RuntimeTypeAdapterFactory
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class ReviewMediaGalleryModule {

    companion object {
        private const val BITMAP_CACHE_MEMORY_ALLOCATION_PERCENTAGE = 0.2
        private const val BITMAP_CACHE_DEFAULT_MEMORY_ALLOCATION_MB = 20
        private const val BYTES_MULTIPLIER = 1024
    }

    @Provides
    @ReviewMediaGalleryScope
    fun provideReviewVideoPlayer(@ApplicationContext context: Context): ReviewVideoPlayer {
        return ReviewVideoPlayer(context)
    }

    @Provides
    @ReviewMediaGalleryScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ReviewMediaGalleryScope
    @ReviewMediaGalleryGson
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(MediaItemUiModel::class.java)
                    .registerSubtype(LoadingStateItemUiModel::class.java, LoadingStateItemUiModel::class.java.name)
                    .registerSubtype(ImageMediaItemUiModel::class.java, ImageMediaItemUiModel::class.java.name)
                    .registerSubtype(VideoMediaItemUiModel::class.java, VideoMediaItemUiModel::class.java.name)
            ).create()
    }

    @Provides
    @ReviewMediaGalleryScope
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
