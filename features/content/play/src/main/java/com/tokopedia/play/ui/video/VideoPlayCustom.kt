package com.tokopedia.play.ui.video

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.play.R
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation


@SuppressLint("SyntheticAccessor")
class VideoPlayCustom(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {
    companion object{
        private val playerLayoutId = R.layout.video_play_custom
    }
    private val componentListener: ComponentListener
    private var bufferingView: View? = null
    private var contentFrame: AspectRatioFrameLayout? = null
    private var player: Player? = null
    private var surfaceView: TextureView? = null
    private var textureViewRotation = 0

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    val textureView: TextureView?
        get() = surfaceView

    init{
        LayoutInflater.from(context).inflate(playerLayoutId, this)
        componentListener = ComponentListener()
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS

        // Content frame.
        contentFrame = findViewById(com.google.android.exoplayer2.ui.R.id.exo_content_frame)

        surfaceView = TextureView(context)
        surfaceView?.id = R.id.fl_texture_view
        val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        surfaceView?.layoutParams = params
        contentFrame?.addView(surfaceView, 0)
        val newVideoComponent = player?.videoComponent
        newVideoComponent?.setVideoTextureView(surfaceView)
        newVideoComponent?.addVideoListener(componentListener)
        player?.addListener(componentListener)

        // Buffering view.
        bufferingView = findViewById(com.google.android.exoplayer2.ui.R.id.exo_buffering)
        bufferingView?.visibility = View.GONE
    }

    private fun onContentAspectRatioChanged(
            contentAspectRatio: Float,
            contentFrame: AspectRatioFrameLayout?) {
        contentFrame?.setAspectRatio(contentAspectRatio)
    }

    private fun setResizeModeRaw(aspectRatioFrame: AspectRatioFrameLayout, resizeMode: Int) {
        aspectRatioFrame.resizeMode = resizeMode
    }

    private fun applyTextureViewRotation(textureView: TextureView, textureViewRotation: Int) {
        val textureViewWidth = textureView.width.toFloat()
        val textureViewHeight = textureView.height.toFloat()
        if (textureViewWidth == 0f || textureViewHeight == 0f || textureViewRotation == 0) {
            textureView.setTransform(null)
        } else {
            val transformMatrix = Matrix()
            val pivotX = textureViewWidth / 2
            val pivotY = textureViewHeight / 2
            transformMatrix.postRotate(textureViewRotation.toFloat(), pivotX, pivotY)
            // After rotation, scale the rotated texture to fit the TextureView size.
            val originalTextureRect = RectF(0f, 0f, textureViewWidth, textureViewHeight)
            val rotatedTextureRect = RectF()
            transformMatrix.mapRect(rotatedTextureRect, originalTextureRect)

            transformMatrix.postScale(
                    textureViewWidth / rotatedTextureRect.width(),
                    textureViewHeight / rotatedTextureRect.height(),
                    pivotX,
                    pivotY)
            textureView.setTransform(transformMatrix)
        }
    }

    fun setPlayer(player: Player?){
        if (this.player != null) {
            this.player?.removeListener(componentListener)
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
            player.addListener(componentListener)
        }
    }

    fun setOrientation(screen: ScreenOrientation, video: VideoOrientation) {
        contentFrame?.let {
            setResizeModeRaw(it, if (video.isHorizontal) {
                when {
                    screen.isLandscape -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                    else -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                }
            } else AspectRatioFrameLayout.RESIZE_MODE_ZOOM)

            val lParams = it.layoutParams as FrameLayout.LayoutParams
            lParams.gravity =
                    if (video.isHorizontal && !screen.isLandscape) Gravity.NO_GRAVITY
                    else Gravity.CENTER

            it.layoutParams = lParams
        }
    }

    fun release(){
        player?.removeListener(componentListener)
        clearSurface()
    }

    private fun clearSurface(){
        surfaceView?.surfaceTextureListener = null
        surfaceView = null
    }

    inner class ComponentListener : Player.EventListener, VideoListener, OnLayoutChangeListener {
        // VideoListener implementation
        override fun onVideoSizeChanged(
                width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
            var videoAspectRatio: Float = if (height == 0 || width == 0) 1f else width * pixelWidthHeightRatio / height
            surfaceView?.let{
                if (surfaceView is TextureView) { // Try to apply rotation transformation when our surface is a TextureView.
                    if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) { // We will apply a rotation 90/270 degree to the output texture of the TextureView.
                        // In this case, the output video's width and height will be swapped.
                        videoAspectRatio = 1 / videoAspectRatio
                    }
                    if (textureViewRotation != 0) {
                        surfaceView!!.removeOnLayoutChangeListener(this)
                    }
                    textureViewRotation = unappliedRotationDegrees
                    if (textureViewRotation != 0) { // The texture view's dimensions might be changed after layout step.
                        // So add an OnLayoutChangeListener to apply rotation after layout step.
                        surfaceView!!.addOnLayoutChangeListener(this)
                    }
                    applyTextureViewRotation(it, textureViewRotation)
                }
            }
            onContentAspectRatioChanged(videoAspectRatio, contentFrame)
        }

        override fun onRenderedFirstFrame() {
        }

        override fun onSurfaceSizeChanged(width: Int, height: Int) {

        }

        // OnLayoutChangeListener implementation
        override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int) {
            applyTextureViewRotation(view as TextureView, textureViewRotation)
        }
    }
}