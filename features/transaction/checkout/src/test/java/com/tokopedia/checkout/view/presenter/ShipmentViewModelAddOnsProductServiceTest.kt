package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.view.DataProvider
import io.mockk.coEvery
import org.junit.Test

class ShipmentViewModelAddOnsProductServiceTest : BaseShipmentViewModelTest() {

    private var shipmentMapper = ShipmentMapper()

    @Test
    fun verifyAddOnsProductIsSelected() {
        val response = DataProvider.provideShipmentAddressFormWithAddOnsProductEnabledResponse()
        val cartShipmentAddressFormData = shipmentMapper
            .convertToShipmentAddressFormData(response.shipmentAddressFormResponse.data)

        coEvery {
            getShipmentAddressFormV4UseCase(
                any()
            )
        } returns cartShipmentAddressFormData

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )
        viewModel.updateShipmentCostModel()

        // Then
        assert(viewModel.listSummaryAddOnModel.isNotEmpty())
    }
}
