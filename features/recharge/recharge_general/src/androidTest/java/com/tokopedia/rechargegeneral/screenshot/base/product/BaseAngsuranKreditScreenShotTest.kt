package com.tokopedia.rechargegeneral.screenshot.base.product

import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.RechargeGeneralMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.screenshot.base.BaseRechargeGeneralScreenShotTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

abstract class BaseAngsuranKreditScreenShotTest: BaseRechargeGeneralScreenShotTest() {

    override fun getMockConfig(): MockModelConfig {
        return when (isLogin()) {
            true -> RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.ANGSURAN_KREDIT)
            false -> RechargeGeneralMockResponseConfig(RechargeGeneralProduct.ANGSURAN_KREDIT)
        }
    }
    override fun getMenuId(): Int = 123

    override fun getCategoryId(): Int = 7

    override fun filePrefix(): String = "rg_angsuran_kredit"

    override fun run_specific_product_test() {
        // TODO("Not yet implemented")
    }
}