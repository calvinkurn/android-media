package com.tokopedia.checkout.view.presenter

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnsResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.verify
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
    fun verifySaveAddonsFireAndForgetReturnSuccess() {
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

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns cartShipmentAddressFormData

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

    @Test
    fun verifySaveAddonsFireAndForgetReturnFailed() {
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
        val throwable = ResponseErrorException("fail testing delete")

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns cartShipmentAddressFormData

        coEvery { saveAddOnStateUseCase.setParams(any(), true) } just Runs
        coEvery { saveAddOnStateUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
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

    @Test
    fun verifySaveAddonsBeforeCheckoutReturnSuccessResultOk() {
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

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns cartShipmentAddressFormData

        coEvery { saveAddOnStateUseCase.setParams(any(), false) } just Runs
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
        viewModel.saveAddOnsProductBeforeCheckout()

        // Then
        assert(viewModel.listSummaryAddOnModel.isNotEmpty())
        verify {
            view.handleOnSuccessSaveAddOnProduct()
        }
    }

    @Test
    fun verifySaveAddonsBeforeCheckoutReturnSuccessResultNotOkErrorMessageEmpty() {
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

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns cartShipmentAddressFormData

        coEvery { saveAddOnStateUseCase.setParams(any(), false) } just Runs
        coEvery { saveAddOnStateUseCase.execute(any(), any()) } answers {
            firstArg<(SaveAddOnStateResponse) -> Unit>().invoke(
                SaveAddOnStateResponse(
                    saveAddOns = SaveAddOnsResponse(
                        status = "ERROR"
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
        viewModel.saveAddOnsProductBeforeCheckout()

        // Then
        assert(viewModel.listSummaryAddOnModel.isNotEmpty())
        verify {
            view.showToastError(any())
        }
    }

    @Test
    fun verifySaveAddonsBeforeCheckoutReturnSuccessResultNotOkErrorMessageNotEmpty() {
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

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns cartShipmentAddressFormData

        coEvery { saveAddOnStateUseCase.setParams(any(), false) } just Runs
        coEvery { saveAddOnStateUseCase.execute(any(), any()) } answers {
            firstArg<(SaveAddOnStateResponse) -> Unit>().invoke(
                SaveAddOnStateResponse(
                    saveAddOns = SaveAddOnsResponse(
                        status = "ERROR",
                        errorMessage = listOf("Pesan Error")
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
        viewModel.saveAddOnsProductBeforeCheckout()

        // Then
        assert(viewModel.listSummaryAddOnModel.isNotEmpty())
        verify {
            view.showToastError(any())
        }
    }

    @Test
    fun verifySaveAddonsBeforeCheckoutReturnFailed() {
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
        val throwable = ResponseErrorException("fail testing delete")

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns cartShipmentAddressFormData

        coEvery { saveAddOnStateUseCase.setParams(any(), false) } just Runs
        coEvery { saveAddOnStateUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )
        viewModel.saveAddOnsProductBeforeCheckout()

        // Then
        assert(viewModel.listSummaryAddOnModel.isNotEmpty())
        verify {
            view.showToastError(any())
        }
    }
}
