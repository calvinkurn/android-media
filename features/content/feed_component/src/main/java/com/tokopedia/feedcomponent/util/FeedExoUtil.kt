package com.tokopedia.feedcomponent.util

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * Created by kenny.hadisaputra on 06/07/23
 */
object FeedExoUtil {

    fun getDataSourceFactory(context: Context): DataSource.Factory {
        return DefaultHttpDataSourceFactory(
            Util.getUserAgent(context, "Tokopedia Android")
        )
    }

    fun getMediaSourceByUri(
        context: Context,
        uri: Uri,
        cache: Cache? = null
    ): MediaSource {
        val mDataSourceFactory = getDataSourceFactory(context).let {
            if (cache != null) {
                CacheDataSourceFactory(cache, it, CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            } else {
                it
            }
        }

        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(mDataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(mDataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(mDataSourceFactory)
                .setAllowChunklessPreparation(true)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(mDataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }
}
