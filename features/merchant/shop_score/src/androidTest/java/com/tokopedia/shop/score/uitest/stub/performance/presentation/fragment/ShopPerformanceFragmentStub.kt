package com.tokopedia.shop.score.uitest.stub.performance.presentation.fragment

import com.tokopedia.shop.score.performance.presentation.fragment.ShopPerformancePageFragment
import com.tokopedia.shop.score.uitest.stub.performance.di.component.ShopPerformanceComponentStub

class ShopPerformanceFragmentStub: ShopPerformancePageFragment() {

    override fun initInjector() {
        getComponent(ShopPerformanceComponentStub::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ShopPerformanceFragmentStub {
            return ShopPerformanceFragmentStub()
        }
    }
}