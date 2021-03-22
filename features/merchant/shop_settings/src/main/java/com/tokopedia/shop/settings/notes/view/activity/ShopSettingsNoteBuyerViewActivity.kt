package com.tokopedia.shop.settings.notes.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.notes.view.fragment.ShopSettingsNoteBuyerViewFragment

class ShopSettingsNoteBuyerViewActivity : BaseSimpleActivity(), HasComponent<ShopSettingsComponent> {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"

        @JvmStatic
        fun createIntent(context: Context, shopId: String) = Intent(context, ShopSettingsNoteBuyerViewActivity::class.java)
                .apply {
                    putExtra(SHOP_ID, shopId)
                }
    }

    private var shopId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_settings_note_buyer_view)
        shopId = intent.getStringExtra(SHOP_ID)
    }

    override fun getNewFragment(): Fragment = ShopSettingsNoteBuyerViewFragment.createInstance(shopId)

    override fun getComponent(): ShopSettingsComponent = DaggerShopSettingsComponent.builder()
                                                            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                                                            .build()
}