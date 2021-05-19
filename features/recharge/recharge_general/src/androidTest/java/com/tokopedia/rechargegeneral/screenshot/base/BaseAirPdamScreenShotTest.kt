package com.tokopedia.rechargegeneral.screenshot.base

import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

abstract class BaseAirPdamScreenShotTest: BaseRechargeGeneralScreenShotTest() {

    override fun getMockConfig(): MockModelConfig = RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.AIR_PDAM)

    override fun getMenuId(): Int = 120

    override fun getCategoryId(): Int = 5

    override fun run_specific_product_test() {
        // TODO("Not yet implemented")
    }
}