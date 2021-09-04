package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.PhotosData
import com.tokopedia.imagepicker_insta.models.VideoData
import java.io.File

class MediaView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), LifecycleObserver {

    private lateinit var playerView: PlayerView
    private lateinit var assetView: AssetImageView
    private var simpleExoPlayer: SimpleExoPlayer? = null
    private val isSdkLowerThanN = Build.VERSION.SDK_INT < Build.VERSION_CODES.N
    private lateinit var dataFactory: DefaultDataSourceFactory

    fun getLayout() = R.layout.imagepicker_insta_media_view

    /**
     * pass only one of the two  ImageView.ScaleType.CENTER_CROP or ImageView.ScaleType.CENTER_INSIDE
     * */
    var scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
        set(value) {
            //Add logic for scaling in exoplayer as well
            if (value == ImageView.ScaleType.CENTER_CROP) {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            } else {
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }
            assetView.scaleType = value
            field = value
        }

    var asset: Asset? = null

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {})


    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    private fun initViews() {
        playerView = findViewById(R.id.media_player_view)
        assetView = findViewById(R.id.media_asset_view)
        initializePlayer()
        setListeners()
    }

    fun togglePlayPause() {
        if (simpleExoPlayer?.isPlaying == true) {
            simpleExoPlayer?.playWhenReady = false
        } else if (simpleExoPlayer?.isPlaying == false) {
            if (simpleExoPlayer?.playbackState == Player.STATE_ENDED) {
                simpleExoPlayer?.seekTo(0)
            }
            simpleExoPlayer?.playWhenReady = true
        }
    }

    private fun setListeners() {
        playerView.setOnTouchListener { v, event ->
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
            }

        simpleExoPlayer?.addListener(object :Player.EventListener{
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
            }
        })
        scaleType = ImageView.ScaleType.CENTER_CROP
    }

    fun loadAsset(asset: Asset) {
        this.asset = asset

        stopPlayer()

        if (asset is VideoData) {
            playerView.visibility = View.VISIBLE
            createVideoItem(asset)
        } else if (asset is PhotosData) {
            playerView.visibility = View.GONE
            createPhotoItem(asset)
        }
    }

    fun toggleScaleType() {
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        } else {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    fun removeAsset() {
        this.asset = null
        playerView.visibility = View.GONE
        stopPlayer()
        assetView.removeAsset()
    }

    fun createPhotoItem(asset: PhotosData) {
        assetView.loadAsset(asset)
    }

    fun createVideoItem(videoData: VideoData) {
        //TODO Rahul remove dummy
        val tmpFile = File("/data/user/0/com.tokopedia.tkpd/files/image_picker/VID_20210904_051914_415925773560163203.mp4")
//        val tmpUri = Uri.fromFile(tmpFile)
        val tmpUri = videoData.uri
        val videoSource = ProgressiveMediaSource.Factory(dataFactory).createMediaSource(tmpUri)
        simpleExoPlayer?.prepare(videoSource)
        simpleExoPlayer?.playWhenReady = true
    }

    fun stopPlayer() {
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
}