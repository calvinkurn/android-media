package com.tokopedia.shop.settings.basicinfo.view.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.R
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
        const val KEY_IS_CLOSE_NOW = "ops_hour_is_close_now"
        const val KEY_IS_ACTION_EDIT = "ops_hour_is_action_edit"
        const val KEY_IS_OPEN_SCH_BOTTOMSHEET = "ops_hour_is_open_sch_bottomsheet"
        const val KEY_CACHE_ID = "ops_hour_cache_manager_id" // required if go to old fragment
    }

    private var isCloseNow: Boolean = false
    private var isActionEdit: Boolean = false
    private var isOpenSchBottomSheet: Boolean = false
    private var cacheIdForOldEditSchedule: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        getExtraBundles()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        return ShopSettingsOperationalHoursFragment.createInstance(
                isCloseNow,
                isActionEdit,
                isOpenSchBottomSheet,
                cacheIdForOldEditSchedule
        )
    }

    override fun getLayoutRes(): Int {
        return ACTIVITY_LAYOUT
    }

    override fun getParentViewResourceID(): Int {
        return PARENT_VIEW_ID
    }

    private fun getExtraBundles() {
        intent?.getBundleExtra(KEY_EXTRA_BUNDLE)?.let {
            isCloseNow = it.getBoolean(KEY_IS_CLOSE_NOW, false)
            isActionEdit = it.getBoolean(KEY_IS_ACTION_EDIT, false)
            isOpenSchBottomSheet = it.getBoolean(KEY_IS_OPEN_SCH_BOTTOMSHEET, false)
            cacheIdForOldEditSchedule = it.getString(KEY_CACHE_ID, "0")
        }
    }

}