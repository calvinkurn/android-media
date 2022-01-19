package com.tokopedia.shop.score.stub.performance.presentation.fragment

import com.tokopedia.shop.score.performance.presentation.fragment.ShopPerformancePageFragment
import com.tokopedia.shop.score.stub.common.util.ShopPerformanceComponentStubInstance

class ShopPerformanceFragmentStub : ShopPerformancePageFragment() {

    override fun initInjector() {
        ShopPerformanceComponentStubInstance.getShopPerformanceComponentStub(
            context!!.applicationContext
        ).inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ShopPerformanceFragmentStub {
            return ShopPerformanceFragmentStub()
        }
    }
}