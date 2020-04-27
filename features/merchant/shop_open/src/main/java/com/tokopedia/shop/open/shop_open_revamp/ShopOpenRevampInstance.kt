package com.tokopedia.shop.open.shop_open_revamp

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shop.open.shop_open_revamp.di.DaggerShopOpenRevampComponent
import com.tokopedia.shop.open.shop_open_revamp.di.ShopOpenRevampComponent

class ShopOpenRevampInstance {

    companion object {
        private var shopOpenRevampComponent: ShopOpenRevampComponent? = null

        fun getComponent(application: Application): ShopOpenRevampComponent {
            return shopOpenRevampComponent?.run {
                shopOpenRevampComponent
            } ?: DaggerShopOpenRevampComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
    }

}