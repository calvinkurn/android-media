package com.tokopedia.shop.settings.basicinfo.view.activity

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsSetOperationalHoursFragment

/**
 * Created by Rafli Syam on 29/04/2021
 */
class ShopSettingsSetOperationalHoursActivity : BaseSimpleActivity() {

    companion object {

        @LayoutRes
        val ACTIVITY_LAYOUT = R.layout.activity_shop_settings_set_operational_hours_activity

        @IdRes
        val PARENT_VIEW_ID = R.id.parent_view

    }

    override fun getLayoutRes(): Int {
        return ACTIVITY_LAYOUT
    }

    override fun getParentViewResourceID(): Int {
        return PARENT_VIEW_ID
    }

    override fun getNewFragment(): Fragment? {
        return ShopSettingsSetOperationalHoursFragment.createInstance()
    }
}