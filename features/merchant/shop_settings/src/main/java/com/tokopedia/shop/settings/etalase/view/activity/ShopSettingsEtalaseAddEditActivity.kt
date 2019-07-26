package com.tokopedia.shop.settings.etalase.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseAddEditFragment

class ShopSettingsEtalaseAddEditActivity: BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {
    private var isEdit: Boolean = false
    private var etalase: ShopEtalaseViewModel = ShopEtalaseViewModel()

    private val saveTextView: TextView? by lazy {
        toolbar?.findViewById<TextView>(R.id.tvSave)
    }

    companion object {
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_ETALASE = "SHOP_ETALASE"

        @JvmStatic
        fun createIntent(context: Context, isEdit: Boolean, etalase: ShopEtalaseViewModel = ShopEtalaseViewModel()) =
                Intent(context, ShopSettingsEtalaseAddEditActivity::class.java)
                        .putExtra(PARAM_SHOP_ETALASE, etalase)
                        .putExtra(PARAM_IS_EDIT, isEdit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        etalase = intent.getParcelableExtra(PARAM_SHOP_ETALASE) ?: ShopEtalaseViewModel()
        isEdit = intent.getBooleanExtra(PARAM_IS_EDIT, false)

        super.onCreate(savedInstanceState)

        saveTextView?.run {
            setOnClickListener { (fragment as ShopSettingsEtalaseAddEditFragment).saveAddEditEtalase() }
            visibility = View.VISIBLE
        }

        supportActionBar?.setTitle(if (!isEdit) R.string.shop_settings_add_etalase else R.string.shop_settings_edit_etalase)
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

    override fun getNewFragment() = ShopSettingsEtalaseAddEditFragment.createInstance(isEdit, etalase)

    override fun getLayoutRes() = R.layout.activity_shop_setting_address_add_new

    override fun getComponent() = DaggerShopSettingsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()
}