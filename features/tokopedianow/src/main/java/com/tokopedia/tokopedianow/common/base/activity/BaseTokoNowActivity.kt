package com.tokopedia.tokopedianow.common.base.activity

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ActivityTokopedianowBaseBinding
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

abstract class BaseTokoNowActivity : BaseActivity() {

    private var darkStatusBarFlag: Int = 0

    private var binding : ActivityTokopedianowBaseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokopedianowBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setDarkStatusBarFlag()
        setStatusBarColor()
        setBackgroundColor()
        setOrientation()

        if(savedInstanceState == null) {
            attachFragment()
        }
    }

    fun switchToLightToolbar() {
        binding?.let {
            var flags: Int = it.fragmentContainer.systemUiVisibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            setStatusBarFlag(flags)
            setTransparentStatusBar()
        }
    }

    fun switchToDarkToolbar() {
        setStatusBarFlag(darkStatusBarFlag)
        setTransparentStatusBar()
    }

    private fun setStatusBarColor() {
        binding?.let {
            //apply inset to allow recyclerview scrolling behind status bar
            it.fragmentContainer.fitsSystemWindows = false
            it.fragmentContainer.requestApplyInsets()
            switchToLightToolbar()
        }
    }

    private fun setStatusBarFlag(flags: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isDarkMode()) {
            binding?.let {
                it.fragmentContainer.systemUiVisibility = flags
                window.statusBarColor = ContextCompat.getColor(
                    this, unifyprinciplesR.color.Unify_NN0
                )
            }
        }
    }

    private fun setTransparentStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setWindowFlag(
                this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                false
            )
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setDarkStatusBarFlag() {
        binding?.let {
            darkStatusBarFlag = it.fragmentContainer.systemUiVisibility
        }
    }

    private fun setBackgroundColor() {
        val bgColor = ContextCompat.getColor(this,
            unifyprinciplesR.color.Unify_Background)
        window?.decorView?.setBackgroundColor(bgColor)
    }

    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun attachFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, getFragment())
            .commit()
    }

    abstract fun getFragment(): Fragment
}
