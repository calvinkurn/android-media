package com.tokopedia.shop.setting.view.activity

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.setting.di.component.DaggerShopPageSettingComponent
import com.tokopedia.shop.setting.di.component.ShopPageSettingComponent
import com.tokopedia.shop.setting.di.module.ShopPageSettingModule
import com.tokopedia.shop.setting.view.fragment.ShopPageSettingFragment
import com.tokopedia.shop.setting.view.fragment.ShopPageSettingFragment.Companion.SHOP_ID

class ShopPageSettingActivity : BaseSimpleActivity(), HasComponent<ShopPageSettingComponent> {

    companion object {

        fun createIntent(context: Context, shopId: String): Intent {
            return Intent(context, ShopPageSettingActivity::class.java).apply {
                putExtra(SHOP_ID, shopId)
            }
        }
    }

    override fun getNewFragment() = ShopPageSettingFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_shop_new_info

    override fun getComponent(): ShopPageSettingComponent {
        return DaggerShopPageSettingComponent.builder()
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .shopPageSettingModule(ShopPageSettingModule())
                .build()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as? ShopPageSettingFragment)?.onBackPressed()
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }
}
