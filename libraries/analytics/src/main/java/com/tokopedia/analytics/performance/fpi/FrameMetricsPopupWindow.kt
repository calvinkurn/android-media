package com.tokopedia.analytics.performance.fpi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tokopedia.analytics.R
import com.tokopedia.analytics.performance.perf.performanceTracing.AppPerformanceTrace
import com.tokopedia.analytics.performance.perf.performanceTracing.data.DevState
import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData
import com.tokopedia.analytics.performance.perf.performanceTracing.data.State
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.visible
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
        private var EXPANDED = false
        private var SHOW = true

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
    private var frameCountText: Typography? = null

    private var activityNameText: Typography? = null
    private var ttflText: Typography? = null
    private var ttilText: Typography? = null
    private var perfNotesText: Typography? = null
    private var btnExpandGroup: FrameLayout? = null
    private var iconExpand: IconUnify? = null
    private var textExpand: Typography? = null
    private var groupSummary: LinearLayout? = null
    private var iconClose: IconUnify? = null

    private var reloadOnClick: (() -> Unit)? = null
    private val sizeParam = ViewGroup.LayoutParams.WRAP_CONTENT
    private var positionX = 0
    private var positionY = 0

    init {
        val fpiLayout =
            LayoutInflater.from(applicationContext).inflate(R.layout.fpi_monitoring, null)
        fpiContainer = fpiLayout?.findViewById(R.id.popup_container)
        frameCountText = fpiLayout?.findViewById(R.id.frameCount)
        jankyInfoText = fpiLayout?.findViewById(R.id.fpiPercentage)
        fpsInfoText = fpiLayout?.findViewById(R.id.fpiFps)
        renderTimeText = fpiLayout?.findViewById(R.id.fpiRenderTime)
        reloadIcon = fpiLayout?.findViewById(R.id.reload)

        val widthInPixels = (250 * fpiLayout.context.resources.displayMetrics.density).toInt()

        fpiPopUp = PopupWindow(fpiLayout, widthInPixels, sizeParam)

        activityNameText = fpiLayout?.findViewById(R.id.activityName)
        ttflText = fpiLayout?.findViewById(R.id.launchTimeTTFL)
        ttilText = fpiLayout?.findViewById(R.id.launchTimeTTIL)
        perfNotesText = fpiLayout?.findViewById(R.id.perf_notes)

        btnExpandGroup = fpiLayout?.findViewById(R.id.btn_expand)
        textExpand = fpiLayout?.findViewById(R.id.text_expand)
        iconExpand = fpiLayout?.findViewById(R.id.icon_expand)
        groupSummary = fpiLayout?.findViewById(R.id.group_summary)
        iconClose = fpiLayout?.findViewById(R.id.icon_close)

        btnExpandGroup?.setOnClickListener { 
            if (EXPANDED) {
                groupSummary?.gone()
                textExpand?.text = "View Summary"
                iconExpand?.setImage(
                    newIconId = IconUnify.CHEVRON_DOWN
                )
                EXPANDED = false
            } else {
                groupSummary?.visible()
                textExpand?.text = "Hide Summary"
                iconExpand?.setImage(
                    newIconId = IconUnify.CHEVRON_UP
                )
                EXPANDED = true
            }
        }
        
        iconClose?.setOnClickListener { 
            SHOW = false
            fpiPopUp?.dismiss()
            it.context.setFpiMonitoringState(false)
            Toast.makeText(
                it.context, 
                "Performance monitoring dismissed, enable via Developer Options -> Enable Universal Performance Trace",
                Toast.LENGTH_LONG
            ).show()
        }

        setOnEvent()
    }

    fun show(activity: Activity, reloadOnClick: () -> Unit) {
        // use a global layout listener to prevent crashes when the activity has not been already
        if (SHOW) {
            this.reloadOnClick = reloadOnClick
            fpiPopUp?.dismiss()

            activity.findViewById<View>(android.R.id.content).let {
                it.addOneTimeGlobalLayoutListener {
                    positionX = if (positionX == 0) {
                        getScreenWidth()
                    } else {
                        positionX
                    }
                    fpiPopUp?.showAtLocation(it, Gravity.NO_GRAVITY, positionX, positionY)
                }
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

    fun updatePerformanceInfo() {
        // performance data from AppPerformanceTrace
        updatePerformanceData(
            AppPerformanceTrace.currentAppPerformanceTraceData,
            AppPerformanceTrace.currentAppPerformanceDevState,
            AppPerformanceTrace.perfNotes
        )
    }

    fun updateInfo(fpiData: FpiPerformanceData) {
        updateFrameCount(fpiData.allFrames)
        updatePercentage(fpiData = fpiData)
        updateFps(fpiData = fpiData)
        updateRenderingTime(fpiData = fpiData)

        // performance data from AppPerformanceTrace
        updatePerformanceInfo()
    }

    // region percentage information
    private fun updatePercentage(fpiData: FpiPerformanceData) {
        val percentage = fpiData.jankyFramePercentage
        updatePercentageInfo(percentage = percentage)
        updatePercentageColor(percentage)
    }

    private fun updateFrameCount(count: Int) {
        val sPercentage = "Frame Count: $count"
        frameCountText?.text = sPercentage
    }

    private fun updatePercentageInfo(percentage: Int) {
        val sPercentage = "Jank %: $percentage%"
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
        val sFps = String.format(Locale.getDefault(), "FPS: %.0f%s", fps, "fps")
        fpsInfoText?.text = sFps
    }

    private fun updatePerformanceData(
        data: PerformanceTraceData?,
        state: DevState,
        perfNotes: String
    ) {
        when (state.state) {
            State.PERF_MEASURING -> {
                activityNameText?.text = "Measuring performance..."
                ttflText?.text = "..."
                ttilText?.text = "..."
                perfNotesText?.text = ""
            }
            State.PERF_ENABLED -> {
                if (data != null) {
                    data?.let {
                        activityNameText?.text = String.format(Locale.getDefault(), "%s (%s)", data.activityName, data.traceName)
                        ttflText?.text = String.format(Locale.getDefault(), "TTFL: %d%s", data.timeToFirstLayout, "ms")
                        ttilText?.text = String.format(Locale.getDefault(), "TTIL: %d%s", data.timeToInitialLoad, "ms")
                        perfNotesText?.text = perfNotes
                    }
                } else {
                    activityNameText?.text = "Performance not started."
                    ttflText?.text = "-"
                    ttilText?.text = "-"
                }
            }
            State.PERF_RESUMED -> {
                activityNameText?.text = String.format(
                    Locale.getDefault(),
                    "%s",
                    "Activity resumed"
                )
                ttflText?.text = "-"
                ttilText?.text = "-"
                perfNotesText?.text = ""
            }
            State.PERF_DISABLED -> {
                activityNameText?.text = String.format(
                    Locale.getDefault(),
                    "%s",
                    "Perf disabled"
                )
                ttflText?.text = "-"
                ttilText?.text = "-"
            }
            State.PERF_ERROR -> {
                activityNameText?.text = String.format(
                    Locale.getDefault(),
                    "%s",
                    "Perf Error"
                )
                ttflText?.text = "$perfNotes"
                ttilText?.text = ""
                perfNotesText?.text = ""
            }
        }
        
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

        val sFps = String.format(Locale.getDefault(), "Frame Dur: %.2f%s", renderTime, lable)
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

    private fun Context.setFpiMonitoringState(state: Boolean) {
        val sharedPref = getSharedPreferences(
            "fpi_monitoring_popup",
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit().putBoolean(
            "fpi_monitoring_popup",
            state
        )
        editor.apply()
    }
}
