package com.tokopedia.search.result.product.video

import android.content.Context
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.video_widget.VideoPlayerAutoplay
import dagger.Module
import dagger.Provides

@Module
object VideoModule {
    @JvmStatic
    @Provides
    @SearchScope
    fun provideProductVideoAutoplay(
        remoteConfig: RemoteConfig,
        @SearchContext
        context: Context,
    ): VideoPlayerAutoplay {
        return VideoPlayerAutoplay(remoteConfig, context)
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideVideoSneakpeekPreference(
        preferenceImpl: VideoSneakpeekPreferenceImpl,
    ): VideoSneakpeekPreference = preferenceImpl
}
