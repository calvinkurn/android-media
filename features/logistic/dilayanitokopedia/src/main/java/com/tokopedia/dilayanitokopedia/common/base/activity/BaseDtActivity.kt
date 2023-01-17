package com.tokopedia.dilayanitokopedia.common.base.activity

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.databinding.DtBaseActivityBinding
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

/**
 * Created by irpan on 07/09/22.
 */
abstract class BaseDtActivity : BaseActivity() {

    private var binding: DtBaseActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DtBaseActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setStatusBarColor()
        setBackgroundColor()
        setOrientation()

        if (savedInstanceState == null) {
            attachFragment()
        }
    }

    private fun setStatusBarColor() {
        binding?.let {
            it.fragmentContainer.fitsSystemWindows = false
            it.fragmentContainer.requestApplyInsets()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isDarkMode()) {
                var flags: Int = it.fragmentContainer.systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                it.fragmentContainer.systemUiVisibility = flags
                window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        StatusBarUtil.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setBackgroundColor() {
        val bgColor = ContextCompat.getColor(
            this,
            com.tokopedia.unifyprinciples.R.color.Unify_Background
        )
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
