package com.tokopedia.buyerorderdetail.cassava

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest530 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackersOnOrder530() {
        setupMock {
            mockOrderDetail(
                BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_530,
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
            testClickCopyAWB(activityRule.activity)
            testScrollToBottom(activityRule.activity)
            testClickPrimaryActionButton()
            testClickPrimaryActionButtonOnFinishOrderConfirmationBottomSheet(
                activityRule.activity,
                false
            )
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
                BuyerOrderDetailTrackerValidationConstant.clickCopyAWBQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonTrackQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonRequestComplaintQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonFinishOrderQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickFinishOrderOnFinishOrderConfirmationBottomSheet
            )
            hasPassedAnalytics(cassavaTestRule)
        }
    }
}
