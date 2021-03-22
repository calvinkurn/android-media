package com.tokopedia.shop.settings.notes.view.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNoteBuyerViewFragment
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNoteBuyerViewFragment.Companion.SHOP_ID

class ShopSettingsNoteBuyerViewActivity : BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbarActions()
        window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                        this, com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
        )
    }

    override fun getLayoutRes(): Int = R.layout.activity_shop_settings_note_buyer_view

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getNewFragment(): Fragment = ShopSettingsNoteBuyerViewFragment.createInstance(intent.getStringExtra(SHOP_ID))

    override fun getComponent(): ShopSettingsComponent = DaggerShopSettingsComponent.builder()
                                                            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                                                            .build()

    private fun setupToolbarActions() {
        findViewById<HeaderUnify>(R.id.toolbar_shop_note).apply {
            title = context.getString(R.string.toolbar_title_shop_note)
            isShowShadow = false
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }
}