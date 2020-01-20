package com.tokopedia.home.beranda.presentation.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.home.R


@SuppressLint("SyntheticAccessor")
class TokopediaPlayView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {
    companion object{
        private val playerLayoutId = R.layout.tokopedia_play_view

        /** The buffering view is never shown.  */
        private const val SHOW_BUFFERING_NEVER = 0
        /**
         * The buffering view is shown when the player is in the [buffering][Player.STATE_BUFFERING]
         * state and [playWhenReady][Player.getPlayWhenReady] is `true`.
         */
        private const val SHOW_BUFFERING_WHEN_PLAYING = 1
        /**
         * The buffering view is always shown when the player is in the [ buffering][Player.STATE_BUFFERING] state.
         */
        private const val SHOW_BUFFERING_ALWAYS = 2

        const val ANIMATION_TRANSITION_NAME = "play_video"

    }
    private var componentListener: ComponentListener? = null
    private var bufferingView: View? = null
    private var contentFrame: AspectRatioFrameLayout? = null
    private var player: Player? = null
    private var surfaceView: TextureView? = null
    private var textureViewRotation = 0
    private var showBuffering = SHOW_BUFFERING_NEVER
    private var resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init{
        showBuffering = SHOW_BUFFERING_NEVER
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PlayerView, 0, 0)
            try{
                showBuffering = a.getInteger(R.styleable.PlayerView_show_buffering, showBuffering)
            } finally {
                a.recycle()
            }
        }

        LayoutInflater.from(context).inflate(playerLayoutId, this)
        componentListener = ComponentListener()
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS

        // Content frame.
        contentFrame = findViewById(R.id.exo_content_frame)
        contentFrame?.let { setResizeModeRaw(it, resizeMode) }

        surfaceView = TextureView(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            surfaceView?.transitionName = ANIMATION_TRANSITION_NAME
        }
        val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        surfaceView?.layoutParams = params
        contentFrame?.addView(surfaceView, 0)
        // Buffering view.
        bufferingView = findViewById(R.id.exo_buffering)
        bufferingView?.visibility = View.GONE
    }

    private fun onContentAspectRatioChanged(
            aspectRatio: Float,
            contentFrame: AspectRatioFrameLayout?) {
        contentFrame?.setAspectRatio(aspectRatio)
    }

    private fun updateBuffering() {
//        bufferingView?.let{
//            val showBufferingSpinner = player != null && player?.playbackState == Player.STATE_BUFFERING && player?.playWhenReady == true
//            bufferingView?.visibility = if (showBufferingSpinner) View.VISIBLE else View.GONE
//        }
    }

    private fun setResizeModeRaw(aspectRatioFrame: AspectRatioFrameLayout, resizeMode: Int) {
        aspectRatioFrame.resizeMode = resizeMode
    }

    private fun applyCrop(textureView: TextureView, width: Float, height: Float): Float{
        val viewWidth = contentFrame?.width ?: 0
        val viewHeight = contentFrame?.height ?: 0
        var scaleX = 1.0f
        var scaleY = 1.0f

        if (width > viewWidth && height > viewHeight) {
            scaleX = width / viewWidth
            scaleY = height / viewHeight
        } else if (width < viewWidth && height < viewHeight) {
            scaleY = viewWidth / width
            scaleX = viewHeight / height
        } else if (viewWidth > width) {
            scaleY = viewWidth / width / (viewHeight / height)
        } else if (viewHeight > height) {
            scaleX = viewHeight / height / (viewWidth / width)
        }

        // Calculate pivot points, in our case crop from center

        val pivotPointX: Int = textureView.width / 2
        val pivotPointY: Int = textureView.height / 3
        val matrix = Matrix()
        matrix.setScale(scaleX, scaleY, pivotPointX.toFloat(), pivotPointY.toFloat())

        textureView.setTransform(matrix)

        return if (height == 0f || width == 0f) 1f else (contentFrame?.width?.toFloat() ?: 1f) / (contentFrame?.height?.toFloat() ?: 1f)
    }
    fun getSurfaceView() = surfaceView

    fun setPlayer(player: Player?){
        if (this.player != null) {
            this.player?.removeListener(componentListener)
            val oldVideoComponent = this.player?.videoComponent
            if (oldVideoComponent != null) {
                oldVideoComponent.removeVideoListener(componentListener)
                oldVideoComponent.clearVideoTextureView(surfaceView)
            }
        }
        updateBuffering()
        this.player = player
        if (player != null) {
            val newVideoComponent = player.videoComponent
            if (newVideoComponent != null) {
                newVideoComponent.setVideoTextureView(surfaceView)
                newVideoComponent.addVideoListener(componentListener)
            }
            player.addListener(componentListener)
        }
    }

    inner class ComponentListener : Player.EventListener, VideoListener, OnLayoutChangeListener {
        // VideoListener implementation
        override fun onVideoSizeChanged(
                width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
            var videoAspectRatio = 1f
            surfaceView?.let{ surfaceView ->
                // Try to apply rotation transformation when our surface is a TextureView.
                if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) { // We will apply a rotation 90/270 degree to the output texture of the TextureView.
                    // In this case, the output video's width and height will be swapped.
                    videoAspectRatio = 1 / videoAspectRatio
                }
                if (textureViewRotation != 0) {
                    surfaceView.removeOnLayoutChangeListener(this)
                }
                textureViewRotation = unappliedRotationDegrees
                if (textureViewRotation != 0) { // The texture view's dimensions might be changed after layout step.
                    // So add an OnLayoutChangeListener to apply rotation after layout step.
                    surfaceView.addOnLayoutChangeListener(this)
                }
//                applyTextureViewRotation(surfaceView, textureViewRotation)
                videoAspectRatio = applyCrop(surfaceView, width.toFloat(), height.toFloat())
            }
            onContentAspectRatioChanged(videoAspectRatio, contentFrame)
        }

        override fun onRenderedFirstFrame() {

        }

        override fun onSurfaceSizeChanged(width: Int, height: Int) {

        }

        // Player.EventListener implementation

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            updateBuffering()
        }

        override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {

        }
    }
}