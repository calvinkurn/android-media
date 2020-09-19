package com.tokopedia.play.exoplayer

import android.content.Context
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsCollector
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.Clock
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play_common.player.creator.ExoPlayerCreator

/**
 * Created by jegul on 15/09/20
 */
class TestExoPlayerCreator(private val context: Context) : ExoPlayerCreator {

    override fun createExoPlayer(loadControl: LoadControl): SimpleExoPlayer {
        return TestExoPlayer(context)
    }
}

class TestExoPlayer(context: Context) : SimpleExoPlayer(
        context,
        DefaultRenderersFactory(context),
        DefaultTrackSelector(context),
        DefaultLoadControl(),
        DefaultBandwidthMeter.getSingletonInstance(context),
        AnalyticsCollector(Clock.DEFAULT),
        Clock.DEFAULT,
        Util.getLooper()
) {

    private val mListeners = mutableListOf<Player.EventListener>()

    override fun addListener(listener: Player.EventListener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: Player.EventListener) {
        mListeners.remove(listener)
    }

    override fun prepare(mediaSource: MediaSource) {
        //Do nothing
    }

    fun setState(playWhenReady: Boolean, playbackState: Int) {
        mListeners.forEach {
            it.onPlayerStateChanged(playWhenReady, playbackState)
        }
    }

    fun setError(error: ExoPlaybackException) {
        mListeners.forEach {
            it.onPlayerError(error)
        }
    }
}