package com.tokopedia.videoplayer.view.fragment

import android.content.ComponentName
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.videoplayer.R
import com.tokopedia.videoplayer.view.activity.VideoPlayerActivity
import com.tokopedia.videoplayer.view.listener.VideoPlayerListener
import kotlinx.android.synthetic.main.layout_single_video_fragment.*
import java.lang.Exception

/**
 * @author by yfsx on 20/03/19.
 */
class SingleVideoPlayerFragment: BaseDaggerFragment(), MediaPlayer.OnPreparedListener {

//    private var player: SimpleExoPlayer? = null
//    private var playbackStateBuilder : PlaybackStateCompat.Builder? = null
//    private var mediaSession: MediaSessionCompat? = null

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

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.let {
            resizeVideo(it.getVideoWidth(), it.getVideoHeight())
            it.start()
        }
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
        val url = arguments?.getString(VideoPlayerActivity.PARAM_SINGLE_URL)
        val mediaController = MediaController(activity!!)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse(url))
        videoView.setOnErrorListener(object : MediaPlayer.OnErrorListener{
            override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
                when(p1) {
                    MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                        Toast.makeText(context, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                        return true
                    }
                    MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                        Toast.makeText(context, getString(R.string.default_request_error_internal_server), Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                        return true
                    }
                    else -> {
                        Toast.makeText(context, getString(R.string.default_request_error_timeout), Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                        return true
                    }
                }
            }
        })
        videoView.setOnPreparedListener(this)
//        if (player == null) {
//            player = ExoPlayerFactory.newSimpleInstance(activity!!)
//            videoPlayerView.player = player
//        }
//        val mediaSource = buildMediaSource(Uri.parse(url))
//
//        player?.prepare(mediaSource)
//        player?.playWhenReady = true
//
//        val componentName = ComponentName(activity!!, "Exo")
//        mediaSession = MediaSessionCompat(activity!!, "ExoPlayer", componentName, null)
//
//        playbackStateBuilder = PlaybackStateCompat.Builder()
//
//        playbackStateBuilder?.setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE or
//                PlaybackStateCompat.ACTION_FAST_FORWARD)
//
//        mediaSession?.setPlaybackState(playbackStateBuilder?.build())
//        mediaSession?.isActive = true
//        player?.addListener(getDefaultListener())
    }

    private fun initViewListener() {

    }

//    private fun getDefaultListener(): Player.DefaultEventListener {
//        return VideoPlayerListener()
//    }
//
//    private fun buildMediaSource(uri: Uri): MediaSource {
//        val userAgent = Util.getUserAgent(activity!!, "tokopedia")
//        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(activity!!, userAgent)).createMediaSource(uri)
//
//    }

    private fun resizeVideo(mVideoWidth: Int, mVideoHeight: Int) {
        var videoWidth = mVideoWidth
        var videoHeight = mVideoHeight
        val displaymetrics = DisplayMetrics()
        activity!!.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics)

        val heightRatio = videoHeight.toFloat() / displaymetrics.widthPixels.toFloat()
        val widthRatio = videoWidth.toFloat() / displaymetrics.heightPixels.toFloat()

        if (videoWidth > videoHeight) {
            videoWidth = Math.ceil((videoWidth.toFloat() * widthRatio).toDouble()).toInt()
            videoHeight = Math.ceil((videoHeight.toFloat() * widthRatio).toDouble()).toInt()
        } else {
            videoWidth = Math.ceil((videoWidth.toFloat() * heightRatio).toDouble()).toInt()
            videoHeight = Math.ceil((videoHeight.toFloat() * heightRatio).toDouble()).toInt()
        }

        videoView.setSize(videoWidth, videoHeight)
        videoView.holder.setFixedSize(videoWidth, videoHeight)
    }
//
//    private fun releasePlayer() {
//        if (player != null) {
//            player?.stop()
//            player?.release()
//            player = null
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        releasePlayer()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        releasePlayer()
//    }

}