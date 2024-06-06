package com.tokopedia.play.view.viewcomponent

import android.graphics.Bitmap
import android.util.Log
import android.view.Gravity
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.ScreenOrientation2
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.type.isCompact
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
    private val flVideoWrapper = findViewById<FrameLayout>(R.id.fl_video_wrapper)

    fun setPlayer(exoPlayer: Player?) {
        pvVideo.player = exoPlayer
    }

    fun setOrientation(screenOrientation: ScreenOrientation2, videoOrientation: VideoOrientation) {
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

    fun showThumbnail(url: String) {
        ivThumbnail.show()
        ivThumbnail.loadImage(url)
    }

    fun hideThumbnail() {
        ivThumbnail.hide()
    }

    fun getPlayerView(): PlayerView = pvVideo

    private fun configureVideoLayout(screenOrientation: ScreenOrientation2, videoOrientation: VideoOrientation) {

        fun configureVideo() {
            pvVideo.resizeMode = if (videoOrientation.isVertical && screenOrientation.isCompact && screenOrientation.isPortrait) {
                AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            } else {
                AspectRatioFrameLayout.RESIZE_MODE_FIT
            }

            val lParams = flVideoWrapper.layoutParams as ConstraintLayout.LayoutParams

            lParams.height = if (videoOrientation is VideoOrientation.Horizontal && !screenOrientation.isLandscape) {
                val heightRatioDouble = videoOrientation.heightRatio.toDouble()
                (heightRatioDouble/videoOrientation.widthRatio * getScreenWidth()).toInt()
            } else {
                ConstraintLayout.LayoutParams.MATCH_PARENT
            }
            flVideoWrapper.layoutParams = lParams
        }

        fun configureBackground() {
            rootView.setBackgroundColor(
                MethodChecker.getColor(
                    rootView.context,
                    if (videoOrientation.isHorizontal) {
                        if (!screenOrientation.isLandscape) R.color.play_dms_video_background
                        else R.color.play_dms_background
                    } else com.tokopedia.universal_sharing.R.color.transparent
                )
            )
        }

        configureVideo()
        configureBackground()
    }

    private fun configureThumbnailLayout(screenOrientation: ScreenOrientation2, videoOrientation: VideoOrientation) {
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
