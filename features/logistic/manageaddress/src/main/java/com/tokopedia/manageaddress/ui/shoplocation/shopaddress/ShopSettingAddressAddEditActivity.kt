package com.tokopedia.manageaddress.ui.shoplocation.shopaddress

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.shoplocation.DaggerShopLocationComponent
import com.tokopedia.manageaddress.di.shoplocation.ShopLocationComponent
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel
import com.tokopedia.shop.settings.address.view.ShopSettingAddressAddEditFragment

class ShopSettingAddressAddEditActivity: BaseSimpleActivity(), HasComponent<ShopLocationComponent> {
    override fun getComponent() = DaggerShopLocationComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    private var shopLocationOldUiModel: ShopLocationOldUiModel? = null
    private var isAddNew = true

    private val saveTextView: TextView? by lazy {
        toolbar?.findViewById<TextView>(R.id.tvSave)
    }

    companion object {
        private const val PARAM_EXTRA_SHOP_ADDRESS = "shop_address"
        private const val PARAM_EXTRA_IS_ADD_NEW = "is_add_new"

        fun createIntent(context: Context, shopLocationOldUiModel: ShopLocationOldUiModel?, isAddNew: Boolean) =
                Intent(context, ShopSettingAddressAddEditActivity::class.java)
                    .putExtra(PARAM_EXTRA_SHOP_ADDRESS, shopLocationOldUiModel)
                        .putExtra(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopLocationOldUiModel = intent.getParcelableExtra(PARAM_EXTRA_SHOP_ADDRESS)
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

    override fun getNewFragment() = ShopSettingAddressAddEditFragment.createInstance(shopLocationOldUiModel, isAddNew)

    override fun getLayoutRes() = R.layout.activity_shop_setting_address_add_new_logistic
}