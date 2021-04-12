package com.tokopedia.play.util

import android.content.Context
import android.graphics.Point
import com.tokopedia.floatingwindow.FloatingWindowAdapter
import com.tokopedia.floatingwindow.exception.FloatingWindowException
import com.tokopedia.floatingwindow.permission.FloatingWindowPermissionManager
import com.tokopedia.floatingwindow.view.FloatingWindowView
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.pip.PlayViewerPiPView
import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PiPInfoUiModel
import com.tokopedia.play_common.player.PlayVideoWrapper

/**
 * Created by jegul on 01/12/20
 */
class PlayViewerPiPCoordinator(
        context: Context,
        videoPlayer: PlayVideoWrapper,
        videoOrientation: VideoOrientation,
        private val pipInfoUiModel: PiPInfoUiModel,
        private val pipAdapter: FloatingWindowAdapter,
        private val listener: Listener
) {

    private val floatingView: FloatingWindowView

    val view: PlayViewerPiPView

    init {
        val scaleFactor =
                if (videoOrientation is VideoOrientation.Horizontal) 0.6f
                else 0.3f

        val screenWidth = getScreenWidth()
        val screenHeight = getScreenHeight()
        val (width, height) = if (videoOrientation is VideoOrientation.Horizontal) {
            screenWidth to (videoOrientation.heightRatio.toFloat()/videoOrientation.widthRatio * screenWidth).toInt()
        } else {
            screenWidth to screenHeight
        }

        view = PlayViewerPiPView(context.applicationContext).also {
            it.setPlayer(videoPlayer)
            it.setPiPInfo(pipInfoUiModel)
        }

        val scaledWidth = (scaleFactor * width).toInt()
        val scaledHeight = (scaleFactor * height).toInt()

        floatingView = FloatingWindowView.Builder(
                key = PlayVideoFragment.FLOATING_WINDOW_KEY,
                view = view,
                width = scaledWidth,
                height = scaledHeight,
        ).setX(screenWidth - scaledWidth - 16)
                .setY(screenHeight - scaledHeight - 16)
                .build()

        floatingView.doOnDragged { layouter, point ->
            val newPoint = Point()
            val startLimit = 16
            val endLimit = layouter.screenWidth - layouter.viewWidth - 16
            val topLimit = 16
            val bottomLimit = layouter.screenHeight - layouter.viewHeight - 16

            newPoint.x = when {
                point.x > endLimit -> endLimit
                point.x < startLimit -> startLimit
                else -> point.x
            }

            newPoint.y = when {
                point.y > bottomLimit -> bottomLimit
                point.y < topLimit -> topLimit
                else -> point.y
            }

            layouter.updatePosition(newPoint.x, newPoint.y)
        }
    }

    fun startPip() {
        pipAdapter.addView(
                floatingView = floatingView,
                overwrite = true,
                onSuccess = {
                    listener.onSucceededEnterPiPMode(floatingView.view as PlayViewerPiPView)
                    if (pipInfoUiModel.pipMode is PiPMode.BrowsingOtherPage) listener.onShouldOpenApplink(pipInfoUiModel.pipMode.applinkModel)
                },
                onFailure = listener::onFailedEnterPiPMode,
                onShouldRequestPermission = { listener.onShouldRequestPermission(pipMode = pipInfoUiModel.pipMode, requestPermissionFlow = it) }
        )
    }

    interface Listener {

        fun onShouldRequestPermission(pipMode: PiPMode, requestPermissionFlow: FloatingWindowPermissionManager.RequestPermissionFlow)
        fun onFailedEnterPiPMode(error: FloatingWindowException)
        fun onSucceededEnterPiPMode(view: PlayViewerPiPView)

        fun onShouldOpenApplink(applinkModel: OpenApplinkUiModel)
    }
}