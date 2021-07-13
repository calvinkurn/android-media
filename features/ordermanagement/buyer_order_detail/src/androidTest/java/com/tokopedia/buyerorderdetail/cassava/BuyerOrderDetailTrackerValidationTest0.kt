package com.tokopedia.buyerorderdetail.cassava

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest0 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackersOnOrder0() {
        setupMock {
            mockOrderDetail(BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_0)
        } actionTest {
            launchBuyerOrderDetailActivity(activityRule)
            blockAllIntent()
            testClickToolbarChatIcon()
            testClickSeeDetail()
            testClickSeeInvoice(activityRule.activity)
            testClickCopyInvoice(activityRule.activity)
            testClickShopName(activityRule.activity)
            testClickProduct(activityRule.activity)
            testClickProductActionButton(activityRule.activity)
            testClickShipmentTnC(activityRule.activity)
            testClickCopyAWB(activityRule.activity)
            testClickPrimaryActionButton()
        } validate {
            clearQueries()
            addQueriesToValidate(
                    BuyerOrderDetailTrackerValidationConstant.clickToolbarChatIconQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickSeeDetailQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickSeeInvoiceQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickShopNameQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickProductQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickSeeSimilarProductQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickShipmentInfoTnCQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickCopyAWBQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonHelpQueryPath)
            hasPassedAnalytics(cassavaTestRule)
        }
    }
}