package com.tokopedia.play.broadcaster.shorts.factory

import android.net.Uri
import com.google.android.exoplayer2.source.BaseMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
class PlayShortsMediaSourceFactory @Inject constructor(
    private val progressiveMediaSourceFactory: ProgressiveMediaSource.Factory,
) {

    fun create(mediaUri: String): BaseMediaSource {
        return progressiveMediaSourceFactory.createMediaSource(Uri.parse(mediaUri))
    }
}
