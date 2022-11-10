package com.tokopedia.buyerorderdetail.cassava.epharm

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.cassava.BuyerOrderDetailTrackerValidationConstant
import com.tokopedia.buyerorderdetail.cassava.BuyerOrderDetailTrackerValidationTestFixture
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest400 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackersOnOrder400() {
        setupMock {
            mockOrderDetail(
                BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_400_E_PHARMACY,
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
            testClickShipmentTnC(activityRule.activity)
            testClickPrimaryActionButton()
            testClickSecondaryActionButtonHelp(activityRule.activity)
            testClickSecondaryActionButtonRequestCancel(activityRule.activity)
            testClickSecondaryActionButtonCheckPrescription(activityRule.activity)
        } validate {
            clearQueries()
            addQueriesToValidate(
                BuyerOrderDetailTrackerValidationConstant.clickSeeDetailQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSeeInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickCopyInvoiceQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickShopNameQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickProductQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickShipmentInfoTnCQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonChatSellerQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonHelpQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonRequestCancelQueryPath,
                BuyerOrderDetailTrackerValidationConstant.clickSecondaryActionButtonCheckPrescriptionQueryPath,
            )
            hasPassedAnalytics(cassavaTestRule)
        }
    }
}