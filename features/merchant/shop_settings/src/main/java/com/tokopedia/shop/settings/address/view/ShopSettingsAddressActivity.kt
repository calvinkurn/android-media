package com.tokopedia.shop.settings.address.view

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.settings.address.di.component.ShopLocationComponent
import com.tokopedia.shop.settings.address.di.component.DaggerShopLocationComponent

class ShopSettingsAddressActivity : BaseSimpleActivity(), HasComponent<ShopLocationComponent> {
    override fun getComponent() = DaggerShopLocationComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    override fun getNewFragment(): Fragment = ShopSettingAddressNewFragment.createInstance()

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopSettingsAddressActivity::class.java)
    }
}
