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
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.PiPInfoUiModel

/**
 * Created by jegul on 01/12/20
 */
class PlayViewerPiPCoordinator(
        context: Context,
        videoOrientation: VideoOrientation,
        pipInfoUiModel: PiPInfoUiModel,
        private val pipAdapter: FloatingWindowAdapter,
        private val listener: Listener
) {

    private val floatingView: FloatingWindowView

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

        val view = PlayViewerPiPView(context.applicationContext).also {
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
                onSuccess = { listener.onSucceededEnterPiPMode(floatingView.view as PlayViewerPiPView) },
                onFailure = listener::onFailedEnterPiPMode,
                onShouldRequestPermission = listener::onShouldRequestPermission
        )
    }

    interface Listener {

        fun onShouldRequestPermission(requestPermissionFlow: FloatingWindowPermissionManager.RequestPermissionFlow)
        fun onFailedEnterPiPMode(error: FloatingWindowException)
        fun onSucceededEnterPiPMode(view: PlayViewerPiPView)
    }
}