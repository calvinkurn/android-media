package com.tokopedia.buyerorderdetail.cassava

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest450 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackersOnOrder450() {
        setupMock {
            mockOrderDetail(
                BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_450,
                graphqlRepositoryStub,
                context
            )
        } actionTest {
            launchBuyerOrderDetailActivity(activityRule)
            blockAllIntent()
            testClickToolbarChatIcon()
            testClickSeeDetail()
            testClickSeeInvoice(activityRule.activity)
            testClickCopyInvoice(activityRule.activity)
            testClickShopName(activityRule.activity)
            testClickProduct(activityRule.activity)
            testClickShipmentTnC(activityRule.activity)
            testClickCopyAWB(activityRule.activity)
            testClickPrimaryActionButton()
            testScrollToBottom(activityRule.activity)
            testClickSecondaryActionButtonRequestComplaint(activityRule.activity)
        } validate {
            clearQueries()
            addQueriesToValidate(
                BuyerOrderDetailTrackerValidationConstant.clickToolbarChatIconQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSeeDetailQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSeeInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickCopyInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickShopNameQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickProductQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickShipmentInfoTnCQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickCopyAWBQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonTrackQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonRequestComplaintQueryPath
            )
            hasPassedAnalytics(cassavaTestRule)
        }
    }
}
