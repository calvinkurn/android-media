package com.tokopedia.shop.score.cassavatest.features.performance

import com.tokopedia.shop.score.cassavatest.base.ShopScoreCassavaTest
import org.junit.Test

class RegularMerchantExistingCassavaTest: ShopScoreCassavaTest() {

    override fun setup() {
        super.setup()
        shopScorePrefManagerStub.setFinishCoachMark(true)
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = existingSellerRmResponse
    }

    @Test
    fun validateClickRegularMerchantSection() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        clickRegularMerchantSection()
    }
}