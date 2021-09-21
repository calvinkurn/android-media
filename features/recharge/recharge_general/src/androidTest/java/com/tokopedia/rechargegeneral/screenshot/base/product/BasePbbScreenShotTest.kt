package com.tokopedia.rechargegeneral.screenshot.base.product

import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.screenshot.base.BaseRechargeGeneralScreenShotTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

abstract class BasePbbScreenShotTest: BaseRechargeGeneralScreenShotTest() {

    override fun getMockConfig(): MockModelConfig {
        return when (isLogin()) {
            true -> RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.PBB)
            false -> RechargeGeneralMockResponseConfig(RechargeGeneralProduct.PBB)
        }
    }
    override fun getMenuId(): Int = 127

    override fun getCategoryId(): Int = 22

    override fun filePrefix(): String = "rg_pbb"

    override fun run_specific_product_test() {
        // TODO("you can add product specific test here")
    }
}