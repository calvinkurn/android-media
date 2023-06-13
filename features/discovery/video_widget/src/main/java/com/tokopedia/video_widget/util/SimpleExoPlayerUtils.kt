package com.tokopedia.video_widget.util

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector

internal object SimpleExoPlayerUtils {
    private const val MAXIMUM_VIDEO_BANDWIDTH = 600_000

    private fun initTrackSelector(context: Context): TrackSelector {
        val defaultTrackSelector = DefaultTrackSelector.ParametersBuilder(context)
            .setMaxVideoBitrate(MAXIMUM_VIDEO_BANDWIDTH)
            .setExceedVideoConstraintsIfNecessary(true)
            .setRendererDisabled(C.TRACK_TYPE_AUDIO, true)
            .build()
        return DefaultTrackSelector(context).apply {
            parameters = defaultTrackSelector
        }
    }

    /**
     * To handle audio focus and content type
     */
    private fun initAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }

    private fun mute(videoPlayer: SimpleExoPlayer) = synchronized(this) {
        videoPlayer.volume = 0f
    }

    private fun setRepeatMode(
        videoPlayer: SimpleExoPlayer,
        repeatMode: Int,
    ) = synchronized(this) {
        videoPlayer.repeatMode = repeatMode
    }

    fun create(
        context: Context?,
        playerEventListener: Player.EventListener,
        repeatMode: Int = Player.REPEAT_MODE_OFF,
    ): ExoPlayer? {
        context ?: return null
        val trackSelector = initTrackSelector(context)
        return SimpleExoPlayer.Builder(context)
            .setLoadControl(DefaultLoadControl())
            .setTrackSelector(trackSelector)
            .build()
            .apply {
                addListener(playerEventListener)
                setAudioAttributes(initAudioAttributes(), false)
            }
            .also {
                mute(it)
                setRepeatMode(it, repeatMode)
            }
    }
}
