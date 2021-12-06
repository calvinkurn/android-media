package com.tokopedia.shop.settings.basicinfo.view.activity

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.fragment.NewShopSettingsOperationalHoursFragment

/**
 * Created by Rafli Syam on 28/04/2021
 */
class ShopSettingsOperationalHoursActivity : BaseSimpleActivity() {

    companion object {
        @LayoutRes
        val ACTIVITY_LAYOUT = R.layout.activity_shop_settings_operational_hours_activity

        @IdRes
        val PARENT_VIEW_ID = R.id.parent_view
    }

    override fun getNewFragment(): Fragment? {
        return NewShopSettingsOperationalHoursFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return ACTIVITY_LAYOUT
    }

    override fun getParentViewResourceID(): Int {
        return PARENT_VIEW_ID
    }

}