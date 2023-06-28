package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.data.creditcard.CartDetailsItem
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListRequest
import com.tokopedia.oneclickcheckout.order.view.mapper.SaveAddOnStateMapper.SAVE_ADD_ON_STATE_QUANTITY
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccData
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccPaymentParameter
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccRedirectParam
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccResult
import com.tokopedia.oneclickcheckout.order.view.model.CreditCardTenorListData
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCard
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCardAdditionalData
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.oneclickcheckout.order.view.model.TenorListData
import com.tokopedia.oneclickcheckout.utils.TestUtil.getPrivateField
import com.tokopedia.oneclickcheckout.utils.TestUtil.mockPrivateField
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_UNCHECK
import com.tokopedia.purchase_platform.common.feature.addons.data.response.AddOnDataResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.AddOnResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.DataResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnsResponse
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelSaveAddOnTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `WHEN update some addons on product, save addon failed but THEN still continue to calculate total order`() {
        // Given
        val productId = "1233"
        val productOrderQuantity = 2
        val productPrice = 100000.0
        val addOnProduct1Id = "123"
        val addOnProduct1Price = 20000L
        val addOnProduct1Quantity = 2
        val addOnProduct2Id = "125"
        val addOnProduct2Price = 40000L
        val addOnProduct2Quantity = 2
        val deltaAssertEquals = .1

        val addOnProduct1 = AddOnsProductDataModel.Data(
            id = addOnProduct1Id,
            price = addOnProduct1Price,
            productQuantity = addOnProduct1Quantity,
            status = ADD_ON_PRODUCT_STATUS_UNCHECK
        )

        val addOnProduct2 = AddOnsProductDataModel.Data(
            id = addOnProduct2Id,
            price = addOnProduct2Price,
            productQuantity = addOnProduct2Quantity,
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val newAddOnProduct = addOnProduct1.copy(
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val orderProduct = OrderProduct(
            productId = productId,
            orderQuantity = productOrderQuantity,
            productPrice = productPrice,
            addOnsProductData = AddOnsProductDataModel(
                data = listOf(
                    addOnProduct1,
                    addOnProduct2
                )
            )
        )

        setupDummyDataForCalculationPurpose(
            orderProduct = orderProduct
        )

        coEvery {
            saveAddOnStateUseCase.executeOnBackground()
        } throws Throwable()

        // When
        orderSummaryPageViewModel.updateAddOnProduct(
            newAddOnProductData = newAddOnProduct,
            product = orderProduct
        )

        // Then
        val expectedGlobalEvent = OccGlobalEvent.Normal
        val expectedOrderProducts = listOf(
            orderProduct.copy(
                addOnsProductData = orderProduct.addOnsProductData.copy(
                    data = orderProduct.addOnsProductData.data.map { addOnData ->
                        if (addOnData.id == newAddOnProduct.id) {
                            addOnData.status = newAddOnProduct.status
                            addOnData
                        } else {
                            addOnData
                        }
                    }
                )
            )
        )
        val expectedTotalPrice = orderProduct.addOnsProductData.data.filter { it.status == ADD_ON_PRODUCT_STATUS_CHECK }.sumOf { it.price * it.productQuantity }.toDouble() + orderProduct.orderQuantity * orderProduct.productPrice
        Assert.assertEquals(
            expectedGlobalEvent,
            orderSummaryPageViewModel.globalEvent.value,
        )
        Assert.assertEquals(
            expectedOrderProducts,
            orderSummaryPageViewModel.orderProducts.value,
        )
        Assert.assertEquals(
            expectedTotalPrice,
            orderSummaryPageViewModel.orderTotal.value.orderCost.totalPrice,
            deltaAssertEquals
        )
    }

    @Test
    fun `WHEN update some addons on product, save addons get successful result THEN calculate total order`() {
        // Given
        val productId = "1233"
        val productOrderQuantity = 2
        val productPrice = 100000.0
        val addOnProduct1Id = "123"
        val addOnProduct1Price = 20000L
        val addOnProduct1Quantity = 2
        val addOnProduct2Id = "125"
        val addOnProduct2Price = 40000L
        val addOnProduct2Quantity = 2
        val deltaAssertEquals = .1

        val addOnProduct1 = AddOnsProductDataModel.Data(
            id = addOnProduct1Id,
            price = addOnProduct1Price,
            productQuantity = addOnProduct1Quantity,
            status = ADD_ON_PRODUCT_STATUS_UNCHECK
        )

        val addOnProduct2 = AddOnsProductDataModel.Data(
            id = addOnProduct2Id,
            price = addOnProduct2Price,
            productQuantity = addOnProduct2Quantity,
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val newAddOnProduct = addOnProduct1.copy(
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val orderProduct = OrderProduct(
            productId = productId,
            orderQuantity = productOrderQuantity,
            productPrice = productPrice,
            addOnsProductData = AddOnsProductDataModel(
                data = listOf(
                    addOnProduct1,
                    addOnProduct2
                )
            )
        )

        setupDummyDataForCalculationPurpose(
            orderProduct = orderProduct
        )

        val saveAddOnsResponse = SaveAddOnsResponse(
            status = STATUS_OK,
            data = DataResponse(
                addOns = listOf(
                    AddOnResponse(
                        addOnData = listOf(
                            AddOnDataResponse(
                                addOnId = addOnProduct1Id,
                                addOnPrice = addOnProduct1Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            ),
                            AddOnDataResponse(
                                addOnId = addOnProduct2Id,
                                addOnPrice = addOnProduct2Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            saveAddOnStateUseCase.executeOnBackground()
        } returns SaveAddOnStateResponse(
            saveAddOns = saveAddOnsResponse
        )

        // When
        orderSummaryPageViewModel.updateAddOnProduct(
            newAddOnProductData = newAddOnProduct,
            product = orderProduct
        )

        // Then
        val expectedGlobalEvent = OccGlobalEvent.Normal
        val expectedOrderProducts = listOf(
            orderProduct.copy(
                addOnsProductData = orderProduct.addOnsProductData.copy(
                    data = orderProduct.addOnsProductData.data.map { addOnData ->
                        if (addOnData.id == newAddOnProduct.id) {
                            addOnData.status = newAddOnProduct.status
                            addOnData
                        } else {
                            addOnData
                        }
                    }
                )
            )
        )
        val expectedTotalPrice = orderProduct.addOnsProductData.data.filter { it.status == ADD_ON_PRODUCT_STATUS_CHECK }.sumOf { it.price * it.productQuantity }.toDouble() + orderProduct.orderQuantity * orderProduct.productPrice

        Assert.assertEquals(
            expectedGlobalEvent,
            orderSummaryPageViewModel.globalEvent.value,
        )
        Assert.assertEquals(
            expectedOrderProducts,
            orderSummaryPageViewModel.orderProducts.value,
        )
        Assert.assertEquals(
            expectedTotalPrice,
            orderSummaryPageViewModel.orderTotal.value.orderCost.totalPrice,
            deltaAssertEquals
        )
    }

    @Test
    fun `WHEN update some addons on product, save addons get successful result THEN data and total order will not be updated because the product id is not found`() {
        // Given
        val productId = "1233"
        val actualProductId = "23123"
        val productOrderQuantity = 2
        val productPrice = 100000.0
        val addOnProduct1Id = "123"
        val addOnProduct1Price = 20000L
        val addOnProduct1Quantity = 2
        val addOnProduct2Id = "125"
        val addOnProduct2Price = 40000L
        val addOnProduct2Quantity = 2
        val deltaAssertEquals = .1

        val addOnProduct1 = AddOnsProductDataModel.Data(
            id = addOnProduct1Id,
            price = addOnProduct1Price,
            productQuantity = addOnProduct1Quantity,
            status = ADD_ON_PRODUCT_STATUS_UNCHECK
        )

        val addOnProduct2 = AddOnsProductDataModel.Data(
            id = addOnProduct2Id,
            price = addOnProduct2Price,
            productQuantity = addOnProduct2Quantity,
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val newAddOnProduct = addOnProduct1.copy(
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val orderProduct = OrderProduct(
            productId = productId,
            orderQuantity = productOrderQuantity,
            productPrice = productPrice,
            addOnsProductData = AddOnsProductDataModel(
                data = listOf(
                    addOnProduct1,
                    addOnProduct2
                )
            )
        )

        setupDummyDataForCalculationPurpose(
            orderProduct = orderProduct.copy(productId = actualProductId)
        )

        val saveAddOnsResponse = SaveAddOnsResponse(
            status = STATUS_OK,
            data = DataResponse(
                addOns = listOf(
                    AddOnResponse(
                        addOnData = listOf(
                            AddOnDataResponse(
                                addOnId = addOnProduct1Id,
                                addOnPrice = addOnProduct1Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            ),
                            AddOnDataResponse(
                                addOnId = addOnProduct2Id,
                                addOnPrice = addOnProduct2Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            saveAddOnStateUseCase.executeOnBackground()
        } returns SaveAddOnStateResponse(
            saveAddOns = saveAddOnsResponse
        )

        // When
        orderSummaryPageViewModel.updateAddOnProduct(
            newAddOnProductData = newAddOnProduct,
            product = orderProduct
        )

        // Then
        val expectedGlobalEvent = OccGlobalEvent.Normal
        val expectedOrderProducts = listOf(orderProduct.copy(productId = actualProductId, finalPrice = productPrice))
        val expectedTotalPrice = orderProduct.addOnsProductData.data.filter { it.status == ADD_ON_PRODUCT_STATUS_CHECK }.sumOf { it.price * it.productQuantity }.toDouble() + orderProduct.orderQuantity * orderProduct.productPrice

        Assert.assertEquals(
            expectedGlobalEvent,
            orderSummaryPageViewModel.globalEvent.value,
        )
        Assert.assertEquals(
            expectedOrderProducts,
            orderSummaryPageViewModel.orderProducts.value,
        )
        Assert.assertEquals(
            expectedTotalPrice,
            orderSummaryPageViewModel.orderTotal.value.orderCost.totalPrice,
            deltaAssertEquals
        )
    }

    @Test
    fun `WHEN update some addons on product, save addons get successful result THEN data and total order will not be updated because the addon id is not found`() {
        // Given
        val productId = "1233"
        val productOrderQuantity = 2
        val productPrice = 100000.0
        val addOnProduct1Id = "123"
        val addOnProduct1Price = 20000L
        val addOnProduct1Quantity = 2
        val addOnProduct2Id = "125"
        val addOnProduct2Price = 40000L
        val addOnProduct2Quantity = 2
        val newAddOnProductId = "1222313"
        val deltaAssertEquals = .1

        val addOnProduct1 = AddOnsProductDataModel.Data(
            id = addOnProduct1Id,
            price = addOnProduct1Price,
            productQuantity = addOnProduct1Quantity,
            status = ADD_ON_PRODUCT_STATUS_UNCHECK
        )

        val addOnProduct2 = AddOnsProductDataModel.Data(
            id = addOnProduct2Id,
            price = addOnProduct2Price,
            productQuantity = addOnProduct2Quantity,
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val newAddOnProduct = addOnProduct1.copy(
            id = newAddOnProductId,
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val orderProduct = OrderProduct(
            productId = productId,
            orderQuantity = productOrderQuantity,
            productPrice = productPrice,
            addOnsProductData = AddOnsProductDataModel(
                data = listOf(
                    addOnProduct1,
                    addOnProduct2
                )
            )
        )

        setupDummyDataForCalculationPurpose(
            orderProduct = orderProduct
        )

        val saveAddOnsResponse = SaveAddOnsResponse(
            status = STATUS_OK,
            data = DataResponse(
                addOns = listOf(
                    AddOnResponse(
                        addOnData = listOf(
                            AddOnDataResponse(
                                addOnId = addOnProduct1Id,
                                addOnPrice = addOnProduct1Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            ),
                            AddOnDataResponse(
                                addOnId = addOnProduct2Id,
                                addOnPrice = addOnProduct2Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            saveAddOnStateUseCase.executeOnBackground()
        } returns SaveAddOnStateResponse(
            saveAddOns = saveAddOnsResponse
        )

        // When
        orderSummaryPageViewModel.updateAddOnProduct(
            newAddOnProductData = newAddOnProduct,
            product = orderProduct
        )

        // Then
        val expectedGlobalEvent = OccGlobalEvent.Normal
        val expectedOrderProducts = listOf(
            orderProduct.copy(
                addOnsProductData = orderProduct.addOnsProductData.copy(
                    data = orderProduct.addOnsProductData.data.map { addOnData ->
                        if (addOnData.id == newAddOnProduct.id) {
                            addOnData.status = newAddOnProduct.status
                            addOnData
                        } else {
                            addOnData
                        }
                    }
                )
            )
        )
        val expectedTotalPrice = orderProduct.addOnsProductData.data.filter { it.status == ADD_ON_PRODUCT_STATUS_CHECK }.sumOf { it.price * it.productQuantity }.toDouble() + orderProduct.orderQuantity * orderProduct.productPrice
        Assert.assertEquals(
            expectedGlobalEvent,
            orderSummaryPageViewModel.globalEvent.value,
        )
        Assert.assertEquals(
            expectedOrderProducts,
            orderSummaryPageViewModel.orderProducts.value,
        )
        Assert.assertEquals(
            expectedTotalPrice,
            orderSummaryPageViewModel.orderTotal.value.orderCost.totalPrice,
            deltaAssertEquals
        )
    }

    @Test
    fun `WHEN save all addons on product is executed but the result is successful THEN final update will not be blocked`() {
        // Given
        orderSummaryPageViewModel.mockPrivateField("saveAddOnProductStateJobs", mutableMapOf("123" to Job(), "444" to Job()))

        val productId = "1233"
        val productOrderQuantity = 2
        val productPrice = 100000.0
        val addOnProduct1Id = "123"
        val addOnProduct1Price = 20000L
        val addOnProduct1Quantity = 2
        val addOnProduct2Id = "125"
        val addOnProduct2Price = 40000L
        val addOnProduct2Quantity = 2
        val newAddOnProductId = "1222313"

        val addOnProduct1 = AddOnsProductDataModel.Data(
            id = addOnProduct1Id,
            price = addOnProduct1Price,
            productQuantity = addOnProduct1Quantity,
            status = ADD_ON_PRODUCT_STATUS_UNCHECK
        )

        val addOnProduct2 = AddOnsProductDataModel.Data(
            id = addOnProduct2Id,
            price = addOnProduct2Price,
            productQuantity = addOnProduct2Quantity,
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val newAddOnProduct = addOnProduct1.copy(
            id = newAddOnProductId,
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val orderProduct = OrderProduct(
            productId = productId,
            orderQuantity = productOrderQuantity,
            productPrice = productPrice,
            addOnsProductData = AddOnsProductDataModel(
                data = listOf(
                    addOnProduct1,
                    addOnProduct2
                )
            )
        )

        setupDummyDataForCalculationPurpose(
            orderProduct = orderProduct
        )

        val saveAddOnsResponse = SaveAddOnsResponse(
            status = STATUS_OK,
            data = DataResponse(
                addOns = listOf(
                    AddOnResponse(
                        addOnData = listOf(
                            AddOnDataResponse(
                                addOnId = addOnProduct1Id,
                                addOnPrice = addOnProduct1Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            ),
                            AddOnDataResponse(
                                addOnId = addOnProduct2Id,
                                addOnPrice = addOnProduct2Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            saveAddOnStateUseCase.executeOnBackground()
        } returns SaveAddOnStateResponse(
            saveAddOns = saveAddOnsResponse
        )

        // When
        orderSummaryPageViewModel.finalUpdate(
            onSuccessCheckout = {},
            skipCheckIneligiblePromo = false
        )

        // Then
        val expectedOrderProducts = listOf(
            orderProduct.copy(
                addOnsProductData = orderProduct.addOnsProductData.copy(
                    data = orderProduct.addOnsProductData.data.map { addOnData ->
                        if (addOnData.id == newAddOnProduct.id) {
                            addOnData.status = newAddOnProduct.status
                            addOnData
                        } else {
                            addOnData
                        }
                    }
                )
            )
        )
        Assert.assertEquals(
            expectedOrderProducts,
            orderSummaryPageViewModel.orderProducts.value,
        )
    }

    @Test
    fun `WHEN save all addons on product is executed but there is no addons THEN final update will not be blocked`() {
        // Given
        orderSummaryPageViewModel.mockPrivateField("saveAddOnProductStateJobs", mutableMapOf("123" to Job(), "444" to Job()))

        val productId = "1233"
        val productOrderQuantity = 2
        val productPrice = 100000.0
        val addOnProduct1Id = "123"
        val addOnProduct1Price = 20000L
        val addOnProduct2Id = "125"
        val addOnProduct2Price = 40000L

        val orderProduct = OrderProduct(
            productId = productId,
            orderQuantity = productOrderQuantity,
            productPrice = productPrice,
            addOnsProductData = AddOnsProductDataModel(
                data = listOf()
            )
        )

        setupDummyDataForCalculationPurpose(
            orderProduct = orderProduct
        )

        val saveAddOnsResponse = SaveAddOnsResponse(
            status = STATUS_OK,
            data = DataResponse(
                addOns = listOf(
                    AddOnResponse(
                        addOnData = listOf(
                            AddOnDataResponse(
                                addOnId = addOnProduct1Id,
                                addOnPrice = addOnProduct1Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            ),
                            AddOnDataResponse(
                                addOnId = addOnProduct2Id,
                                addOnPrice = addOnProduct2Price.toDouble(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            saveAddOnStateUseCase.executeOnBackground()
        } returns SaveAddOnStateResponse(
            saveAddOns = saveAddOnsResponse
        )

        // When
        orderSummaryPageViewModel.finalUpdate(
            onSuccessCheckout = {},
            skipCheckIneligiblePromo = false
        )

        // Then
        val expectedOrderProducts = listOf(
            orderProduct
        )
        Assert.assertEquals(
            expectedOrderProducts,
            orderSummaryPageViewModel.orderProducts.value,
        )
    }

    @Test
    fun `WHEN save all addons on product is executed but the result is failed THEN it will block the final update and show the toaster`() {
        // Given
        orderSummaryPageViewModel.mockPrivateField("saveAddOnProductStateJobs", mutableMapOf("123" to Job(), "444" to Job()))

        val productId = "1233"
        val productOrderQuantity = 2
        val productPrice = 100000.0
        val addOnProduct1Id = "123"
        val addOnProduct1Price = 20000L
        val addOnProduct1Quantity = 2
        val addOnProduct2Id = "125"
        val addOnProduct2Price = 40000L
        val addOnProduct2Quantity = 2
        val errorCta = "Oke"
        val errorThrowable = Throwable()

        val addOnProduct1 = AddOnsProductDataModel.Data(
            id = addOnProduct1Id,
            price = addOnProduct1Price,
            productQuantity = addOnProduct1Quantity,
            status = ADD_ON_PRODUCT_STATUS_UNCHECK
        )

        val addOnProduct2 = AddOnsProductDataModel.Data(
            id = addOnProduct2Id,
            price = addOnProduct2Price,
            productQuantity = addOnProduct2Quantity,
            status = ADD_ON_PRODUCT_STATUS_CHECK
        )

        val orderProduct = OrderProduct(
            productId = productId,
            orderQuantity = productOrderQuantity,
            productPrice = productPrice,
            addOnsProductData = AddOnsProductDataModel(
                data = listOf(
                    addOnProduct1,
                    addOnProduct2
                )
            )
        )

        setupDummyDataForCalculationPurpose(
            orderProduct = orderProduct
        )

        coEvery {
            saveAddOnStateUseCase.executeOnBackground()
        } throws errorThrowable

        // When
        orderSummaryPageViewModel.finalUpdate(
            onSuccessCheckout = {},
            skipCheckIneligiblePromo = false
        )

        // Then
        val expectedGlobalEvent = OccGlobalEvent.Error(
            throwable = errorThrowable,
            ctaText = errorCta
        )
        Assert.assertEquals(
            expectedGlobalEvent,
            orderSummaryPageViewModel.globalEvent.value,
        )
    }

    @Test
    fun `WHEN call onCleared THEN cancel and remove all jobs from saveAddOnProductStateJobs`() {
        orderSummaryPageViewModel.mockPrivateField("saveAddOnProductStateJobs", mutableMapOf("123" to Job(), "444" to Job()))

        val method = orderSummaryPageViewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(orderSummaryPageViewModel)

        Assert.assertEquals(
            emptyMap<String, Job>(),
            orderSummaryPageViewModel.getPrivateField("saveAddOnProductStateJobs")
        )
    }

    private fun setupDummyDataForCalculationPurpose(
        orderProduct: OrderProduct
    ) {
        val profileCode = "TKPD_DEFAULT"
        val totalProductPrice = "50000"
        val tenorType = "FULL"
        val tenorAmount = 18500.0
        val tenorBank = "014"
        val tenorFee = 5500.0
        val tenorKey = "INPUT"
        val userId = 1234L
        val shopType = 1
        val paymentAmount = 40000.0
        val shippingPrice = 0
        val shippingServiceName = "service"
        val shippingProductId = orderProduct.productId.toIntOrZero()
        val checkoutOccResultSuccess = 1
        val checkoutOccResultUrl = "testurl"
        val checkoutOccResultForm = "transaction_id=123"

        val additionalData = OrderPaymentCreditCardAdditionalData(
            profileCode = profileCode,
            totalProductPrice = totalProductPrice
        )

        val creditCardTenorListData = CreditCardTenorListData(
            tenorList = listOf(
                TenorListData(
                    type = tenorType,
                    amount = tenorAmount,
                    bank = tenorBank,
                    fee = tenorFee,
                    rate = Int.ZERO.toDouble()
                )
            )
        )

        val ccTenorListRequest = mapOf(
            tenorKey to CreditCardTenorListRequest(
                String.EMPTY,
                userId,
                totalProductPrice.toDouble(),
                Int.ZERO.toDouble(),
                profileCode,
                Int.ZERO.toDouble(),
                listOf(
                    CartDetailsItem(
                        shopType,
                        paymentAmount
                    )
                )
            )
        )

        orderSummaryPageViewModel.orderTotal.value = OrderTotal(
            buttonState = OccButtonState.NORMAL
        )
        orderSummaryPageViewModel.orderCart = OrderCart(
            products = mutableListOf(
                orderProduct
            )
        )
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(
            shippingPrice = shippingPrice,
            shipperProductId = shippingProductId,
            serviceName = shippingServiceName
        )
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
            creditCard = OrderPaymentCreditCard(
                isAfpb = true,
                additionalData = additionalData
            )
        )
        orderSummaryPageViewModel.orderProducts.value = listOf(
            orderProduct
        )
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(
            state = OccButtonState.NORMAL
        )

        every {
            userSessionInterface.userId
        } returns userId.toString()
        every {
            creditCardTenorListUseCase.generateParam(any())
        } returns ccTenorListRequest
        coEvery {
            creditCardTenorListUseCase.executeSuspend(any())
        } returns creditCardTenorListData
        coEvery {
            updateCartOccUseCase.executeSuspend(any())
        } returns OccPrompt()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel()
        coEvery {
            checkoutOccUseCase.executeSuspend(any())
        } returns CheckoutOccData(
            status = STATUS_OK,
            result = CheckoutOccResult(
                success = checkoutOccResultSuccess,
                paymentParameter = CheckoutOccPaymentParameter(
                    redirectParam = CheckoutOccRedirectParam(
                        url = checkoutOccResultUrl,
                        form = checkoutOccResultForm
                    )
                )
            )
        )
    }
}
