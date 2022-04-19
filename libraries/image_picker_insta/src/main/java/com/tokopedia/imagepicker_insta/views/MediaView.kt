package com.tokopedia.imagepicker_insta.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioListener
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.*
import timber.log.Timber

class MediaView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), LifecycleObserver {

    private lateinit var playerView: PlayerView
    private lateinit var playPauseIcon: View
    lateinit var assetView: ZoomAssetImageView
    private var simpleExoPlayer: SimpleExoPlayer? = null
    private val isSdkLowerThanN = Build.VERSION.SDK_INT < Build.VERSION_CODES.N
    private lateinit var dataFactory: DefaultDataSourceFactory

    fun getLayout() = R.layout.imagepicker_insta_media_view
    var lastVolume = -10f

    @MediaScaleType
    var mediaScaleType: Int = MediaScaleType.MEDIA_CENTER_CROP
        set(value) {
            //Add logic for scaling in exoplayer as well
            if (value == MediaScaleType.MEDIA_CENTER_CROP) {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            } else {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }
            field = value
            assetView.resetZoom(true)
        }

    var imageAdapterData: ImageAdapterData? = null

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {})


    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    private fun initViews() {
        playerView = findViewById(R.id.media_player_view)
        assetView = findViewById(R.id.media_asset_view)
        playPauseIcon = findViewById(R.id.play_icon)
        assetView.mediaScaleTypeContract = object : MediaScaleTypeContract {
            override fun getCurrentMediaScaleType(): Int {
                return mediaScaleType
            }

        }
        initializePlayer()
        setListeners()
    }

    fun togglePlayPause() {
        if (simpleExoPlayer?.isPlaying == true) {
            simpleExoPlayer?.playWhenReady = false
            playPauseIcon.visibility = View.VISIBLE
        } else if (simpleExoPlayer?.isPlaying == false) {
            if (simpleExoPlayer?.playbackState == Player.STATE_ENDED) {
                simpleExoPlayer?.seekTo(0)
            }
            simpleExoPlayer?.playWhenReady = true
            playPauseIcon.visibility = View.GONE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        playerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }

        val doubleTapGestureListener = object : GestureDetector.OnDoubleTapListener {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                //Logic for play-pause
                togglePlayPause()
                return true
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                return false
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                if (e?.action == MotionEvent.ACTION_UP) {
                    toggleScaleType()
                    return true
                }
                return false
            }
        }
        gestureDetector.setOnDoubleTapListener(doubleTapGestureListener)
    }

    private fun initializePlayer() {
        dataFactory = DefaultDataSourceFactory(context, context.packageName)

        simpleExoPlayer = SimpleExoPlayer.Builder(context)
            .build()
            .also {
                playerView.player = it
                lastVolume = playerView.player?.audioComponent?.volume ?: 0f
                playerView.player?.audioComponent?.volume = 0f
            }

        simpleExoPlayer?.addAudioListener(object :AudioListener{
            override fun onVolumeChanged(volume: Float) {
                super.onVolumeChanged(volume)
            }
        })

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                Timber.e(error)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_ENDED) {
                    playerView.player?.seekTo(0)
                    playerView.player?.playWhenReady
                }
            }
        })
        mediaScaleType = MediaScaleType.MEDIA_CENTER_CROP
    }

    fun onVolumeUp() {
        if (simpleExoPlayer?.volume == 0f && lastVolume != -10f) {
            simpleExoPlayer?.volume = lastVolume
        }
    }

    fun loadAsset(imageAdapterData: ImageAdapterData, zoomInfo: ZoomInfo) {
        this.imageAdapterData = imageAdapterData
        val asset = imageAdapterData.asset
        stopPlayer()

        if (asset is VideoData) {
            playerView.visibility = View.VISIBLE
            assetView.visibility = View.GONE
            createVideoItem(asset, zoomInfo)
            // send zoom info to show preview
        } else if (asset is PhotosData) {
            assetView.visibility = View.VISIBLE
            playerView.visibility = View.GONE
            createPhotoItem(asset, zoomInfo)
        }
    }

    fun toggleScaleType() {
        if (mediaScaleType == MediaScaleType.MEDIA_CENTER_CROP) {
            mediaScaleType = MediaScaleType.MEDIA_CENTER_INSIDE
        } else {
            mediaScaleType = MediaScaleType.MEDIA_CENTER_CROP
        }
    }

    fun stopVideo() {
        stopPlayer()
    }

    fun removeAsset() {
        this.imageAdapterData = null
        playerView.visibility = View.GONE
        stopPlayer()
        assetView.removeAsset()
    }

    fun createPhotoItem(asset: PhotosData, zoomInfo: ZoomInfo) {
        assetView.loadAsset(asset, zoomInfo)
    }

    fun createVideoItem(videoData: VideoData, zoomInfo: ZoomInfo) {
        val tmpUri = videoData.contentUri
        val videoSource = ProgressiveMediaSource.Factory(dataFactory).createMediaSource(tmpUri)
        simpleExoPlayer?.prepare(videoSource)
        simpleExoPlayer?.playWhenReady = true
        playPauseIcon.visibility = View.GONE
    }

    private fun stopPlayer() {
        playPauseIcon.visibility = View.VISIBLE
        simpleExoPlayer?.stop()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onActivityResume() {
        if (isSdkLowerThanN || simpleExoPlayer == null) {
            initializePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestroy() {
        releasePlayer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onActivityStop() {
        if (isSdkLowerThanN) {
            releasePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onActivityPause() {
        if (isSdkLowerThanN) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        simpleExoPlayer?.release()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (MeasureSpec.getSize(widthMeasureSpec) < MeasureSpec.getSize(heightMeasureSpec)) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        }
    }

    fun lockAspectRatio() {
//        assetView.lockMinZoom()
    }

    fun unLockAspectRatio() {
//        assetView.unLockMinZoom()
    }
}