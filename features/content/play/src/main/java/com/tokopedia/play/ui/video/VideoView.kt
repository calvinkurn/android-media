package com.tokopedia.play.ui.video

import android.graphics.Bitmap
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 02/12/19
 */
class VideoView(container: ViewGroup) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_video, container, true)
                    .findViewById(R.id.cl_video_view)

    private val pvVideo = view.findViewById<PlayerView>(R.id.pv_video)
    private val ivThumbnail = view.findViewById<ImageView>(R.id.iv_thumbnail)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun onDestroy() {
        setPlayer(null)
    }

    internal fun setPlayer(exoPlayer: ExoPlayer?) {
        pvVideo.player = exoPlayer
    }

    internal fun setOrientation(screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation) {
        configureVideoLayout(screenOrientation, videoOrientation)
        configureThumbnailLayout(screenOrientation, videoOrientation)
    }

    internal fun getCurrentBitmap(): Bitmap? {
        val textureView = pvVideo.videoSurfaceView as? TextureView
        return textureView?.bitmap
    }

    internal fun showThumbnail(bitmap: Bitmap?) {
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
            view.setBackgroundColor(MethodChecker.getColor(view.context,
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
                view.changeConstraint {
                    clear(ivThumbnail.id, ConstraintSet.BOTTOM)
                    setDimensionRatio(ivThumbnail.id, "H, ${videoOrientation.aspectRatio}")
                }
                ivThumbnail.scaleType = ImageView.ScaleType.FIT_CENTER
            }
            else -> {
                view.changeConstraint {
                    connect(ivThumbnail.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                    setDimensionRatio(ivThumbnail.id, null)
                }
                ivThumbnail.scaleType = if (videoOrientation.isHorizontal) ImageView.ScaleType.FIT_CENTER else ImageView.ScaleType.CENTER
            }
        }
    }
}