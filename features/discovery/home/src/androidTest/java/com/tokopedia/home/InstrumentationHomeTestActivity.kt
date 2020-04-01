package com.tokopedia.home

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.tokopedia.analytics.performance.util.JankyFrameMonitoringUtil
import com.tokopedia.analytics.performance.util.PerformanceData
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.navigation_common.listener.JankyFramesMonitoringListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import java.io.File

class InstrumentationHomeTestActivity : AppCompatActivity(),
        MainParentStatusBarListener,
        JankyFrameMonitoringUtil.OnFrameListener,
        JankyFramesMonitoringListener,
        EspressoPerformanceActivity {
    override fun getMainJankyFrameMonitoringUtil(): JankyFrameMonitoringUtil? {
        return jankyFramePerformanceUtil
    }

    private var performanceData: PerformanceData? = null
    private var jankyFramePerformanceUtil: JankyFrameMonitoringUtil? = null

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumentation_home_test)
        jankyFramePerformanceUtil = TestJankyFrameMonitoringUtil()
        jankyFramePerformanceUtil?.init(this, this)

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container, HomeFragment())//R.id.content_frame is the layout you want to replace
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onFrameRendered(performanceData: PerformanceData) {
        Log.d("HomeTestIntrumentedTest", "------------------------------")
        Log.d("HomeTestIntrumentedTest", "allFrames: "+performanceData.allFrames)
        Log.d("HomeTestIntrumentedTest", "jankyFrames: "+performanceData.jankyFrames)
        Log.d("HomeTestIntrumentedTest", "jankyPercentage: "+performanceData.jankyFramePercentage)
        Log.d("HomeTestIntrumentedTest", "Index Performance: "+(100 - performanceData.jankyFramePercentage))
        Log.d("HomeTestIntrumentedTest", "------------------------------")
        this.performanceData = performanceData
    }

    override fun getPerformanceResultData(): PerformanceData? {
        return performanceData
    }
}
