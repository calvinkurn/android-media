package com.tokopedia.buyerorderdetail.cassava

import com.tokopedia.buyerorderdetail.BuyerOrderDetailMock
import com.tokopedia.buyerorderdetail.setupMock
import org.junit.Test

class BuyerOrderDetailTrackerValidationTest601 : BuyerOrderDetailTrackerValidationTestFixture() {
    @Test
    fun validateTrackersOnOrder601() {
        setupMock {
            mockOrderDetail(BuyerOrderDetailMock.BuyerOrderDetailMockResponse.MOCK_RESPONSE_601)
        } actionTest {
            launchBuyerOrderDetailActivity(activityRule)
            blockAllIntent()
            testClickToolbarChatIcon(context)
            testClickSeeDetail()
            testClickSeeInvoice(activityRule.activity)
            testClickCopyInvoice(activityRule.activity)
            testClickShopName(activityRule.activity)
            testClickProduct(activityRule.activity)
            testClickShipmentTnC(activityRule.activity)
            testClickCopyAWB(activityRule.activity)
            testClickPrimaryActionButton()
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
                    BuyerOrderDetailTrackerValidationConstant.clickPrimaryActionButtonSeeComplaintQueryPath
            )
        }
    }
}