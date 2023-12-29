package com.tokopedia.tokofood.cassavatest.features.purchase.promo

import com.tokopedia.tokofood.cassavatest.base.TokoFoodPromoCassavaTest
import com.tokopedia.tokofood.stub.base.presentation.activity.BaseTokofoodActivityStub
import org.junit.Test

class LoadPromoCassavaTest: TokoFoodPromoCassavaTest() {

    override fun setup() {
        super.setup()
        promoListTokoFoodUseCaseStub.responseStub = promoListTokofoodResponseStub
        launchActivity(BaseTokofoodActivityStub.PROMO_PAGE)
    }

    @Test
    fun loadPromoPage() {
        validateTracker(LOAD_PROMO_LIST)
        dismissPage()
    }

}
