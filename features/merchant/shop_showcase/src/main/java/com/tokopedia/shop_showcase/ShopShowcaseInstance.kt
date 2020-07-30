package com.tokopedia.shop_showcase

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shop_showcase.di.DaggerShopShowcaseComponent
import com.tokopedia.shop_showcase.di.ShopShowcaseComponent


class ShopShowcaseInstance {
    companion object {
        private var shopShowcaseComponent: ShopShowcaseComponent? = null

        fun getComponent(application: Application): ShopShowcaseComponent {
            return shopShowcaseComponent?.run {
                shopShowcaseComponent
            } ?: DaggerShopShowcaseComponent
                    .builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }
}