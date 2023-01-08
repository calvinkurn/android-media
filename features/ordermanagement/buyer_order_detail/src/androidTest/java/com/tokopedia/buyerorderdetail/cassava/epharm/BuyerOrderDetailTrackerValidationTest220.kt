package com.tokopedia.buyerorderdetail.cassava.epharm

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.cassava.BuyerOrderDetailTrackerValidationConstant
import com.tokopedia.buyerorderdetail.cassava.BuyerOrderDetailTrackerValidationTestFixture
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest220 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackersOnOrder220() {
        setupMock {
            mockOrderDetail(
                BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_220_E_PHARMACY,
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
            testClickPrimaryActionButton()
            testClickSecondaryActionButtonHelp(activityRule.activity)
            testClickSecondaryActionButtonRequestCancel(activityRule.activity)
        } validate {
            clearQueries()
            addQueriesToValidate(
                BuyerOrderDetailTrackerValidationConstant.clickSeeDetailQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSeeInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickCopyInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickShopNameQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickProductQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonReUploadPrescriptionQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonHelpQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonRequestCancelQueryPath
            )
            hasPassedAnalytics(cassavaTestRule)
        }
    }
}