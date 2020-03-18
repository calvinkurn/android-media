package com.tokopedia.sellerhome.settings.view.activity

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.analytics.sendTrackingManual
import com.tokopedia.sellerhome.settings.view.fragment.MenuSettingFragment

class MenuSettingActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? =
        MenuSettingFragment.createInstance()

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        setupDrawerStatusBar(context)
        return super.onCreateView(parent, name, context, attrs)
    }

    private fun setupDrawerStatusBar(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, R.color.white_95)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            sendTrackingManual(
                    eventName = SettingTrackingConstant.CLICK_SHOP_SETTING,
                    eventCategory = SettingTrackingConstant.SETTINGS,
                    eventAction = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.BACK_ARROW}",
                    eventLabel = ""
            )
        }
        return super.onOptionsItemSelected(item)
    }

}