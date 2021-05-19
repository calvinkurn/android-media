package com.tokopedia.rechargegeneral.screenshot.base

import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

abstract class BaseAngsuranKreditScreenShotTest: BaseRechargeGeneralScreenShotTest() {

    override fun getMockConfig(): MockModelConfig = RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.ANGSURAN_KREDIT)

    override fun getMenuId(): Int = 123

    override fun getCategoryId(): Int = 7

    override fun run_specific_product_test() {
        // TODO("Not yet implemented")
    }
}