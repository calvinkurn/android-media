package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnsResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
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

    @Test
    fun verifySaveAddonsFireAndForget() {
        val response = DataProvider.provideShipmentAddressFormWithAddOnsProductEnabledResponse()
        val cartShipmentAddressFormData = shipmentMapper
            .convertToShipmentAddressFormData(response.shipmentAddressFormResponse.data)
        cartShipmentAddressFormData.groupAddress.forEach { groupAddress ->
            groupAddress.groupShop.forEach { groupShop ->
                groupShop.groupShopData.forEach { groupShopV2 ->
                    groupShopV2.products.forEach {
                    }
                }
            }
        }
        val cartItemModel = CartItemModel(cartStringGroup = "111-111-111")

        coEvery {
            getShipmentAddressFormV4UseCase(
                any()
            )
        } returns cartShipmentAddressFormData

        coEvery { saveAddOnStateUseCase.setParams(any(), true) } just Runs
        coEvery { saveAddOnStateUseCase.execute(any(), any()) } answers {
            firstArg<(SaveAddOnStateResponse) -> Unit>().invoke(
                SaveAddOnStateResponse(
                    saveAddOns = SaveAddOnsResponse(
                        status = "OK"
                    )
                )
            )
        }

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )
        viewModel.saveAddOnsProduct(cartItemModel)

        // Then
        assert(viewModel.listSummaryAddOnModel.isNotEmpty())
    }
}
