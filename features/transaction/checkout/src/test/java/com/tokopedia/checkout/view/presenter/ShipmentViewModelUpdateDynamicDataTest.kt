package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.model.UpdateDynamicDataPassingUiModel
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingButtonModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingMetadataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingNoteItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingTickerModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingWordingModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.AddOnGiftingResponse
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentViewModelUpdateDynamicDataTest : BaseShipmentViewModelTest() {

    private var updateDynamicDataParams = DynamicDataPassingParamRequest()

    @Test
    fun updateDynamicData_fireAndForget() {
        // Given
        val dynamicData = UpdateDynamicDataPassingUiModel("data")

        coEvery {
            updateDynamicDataPassingUseCase.setParams(any(), any())
        } just Runs
        coEvery {
            updateDynamicDataPassingUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateDynamicDataPassingUiModel) -> Unit>().invoke(dynamicData)
        }

        // When
        viewModel.validateDynamicData()
        viewModel.updateDynamicData(updateDynamicDataParams, true)

        // Then
        assertEquals("data", viewModel.dynamicData)
    }

    @Test
    fun updateDynamicData_notFireAndForget() {
        // Given
        val dynamicData = UpdateDynamicDataPassingUiModel("data")

        coEvery {
            updateDynamicDataPassingUseCase.setParams(any(), any())
        } just Runs
        coEvery {
            updateDynamicDataPassingUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateDynamicDataPassingUiModel) -> Unit>().invoke(dynamicData)
        }

        // When
        viewModel.validateDynamicData()
        viewModel.updateDynamicData(updateDynamicDataParams, false)

        // Then
        verify {
            view.doCheckout()
        }
    }

    @Test
    fun updateDynamicData_throwsCartResponseErrorException() {
        // Given
        val error = CartResponseErrorException("error")

        coEvery {
            updateDynamicDataPassingUseCase.setParams(any(), any())
        } just Runs
        coEvery { updateDynamicDataPassingUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        viewModel.validateDynamicData()
        viewModel.updateDynamicData(updateDynamicDataParams, true)

        // Then
        verify {
            view.showToastError(any())
        }
    }

    @Test
    fun updateDynamicData_throwsException() {
        // Given
        val error = Exception("error")
        coEvery {
            updateDynamicDataPassingUseCase.setParams(any(), any())
        } just Runs
        coEvery { updateDynamicDataPassingUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        viewModel.validateDynamicData()
        viewModel.updateDynamicData(updateDynamicDataParams, true)

        // Then
        verify {
            view.showToastError(any())
        }
    }

    @Test
    fun `WHEN presenter detached THEN all usecases is unsubscribed`() {
        // When
        viewModel.detachView()

        // Then
        verify {
            eligibleForAddressUseCase.cancelJobs()
            updateDynamicDataPassingUseCase.cancelJobs()
        }
    }

    @Test
    fun `WHEN update addOn product level data bottomsheet and is using ddp`() {
        // Given
        val isDdp = true
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                errorCode = 0,
                groupAddress = emptyList(),
                isUsingDdp = isDdp
            )
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "239594-0-301643").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(
                        cartId = 88,
                        cartStringGroup = "239594-0-301643"
                    )
                )
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-88"
            }
        )
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )
        viewModel.updateAddOnGiftingProductLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

        // Then
        assertEquals(isDdp, viewModel.isUsingDynamicDataPassing())
        verify {
            view.updateAddOnsData(
                0,
                shipmentCartItemModelList[0].cartItemModels[0].cartStringGroup,
                shipmentCartItemModelList[0].cartItemModels[0].cartId
            )
            view.updateAddOnsDynamicDataPassing(any(), any(), any(), any())
        }
    }

    @Test
    fun `WHEN update addOn product level data bottomsheet and not using ddp`() {
        // Given
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                errorCode = 0,
                groupAddress = emptyList(),
                isUsingDdp = true
            )
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "239594-0-301643").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(
                        cartId = 88,
                        cartStringGroup = "239594-0-301643"
                    )
                )
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-88"
            }
        )
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )
        viewModel.updateAddOnGiftingProductLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

        // Then
        verify {
            view.updateAddOnsData(
                0,
                shipmentCartItemModelList[0].cartItemModels[0].cartStringGroup,
                shipmentCartItemModelList[0].cartItemModels[0].cartId
            )
            view.updateAddOnsDynamicDataPassing(any(), any(), any(), any())
        }
    }

    @Test
    fun `WHEN SAF returns donation already checked THEN verify set param with donation`() {
        // Given
        val isDdp = true
        val listGroupAddress = arrayListOf<GroupAddress>()
        val groupAddress = GroupAddress(
            isError = false,
            userAddress = UserAddress(addressId = "1")
        )
        listGroupAddress.add(groupAddress)
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                errorCode = 0,
                groupAddress = listGroupAddress,
                isUsingDdp = isDdp,
                donation = Donation(isChecked = true)
            )
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "239594-0-301643").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(
                        cartId = 88,
                        cartStringGroup = "239594-0-301643"
                    )
                )
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-88"
            }
        )
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        assert(viewModel.getDynamicDataParam().data.isNotEmpty())
        assertEquals(isDdp, viewModel.isUsingDynamicDataPassing())
    }

    @Test
    fun `WHEN SAF returns addons order level already checked THEN verify set param with addons order level`() {
        // Given
        val isDdp = true
        val listGroupAddress = arrayListOf<GroupAddress>()
        val listGroupShop = arrayListOf<GroupShop>()
        val listAddOnDataItemModel = arrayListOf<AddOnGiftingDataItemModel>()
        val addOnDataItemModel = AddOnGiftingDataItemModel(
            addOnPrice = 5000.0,
            addOnId = "id",
            addOnMetadata = AddOnGiftingMetadataItemModel(
                addOnNoteItemModel = AddOnGiftingNoteItemModel(
                    isCustomNote = true,
                    to = "to",
                    from = "from",
                    notes = "notes"
                )
            )
        )
        listAddOnDataItemModel.add(addOnDataItemModel)
        val groupShop = GroupShop(
            groupShopData = listOf(
                GroupShopV2()
            ),
            addOns = AddOnGiftingDataModel(
                status = 1,
                addOnsButtonModel = AddOnGiftingButtonModel(
                    title = "test title button",
                    description = "test description button"
                ),
                addOnsBottomSheetModel = AddOnGiftingBottomSheetModel(
                    headerTitle = "test header title bottom sheet",
                    description = "test description bottom sheet",
                    ticker = AddOnGiftingTickerModel(
                        text = "test ticker model"
                    )
                ),
                addOnsDataItemModelList = listAddOnDataItemModel
            )
        )
        listGroupShop.add(groupShop)
        val groupAddress = GroupAddress(
            isError = false,
            userAddress = UserAddress(addressId = "1"),
            groupShop = listGroupShop
        )

        listGroupAddress.add(groupAddress)
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                errorCode = 0,
                groupAddress = listGroupAddress,
                isUsingDdp = isDdp,
                addOnWording = AddOnWordingData(
                    packagingAndGreetingCard = "packaging and greeting",
                    onlyGreetingCard = "only greeting card",
                    invoiceNotSendToRecipient = "invoice not send"
                )
            )
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(
                cartStringGroup = "239594-0-301643",
                cartItemModels = arrayListOf(
                    CartItemModel(
                        cartId = 88,
                        cartStringGroup = "239594-0-301643"
                    )
                ),
                addOnWordingModel = AddOnGiftingWordingModel(
                    packagingAndGreetingCard = "packaging and greeting",
                    onlyGreetingCard = "only greeting card",
                    invoiceNotSendToRecipient = "invoice not send"
                )
            )
        )

        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        assert(viewModel.getDynamicDataParam().data.isNotEmpty())
        assertEquals(isDdp, viewModel.isUsingDynamicDataPassing())
    }

    @Test
    fun `WHEN SAF returns addons product level already checked THEN verify set param with addons product level`() {
        // Given
        val isDdp = true
        val listGroupAddress = arrayListOf<GroupAddress>()
        val listGroupShop = arrayListOf<GroupShop>()
        val listAddOnDataItemModel = arrayListOf<AddOnGiftingDataItemModel>()
        val addOnDataItemModel = AddOnGiftingDataItemModel(
            addOnPrice = 5000.0,
            addOnId = "id",
            addOnMetadata = AddOnGiftingMetadataItemModel(
                addOnNoteItemModel = AddOnGiftingNoteItemModel(
                    isCustomNote = true,
                    to = "to",
                    from = "from",
                    notes = "notes"
                )
            )
        )
        listAddOnDataItemModel.add(addOnDataItemModel)
        val listProduct = arrayListOf<Product>()
        val listAddonItem = arrayListOf<AddOnGiftingResponse.AddOnDataItem>()
        val addOnDataItem = AddOnGiftingResponse.AddOnDataItem(
            addOnPrice = 5000.0,
            addOnId = "id",
            addOnMetadata = AddOnGiftingResponse.AddOnDataItem.AddOnMetadata(
                addOnNote = AddOnGiftingResponse.AddOnDataItem.AddOnMetadata.AddOnNote(
                    isCustomNote = true,
                    to = "to",
                    from = "from",
                    notes = "notes"
                )
            )
        )
        listAddonItem.add(addOnDataItem)
        val product = Product(
            addOnGiftingProduct = AddOnGiftingDataModel(
                status = 1,
                addOnsButtonModel = AddOnGiftingButtonModel(
                    title = "test title button",
                    description = "test description button"
                ),
                addOnsBottomSheetModel = AddOnGiftingBottomSheetModel(
                    headerTitle = "test header title bottom sheet",
                    description = "test description bottom sheet",
                    ticker = AddOnGiftingTickerModel(
                        text = "test ticker model"
                    )
                ),
                addOnsDataItemModelList = listAddOnDataItemModel
            )
        )
        listProduct.add(product)
        val groupShop = GroupShop(
            groupShopData = listOf(
                GroupShopV2(
                    products = listProduct
                )
            )
        )
        listGroupShop.add(groupShop)
        val groupAddress = GroupAddress(
            isError = false,
            userAddress = UserAddress(addressId = "1"),
            groupShop = listGroupShop
        )

        listGroupAddress.add(groupAddress)
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                errorCode = 0,
                groupAddress = listGroupAddress,
                isUsingDdp = isDdp,
                addOnWording = AddOnWordingData(
                    packagingAndGreetingCard = "packaging and greeting",
                    onlyGreetingCard = "only greeting card",
                    invoiceNotSendToRecipient = "invoice not send"
                )
            )
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(
                cartStringGroup = "239594-0-301643",
                cartItemModels = arrayListOf(
                    CartItemModel(
                        cartId = 88,
                        cartStringGroup = "239594-0-301643"
                    )
                ),
                addOnWordingModel = AddOnGiftingWordingModel(
                    packagingAndGreetingCard = "packaging and greeting",
                    onlyGreetingCard = "only greeting card",
                    invoiceNotSendToRecipient = "invoice not send"
                )
            )
        )

        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        assert(viewModel.getDynamicDataParam().data.isNotEmpty())
        assertEquals(isDdp, viewModel.isUsingDynamicDataPassing())
    }

    @Test
    fun `WHEN SAF returns isDdp is false THEN verify param is empty`() {
        // Given
        val isDdp = false
        val listGroupAddress = arrayListOf<GroupAddress>()
        val groupAddress = GroupAddress(
            isError = false,
            userAddress = UserAddress(addressId = "1")
        )
        listGroupAddress.add(groupAddress)
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                errorCode = 0,
                groupAddress = listGroupAddress,
                isUsingDdp = isDdp
            )
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "239594-0-301643").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(
                        cartId = 88,
                        cartStringGroup = "239594-0-301643"
                    )
                )
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-88"
            }
        )
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        assert(viewModel.getDynamicDataParam().data.isEmpty())
        assert(viewModel.getDynamicDataParam().data.isEmpty())
        assertEquals(isDdp, viewModel.isUsingDynamicDataPassing())
    }
}
