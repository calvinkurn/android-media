package com.tokopedia.buyerorderdetail.cassava

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest600 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackerOnOrder600() {
        setupMock {
            mockOrderDetail(BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_600)
        } actionTest {
            launchBuyerOrderDetailActivity(activityRule)
            blockAllIntent()
            testClickToolbarChatIcon()
            testClickSeeDetail()
            testClickSeeInvoice(activityRule.activity)
            testClickCopyInvoice(activityRule.activity)
            testClickShopName(activityRule.activity)
            testClickProduct(activityRule.activity)
            testClickCopyAWB(activityRule.activity)
            testClickPrimaryActionButtonOnFinishOrderConfirmationBottomSheet(activityRule.activity, true)
            testClickSecondaryActionButtonOnFinishOrderConfirmationBottomSheet(activityRule.activity, true)
            testClickSecondaryActionButtonRequestComplaint(activityRule.activity)
            testClickSecondaryActionButtonTrack(activityRule.activity)
        } validate {
            clearQueries()
            addQueriesToValidate(
                    BuyerOrderDetailTrackerValidationConstant.clickToolbarChatIconQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickSeeDetailQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickSeeInvoiceQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickCopyInvoiceQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickShopNameQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickProductQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickCopyAWBQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonFinishOrderQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickFinishOrderOnFinishOrderConfirmationBottomSheet,
                    BuyerOrderDetailTrackerValidationConstant.clickRequestComplaintOnFinishOrderConfirmationBottomSheet,
                    BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonRequestComplaintQueryPath,
                    BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonTrackQueryPath)
            hasPassedAnalytics(cassavaTestRule)
        }
    }
}