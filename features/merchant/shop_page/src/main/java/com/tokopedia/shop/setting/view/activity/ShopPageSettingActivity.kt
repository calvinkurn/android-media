package com.tokopedia.shop.setting.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.setting.di.component.DaggerShopPageSettingComponent
import com.tokopedia.shop.setting.di.component.ShopPageSettingComponent
import com.tokopedia.shop.setting.di.module.ShopPageSettingModule
import com.tokopedia.shop.setting.view.fragment.ShopPageSettingFragment

class ShopPageSettingActivity : BaseSimpleActivity(), HasComponent<ShopPageSettingComponent> {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ShopPageSettingActivity::class.java)
        }
    }

    override fun getNewFragment() = ShopPageSettingFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_shop_new_info

    override fun getComponent(): ShopPageSettingComponent {
        return DaggerShopPageSettingComponent.builder()
                .shopComponent(ShopComponentInstance.getComponent(application))
                .shopPageSettingModule(ShopPageSettingModule())
                .build()
    }
}
