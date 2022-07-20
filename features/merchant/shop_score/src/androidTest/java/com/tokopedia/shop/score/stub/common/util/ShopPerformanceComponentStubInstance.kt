package com.tokopedia.shop.score.stub.common.util

import android.content.Context
import com.tokopedia.shop.score.stub.performance.di.component.DaggerShopPerformanceComponentStub
import com.tokopedia.shop.score.stub.performance.di.component.ShopPerformanceComponentStub
import com.tokopedia.shop.score.stub.performance.di.module.ShopPerformanceModuleStub

class ShopPerformanceComponentStubInstance {

    companion object {
        private var shopPerformanceComponentStub: ShopPerformanceComponentStub? = null

        fun getShopPerformanceComponentStub(
            context: Context
        ): ShopPerformanceComponentStub {
            val baseAppComponentStub =
                BaseAppComponentStubInstance.getBaseAppComponentStub(context)
            return shopPerformanceComponentStub?.run { shopPerformanceComponentStub }
                ?: DaggerShopPerformanceComponentStub
                    .builder()
                    .baseAppComponentStub(baseAppComponentStub)
                    .shopPerformanceModuleStub(ShopPerformanceModuleStub())
                    .build().also { shopPerformanceComponentStub = it }
        }
    }
}