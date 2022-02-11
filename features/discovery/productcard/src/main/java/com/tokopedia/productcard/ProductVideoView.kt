package com.tokopedia.productcard

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.video.VideoListener

class ProductVideoView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : FrameLayout(context, attrs, defStyleAttr)  {
    companion object{
        private val playerLayoutId = R.layout.product_video_view
    }
    private val componentListener: ComponentListener
    private var contentFrame: AspectRatioFrameLayout? = null
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
        contentFrame?.resizeMode = resizeMode

        surfaceView = TextureView(context)
        surfaceView?.id = R.id.product_video_texture
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
            pivotY = viewHeight * 0.12f
            PointF(1f, scaling)
        } else {
            // Landscape
            pivotX = 0f
            pivotY = 0f
            PointF(1f, 1f)
        }

        val matrix = Matrix()
        matrix.preScale(scaleFactor.x, scaleFactor.y, pivotX, pivotY)
        textureView.setTransform(matrix)
        contentFrame?.setAspectRatio(viewWidth / viewHeight)
    }

    fun applyZoom(){
        contentFrame?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    fun resetZoom(){
        contentFrame?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
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
    }
}