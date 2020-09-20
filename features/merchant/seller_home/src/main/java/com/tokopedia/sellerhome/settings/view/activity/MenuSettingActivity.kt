package com.tokopedia.sellerhome.settings.view.activity

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.sellerhome.settings.analytics.sendSettingClickBackButtonTracking
import com.tokopedia.sellerhome.settings.view.fragment.MenuSettingFragment
import timber.log.Timber

class MenuSettingActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? =
        MenuSettingFragment.createInstance()

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        window.decorView.setBackgroundColor(Color.WHITE)
        setWhiteStatusBar(context)
        return super.onCreateView(parent, name, context, attrs)
    }

    private fun setWhiteStatusBar(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                setStatusBarColor(ContextCompat.getColor(context, com.tokopedia.design.R.color.transparent))
                setLightStatusBar(true)
            } catch (ex: Resources.NotFoundException) {
                Timber.e(ex)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            sendSettingClickBackButtonTracking()
        }
        return super.onOptionsItemSelected(item)
    }

}