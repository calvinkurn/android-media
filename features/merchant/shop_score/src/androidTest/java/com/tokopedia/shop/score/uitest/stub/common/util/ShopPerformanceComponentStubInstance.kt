package com.tokopedia.shop.score.uitest.stub.common.util

import android.app.Application
import com.tokopedia.shop.score.uitest.stub.performance.di.component.DaggerShopPerformanceComponentStub
import com.tokopedia.shop.score.uitest.stub.performance.di.component.ShopPerformanceComponentStub
import com.tokopedia.shop.score.uitest.stub.performance.di.module.ShopPerformanceModuleStub

class ShopPerformanceComponentStubInstance {

    companion object {
        private var shopPerformanceComponentStub: ShopPerformanceComponentStub? = null

        fun getShopPerformanceComponentStub(
            application: Application
        ): ShopPerformanceComponentStub {
            val baseAppComponentStub =
                BaseAppComponentStubInstance.getBaseAppComponentStub(application)
            return shopPerformanceComponentStub?.run { shopPerformanceComponentStub }
                ?: DaggerShopPerformanceComponentStub
                    .builder()
                    .baseAppComponentStub(baseAppComponentStub)
                    .shopPerformanceModuleStub(ShopPerformanceModuleStub())
                    .build()
        }
    }
}