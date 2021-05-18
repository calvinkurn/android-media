package com.tokopedia.sellerhome.settings.view.activity

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller.menu.common.analytics.sendSettingClickBackButtonTracking
import com.tokopedia.sellerhome.settings.view.fragment.MenuSettingFragment

class MenuSettingActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? =
        MenuSettingFragment.createInstance()

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        window.decorView.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        return super.onCreateView(parent, name, context, attrs)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            sendSettingClickBackButtonTracking()
        }
        return super.onOptionsItemSelected(item)
    }
}