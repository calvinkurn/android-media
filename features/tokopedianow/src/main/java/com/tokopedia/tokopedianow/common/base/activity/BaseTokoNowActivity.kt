package com.tokopedia.tokopedianow.common.base.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import com.tokopedia.tokopedianow.R

abstract class BaseTokoNowActivity : BaseActivity() {

    private val fragmentContainer by lazy {
        findViewById<FrameLayout>(R.id.fragment_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_base)
        setStatusBarColor()
        setBackgroundColor()
        attachFragment()
    }

    private fun setStatusBarColor() {
        //apply inset to allow recyclerview scrolling behind status bar
        fragmentContainer.fitsSystemWindows = false
        fragmentContainer.requestApplyInsets()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags: Int = fragmentContainer.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            fragmentContainer.systemUiVisibility = flags
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }

        //make full transparent statusBar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setBackgroundColor() {
        val bgColor = ContextCompat.getColor(this,
            com.tokopedia.unifyprinciples.R.color.Unify_N0)
        window?.decorView?.setBackgroundColor(bgColor)
    }

    private fun attachFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, getFragment())
            .commit()
    }

    abstract fun getFragment(): Fragment
}