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
import com.tokopedia.play.util.blur.ImageBlurUtil
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 31/07/20
 */
class VideoViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val pvVideo = findViewById<PlayerView>(R.id.pv_video)
    private val ivThumbnail = findViewById<ImageView>(R.id.iv_thumbnail)

    private var mExoPlayer: ExoPlayer? = null

    private lateinit var blurUtil: ImageBlurUtil

    fun setPlayer(exoPlayer: ExoPlayer?) {
        pvVideo.player = exoPlayer
    }

    fun setOrientation(screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation) {
        configureVideoLayout(screenOrientation, videoOrientation)
        configureThumbnailLayout(screenOrientation, videoOrientation)
    }

    fun showBlurredThumbnail() {
        val currentThumbnail = getCurrentBitmap()
        showThumbnail(
                currentThumbnail?.let {
                    getBlurUtil().blurImage(it, radius = 25f)
                }
        )
    }

    fun hideBlurredThumbnail() {
        showThumbnail(null)
    }

    private fun getCurrentBitmap(): Bitmap? {
        val textureView = pvVideo.videoSurfaceView as? TextureView
        return textureView?.bitmap
    }

    private fun showThumbnail(bitmap: Bitmap?) {
        if (bitmap != null) {
            ivThumbnail.setImageBitmap(bitmap)
            ivThumbnail.show()
        } else {
            ivThumbnail.hide()
        }
    }

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
                    if (videoOrientation.isHorizontal && !screenOrientation.isLandscape) R.color.play_solid_black
                    else R.color.transparent
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

    private fun getBlurUtil(): ImageBlurUtil {
        if (!::blurUtil.isInitialized) {
            blurUtil = ImageBlurUtil(ivThumbnail.context)
        }
        return blurUtil
    }

    /**
     * Lifecycle Function
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mExoPlayer = pvVideo.player as? ExoPlayer
        setPlayer(null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mExoPlayer?.let { setPlayer(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        setPlayer(null)
        if (::blurUtil.isInitialized) blurUtil.close()
    }
}