package com.tokopedia.shop.score.cassavatest.features.penalty

import com.tokopedia.shop.score.cassavatest.base.ShopPenaltyCassavaTest
import org.junit.Test

class PenaltyCassavaTest: ShopPenaltyCassavaTest() {

    override fun setup() {
        super.setup()
        getShopPenaltyDetailMergeUseCaseStub.responseStub = shopPenaltyResponse
    }

    @Test
    fun validateClickLearnMorePenalty() {
        activityRule.launchActivity(getShopPenaltyPageIntent())
        clickLearnMorePenalty()
    }

    @Test
    fun validateClickHelpCenterPenaltyDetail() {
        activityRule.launchActivity(getShopPenaltyPageIntent())
        clickHelpCenterPenaltyDetail()
    }
}