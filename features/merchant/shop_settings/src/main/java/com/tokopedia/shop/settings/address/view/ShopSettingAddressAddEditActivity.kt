package com.tokopedia.shop.settings.address.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent

class ShopSettingAddressAddEditActivity: BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {
    override fun getComponent() = DaggerShopSettingsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

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
            setOnClickListener { (fragment as ShopSettingAddressAddEditFragment).saveAddEditAddress() }
            visibility = View.VISIBLE
        }

        supportActionBar?.setTitle(if (isAddNew) R.string.shop_settings_add_address else R.string.shop_settings_edit_address)
    }

    override fun inflateFragment() {
        val newFragment = newFragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, tagFragment)
                .commit()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.title = this.title
        }
    }

    override fun getNewFragment() = ShopSettingAddressAddEditFragment.createInstance(shopLocationViewModel, isAddNew)

    override fun getLayoutRes() = R.layout.activity_shop_setting_address_add_new
}