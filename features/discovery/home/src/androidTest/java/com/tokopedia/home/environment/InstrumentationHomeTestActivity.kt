package com.tokopedia.home.environment

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.analytics.performance.util.*
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener
import com.tokopedia.navigation_common.listener.JankyFramesMonitoringListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener

class InstrumentationHomeTestActivity : AppCompatActivity(),
        MainParentStatusBarListener,
        JankyFrameMonitoringUtil.OnFrameListener,
        JankyFramesMonitoringListener,
        EspressoPerformanceActivity,
        HomePerformanceMonitoringListener {
    override fun getMainJankyFrameMonitoringUtil(): JankyFrameMonitoringUtil? {
        return jankyFramePerformanceUtil
    }

    private var fpiPerformanceData: FpiPerformanceData? = null
    private var jankyFramePerformanceUtil: JankyFrameMonitoringUtil? = null
    private var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null
    var isFromCache: Boolean = false

    override fun requestStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            this.window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun requestStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            this.window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startHomePerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumentation_home_test)
        jankyFramePerformanceUtil = TestJankyFrameMonitoringUtil()
        jankyFramePerformanceUtil?.init(this, this)

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container, HomeFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onFrameRendered(fpiPerformanceData: FpiPerformanceData) {
        this.fpiPerformanceData = fpiPerformanceData
    }

    override fun getFpiPerformanceResultData(): FpiPerformanceData? {
        return fpiPerformanceData
    }

    override fun getPltPerformanceResultData(): PltPerformanceData? {
        return pageLoadTimePerformanceInterface?.getPltPerformanceData()
    }

    override fun getPageLoadTimePerformanceInterface(): PageLoadTimePerformanceInterface? {
        return pageLoadTimePerformanceInterface
    }

    override fun stopHomePerformanceMonitoring(isCache: Boolean) {
        pageLoadTimePerformanceInterface?.stopMonitoring()
        isFromCache = isCache
    }

    override fun startHomePerformanceMonitoring() {
        pageLoadTimePerformanceInterface = PageLoadTimePerformanceCallback(
                tagPrepareDuration = "prepare",
                tagNetworkRequestDuration = "network",
                tagRenderDuration = "render"
        )
        pageLoadTimePerformanceInterface?.startMonitoring("start monitoring")
        pageLoadTimePerformanceInterface?.startPreparePagePerformanceMonitoring()
    }
}
