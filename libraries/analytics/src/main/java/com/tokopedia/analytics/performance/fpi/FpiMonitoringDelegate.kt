package com.tokopedia.analytics.performance.fpi

import android.annotation.SuppressLint
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
import com.tokopedia.analytics.R
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by yovi.putra on 05/07/23"
 * Project name: android-tokopedia-core
 **/

interface FpiMonitoringDelegate {
    fun onViewCreated(fragment: Fragment)

    fun onHiddenChanged(hidden: Boolean)
}

/**
 * Method 1 for measure frame performance
 */
class FpiMonitoringDelegateImpl :
    FpiMonitoringDelegate,
    FragmentFramePerformanceIndexMonitoring.OnFrameListener,
    LifecycleEventObserver {

    // Core for fpi monitoring
    private val fpiMonitoring by lazy {
        FragmentFramePerformanceIndexMonitoring()
    }

    // Widget for Report UI
    private var fpiPopUp: PopupWindow? = null
    private var jankyInfoText: Typography? = null
    private var fpsInfoText: Typography? = null

    private val defaultColor = com.tokopedia.unifyprinciples.R.color.Unify_GN500
    private val warningColor = com.tokopedia.unifyprinciples.R.color.Unify_YN300
    private val errorColor = com.tokopedia.unifyprinciples.R.color.Unify_RN600
    private val warningPercentage = 40
    private val dangerPercentage = 30
    private val warningFps = 50
    private val dangerFps = 30

    override fun onViewCreated(fragment: Fragment) {
        fpiMonitoring.init(
            pageName = fragment.javaClass.simpleName,
            fragment = fragment,
            onFrameListener = this
        )
        fragment.lifecycle.addObserver(this)
        fragment.view?.let {
            bindView(view = it)
        }
    }

    private fun bindView(view: View) {
        val mContext = view.context ?: return
        val fpiLayout = LayoutInflater.from(mContext).inflate(R.layout.fpi_monitoring, null)
        val sizeParam = ViewGroup.LayoutParams.WRAP_CONTENT

        jankyInfoText = fpiLayout.findViewById(R.id.fpiPercentage)
        fpsInfoText = fpiLayout.findViewById(R.id.fpiFps)
        fpiPopUp = PopupWindow(fpiLayout, sizeParam, sizeParam).apply {
            setUpPopUp(popupWindow = this)
        }

        view.addOneTimeGlobalLayoutListener {
            fpiPopUp?.showAtLocation(view, (Gravity.CENTER or Gravity.TOP), 0, 0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPopUp(popupWindow: PopupWindow) {
        var widgetDX = 0F
        var widgetDY = 0F
        var widgetXOrigin = 0F
        var widgetYOrigin = 0F

        popupWindow.setTouchInterceptor { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    widgetDX = v.x - event.rawX
                    widgetDY = v.y - event.rawY
                    // save widget origin coordinate
                    widgetXOrigin = v.x
                    widgetYOrigin = v.y
                }
                MotionEvent.ACTION_MOVE -> {
                    // Screen border Collision
                    var newX = event.rawX + widgetDX
                    newX = 0F.coerceAtLeast(newX)
                    newX = v.width.toFloat().coerceAtMost(newX)

                    var newY = event.rawY + widgetDY
                    newY = 0F.coerceAtLeast(newY)
                    newY = v.height.toFloat().coerceAtMost(newY)
                    updatePositionPopup(x = newX, y = newY)
                }
                MotionEvent.ACTION_UP -> {
                    // Back to original position
                    updatePositionPopup(x = widgetXOrigin, y = widgetYOrigin)
                }
                else -> {
                    return@setTouchInterceptor false
                }
            }
            true
        }
    }

    private fun updatePositionPopup(x: Float, y: Float) {
        fpiPopUp?.update(x.toIntSafely(), y.toIntSafely(), -1, -1, true)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        fpiMonitoring.onFragmentHidden(isHidden = hidden)
    }

    override fun onFrameRendered(fpiPerformanceData: FpiPerformanceData) {
        updatePercentage(fpiData = fpiPerformanceData)
        updateFps(fpiData = fpiPerformanceData)
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
        val activeColor = if (percentage > dangerPercentage) {
            errorColor
        } else if (percentage > warningPercentage) {
            warningColor
        } else {
            defaultColor
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
        val sFps = String.format("%.2f%s", fps, "fps")
        fpsInfoText?.text = sFps
    }

    private fun updateFpsColor(fps: Double) {
        val context = fpiMonitoring.fragment?.context ?: return
        val activeColor = if (fps < dangerFps) {
            errorColor
        } else if (fps < warningFps) {
            warningColor
        } else {
            defaultColor
        }

        val textColor = runCatching { ContextCompat.getColor(context, activeColor) }.getOrElse { 0 }

        fpsInfoText?.setTextColor(textColor)
    }
    // endregion

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                fpiPopUp?.dismiss()
                fpiPopUp = null
                jankyInfoText = null
            }

            else -> {
                // no-ops
            }
        }
    }
}
