package com.tokopedia.shop.score.cassavatest.features.performance

import com.tokopedia.shop.score.cassavatest.base.ShopScoreCassavaTest
import org.junit.Test

class PowerMerchantExistingCassavaTest : ShopScoreCassavaTest() {

    override fun setup() {
        super.setup()
        shopScorePrefManagerStub.setFinishCoachMark(true)
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = existingSellerPmRecomToolsResponse
    }

    @Test
    fun validateClickPowerMerchantSection() {
        activityRule.launchActivity(getShopPerformancePageIntent())
    }

    @Test
    fun validateClickTickerPenalty() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        clickTickerPenalty()
    }
}
