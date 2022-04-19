package com.tokopedia.rechargegeneral.screenshot.base.product

import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.screenshot.base.BaseRechargeGeneralScreenShotTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

abstract class BaseAirPdamScreenShotTest: BaseRechargeGeneralScreenShotTest() {

    override fun getMockConfig(): MockModelConfig {
        return when (isLogin()) {
            true -> RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.AIR_PDAM)
            false -> RechargeGeneralMockResponseConfig(RechargeGeneralProduct.AIR_PDAM)
        }
    }

    override fun getMenuId(): Int = 120

    override fun getCategoryId(): Int = 5

    override fun filePrefix(): String = "rg_airpdam"

    override fun run_specific_product_test() {
        // TODO("you can add product specific test here")
    }
}