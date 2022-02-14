package com.tokopedia.tokopedianow.common.base.activity

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

abstract class BaseTokoNowActivity : BaseActivity() {

    private var binding : ActivityTokopedianowBaseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokopedianowBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setStatusBarColor()
        setBackgroundColor()

        if(savedInstanceState == null) {
            attachFragment()
        }
    }

    private fun setStatusBarColor() {
        binding?.let {
            //apply inset to allow recyclerview scrolling behind status bar
            it.fragmentContainer.fitsSystemWindows = false
            it.fragmentContainer.requestApplyInsets()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isDarkMode()) {
                var flags: Int = it.fragmentContainer.systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                it.fragmentContainer.systemUiVisibility = flags
                window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
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
            com.tokopedia.unifyprinciples.R.color.Unify_Background)
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