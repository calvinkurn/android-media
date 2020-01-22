package com.tokopedia.home.beranda.presentation.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TextureView
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
        const val ANIMATION_TRANSITION_NAME = "play_video"

    }
    private var componentListener: ComponentListener? = null
    private var contentFrame: FrameLayout? = null
    private var player: Player? = null
    private var surfaceView: TextureView? = null
    private var resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init{

        LayoutInflater.from(context).inflate(playerLayoutId, this)
        componentListener = ComponentListener()
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS

        // Content frame.
        contentFrame = findViewById(R.id.exo_content_frame)
//        contentFrame?.let { setResizeModeRaw(it, resizeMode) }

        surfaceView = TextureView(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            surfaceView?.transitionName = ANIMATION_TRANSITION_NAME
        }
        val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        surfaceView?.layoutParams = params
        contentFrame?.addView(surfaceView, 0)
    }

    private fun applyCrop(textureView: TextureView, width: Float, height: Float){
        val viewWidth = this.width.toFloat()
        val viewHeight = this.height.toFloat()
        val videoWidth = width
        val videoHeight = height
        val pivotX: Float
        val pivotY: Float

        val scaleFactor = if (videoHeight > videoWidth){
            // Portrait
            val previewRatio = videoHeight / videoWidth
            val viewFinderRatio = viewWidth / viewHeight
            val scaling = viewFinderRatio * previewRatio
            pivotX = 0f
            pivotY = viewHeight * 0.3f
            PointF(1f, scaling)
        } else {
            // Landscape
            val previewRatio = videoWidth / videoHeight
            val viewFinderRatio = viewHeight / viewWidth
            val scaling = viewFinderRatio * previewRatio
            pivotX = viewWidth * 0.5f
            pivotY = 0f
            PointF(scaling, 1f)
        }

        val matrix = Matrix()
        matrix.preScale(scaleFactor.x, scaleFactor.y, pivotX, pivotY)
        textureView.setTransform(matrix)
    }

    fun getSurfaceView() = surfaceView

    fun setPlayer(player: Player?){
        if (this.player != null) {
            val oldVideoComponent = this.player?.videoComponent
            if (oldVideoComponent != null) {
                oldVideoComponent.removeVideoListener(componentListener)
                oldVideoComponent.clearVideoTextureView(surfaceView)
            }
        }
        this.player = player
        if (player != null) {
            val newVideoComponent = player.videoComponent
            if (newVideoComponent != null) {
                newVideoComponent.setVideoTextureView(surfaceView)
                newVideoComponent.addVideoListener(componentListener)
            }
        }
    }

    inner class ComponentListener : VideoListener {
        // VideoListener implementation
        override fun onVideoSizeChanged(
                width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
            surfaceView?.let{ surfaceView ->
                applyCrop(surfaceView, width.toFloat(), height.toFloat())
            }
        }

        override fun onRenderedFirstFrame() {

        }

        override fun onSurfaceSizeChanged(width: Int, height: Int) {

        }
    }
}