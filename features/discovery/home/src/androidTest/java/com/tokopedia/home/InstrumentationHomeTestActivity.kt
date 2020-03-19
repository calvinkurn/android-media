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

class InstrumentationHomeTestActivity : AppCompatActivity(), MainParentStatusBarListener, JankyFrameMonitoringUtil.OnFrameListener, JankyFramesMonitoringListener {
    override fun getMainJankyFrameMonitoringUtil(): JankyFrameMonitoringUtil? {
        return jankyFramePerformanceUtil
    }

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
//        val currentRelativePath = Paths.get("")
//        val s = currentRelativePath.toAbsolutePath().toString()
//        println("Current relative path is: $s")
//
//        val classLoader = javaClass.classLoader
//        val t = classLoader.getResource("InstrumentationHomeTestActivity.kt")
//        println("Classloader name is: $t")
//
//        try {
//            val myObj = File("/Users/nakama/Documents/TokoRepo/android-tokopedia-core/features/discovery/home/src/androidTest/java/com/tokopedia/home/devara.txt")
//            if (myObj.createNewFile()) {
//                System.out.println("File created: " + myObj.getName())
//            } else {
//                println("File already exists.")
//            }
//        } catch (e: IOException) {
//            println("An error occurred.")
//            e.printStackTrace()
//        }
//

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
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
    }
}
