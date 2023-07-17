package com.tokopedia.analytics.performance.fpi

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.analytics.R
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * Created by yovi.putra on 05/07/23"
 * Project name: android-tokopedia-core
 **/

interface FpiMonitoringDelegate {
    fun onViewCreated(pageName: String, fragment: Fragment)

    fun onHiddenChanged(hidden: Boolean)

    fun getFrameMetric(): FragmentFramePerformanceIndexMonitoring
}

/**
 * Method 1 for measure frame performance
 */
class FpiMonitoringDelegateImpl :
    FpiMonitoringDelegate,
    FragmentFramePerformanceIndexMonitoring.OnFrameListener,
    LifecycleEventObserver {

    companion object {
        // ref to DeveloperOptionActivity.PREF_KEY_FPI_MONITORING_POPUP
        private const val PREF_KEY = "fpi_monitoring_popup"

        // base on median of main-app firebase performance
        private const val WARNING_PERCENTAGE = 40
        private const val DANGER_PERCENTAGE = 30
        private const val FPS_WARNING = 50
        private const val FPS_DANGER = 30
        private const val JANKY_FRAME_WARNING = 16
        private const val JANKY_FRAME_DANGER = 34

        private val COLOR_DEFAULT = com.tokopedia.unifyprinciples.R.color.Unify_GN500
        private val COLOR_WARNING = com.tokopedia.unifyprinciples.R.color.Unify_YN300
        private val COLOR_ERROR = com.tokopedia.unifyprinciples.R.color.Unify_RN500
    }

    // Core for fpi monitoring
    private val fpiMonitoring by lazy {
        FragmentFramePerformanceIndexMonitoring()
    }

    // Widget for Report UI
    private var fpiPopUp: PopupWindow? = null
    private var jankyInfoText: Typography? = null
    private var fpsInfoText: Typography? = null
    private var renderTimeText: Typography? = null

    override fun onViewCreated(pageName: String, fragment: Fragment) {
        fpiMonitoring.init(
            pageName = pageName,
            fragment = fragment,
            onFrameListener = this
        )

        if (isActive()) {
            fragment.lifecycle.addObserver(this)
            fragment.view?.let {
                bindView(view = it)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        fpiMonitoring.onFragmentHidden(isHidden = hidden)
    }

    override fun getFrameMetric(): FragmentFramePerformanceIndexMonitoring {
        return fpiMonitoring
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                fpiPopUp?.dismiss()
                fpiPopUp = null
                jankyInfoText = null
                fpsInfoText = null
                renderTimeText = null
            }

            else -> {
                // no-ops
            }
        }
    }

    private fun bindView(view: View) {
        val mContext = view.context ?: return
        val fpiLayout = LayoutInflater.from(mContext).inflate(R.layout.fpi_monitoring, null)
        val sizeParam = ViewGroup.LayoutParams.WRAP_CONTENT

        jankyInfoText = fpiLayout.findViewById(R.id.fpiPercentage)
        fpsInfoText = fpiLayout.findViewById(R.id.fpiFps)
        renderTimeText = fpiLayout.findViewById(R.id.fpiRenderTime)
        fpiPopUp = PopupWindow(fpiLayout, sizeParam, sizeParam).apply {
            setUpPopUp(popupWindow = this)
        }

        // use a global layout listener to prevent crashes when the activity has not been already
        view.addOneTimeGlobalLayoutListener {
            val halfScreen = getScreenWidth() - fpiLayout.width
            val toolbarHeight = getToolbarHeight(mContext)
            fpiPopUp?.showAtLocation(view, Gravity.NO_GRAVITY, halfScreen, toolbarHeight)
        }
    }

    // region popup
    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPopUp(popupWindow: PopupWindow) {
        var widgetDX = 0F
        var widgetDY = 0F

        popupWindow.setTouchInterceptor { v, event ->
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
            true
        }
    }

    private fun updatePositionPopup(x: Float, y: Float) {
        fpiPopUp?.update(x.toIntSafely(), y.toIntSafely(), -1, -1, true)
    }

    private fun getToolbarHeight(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
            actionBarHeight + 20.toPx()
        } else {
            0
        }
    }
    // endregion

    override fun onFrameRendered(fpiPerformanceData: FpiPerformanceData) {
        updatePercentage(fpiData = fpiPerformanceData)
        updateFps(fpiData = fpiPerformanceData)
        updateRenderingTime(fpiData = fpiPerformanceData)
    }

    // region percentage information
    private fun updatePercentage(fpiData: FpiPerformanceData) {
        val percentage = fpiData.framePerformanceIndex
        updatePercentageInfo(percentage = percentage)
        updatePercentageColor(percentage)
    }

    private fun updatePercentageInfo(percentage: Int) {
        val sPercentage = "$percentage%"
        jankyInfoText?.text = sPercentage
    }

    private fun updatePercentageColor(percentage: Int) {
        val context = fpiMonitoring.fragment?.context ?: return
        val activeColor = if (percentage > DANGER_PERCENTAGE) {
            COLOR_ERROR
        } else if (percentage > WARNING_PERCENTAGE) {
            COLOR_WARNING
        } else {
            COLOR_DEFAULT
        }

        val textColor = runCatching { ContextCompat.getColor(context, activeColor) }.getOrElse { 0 }

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
        val sFps = stringFormat("%.2f%s", fps, "fps")
        fpsInfoText?.text = sFps
    }

    private fun updateFpsColor(fps: Double) {
        val context = fpiMonitoring.fragment?.context ?: return
        val activeColor = if (fps < FPS_DANGER) {
            COLOR_ERROR
        } else if (fps < FPS_WARNING) {
            COLOR_WARNING
        } else {
            COLOR_DEFAULT
        }

        val textColor = runCatching { ContextCompat.getColor(context, activeColor) }.getOrElse { 0 }

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

        val sFps = stringFormat("%.2f%s", renderTime, lable)
        renderTimeText?.text = sFps
    }

    private fun updateRenderingTimeColor(duration: Double) {
        val context = fpiMonitoring.fragment?.context ?: return
        val activeColor = if (duration > JANKY_FRAME_DANGER) {
            COLOR_ERROR
        } else if (duration > JANKY_FRAME_WARNING) {
            COLOR_WARNING
        } else {
            COLOR_DEFAULT
        }

        val textColor = runCatching { ContextCompat.getColor(context, activeColor) }.getOrElse { 0 }

        renderTimeText?.setTextColor(textColor)
    }
    // endregion

    // region controller
    private fun isActive(): Boolean {
        val context = fpiMonitoring.fragment?.context ?: return false
        return GlobalConfig.DEBUG && context.isFpiMonitoringEnable()
    }

    private fun Context.isFpiMonitoringEnable(): Boolean = getSharedPreferences(
        PREF_KEY,
        BaseActivity.MODE_PRIVATE
    ).getBoolean(PREF_KEY, false)

    private fun stringFormat(format: String, vararg args: Any?) =
        String.format(Locale.getDefault(), format, args)
    // endregion
}
