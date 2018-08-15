package com.tokopedia.shop.settings.address.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel

class ShopSettingAddressAddEditActivity: BaseSimpleActivity() {
    private var shopLocationViewModel: ShopLocationViewModel? = null
    private var isAddNew = true

    companion object {
        private const val PARAM_EXTRA_SHOP_ADDRESS = "shop_address"
        private const val PARAM_EXTRA_IS_ADD_NEW = "is_add_new"

        fun createIntent(context: Context, shopLocationViewModel: ShopLocationViewModel?, isAddNew: Boolean) =
                Intent(context, ShopSettingAddressAddEditActivity::class.java)
                    .putExtra(PARAM_EXTRA_SHOP_ADDRESS, shopLocationViewModel)
                        .putExtra(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopLocationViewModel = intent.getParcelableExtra(PARAM_EXTRA_SHOP_ADDRESS)
        isAddNew = intent.getBooleanExtra(PARAM_EXTRA_IS_ADD_NEW, true)
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment() = ShopSettingAddressAddEditFragment.createInstance(shopLocationViewModel, isAddNew)
}