package com.tokopedia.tkpd

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener


/**
 * Created by mzennis on 2020-01-23.
 */
class SimpleHomeActivity: AppCompatActivity(), MainParentStatusBarListener {

    override fun requestStatusBarDark() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//            this.window.statusBarColor = Color.TRANSPARENT
//        }
    }

    override fun requestStatusBarLight() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//            this.window.statusBarColor = Color.TRANSPARENT
//        }
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
        setContentView(R.layout.main_simple_testapp)

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container, HomeFragment())//R.id.content_frame is the layout you want to replace
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
    }
}