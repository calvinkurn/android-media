package com.tokopedia.shop.settings.etalase.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
    private var existedEtalase: MutableList<String> = mutableListOf()

    private val saveTextView: TextView? by lazy {
        toolbar?.findViewById<TextView>(R.id.tvSave)
    }

    companion object {
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_ETALASE = "SHOP_ETALASE"
        private const val PARAM_EXISTED_ETALASE = "EXISTED_ETALASE"

        @JvmStatic
        fun createIntent(context: Context, isEdit: Boolean, existedEtalase: List<String> = listOf(), etalase: ShopEtalaseViewModel = ShopEtalaseViewModel()) =
                Intent(context, ShopSettingsEtalaseAddEditActivity::class.java)
                        .putExtra(PARAM_SHOP_ETALASE, etalase)
                        .putStringArrayListExtra(PARAM_EXISTED_ETALASE, ArrayList(existedEtalase))
                        .putExtra(PARAM_IS_EDIT, isEdit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        etalase = intent.getParcelableExtra(PARAM_SHOP_ETALASE) ?: ShopEtalaseViewModel()
        isEdit = intent.getBooleanExtra(PARAM_IS_EDIT, false)
        existedEtalase = intent.getStringArrayListExtra(PARAM_EXISTED_ETALASE)

        super.onCreate(savedInstanceState)

        saveTextView?.run {
            text = getString( if(!isEdit) R.string.save else R.string.label_change)
            setOnClickListener { (fragment as ShopSettingsEtalaseAddEditFragment).saveAddEditEtalase() }
            visibility = View.VISIBLE
        }

        supportActionBar?.setTitle(if (!isEdit) R.string.shop_settings_add_etalase else R.string.shop_settings_edit_etalase)
    }

    override fun getNewFragment() = ShopSettingsEtalaseAddEditFragment.createInstance(isEdit, existedEtalase, etalase)

    override fun getLayoutRes() = R.layout.activity_shop_setting_address_add_new

    override fun getComponent() = DaggerShopSettingsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()
}