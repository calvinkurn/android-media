package com.tokopedia.shop.settings.address.view

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent

/**
 * Deeplink: SHOP_SETTINGS_ADDRESS
 */
class ShopSettingsAddressActivity : BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {
    override fun getComponent() = DaggerShopSettingsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    override fun getNewFragment(): Fragment = ShopSettingAddressFragment.createInstance()

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopSettingsAddressActivity::class.java)
    }
}
