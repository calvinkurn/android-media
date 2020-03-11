package com.tokopedia.sellerhome.settings.view.activity

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerhome.settings.view.fragment.MenuSettingFragment

class MenuSettingActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? =
        MenuSettingFragment.createInstance()

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        setupDrawerStatusBar()
        return super.onCreateView(parent, name, context, attrs)
    }

    fun setupDrawerStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = ContextCompat.getColor(context, com.tokopedia.design.R.color.white_95)
            }
        }
    }

}