package com.tokopedia.rechargegeneral.screenshot.base

import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

abstract class BasePbbScreenShotTest: BaseRechargeGeneralScreenShotTest() {

    override fun getMockConfig(): MockModelConfig = RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.PBB)

    override fun getMenuId(): Int = 127

    override fun getCategoryId(): Int = 22

    override fun run_specific_product_test() {
        // TODO("Not yet implemented")
    }
}