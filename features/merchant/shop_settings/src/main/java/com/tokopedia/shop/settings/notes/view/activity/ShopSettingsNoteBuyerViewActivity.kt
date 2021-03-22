package com.tokopedia.shop.settings.notes.view.activity

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_settings_note_buyer_view)
    }

    override fun getNewFragment(): Fragment = ShopSettingsNoteBuyerViewFragment.createInstance()

    override fun getComponent(): ShopSettingsComponent = DaggerShopSettingsComponent.builder()
                                                            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                                                            .build()
}