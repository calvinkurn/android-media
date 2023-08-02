package com.tokopedia.analytics.performance.fpi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import com.tokopedia.analytics.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * Created by yovi.putra on 18/07/23"
 * Project name: android-tokopedia-core
 **/


class FrameMetricsPopupWindow(
    private val applicationContext: Context
) : PopupWindow() {

    companion object {
        // base on median of main-app firebase performance
        private const val WARNING_PERCENTAGE = 20
        private const val DANGER_PERCENTAGE = 30
        private const val FPS_WARNING = 50
        private const val FPS_DANGER = 30
        private const val JANKY_FRAME_WARNING = 16
        private const val JANKY_FRAME_DANGER = 34

        private val COLOR_DEFAULT = com.tokopedia.unifyprinciples.R.color.Unify_GN500
        private val COLOR_WARNING = com.tokopedia.unifyprinciples.R.color.Unify_YN300
        private val COLOR_ERROR = com.tokopedia.unifyprinciples.R.color.Unify_RN500
    }

    // Widget for Report UI
    private var fpiContainer: View? = null
    private var fpiPopUp: PopupWindow? = null
    private var jankyInfoText: Typography? = null
    private var fpsInfoText: Typography? = null
    private var renderTimeText: Typography? = null
    private var reloadIcon: IconUnify? = null

    private var reloadOnClick: (() -> Unit)? = null
    private val sizeParam = ViewGroup.LayoutParams.WRAP_CONTENT
    private var positionX = 0
    private var positionY = 0

    init {
        val fpiLayout =
            LayoutInflater.from(applicationContext).inflate(R.layout.fpi_monitoring, null)
        fpiContainer = fpiLayout?.findViewById(R.id.popup_container)
        jankyInfoText = fpiLayout?.findViewById(R.id.fpiPercentage)
        fpsInfoText = fpiLayout?.findViewById(R.id.fpiFps)
        renderTimeText = fpiLayout?.findViewById(R.id.fpiRenderTime)
        reloadIcon = fpiLayout?.findViewById(R.id.reload)
        fpiPopUp = PopupWindow(fpiLayout, sizeParam, sizeParam)
        setOnEvent()
    }

    fun show(activity: Activity, reloadOnClick: () -> Unit) {
        // use a global layout listener to prevent crashes when the activity has not been already
        this.reloadOnClick = reloadOnClick
        fpiPopUp?.dismiss()

        activity.findViewById<View>(android.R.id.content).let {
            it.addOneTimeGlobalLayoutListener {
                positionX = if (positionX == 0) getScreenWidth()
                else positionX
                fpiPopUp?.showAtLocation(it, Gravity.NO_GRAVITY, positionX, positionY)
            }
        }
    }


    private fun setOnEvent() {
        setOnDraggable()
        reloadIcon?.setOnClickListener {
            reloadOnClick?.invoke()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnDraggable() {
        var widgetDX = 0F
        var widgetDY = 0F

        fpiPopUp?.setTouchInterceptor { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    widgetDX = v.x - event.x
                    widgetDY = v.y - event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    // Screen border Collision
                    var newX = event.rawX + widgetDX
                    newX = 0F.coerceAtLeast(newX)

                    var newY = event.rawY + widgetDY
                    newY = 0F.coerceAtLeast(newY)
                    updatePositionPopup(x = newX, y = newY)
                }
            }
            false
        }
    }

    private fun updatePositionPopup(x: Float, y: Float) {
        positionX = x.toIntSafely()
        positionY = y.toIntSafely()
        fpiPopUp?.update(positionX, positionY, -1, -1, true)
    }

    fun updateInfo(fpiData: FpiPerformanceData) {
        updatePercentage(fpiData = fpiData)
        updateFps(fpiData = fpiData)
        updateRenderingTime(fpiData = fpiData)
    }

    // region percentage information
    private fun updatePercentage(fpiData: FpiPerformanceData) {
        val percentage = fpiData.jankyFramePercentage
        updatePercentageInfo(percentage = percentage)
        updatePercentageColor(percentage)
    }

    private fun updatePercentageInfo(percentage: Int) {
        val sPercentage = "$percentage%"
        jankyInfoText?.text = sPercentage
    }

    private fun updatePercentageColor(percentage: Int) {
        val activeColor = if (percentage > DANGER_PERCENTAGE) {
            COLOR_ERROR
        } else if (percentage > WARNING_PERCENTAGE) {
            COLOR_WARNING
        } else {
            COLOR_DEFAULT
        }

        val textColor =
            runCatching { ContextCompat.getColor(applicationContext, activeColor) }.getOrElse { 0 }

        jankyInfoText?.setTextColor(textColor)
    }
    // endregion

    // region fps information
    private fun updateFps(fpiData: FpiPerformanceData) {
        val fps = fpiData.fps
        updateFpsInfo(fps = fps)
        updateFpsColor(fps = fps)
    }

    private fun updateFpsInfo(fps: Double) {
        val sFps = String.format(Locale.getDefault(), "%.0f%s", fps, "fps")
        fpsInfoText?.text = sFps
    }

    private fun updateFpsColor(fps: Double) {
        val activeColor = if (fps < FPS_DANGER) {
            COLOR_ERROR
        } else if (fps < FPS_WARNING) {
            COLOR_WARNING
        } else {
            COLOR_DEFAULT
        }

        val textColor =
            runCatching { ContextCompat.getColor(applicationContext, activeColor) }.getOrElse { 0 }

        fpsInfoText?.setTextColor(textColor)
    }
    // endregion

    // region render time
    private fun updateRenderingTime(fpiData: FpiPerformanceData) {
        val duration = fpiData.totalDurationMs
        updateRenderingTimeInfo(duration = duration)
        updateRenderingTimeColor(duration = duration)
    }

    private fun updateRenderingTimeInfo(duration: Double) {
        var renderTime = duration
        var lable = "ms"

        if (duration > 1000) {
            renderTime = duration.div(1000.0)
            lable = "s"
        }

        val sFps = String.format(Locale.getDefault(), "%.2f%s", renderTime, lable)
        renderTimeText?.text = sFps
    }

    private fun updateRenderingTimeColor(duration: Double) {
        val activeColor = if (duration > JANKY_FRAME_DANGER) {
            COLOR_ERROR
        } else if (duration > JANKY_FRAME_WARNING) {
            COLOR_WARNING
        } else {
            COLOR_DEFAULT
        }

        val textColor =
            runCatching { ContextCompat.getColor(applicationContext, activeColor) }.getOrElse { 0 }

        renderTimeText?.setTextColor(textColor)
    }
    // endregion
}
