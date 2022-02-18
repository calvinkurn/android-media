package com.tokopedia.productcard

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IntDef
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.video.VideoListener

class ProductCardVideoView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : FrameLayout(context, attrs, defStyleAttr)  {
    companion object{
        private val playerLayoutId = R.layout.product_card_video_view

        private const val PIVOT_MULTIPLIER_CENTER_CROP = 0.5f
        private const val PIVOT_MULTIPLIER_FIT_CENTER = 0.5f
    }
    private val componentListener: ComponentListener
    private var contentFrame: AspectRatioFrameLayout? = null
    private var player: Player? = null
    private var surfaceView: TextureView? = null
    private var resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
    private var scaleType = ScaleType.TYPE_CENTER_CROP

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init{
        LayoutInflater.from(context).inflate(playerLayoutId, this)
        componentListener = ComponentListener()
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS

        applyStyledAttributes(context, attrs, defStyleAttr)

        // Content frame.
        contentFrame = findViewById(R.id.exo_content_frame)
        updateResizeModeBasedOnScaleType(scaleType)

        surfaceView = TextureView(context)
        surfaceView?.id = R.id.product_video_texture
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        surfaceView?.layoutParams = params
        contentFrame?.addView(surfaceView, 0)
    }

    fun setScaleType(@ScaleType scaleType: Int) {
        this.scaleType = scaleType
        updateResizeModeBasedOnScaleType(scaleType)
    }

    @ScaleType
    fun getScaleType() : Int {
        return this.scaleType
    }

    private fun updateResizeModeBasedOnScaleType(@ScaleType scaleType: Int) {
        when(scaleType) {
            ScaleType.TYPE_CENTER_CROP -> setResizeModeZoom()
            ScaleType.TYPE_FIT_CENTER -> setResizeModeFit()
        }
    }

    private fun setResizeModeZoom() {
        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        contentFrame?.resizeMode = resizeMode
    }

    private fun setResizeModeFit() {
        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        contentFrame?.resizeMode = resizeMode
    }

    private fun applyStyledAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.ProductVideoView, defStyleAttr, 0)
            .apply {
                try {
                    scaleType = getInteger(
                        R.styleable.ProductVideoView_scaleType,
                        ScaleType.TYPE_CENTER_CROP
                    )
                } finally {
                    recycle()
                }
            }
    }

    private fun adjustVideoScale(textureView: TextureView, videoWidth: Float, videoHeight: Float){
        val viewWidth = this.width.toFloat()
        val viewHeight = this.height.toFloat()

        val matrix = when(scaleType){
            ScaleType.TYPE_FIT_CENTER -> getFitCenterMatrix(viewWidth, viewHeight, videoWidth, videoHeight)
            else -> getCenterCropMatrix(viewWidth, viewHeight, videoWidth, videoHeight)
        }
        textureView.setTransform(matrix)
        contentFrame?.setAspectRatio(viewWidth / viewHeight)
    }

    private fun getFitCenterMatrix(
        viewWidth: Float,
        viewHeight: Float,
        videoWidth: Float,
        videoHeight: Float
    ) : Matrix {
        val pivotX: Float
        val pivotY: Float
        val scaleFactor = when {
            videoHeight > videoWidth -> {
                // Portrait
                val previewRatio = videoWidth / videoHeight
                val viewFinderRatio = viewWidth / viewHeight
                val scaling = viewFinderRatio * previewRatio
                pivotX = viewWidth * PIVOT_MULTIPLIER_FIT_CENTER
                pivotY = 0f
                PointF(scaling, 1f)
            }
            videoWidth > videoHeight -> {
                // Landscape
                val previewRatio = videoHeight / videoWidth
                val viewFinderRatio = viewWidth / viewHeight
                val scaling = viewFinderRatio * previewRatio
                pivotX = 0f
                pivotY = viewHeight * PIVOT_MULTIPLIER_FIT_CENTER
                PointF(1f, scaling)
            }
            else -> {
                // 1:1
                pivotX = 0f
                pivotY = 0f
                PointF(1f, 1f)
            }
        }

        return getPreScaleMatrix(scaleFactor, pivotX, pivotY)
    }

    private fun getCenterCropMatrix(
        viewWidth: Float,
        viewHeight: Float,
        videoWidth: Float,
        videoHeight: Float
    ) : Matrix {
        val pivotX: Float
        val pivotY: Float

        val scaleFactor = when {
            videoHeight > videoWidth -> {
                // Portrait
                val previewRatio = videoHeight / videoWidth
                val viewFinderRatio = viewWidth / viewHeight
                val scaling = viewFinderRatio * previewRatio
                pivotX = 0f
                pivotY = viewHeight * PIVOT_MULTIPLIER_CENTER_CROP
                PointF(1f, scaling)
            }
            videoWidth > videoHeight -> {
                // Landscape
                val previewRatio = videoWidth / videoHeight
                val viewFinderRatio = viewWidth / viewHeight
                val scaling = viewFinderRatio * previewRatio
                pivotX = viewWidth * PIVOT_MULTIPLIER_CENTER_CROP
                pivotY = 0f
                PointF(scaling, 1f)
            }
            else -> {
                // 1:1
                pivotX = 0f
                pivotY = 0f
                PointF(1f, 1f)
            }
        }

        return getPreScaleMatrix(scaleFactor, pivotX, pivotY)
    }

    private fun getPreScaleMatrix(
        scaleFactor: PointF,
        pivotX: Float,
        pivotY: Float
    ) :Matrix {
        return Matrix().apply {
            preScale(scaleFactor.x, scaleFactor.y, pivotX, pivotY)
        }
    }

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

    internal inner class ComponentListener : VideoListener {
        // VideoListener implementation
        override fun onVideoSizeChanged(
            width: Int, height: Int,
            unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float
        ) {
            surfaceView?.let{ surfaceView ->
                adjustVideoScale(surfaceView, width.toFloat(), height.toFloat())
            }
        }
    }

    @IntDef(ScaleType.TYPE_CENTER_CROP, ScaleType.TYPE_FIT_CENTER)
    annotation class ScaleType {
        companion object {
            const val TYPE_CENTER_CROP = 0
            const val TYPE_FIT_CENTER = 1
        }
    }
}