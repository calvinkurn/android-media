package com.tokopedia.tkpd

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
//import com.moengage.core.ConfigurationCache
//import com.moengage.core.model.RemoteConfiguration
//import com.moengage.inapp.InAppActionManager
//import com.moengage.inapp.InAppController
//import com.moengage.inapp.InAppManager
import com.tokopedia.SecondActivity
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import com.tokopedia.home.beranda.presentation.view.listener.FramePerformanceIndexInterface
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener
import com.tokopedia.analytics.performance.fpi.FpiPerformanceData
import com.tokopedia.analytics.performance.fpi.FragmentFramePerformanceIndexMonitoring
import com.tokopedia.analytics.performance.util.*

/**
 * Created by mzennis on 2020-01-23.
 */
class SimpleHomeActivity: AppCompatActivity(), MainParentStatusBarListener, HomePerformanceMonitoringListener {

    private var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null
    private var fragmentFramePerformanceIndexMonitoring: FragmentFramePerformanceIndexMonitoring? = null
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
        setContentView(R.layout.main_simple_testapp)
//
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction
                .replace(R.id.container, HomeFragment())//R.id.content_frame is the layout you want to replace
        fragmentTransaction.commitNow()
//
//        val inAppActionManager = InAppController.getInstance()
//        val remoteConfiguration = RemoteConfiguration()
//        remoteConfiguration.setInAppStatus(true)
//        InAppManager.getInstance().updateCurrentActivityContext(this)
//        ConfigurationCache.getInstance().remoteConfiguration = remoteConfiguration
//        inAppActionManager.showTestInAppErrorDialog(this, "Adad deeeeh")

//        val intent = Intent(this, SecondActivity::class.java)
//        startActivity(intent)

//        val fragmentManager2 = supportFragmentManager
//        val fragmentTransaction2 = fragmentManager2
//                .beginTransaction()
//        val dialogFragment: DialogFragment = MyCustomDialogFragment()
//        dialogFragment.show(fragmentTransaction2, "dialog")
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun getPageLoadTimePerformanceInterface(): PageLoadTimePerformanceInterface? {
        return pageLoadTimePerformanceInterface
    }

    override fun stopHomePerformanceMonitoring(isCache: Boolean) {
        pageLoadTimePerformanceInterface?.stopRenderPerformanceMonitoring()
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