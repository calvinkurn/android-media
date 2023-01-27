package com.tokopedia.buyerorderdetail.cassava

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest700 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackersOnOrder700() {
        setupMock {
            mockOrderDetail(
                BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_700,
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
            testClickProductActionButton(activityRule.activity)
            testClickInsuranceWidget()
            testClickCopyAWB(activityRule.activity)
            testClickPrimaryActionButton()
            testScrollToBottom(activityRule.activity)
            testClickSecondaryActionButtonSeePoD(activityRule.activity)
            testClickSecondaryActionButtonHelp(activityRule.activity)
        } validate {
            clearQueries()
            addQueriesToValidate(
                BuyerOrderDetailTrackerValidationConstant.clickToolbarChatIconQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSeeDetailQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSeeInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickCopyInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickShopNameQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickProductQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickBuyAgainProductQueryPath,
                BuyerOrderDetailTrackerValidationConstant.successBuyAgainProductQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickCopyAWBQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonGiveReviewQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonSeePoDQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonHelpQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickInsuranceWidgetQueryPath,
                BuyerOrderDetailTrackerValidationConstant.impressionInsuranceWidgetQueryPath
            )
            hasPassedAnalytics(cassavaTestRule)
        }
    }
}
