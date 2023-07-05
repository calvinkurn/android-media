package com.tokopedia.analytics.performance.fpi

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.tokopedia.analytics.R
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by yovi.putra on 05/07/23"
 * Project name: android-tokopedia-core
 **/

interface FfpiMonitoringDelegate {

    fun onCreateView(fragment: Fragment)

    fun onViewCreated(view: View)

    fun onHiddenChanged(hidden: Boolean)
}

class FfpiMonitoringDelegateImpl : FfpiMonitoringDelegate {

    private val fpiMonitoring by lazy {
        FragmentFramePerformanceIndexMonitoring()
    }

    private var fpiContainer: PopupWindow? = null
    private var fpiInfo: Typography? = null

    override fun onCreateView(fragment: Fragment) {
        fpiMonitoring.init(
            pageName = javaClass.simpleName,
            fragment = fragment,
            onFrameListener = object : FragmentFramePerformanceIndexMonitoring.OnFrameListener {
                @SuppressLint("SetTextI18n")
                override fun onFrameRendered(fpiPerformanceData: FpiPerformanceData) {
                    fpiInfo?.text = "${fpiPerformanceData.jankyFramePercentage}%"
                }
            }
        )
    }

    override fun onViewCreated(view: View) {
        val mContext = view.context ?: return
        val fpiLayout = LayoutInflater.from(mContext).inflate(R.layout.ffpi_monitoring, null)

        fpiInfo = fpiLayout.findViewById(R.id.ffpiInfo)
        fpiContainer = PopupWindow(
            fpiLayout,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            view.postDelayed({
                showAtLocation(view, (Gravity.CENTER or Gravity.TOP), 0, 0)
            }, 2000)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        fpiMonitoring.onFragmentHidden(isHidden = hidden)
    }

}
