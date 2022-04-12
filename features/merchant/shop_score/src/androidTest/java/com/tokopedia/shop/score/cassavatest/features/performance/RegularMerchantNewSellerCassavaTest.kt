package com.tokopedia.shop.score.cassavatest.features.performance

import com.tokopedia.shop.score.cassavatest.base.ShopScoreCassavaTest
import org.junit.Test

class RegularMerchantNewSellerCassavaTest: ShopScoreCassavaTest() {

    override fun setup() {
        super.setup()
        shopScorePrefManagerStub.setFinishCoachMark(true)
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = newSellerRmResponse
    }

    @Test
    fun validateClickWatchVideoNewSeller() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        clickWatchVideoNewSeller()
    }

    @Test
    fun validateClickLearnPerformanceNewSeller() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        clickLearnPerformanceNewSeller()
    }

    @Test
    fun validateClickHelpCenterFaqNewSeller() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        clickHelpCenterFaqNewSeller()
    }
}