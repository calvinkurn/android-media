package com.tokopedia.shop.score.uitest.features.performance.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreTest
import org.junit.Test

class ShopScoreExistingOSActivityTest: ShopScoreTest() {

    override fun setup() {
        super.setup()
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = existingSellerOsResponse


    }

    @Test
    fun `header performance when existing official store`() {
    }

}