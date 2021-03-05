package com.tokopedia.shop.score.performance.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent

class ShopPerformanceComponentBuilder {
    companion object {
        private var shopPerformanceComponent: ShopPerformanceComponent? = null

        fun getComponent(application: Application): ShopPerformanceComponent {
            return shopPerformanceComponent?.run { shopPerformanceComponent }
                    ?: DaggerShopPerformanceComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}