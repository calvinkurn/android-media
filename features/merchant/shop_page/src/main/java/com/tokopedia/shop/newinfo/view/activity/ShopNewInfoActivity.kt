package com.tokopedia.shop.newinfo.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.newinfo.di.component.DaggerShopNewInfoComponent
import com.tokopedia.shop.newinfo.di.component.ShopNewInfoComponent
import com.tokopedia.shop.newinfo.di.module.ShopNewInfoModule
import com.tokopedia.shop.newinfo.view.fragment.ShopNewInfoFragment

class ShopNewInfoActivity : BaseSimpleActivity(), HasComponent<ShopNewInfoComponent> {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ShopNewInfoActivity::class.java)
        }
    }

    override fun getNewFragment() = ShopNewInfoFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_shop_new_info

    override fun getComponent(): ShopNewInfoComponent {
        return DaggerShopNewInfoComponent.builder()
                .shopComponent(ShopComponentInstance.getComponent(application))
                .shopNewInfoModule(ShopNewInfoModule())
                .build()
    }
}
