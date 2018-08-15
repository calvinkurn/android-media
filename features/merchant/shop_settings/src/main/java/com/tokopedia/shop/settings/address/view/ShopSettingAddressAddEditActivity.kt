package com.tokopedia.shop.settings.address.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel
import com.tokopedia.shop.settings.R

class ShopSettingAddressAddEditActivity: BaseSimpleActivity() {
    private var shopLocationViewModel: ShopLocationViewModel? = null
    private var isAddNew = true

    private val saveTextView: TextView? by lazy {
        toolbar?.findViewById<TextView>(R.id.tvSave)
    }

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

        saveTextView?.run {
            if(isAddNew){
                text = getString(R.string.save)
                setOnClickListener { (fragment as ShopSettingAddressAddEditFragment).saveAddress() }
            } else {
                text = getString(R.string.label_change)
                setOnClickListener { (fragment as ShopSettingAddressAddEditFragment).updateAddess() }
            }
        }
    }

    override fun getNewFragment() = ShopSettingAddressAddEditFragment.createInstance(shopLocationViewModel, isAddNew)

    override fun getLayoutRes() = R.layout.activity_shop_setting_address_add_new
}