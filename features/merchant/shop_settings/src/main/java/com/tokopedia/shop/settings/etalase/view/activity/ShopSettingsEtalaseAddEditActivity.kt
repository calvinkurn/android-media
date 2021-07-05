package com.tokopedia.shop.settings.etalase.view.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseAddEditFragment

class ShopSettingsEtalaseAddEditActivity: BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {

    companion object {
        private const val PARAM_IS_EDIT = "IS_EDIT"
        private const val PARAM_SHOP_ETALASE = "SHOP_ETALASE"
    }

    private var isEdit: Boolean = false
    private var etalase: ShopEtalaseUiModel = ShopEtalaseUiModel()
    private var header: HeaderUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        etalase = intent.getParcelableExtra(PARAM_SHOP_ETALASE) ?: ShopEtalaseUiModel()
        isEdit = intent.getBooleanExtra(PARAM_IS_EDIT, false)

        super.onCreate(savedInstanceState)

        setupToolbar()

        supportActionBar?.setTitle(if (!isEdit) R.string.shop_settings_add_etalase else R.string.shop_settings_edit_etalase)
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    override fun inflateFragment() {
        val newFragment = newFragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, tagFragment)
                .commit()
    }

    override fun getNewFragment() = ShopSettingsEtalaseAddEditFragment.createInstance(isEdit, etalase)

    override fun getLayoutRes() = R.layout.activity_shop_settings_add_new

    override fun getComponent() = DaggerShopSettingsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()

    private fun setupToolbar() {
        header = findViewById<HeaderUnify>(R.id.header)?.apply {
            isShowShadow = true
            setSupportActionBar(this)
            actionTextView?.apply {
                setOnClickListener { (fragment as? ShopSettingsEtalaseAddEditFragment)?.saveAddEditEtalase() }
                gone()
            }
        }
    }

    fun showSaveButton() {
        header?.actionTextView?.show()
    }
}