package com.tokopedia.play.view.viewcomponent

import android.graphics.Bitmap
import android.view.Gravity
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 31/07/20
 */
class VideoViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val dataSource: DataSource
) : ViewComponent(container, idRes) {

    private val pvVideo = findViewById<PlayerView>(R.id.pv_video)
    private val ivThumbnail = findViewById<ImageView>(R.id.iv_thumbnail)

    fun setPlayer(exoPlayer: ExoPlayer?) {
        pvVideo.player = exoPlayer
    }

    fun setOrientation(screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation) {
        configureVideoLayout(screenOrientation, videoOrientation)
        configureThumbnailLayout(screenOrientation, videoOrientation)
    }

    fun getCurrentBitmap(): Bitmap? {
        val textureView = pvVideo.videoSurfaceView as? TextureView
        return textureView?.bitmap
    }

    fun showThumbnail(bitmap: Bitmap?) {
        if (bitmap != null) {
            ivThumbnail.setImageBitmap(bitmap)
            ivThumbnail.show()
        } else {
            ivThumbnail.hide()
        }
    }

    fun hideThumbnail() {
        ivThumbnail.hide()
    }

    fun getPlayerView(): PlayerView = pvVideo

    private fun configureVideoLayout(screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation) {

        fun configureVideo() {
            pvVideo.resizeMode = when {
                !videoOrientation.isHorizontal -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                screenOrientation.isLandscape -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                else -> AspectRatioFrameLayout.RESIZE_MODE_FIT
            }

            val lParams = pvVideo.layoutParams as FrameLayout.LayoutParams
            lParams.gravity =
                    if (videoOrientation.isHorizontal && !screenOrientation.isLandscape) Gravity.NO_GRAVITY
                    else Gravity.CENTER

            lParams.height = if (videoOrientation is VideoOrientation.Horizontal && !screenOrientation.isLandscape) {
                val heightRatioDouble = videoOrientation.heightRatio.toDouble()
                (heightRatioDouble/videoOrientation.widthRatio * getScreenWidth()).toInt()
            }
            else FrameLayout.LayoutParams.WRAP_CONTENT

            lParams.width =
                    if (videoOrientation is VideoOrientation.Horizontal && !screenOrientation.isLandscape) FrameLayout.LayoutParams.MATCH_PARENT
                    else FrameLayout.LayoutParams.WRAP_CONTENT

            pvVideo.layoutParams = lParams
        }

        fun configureBackground() {
            rootView.setBackgroundColor(MethodChecker.getColor(rootView.context,
                    if (videoOrientation.isHorizontal) {
                        if (!screenOrientation.isLandscape) R.color.play_dms_video_background
                        else R.color.play_dms_background
                    } else R.color.transparent
            ))
        }

        configureVideo()
        configureBackground()
    }

    private fun configureThumbnailLayout(screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation) {
        when {
            !screenOrientation.isLandscape && videoOrientation is VideoOrientation.Horizontal -> {
                rootView.changeConstraint {
                    clear(ivThumbnail.id, ConstraintSet.BOTTOM)
                    setDimensionRatio(ivThumbnail.id, "H, ${videoOrientation.aspectRatio}")
                }
                ivThumbnail.scaleType = ImageView.ScaleType.FIT_CENTER
            }
            else -> {
                rootView.changeConstraint {
                    connect(ivThumbnail.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                    setDimensionRatio(ivThumbnail.id, null)
                }
                ivThumbnail.scaleType = if (videoOrientation.isHorizontal) ImageView.ScaleType.FIT_CENTER else ImageView.ScaleType.CENTER
            }
        }
    }

    /**
     * Lifecycle Function
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (dataSource.isInPiPMode()) setPlayer(null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        setPlayer(null)
    }

    interface DataSource {

        fun isInPiPMode(): Boolean
    }
}