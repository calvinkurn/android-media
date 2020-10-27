package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.TextureView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.R
import com.google.android.exoplayer2.video.VideoListener

/**
 * Created by jegul on 27/10/20
 */
class PlayWidgetPlayerView : PlayerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val mVideoListener = object : VideoListener {

        override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
            val surfaceView = videoSurfaceView
            if (surfaceView is TextureView) updateTextureViewSize(surfaceView, width, height)
        }
    }

    override fun setPlayer(player: Player?) {
        val oldVideoComponent = getPlayer()?.videoComponent
        oldVideoComponent?.removeVideoListener(mVideoListener)
        super.setPlayer(player)
        val newVideoComponent = player?.videoComponent
        newVideoComponent?.addVideoListener(mVideoListener)
    }

    private fun updateTextureViewSize(textureView: TextureView, videoWidth: Int, videoHeight: Int) {
        if (videoWidth > videoHeight) updateTextureViewSizeHorizontal(textureView, videoWidth, videoHeight)
        else updateTextureViewSizeVertical(textureView, videoWidth, videoHeight)

        findViewById<AspectRatioFrameLayout>(R.id.exo_content_frame)
                .setAspectRatio(textureView.width / textureView.height.toFloat())
    }

    private fun updateTextureViewSizeHorizontal(textureView: TextureView, videoWidth: Int, videoHeight: Int) {
        val viewWidth = textureView.width
        val viewHeight = textureView.height

        val scaleFactors = if (viewHeight <= viewWidth) {
            //TODO("This might be wrong, need to recheck again")
            val previewRatio = videoWidth / videoHeight.toFloat()
            val viewFinderRatio = viewWidth / viewHeight.toFloat()
            val scaling = viewFinderRatio * previewRatio
            PointF(1f, scaling)
        } else {
            val previewRatio = videoWidth / videoHeight.toFloat()
            val viewFinderRatio = viewHeight / viewWidth.toFloat()
            val scaling = viewFinderRatio * previewRatio
            PointF(scaling, 1f)
        }

        val centerX = viewWidth.toFloat() / 2
        val centerY = viewHeight.toFloat() / 2

        val videoMatrix = Matrix()
        videoMatrix.preScale(scaleFactors.x, scaleFactors.y, centerX, centerY)

        textureView.setTransform(videoMatrix)
    }

    private fun updateTextureViewSizeVertical(textureView: TextureView, videoWidth: Int, videoHeight: Int) {
        val viewWidth = textureView.width
        val viewHeight = textureView.height

        val scaleFactors = if (viewHeight <= viewWidth) {
            //TODO("This might be wrong, need to recheck again")
            val previewRatio = videoWidth / videoHeight.toFloat()
            val viewFinderRatio = viewWidth / viewHeight.toFloat()
            val scaling = viewFinderRatio * previewRatio
            PointF(1f, scaling)
        } else {
            val previewRatio = videoHeight / videoWidth.toFloat()
            val viewFinderRatio = viewWidth / viewHeight.toFloat()
            val scaling = viewFinderRatio * previewRatio

            PointF(1f, scaling)
        }

        val centerX = viewWidth.toFloat() / 2
        val centerY = viewHeight.toFloat() / 2

        val videoMatrix = Matrix()
        videoMatrix.preScale(scaleFactors.x, scaleFactors.y, centerX, centerY)

        textureView.setTransform(videoMatrix)
    }
}