package com.tokopedia.shop.settings.basicinfo.view.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.fragment.NewShopSettingsOperationalHoursFragment
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsOperationalHoursFragment

/**
 * Created by Rafli Syam on 28/04/2021
 */
class ShopSettingsOperationalHoursActivity : BaseSimpleActivity() {

    companion object {

        @LayoutRes
        val ACTIVITY_LAYOUT = R.layout.activity_shop_settings_operational_hours_activity

        @IdRes
        val PARENT_VIEW_ID = R.id.parent_view

        const val KEY_EXTRA_BUNDLE = "ops_hour_bundle"
        const val KEY_CACHE_ID = "ops_hour_cache_manager_id" // required if go to old fragment
    }

    private var cacheIdForOldEditSchedule: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        getExtraBundles()
        super.onCreate(savedInstanceState)
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

    private fun getExtraBundles() {
        intent?.getBundleExtra(KEY_EXTRA_BUNDLE)?.let {
            cacheIdForOldEditSchedule = it.getString(KEY_CACHE_ID, "0")
        }
    }

}