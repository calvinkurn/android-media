package com.tokopedia.buyerorderdetail.cassava

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest0 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackersOnOrder0() {
        setupMock {
            mockOrderDetail(
                BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_10,
                graphqlRepositoryStub,
                context
            )
        } actionTest {
            launchBuyerOrderDetailActivity(activityRule)
            blockAllIntent()
            testClickSeeDetail()
            testClickSeeInvoice(activityRule.activity)
            testClickCopyInvoice(activityRule.activity)
            testClickShopName(activityRule.activity)
            testClickProduct(activityRule.activity)
            testClickProductActionButton(activityRule.activity)
            testScrollToBottom(activityRule.activity)
            testClickPrimaryActionButton()
            testClickSecondaryActionButtonHelp(activityRule.activity)
        } validate {
            clearQueries()
            addQueriesToValidate(
                BuyerOrderDetailTrackerValidationConstant.clickSeeDetailQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSeeInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickCopyInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickShopNameQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickProductQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSeeSimilarProductQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonChatSellerQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonHelpQueryPath
            )
            hasPassedAnalytics(cassavaTestRule)
        }
    }
}
