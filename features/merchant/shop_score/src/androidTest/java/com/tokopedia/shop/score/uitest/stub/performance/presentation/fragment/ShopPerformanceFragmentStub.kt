package com.tokopedia.shop.score.uitest.stub.performance.presentation.fragment

import com.tokopedia.shop.score.performance.presentation.fragment.ShopPerformancePageFragment
import com.tokopedia.shop.score.uitest.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.shop.score.uitest.stub.common.di.component.DaggerBaseAppComponentStub
import com.tokopedia.shop.score.uitest.stub.common.di.module.AppModuleStub
import com.tokopedia.shop.score.uitest.stub.common.util.BaseAppComponentStubInstance
import com.tokopedia.shop.score.uitest.stub.common.util.ShopPerformanceComponentStubInstance
import com.tokopedia.shop.score.uitest.stub.performance.di.component.DaggerShopPerformanceComponentStub
import com.tokopedia.shop.score.uitest.stub.performance.di.module.ShopPerformanceModuleStub

class ShopPerformanceFragmentStub : ShopPerformancePageFragment() {

    override fun initInjector() {
        ShopPerformanceComponentStubInstance.getShopPerformanceComponentStub(
            activity!!.application
        ).inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ShopPerformanceFragmentStub {
            return ShopPerformanceFragmentStub()
        }
    }
}