package com.tokopedia.manageaddress.ui.shoplocation.shopaddress

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.manageaddress.di.DaggerShopLocationComponent
import com.tokopedia.manageaddress.di.ShopLocationComponent

/**
 * Deeplink: SHOP_SETTINGS_ADDRESS
 */
class ShopSettingsAddressActivity : BaseSimpleActivity(), HasComponent<ShopLocationComponent> {
    override fun getComponent() = DaggerShopLocationComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    override fun getNewFragment(): Fragment = ShopSettingAddressFragment.createInstance()

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopSettingsAddressActivity::class.java)
    }
}
