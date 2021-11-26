package com.tokopedia.shop.score.uitest.stub.performance.presentation.fragment

import com.tokopedia.shop.score.performance.presentation.fragment.ShopPerformancePageFragment
import com.tokopedia.shop.score.uitest.stub.common.di.component.DaggerBaseAppComponentStub
import com.tokopedia.shop.score.uitest.stub.common.di.module.AppModuleStub
import com.tokopedia.shop.score.uitest.stub.performance.di.component.DaggerShopPerformanceComponentStub
import com.tokopedia.shop.score.uitest.stub.performance.di.module.ShopPerformanceModuleStub

class ShopPerformanceFragmentStub: ShopPerformancePageFragment() {

    override fun initInjector() {
        val baseComponent = DaggerBaseAppComponentStub.builder()
            .appModuleStub(AppModuleStub(context!!.applicationContext))
            .build()
        DaggerShopPerformanceComponentStub
            .builder()
            .baseAppComponentStub(baseComponent)
            .shopPerformanceModuleStub(ShopPerformanceModuleStub())
            .build()
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ShopPerformanceFragmentStub {
            return ShopPerformanceFragmentStub()
        }
    }
}