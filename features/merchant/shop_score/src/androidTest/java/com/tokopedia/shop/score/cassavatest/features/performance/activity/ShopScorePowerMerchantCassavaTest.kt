package com.tokopedia.shop.score.cassavatest.features.performance.activity

import com.tokopedia.shop.score.cassavatest.features.performance.base.ShopScoreCassavaTest
import org.junit.Test

class ShopScorePowerMerchantCassavaTest : ShopScoreCassavaTest() {

    override fun setup() {
        super.setup()
        shopScorePrefManagerStub.setFinishCoachMark(false)
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = existingSellerWithRecomToolsResponse
        activityRule.launchActivity(getShopPerformancePageIntent())
    }

    @Test
    fun validateClickMerchantToolsRecommendation() {
        clickMerchantToolsRecommendation()
    }

    @Test
    fun validateClickPowerMerchantSection() {
        clickPowerMerchantSection()
    }

    @Test
    fun validateClickTickerPenalty() {
        clickTickerPenalty()
    }
}