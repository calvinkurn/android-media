package com.tokopedia.videoplayer.view.fragment

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.videoplayer.R
import com.tokopedia.videoplayer.view.activity.VideoPlayerActivity
import kotlinx.android.synthetic.main.layout_single_video_fragment.*
import java.lang.Exception

/**
 * @author by yfsx on 20/03/19.
 */
class SingleVideoPlayerFragment: BaseDaggerFragment() {

    private var player: SimpleExoPlayer? = null
    private var playbackStateBuilder : PlaybackStateCompat.Builder? = null
    private var mediaSession: MediaSessionCompat? = null

    companion object {
        fun getInstance(bundle: Bundle): SingleVideoPlayerFragment {
            val fragment = SingleVideoPlayerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    init {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_single_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewListener()

    }

    private fun initView() {
        try {
            initPlayer()
        } catch (e: Exception) {
            e.localizedMessage
        } catch (e: IllegalStateException) {
            e.localizedMessage
        } catch (e: AbstractMethodError) {
            e.localizedMessage
        }
    }

    private fun initPlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    activity!!,
                    DefaultTrackSelector())
            videoPlayerView.player = player
        }
        val url = arguments?.getString(VideoPlayerActivity.PARAM_SINGLE_URL)
        val mediaSource = buildMediaSource(Uri.parse(url))

        player?.prepare(mediaSource)
        player?.playWhenReady = true

        val componentName = ComponentName(activity!!, "Exo")
        mediaSession = MediaSessionCompat(activity!!, "ExoPlayer", componentName, null)

        playbackStateBuilder = PlaybackStateCompat.Builder()

        playbackStateBuilder?.setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_FAST_FORWARD)

        mediaSession?.setPlaybackState(playbackStateBuilder?.build())
        mediaSession?.isActive = true
    }

    private fun initViewListener() {

    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = Util.getUserAgent(activity!!, "Exo")
        return ExtractorMediaSource(uri, DefaultDataSourceFactory(activity!!, userAgent), DefaultExtractorsFactory(), null, null)

    }

    private fun releasePlayer() {
        if (player != null) {
            player?.stop()
            player?.release()
            player = null
        }
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

}