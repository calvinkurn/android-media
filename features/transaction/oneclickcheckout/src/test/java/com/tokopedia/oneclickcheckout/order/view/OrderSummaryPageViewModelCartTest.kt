package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiData
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PriceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.order.data.creditcard.CartDetailsItem
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListRequest
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentOption
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccCartRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccProfileRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt.Companion.TYPE_DIALOG
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable
import java.io.IOException

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelCartTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Atc Occ External Success`() {
        // Given
        coEvery { addToCartOccMultiExternalUseCase.get().setParams(any(), any()).executeOnBackground() } returns AddToCartOccMultiDataModel(status = "OK", data = AddToCartOccMultiData(success = 1))

        // When
        orderSummaryPageViewModel.atcOcc("1")

        // Then
        assertEquals(OccGlobalEvent.AtcSuccess(), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Atc Occ External Error`() {
        // Given
        every { userSessionInterface.userId } returns "123"
        val errorMessage = "error"
        coEvery { addToCartOccMultiExternalUseCase.get().setParams(any(), any()).executeOnBackground() } returns AddToCartOccMultiDataModel(errorMessage = arrayListOf(errorMessage), data = AddToCartOccMultiData(success = 0))

        // When
        orderSummaryPageViewModel.atcOcc("1")

        // Then
        assertEquals(OccGlobalEvent.AtcError(errorMessage = errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Atc Occ External Failed`() {
        // Given
        every { userSessionInterface.userId } returns "123"
        val response = Throwable()
        coEvery { addToCartOccMultiExternalUseCase.get().setParams(any(), any()).executeOnBackground() } throws response

        // When
        orderSummaryPageViewModel.atcOcc("1")

        // Then
        assertEquals(OccGlobalEvent.AtcError(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Get Occ Cart Success With No Address`() {
        // Given
        val response = OrderData(errorCode = AddressState.ERROR_CODE_OPEN_ANA)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(hasValidProfile = false)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any(), any(), any())
        }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success Twice Should Trigger Analytics Once`() {
        // Given
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns helper.orderData
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.getOccCart("")
        orderSummaryPageViewModel.getOccCart("")

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any(), any(), any())
        }
    }

    @Test
    fun `Get Occ Cart Success With PPP Twice Should Trigger PPP Analytics Once`() {
        // Given
        val response = helper.orderData.copy(
                cart = helper.orderData.cart.copy(
                        products = mutableListOf(helper.product.copy(
                                purchaseProtectionPlanData = PurchaseProtectionPlanData(isProtectionAvailable = true)
                        ))
                )
        )
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")
        orderSummaryPageViewModel.getOccCart("")

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any(), any(), any())
            orderSummaryAnalytics.eventPPImpressionOnInsuranceSection(any(), any(), any(), any())
        }
    }

    @Test
    fun `Get Occ Cart Success Should Trigger Payment Tracker`() {
        // Given
        val response = helper.orderData
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPaymentMethod(any())
        }
    }

    @Test
    fun `Get Occ Cart Success With CC Should Trigger Payment Tracker And Tenure Tracker`() {
        // Given
        val selectedTenure = 1
        val response = helper.orderData.copy(
                payment = OrderPayment(
                        creditCard = OrderPaymentCreditCard(
                                selectedTerm = OrderPaymentInstallmentTerm(
                                        term = selectedTenure
                                )
                        )
                )
        )
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPaymentMethod(any())
            orderSummaryAnalytics.eventViewTenureOption(selectedTenure.toString())
        }
    }

    @Test
    fun `Get Occ Cart Failed`() {
        // Given
        val response = Throwable()
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } throws response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccState.Failed(Failure(response)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any(), any(), any())
        }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart With Invalid Address State`() {
        // Given
        val shipment = OrderProfileShipment(serviceId = 1)
        val address = OrderProfileAddress(addressId = 0)
        val profile = OrderProfile(shipment = shipment, address = address)
        val response = OrderData(cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1))), preference = profile, errorCode = "")
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccState.Failed(Failure(null)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any(), any(), any())
        }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With Preference`() {
        // Given
        val shipment = OrderProfileShipment(serviceId = 1)
        val address = OrderProfileAddress(addressId = 1)
        val payment = OrderProfilePayment(gatewayCode = "payment")
        val profile = OrderProfile(shipment = shipment, address = address, payment = payment)
        val response = OrderData(cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1))), preference = profile)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(hasValidProfile = true)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(profile, orderSummaryPageViewModel.orderProfile.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With Prompt`() {
        // Given
        val address = OrderProfileAddress(addressId = 1)
        val shipment = OrderProfileShipment(serviceId = 1)
        val payment = OrderProfilePayment(gatewayCode = "payment")
        val profile = OrderProfile(shipment = shipment, address = address, payment = payment)
        val prompt = OccPrompt(TYPE_DIALOG)
        val response = OrderData(cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1))), preference = profile, prompt = prompt)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(hasValidProfile = true)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(profile, orderSummaryPageViewModel.orderProfile.value)
        assertEquals(OccGlobalEvent.Prompt(prompt), orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With OccUIMessage`() {
        // Given
        val address = OrderProfileAddress(addressId = 1)
        val shipment = OrderProfileShipment(serviceId = 1)
        val payment = OrderProfilePayment(gatewayCode = "payment")
        val profile = OrderProfile(shipment = shipment, address = address, payment = payment)
        val response = OrderData(cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1))), preference = profile)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        val uiMessage = OccToasterAction("message")

        // When
        orderSummaryPageViewModel.getOccCart("", uiMessage = uiMessage)

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(hasValidProfile = true)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(profile, orderSummaryPageViewModel.orderProfile.value)
        assertEquals(OccGlobalEvent.ToasterAction(uiMessage), orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With PopUpMessage`() {
        // Given
        val address = OrderProfileAddress(addressId = 1)
        val shipment = OrderProfileShipment(serviceId = 1)
        val payment = OrderProfilePayment(gatewayCode = "payment")
        val profile = OrderProfile(shipment = shipment, address = address, payment = payment)
        val popUpMessage = "popUpMessage"
        val response = OrderData(cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1))), preference = profile, popUpMessage = popUpMessage)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(hasValidProfile = true)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(profile, orderSummaryPageViewModel.orderProfile.value)
        assertEquals(OccGlobalEvent.ToasterInfo(popUpMessage), orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With Preference And Rates`() {
        // Given
        val shipment = OrderProfileShipment(serviceId = 1)
        val address = OrderProfileAddress(addressId = 1)
        val payment = OrderProfilePayment(gatewayCode = "payment")
        val profile = OrderProfile(shipment = shipment, address = address, payment = payment)
        val cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1, orderQuantity = 1)))
        val promo = OrderPromo(LastApplyUiModel(listOf("promo")))
        val response = OrderData(cart = cart, preference = profile, promo = promo)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        val shippingRecommendationData = ShippingRecommendationData().apply {
            shippingDurationUiModels = listOf(
                    ShippingDurationUiModel().apply {
                        serviceData = ServiceData().apply {
                            serviceId = 1
                            serviceName = "kirimaja (2 hari)"
                        }
                        shippingCourierViewModelList = listOf(
                                ShippingCourierUiModel().apply {
                                    productData = ProductData().apply {
                                        shipperName = "kirimin"
                                        shipperProductId = 1
                                        shipperId = 1
                                        insurance = InsuranceData()
                                        price = PriceData()
                                    }
                                    ratesId = "0"
                                }
                        )
                    }
            )
        }
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns ValidateUsePromoRevampUiModel()

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(hasValidProfile = true)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(profile, orderSummaryPageViewModel.orderProfile.value)
        assertEquals(OrderShipment(serviceName = "kirimaja (2 hari)", serviceDuration = "kirimaja (2 hari)", serviceId = 1, shipperName = "kirimin",
                shipperId = 1, shipperProductId = 1, ratesId = "0", shippingPrice = 0, shippingRecommendationData = shippingRecommendationData,
                insurance = OrderInsurance(shippingRecommendationData.shippingDurationUiModels[0].shippingCourierViewModelList[0].productData.insurance)),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any(), any(), any())
        }
        assertEquals(cart, orderSummaryPageViewModel.orderCart)
        assertEquals(1, orderSummaryPageViewModel.orderShipment.value.getRealShipperId())
        verify(exactly = 1) { ratesUseCase.execute(any()) }
        coVerify(exactly = 1) { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() }
    }

    @Test
    fun `Get Occ Cart Success Trigger Payment Tracker`() {
        // Given
        val shipment = OrderProfileShipment(serviceId = 1)
        val address = OrderProfileAddress(addressId = 1)
        val payment = OrderProfilePayment(gatewayCode = "payment")
        val profile = OrderProfile(shipment = shipment, address = address, payment = payment)
        val cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1, orderQuantity = 1)))
        val promo = OrderPromo(LastApplyUiModel(listOf("promo")))
        val response = OrderData(cart = cart, preference = profile, promo = promo)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        val shippingRecommendationData = ShippingRecommendationData().apply {
            shippingDurationUiModels = listOf(
                    ShippingDurationUiModel().apply {
                        serviceData = ServiceData().apply {
                            serviceId = 1
                            serviceName = "kirimaja (2 hari)"
                        }
                        shippingCourierViewModelList = listOf(
                                ShippingCourierUiModel().apply {
                                    productData = ProductData().apply {
                                        shipperName = "kirimin"
                                        shipperProductId = 1
                                        shipperId = 1
                                        insurance = InsuranceData()
                                        price = PriceData()
                                    }
                                    ratesId = "0"
                                }
                        )
                    }
            )
        }
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns ValidateUsePromoRevampUiModel()

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(hasValidProfile = true)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(profile, orderSummaryPageViewModel.orderProfile.value)
        assertEquals(OrderShipment(serviceName = "kirimaja (2 hari)", serviceDuration = "kirimaja (2 hari)", serviceId = 1, shipperName = "kirimin",
                shipperId = 1, shipperProductId = 1, ratesId = "0", shippingPrice = 0, shippingRecommendationData = shippingRecommendationData,
                insurance = OrderInsurance(shippingRecommendationData.shippingDurationUiModels[0].shippingCourierViewModelList[0].productData.insurance)),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any(), any(), any())
        }
        assertEquals(cart, orderSummaryPageViewModel.orderCart)
        assertEquals(1, orderSummaryPageViewModel.orderShipment.value.getRealShipperId())
        verify(exactly = 1) { ratesUseCase.execute(any()) }
        coVerify(exactly = 1) { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() }
    }

    @Test
    fun `Update Product Debounce Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(helper.product))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProducts.value = orderSummaryPageViewModel.orderCart.products

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        assertEquals(OccButtonState.LOADING, orderSummaryPageViewModel.orderTotal.value.buttonState)
        testDispatchers.main.advanceUntilIdle()

        // Then
        assertEquals(10, orderSummaryPageViewModel.orderProducts.value.first().orderQuantity)
        verify { ratesUseCase.execute(any()) }
        coVerify { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `Update Product Debounce Should Not Reload Rates`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = helper.orderData.cart

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0, false)
        testDispatchers.main.advanceUntilIdle()

        // Then
        verify(inverse = true) { ratesUseCase.execute(any()) }
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `Update Product Debounce Within Delay Time`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = helper.orderData.cart

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceTimeBy(500)
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 20), 0)
        testDispatchers.main.advanceUntilIdle()

        // Then
        verify(exactly = 1) { ratesUseCase.execute(any()) }
        coVerify { updateCartOccUseCase.executeSuspend(match { it.cart.first().quantity == 20 }) }
    }

    @Test
    fun `Consume Force Show Onboarding`() {
        // Given
        orderSummaryPageViewModel.orderPreferenceData = OrderPreference(onboarding = OccOnboarding(isForceShowCoachMark = true))

        // When
        orderSummaryPageViewModel.consumeForceShowOnboarding()

        // Then
        assertEquals(OrderPreference(onboarding = OccOnboarding(isForceShowCoachMark = false)), orderSummaryPageViewModel.orderPreferenceData)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Consume Force Show Onboarding On Invalid State`() {
        // Given
        orderSummaryPageViewModel.orderPreferenceData = OrderPreference(onboarding = OccOnboarding(isForceShowCoachMark = false))

        // When
        orderSummaryPageViewModel.consumeForceShowOnboarding()

        // Then
        assertEquals(OccState.Loading, orderSummaryPageViewModel.orderPreference.value)
    }

    @Test
    fun `Update Cart Params`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.updateCart()

        // Then
        coVerify {
            updateCartOccUseCase.executeSuspend(withArg {
                assertEquals(UpdateCartOccRequest(arrayListOf(
                        UpdateCartOccCartRequest(cartId = "", quantity = 1, productId = helper.product.productId.toString())),
                        UpdateCartOccProfileRequest(serviceId = 1, addressId = "1", gatewayCode = "payment", spId = 1, shippingId = 1), source = UpdateCartOccRequest.SOURCE_UPDATE_QTY_NOTES),
                        it)
            })
        }
    }

    @Test
    fun `Update Cart Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(payment = OrderProfilePayment())
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        orderSummaryPageViewModel.updateCart()

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `Update Cart Invalid Address Id`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(address = OrderProfileAddress())
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        orderSummaryPageViewModel.updateCart()

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `Update Cart Invalid Shipment`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(shipment = OrderProfileShipment())

        // When
        orderSummaryPageViewModel.updateCart()

        // Then
        coVerify {
            updateCartOccUseCase.executeSuspend(withArg {
                assertEquals(UpdateCartOccRequest(arrayListOf(UpdateCartOccCartRequest(cartId = "", quantity = 1, productId = helper.product.productId.toString())),
                        UpdateCartOccProfileRequest(serviceId = 0, addressId = "1", gatewayCode = "payment", spId = 0, shippingId = 0), skipShippingValidation = true, source = UpdateCartOccRequest.SOURCE_UPDATE_QTY_NOTES),
                        it)
            })
        }
    }

    @Test
    fun `Choose Installment Success`() {
        // Given
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {"installment_term": "1"}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = preference
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.chooseInstallment(term2, listOf(term1, term2))

        // Then
        assertEquals(term2.copy(isSelected = true), orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
        assertEquals(listOf(term1.copy(isSelected = false), term2.copy(isSelected = true)), orderSummaryPageViewModel.orderPayment.value.creditCard.availableTerms)
    }

    @Test
    fun `Choose Installment Got Prompt`() {
        // Given
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {"installment_term": "1"}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = preference
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        val occPrompt = OccPrompt()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns occPrompt

        // When
        orderSummaryPageViewModel.chooseInstallment(term2, listOf(term1, term2))

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Installment Failed`() {
        // Given
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {"installment_term": "1"}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = preference
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } throws response

        // When
        orderSummaryPageViewModel.chooseInstallment(term2, listOf(term1, term2))

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = responseMessage, shouldTriggerAnalytics = true), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Installment Error`() {
        // Given
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {"installment_term": "1"}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = preference
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        val response = Throwable()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } throws response

        // When
        orderSummaryPageViewModel.chooseInstallment(term2, listOf(term1, term2))

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Installment Using Invalid Metadata From Payment`() {
        // Given
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = preference
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))

        // When
        orderSummaryPageViewModel.chooseInstallment(term2, listOf(term1, term2))

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Installment Using Invalid Metadata`() {
        // Given
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = preference
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2)))

        // When
        orderSummaryPageViewModel.chooseInstallment(term2, listOf(term1, term2))

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Installment Using Invalid Metadata Json`() {
        // Given
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {}
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = preference
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))

        // When
        orderSummaryPageViewModel.chooseInstallment(term2, listOf(term1, term2))

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Success`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val metadata = "metadata"
        val gatewayCode = "gatewayCode"
        coEvery { updateCartOccUseCase.executeSuspend(match { it.profile.metadata == metadata && it.profile.gatewayCode == gatewayCode }) } returns null

        // When
        orderSummaryPageViewModel.choosePayment(gatewayCode, metadata)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Failed`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val metadata = "metadata"
        val gatewayCode = "gatewayCode"
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        coEvery { updateCartOccUseCase.executeSuspend(match { it.profile.metadata == metadata && it.profile.gatewayCode == gatewayCode }) } throws response

        // When
        orderSummaryPageViewModel.choosePayment(gatewayCode, metadata)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = responseMessage, shouldTriggerAnalytics = true), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Error`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val metadata = "metadata"
        val gatewayCode = "gatewayCode"
        val response = Exception()
        coEvery { updateCartOccUseCase.executeSuspend(match { it.profile.metadata == metadata && it.profile.gatewayCode == gatewayCode }) } throws response

        // When
        orderSummaryPageViewModel.choosePayment(gatewayCode, metadata)

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card On Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(address = OrderProfileAddress())

        // When
        orderSummaryPageViewModel.choosePayment("", "")

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `Update Credit Card Got Prompt`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val metadata = "metadata"
        val gatewayCode = "gatewayCode"
        val occPrompt = OccPrompt()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns occPrompt

        // When
        orderSummaryPageViewModel.choosePayment(gatewayCode, metadata)

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Address Success`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val addressModel = RecipientAddressModel().apply {
            id = "address1"
            destinationDistrictId = "districtId1"
            postalCode = "postalcode1"
            latitude = "lat1"
            longitude = "lon1"
            addressName = "addressname1"
            recipientName = "recipientname1"
        }
        coEvery { chooseAddressRepository.get().setStateChosenAddressFromAddress(any()) } returns SetStateChosenAddressQqlResponse()
        coEvery { chooseAddressMapper.get().mapSetStateChosenAddress(any()) } returns ChosenAddressModel()
        coEvery { updateCartOccUseCase.executeSuspend(match { it.profile.addressId == addressModel.id }) } returns null

        // When
        orderSummaryPageViewModel.chooseAddress(addressModel)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Address Failed`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val addressModel = RecipientAddressModel().apply {
            id = "address1"
            destinationDistrictId = "districtId1"
            postalCode = "postalcode1"
            latitude = "lat1"
            longitude = "lon1"
        }
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        coEvery { chooseAddressRepository.get().setStateChosenAddressFromAddress(any()) } returns SetStateChosenAddressQqlResponse()
        coEvery { chooseAddressMapper.get().mapSetStateChosenAddress(any()) } returns ChosenAddressModel()
        coEvery { updateCartOccUseCase.executeSuspend(match { it.profile.addressId == addressModel.id }) } throws response

        // When
        orderSummaryPageViewModel.chooseAddress(addressModel)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = responseMessage, shouldTriggerAnalytics = true), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Address Error`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val addressModel = RecipientAddressModel().apply {
            id = "address1"
            destinationDistrictId = "districtId1"
            postalCode = "postalcode1"
            latitude = "lat1"
            longitude = "lon1"
        }
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val response = Exception()
        coEvery { chooseAddressRepository.get().setStateChosenAddressFromAddress(any()) } returns SetStateChosenAddressQqlResponse()
        coEvery { chooseAddressMapper.get().mapSetStateChosenAddress(any()) } returns ChosenAddressModel()
        coEvery { updateCartOccUseCase.executeSuspend(match { it.profile.addressId == addressModel.id }) } throws response

        // When
        orderSummaryPageViewModel.chooseAddress(addressModel)

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Address LCA Error`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val addressModel = RecipientAddressModel().apply {
            id = "address1"
            destinationDistrictId = "districtId1"
            postalCode = "postalcode1"
            latitude = "lat1"
            longitude = "lon1"
        }
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val response = Exception()
        coEvery { chooseAddressRepository.get().setStateChosenAddressFromAddress(any()) } throws response
        coEvery { chooseAddressMapper.get().mapSetStateChosenAddress(any()) } returns ChosenAddressModel()
        coEvery { updateCartOccUseCase.executeSuspend(match { it.profile.addressId == addressModel.id }) } throws response

        // When
        orderSummaryPageViewModel.chooseAddress(addressModel)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Address On Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(address = OrderProfileAddress())

        // When
        orderSummaryPageViewModel.chooseAddress(RecipientAddressModel())

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Address Got Prompt`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val addressModel = RecipientAddressModel().apply {
            id = "address1"
            destinationDistrictId = "districtId1"
            postalCode = "postalcode1"
            latitude = "lat1"
            longitude = "lon1"
        }
        coEvery { chooseAddressRepository.get().setStateChosenAddressFromAddress(any()) } returns SetStateChosenAddressQqlResponse()
        coEvery { chooseAddressMapper.get().mapSetStateChosenAddress(any()) } returns ChosenAddressModel()
        val occPrompt = OccPrompt()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns occPrompt

        // When
        orderSummaryPageViewModel.chooseAddress(addressModel)

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Update Got Prompt`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
        val occPrompt = OccPrompt()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns occPrompt

        // When
        var isSuccess = false
        orderSummaryPageViewModel.finalUpdate({
            isSuccess = true
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
        assertEquals(false, isSuccess)
    }

    @Test
    fun `Force Show Onboarding Revamp With Profile And Failed Get Rates`() {
        // Given
        val onboarding = OccOnboarding(isForceShowCoachMark = true)
        val shipment = OrderProfileShipment(serviceId = 1)
        val profile = OrderProfile(shipment = shipment)
        val response = OrderData(cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1))), preference = profile, onboarding = onboarding)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        every { ratesUseCase.execute(any()) } throws Throwable()

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Force Show Onboarding Revamp With Profile`() {
        // Given
        val onboarding = OccOnboarding(isForceShowCoachMark = true)
        val shipment = OrderProfileShipment(serviceId = 1)
        val address = OrderProfileAddress(addressId = 1)
        val payment = OrderProfilePayment(gatewayCode = "payment")
        val profile = OrderProfile(shipment = shipment, address = address, payment = payment)
        val response = OrderData(cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1))), preference = profile, onboarding = onboarding)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        val shippingRecommendationData = ShippingRecommendationData().apply {
            shippingDurationUiModels = listOf(
                    ShippingDurationUiModel().apply {
                        serviceData = ServiceData().apply {
                            serviceId = 1
                            serviceName = "kirimaja (2 hari)"
                        }
                        shippingCourierViewModelList = listOf(
                                ShippingCourierUiModel().apply {
                                    productData = ProductData().apply {
                                        shipperName = "kirimin"
                                        shipperProductId = 1
                                        shipperId = 1
                                        insurance = InsuranceData()
                                        price = PriceData()
                                    }
                                    ratesId = "0"
                                }
                        )
                    }
            )
        }
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccGlobalEvent.ForceOnboarding(onboarding), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Force Show Onboarding Revamp With Profile And Prompt`() {
        // Given
        val onboarding = OccOnboarding(isForceShowCoachMark = true)
        val shipment = OrderProfileShipment(serviceId = 1)
        val address = OrderProfileAddress(addressId = 1)
        val profile = OrderProfile(shipment = shipment, address = address)
        val prompt = OccPrompt(type = TYPE_DIALOG, "Prompt")
        val response = OrderData(cart = OrderCart(products = mutableListOf(OrderProduct(productId = 1))), preference = profile,
                onboarding = onboarding, prompt = prompt)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        val shippingRecommendationData = ShippingRecommendationData().apply {
            shippingDurationUiModels = listOf(
                    ShippingDurationUiModel().apply {
                        serviceData = ServiceData().apply {
                            serviceId = 1
                            serviceName = "kirimaja (2 hari)"
                        }
                        shippingCourierViewModelList = listOf(
                                ShippingCourierUiModel().apply {
                                    productData = ProductData().apply {
                                        shipperName = "kirimin"
                                        shipperProductId = 1
                                        shipperId = 1
                                        insurance = InsuranceData()
                                        price = PriceData()
                                    }
                                    ratesId = "0"
                                }
                        )
                    }
            )
        }
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(OccGlobalEvent.Prompt(prompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Get Shop Id`() {
        // Given
        val shopId = 123L
        val response = helper.orderData.copy(cart = helper.orderData.cart.copy(shop = helper.orderData.cart.shop.copy(shopId = shopId)))
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(shopId.toString(), orderSummaryPageViewModel.getShopId())
    }

    @Test
    fun `Get Wallet Activation Data`() {
        // Given
        val activationData = OrderPaymentWalletActionData(isRequired = true)
        val response = helper.orderData.copy(payment = OrderPayment(walletData = OrderPaymentWalletAdditionalData(
                activation = activationData
        )))
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(activationData, orderSummaryPageViewModel.getActivationData())
    }

    @Test
    fun `Afpb get tenor list fee success`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 10000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 1000, shipperProductId = 1, serviceName = "service")
        val additionalData = OrderPaymentCreditCardAdditionalData(profileCode = "TKPD_DEFAULT", totalProductPrice = "52000")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(isAfpb = true, additionalData = additionalData, selectedTerm = OrderPaymentInstallmentTerm()))

        val creditCardTenorListData = CreditCardTenorListData(tenorList = listOf(
                TenorListData(type = "FULL", amount = 12500.0, bank = "014", fee = 1500.0, rate = 0.0)))
        val ccTenorListRequest = mapOf("INPUT" to CreditCardTenorListRequest("", 123,
                52000.0, 0.0, "TKPD_DEFAULT", 0.0, listOf(CartDetailsItem(1, 11000.0))))

        every { userSessionInterface.userId } returns "123"
        every { creditCardTenorListUseCase.generateParam(any()) } returns ccTenorListRequest
        coEvery { creditCardTenorListUseCase.executeSuspend(any()) } returns creditCardTenorListData

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(1500.0, orderSummaryPageViewModel.orderTotal.value.orderCost.paymentFee, 0.0)
        assertEquals(12500.0, orderSummaryPageViewModel.orderTotal.value.orderCost.totalPrice, 0.0)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
        assertEquals(OrderPaymentInstallmentTerm(
                term = 0,
                isSelected = true,
                isEnable = true,
                isError = false,
                fee = 1500.0,
                monthlyAmount = 12500.0
        ), orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Afpb get tenor list fee error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 10000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 1000, shipperProductId = 1, serviceName = "service")
        val additionalData = OrderPaymentCreditCardAdditionalData(profileCode = "TKPD_DEFAULT", totalProductPrice = "52000")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(isAfpb = true, additionalData = additionalData, selectedTerm = OrderPaymentInstallmentTerm()))

        val creditCardTenorListData = CreditCardTenorListData(errorMsg = "Invalid Siggy(00)")
        val ccTenorListRequest = mapOf("INPUT" to CreditCardTenorListRequest("", 123,
                52000.0, 0.0, "TKPD_DEFAULT", 0.0, listOf(CartDetailsItem(1, 11000.0))))

        every { userSessionInterface.userId } returns "123"
        every { creditCardTenorListUseCase.generateParam(any()) } returns ccTenorListRequest
        coEvery { creditCardTenorListUseCase.executeSuspend(any()) } returns creditCardTenorListData

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(0.0, orderSummaryPageViewModel.orderTotal.value.orderCost.paymentFee, 0.0)
        assertEquals(11000.0, orderSummaryPageViewModel.orderTotal.value.orderCost.totalPrice, 0.0)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        assertEquals(null, orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
        assertEquals(OccGlobalEvent.AdjustAdminFeeError, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Afpb get tenor list fee failed`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 10000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 1000, shipperProductId = 1, serviceName = "service")
        val additionalData = OrderPaymentCreditCardAdditionalData(profileCode = "TKPD_DEFAULT", totalProductPrice = "52000")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(isAfpb = true, additionalData = additionalData, selectedTerm = OrderPaymentInstallmentTerm()))

        every { userSessionInterface.userId } returns "123"
        coEvery { creditCardTenorListUseCase.executeSuspend(any()) } throws Exception()

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(0.0, orderSummaryPageViewModel.orderTotal.value.orderCost.paymentFee, 0.0)
        assertEquals(11000.0, orderSummaryPageViewModel.orderTotal.value.orderCost.totalPrice, 0.0)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        assertEquals(null, orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
        assertEquals(OccGlobalEvent.AdjustAdminFeeError, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Afpb get tenor list fee success without selected term`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 10000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 1000, shipperProductId = 1, serviceName = "service")
        val additionalData = OrderPaymentCreditCardAdditionalData(profileCode = "TKPD_DEFAULT", totalProductPrice = "52000")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(isAfpb = true, additionalData = additionalData))

        val creditCardTenorListData = CreditCardTenorListData(tenorList = listOf(
                TenorListData(type = "FULL", amount = 12500.0, bank = "014", fee = 1500.0, rate = 0.0)))
        val ccTenorListRequest = mapOf("INPUT" to CreditCardTenorListRequest("", 123,
                52000.0, 0.0, "TKPD_DEFAULT", 0.0, listOf(CartDetailsItem(1, 11000.0))))

        every { userSessionInterface.userId } returns "123"
        every { creditCardTenorListUseCase.generateParam(any()) } returns ccTenorListRequest
        coEvery { creditCardTenorListUseCase.executeSuspend(any()) } returns creditCardTenorListData

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(0.0, orderSummaryPageViewModel.orderTotal.value.orderCost.paymentFee, 0.0)
        assertEquals(11000.0, orderSummaryPageViewModel.orderTotal.value.orderCost.totalPrice, 0.0)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        assertEquals(null, orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `GoCicil Installment Options Success With Matching Selected Tenure`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 2)))

        val option1 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 2
        )
        val option2 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 3
        )
        val option3 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 4
        )
        coEvery { goCicilInstallmentOptionUseCase.executeSuspend(any()) } returns listOf(option1, option2, option3)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0, installmentData = OrderCostInstallmentData(installmentTerm = option1.installmentTerm),
                totalItemPriceAndShippingFee = 1500.0, totalPriceWithoutDiscountsAndPaymentFees = 1500.0, totalPriceWithoutPaymentFees = 1500.0),
                OccButtonState.NORMAL, OccButtonType.PAY), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentGoCicilData(selectedTenure = 2, availableTerms = listOf(OrderPaymentGoCicilTerms(installmentTerm = 2, isActive = true), OrderPaymentGoCicilTerms(installmentTerm = 3, isActive = true), OrderPaymentGoCicilTerms(installmentTerm = 4, isActive = true)),
                selectedTerm = OrderPaymentGoCicilTerms(installmentTerm = option1.installmentTerm, isActive = true)),
                orderSummaryPageViewModel.orderPayment.value.walletData.goCicilData)
        coVerify { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `GoCicil Installment Options Success With Matching Selected Term`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 2, selectedTerm = OrderPaymentGoCicilTerms(installmentTerm = 3))))

        val option1 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 2
        )
        val option2 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 3
        )
        val option3 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 4
        )
        coEvery { goCicilInstallmentOptionUseCase.executeSuspend(any()) } returns listOf(option1, option2, option3)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0, installmentData = OrderCostInstallmentData(installmentTerm = option2.installmentTerm),
                totalItemPriceAndShippingFee = 1500.0, totalPriceWithoutDiscountsAndPaymentFees = 1500.0, totalPriceWithoutPaymentFees = 1500.0),
                OccButtonState.NORMAL, OccButtonType.PAY), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentGoCicilData(selectedTenure = 2, availableTerms = listOf(OrderPaymentGoCicilTerms(installmentTerm = 2, isActive = true), OrderPaymentGoCicilTerms(installmentTerm = 3, isActive = true), OrderPaymentGoCicilTerms(installmentTerm = 4, isActive = true)),
                selectedTerm = OrderPaymentGoCicilTerms(installmentTerm = option2.installmentTerm, isActive = true)),
                orderSummaryPageViewModel.orderPayment.value.walletData.goCicilData)
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `GoCicil Installment Options Success With Matching Recommendation`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 0)))

        val option1 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 2,
                isRecommended = true,
        )
        val option2 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 3,
                isRecommended = true,
        )
        val option3 = GoCicilInstallmentOption(
                isActive = false,
                installmentTerm = 4,
                isRecommended = true,
        )
        coEvery { goCicilInstallmentOptionUseCase.executeSuspend(any()) } returns listOf(option1, option2, option3)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0, installmentData = OrderCostInstallmentData(installmentTerm = option2.installmentTerm),
                totalItemPriceAndShippingFee = 1500.0, totalPriceWithoutDiscountsAndPaymentFees = 1500.0, totalPriceWithoutPaymentFees = 1500.0),
                OccButtonState.NORMAL, OccButtonType.PAY), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentGoCicilData(selectedTenure = 0, availableTerms = listOf(OrderPaymentGoCicilTerms(installmentTerm = 2, isActive = true, isRecommended = true), OrderPaymentGoCicilTerms(installmentTerm = 3, isActive = true, isRecommended = true), OrderPaymentGoCicilTerms(installmentTerm = 4, isActive = false, isRecommended = true)),
                selectedTerm = OrderPaymentGoCicilTerms(installmentTerm = option2.installmentTerm, isActive = true, isRecommended = true)),
                orderSummaryPageViewModel.orderPayment.value.walletData.goCicilData)
        coVerify { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `GoCicil Installment Options Success With Fallback Matching Active State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 0)))

        val option1 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 2,
                isRecommended = false,
        )
        val option2 = GoCicilInstallmentOption(
                isActive = true,
                installmentTerm = 3,
                isRecommended = false,
        )
        val option3 = GoCicilInstallmentOption(
                isActive = false,
                installmentTerm = 4,
                isRecommended = false,
        )
        coEvery { goCicilInstallmentOptionUseCase.executeSuspend(any()) } returns listOf(option1, option2, option3)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0, installmentData = OrderCostInstallmentData(installmentTerm = option2.installmentTerm),
                totalItemPriceAndShippingFee = 1500.0, totalPriceWithoutDiscountsAndPaymentFees = 1500.0, totalPriceWithoutPaymentFees = 1500.0),
                OccButtonState.NORMAL, OccButtonType.PAY), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentGoCicilData(selectedTenure = 0, availableTerms = listOf(OrderPaymentGoCicilTerms(installmentTerm = 2, isActive = true, isRecommended = false), OrderPaymentGoCicilTerms(installmentTerm = 3, isActive = true, isRecommended = false), OrderPaymentGoCicilTerms(installmentTerm = 4, isActive = false, isRecommended = false)),
                selectedTerm = OrderPaymentGoCicilTerms(installmentTerm = option2.installmentTerm, isActive = true, isRecommended = false)),
                orderSummaryPageViewModel.orderPayment.value.walletData.goCicilData)
        coVerify { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `GoCicil Installment Options Success With Fallback`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 0)))

        val option1 = GoCicilInstallmentOption(
                isActive = false,
                installmentTerm = 2,
                isRecommended = false,
        )
        val option2 = GoCicilInstallmentOption(
                isActive = false,
                installmentTerm = 3,
                isRecommended = false,
        )
        val option3 = GoCicilInstallmentOption(
                isActive = false,
                installmentTerm = 4,
                isRecommended = false,
        )
        coEvery { goCicilInstallmentOptionUseCase.executeSuspend(any()) } returns listOf(option1, option2, option3)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0,
                totalItemPriceAndShippingFee = 1500.0, totalPriceWithoutDiscountsAndPaymentFees = 1500.0, totalPriceWithoutPaymentFees = 1500.0),
                OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentGoCicilData(selectedTenure = 0, availableTerms = listOf(OrderPaymentGoCicilTerms(installmentTerm = 2, isActive = false, isRecommended = false), OrderPaymentGoCicilTerms(installmentTerm = 3, isActive = false, isRecommended = false), OrderPaymentGoCicilTerms(installmentTerm = 4, isActive = false, isRecommended = false)),
                selectedTerm = OrderPaymentGoCicilTerms(installmentTerm = option3.installmentTerm, isActive = false, isRecommended = false)),
                orderSummaryPageViewModel.orderPayment.value.walletData.goCicilData)
        assertEquals(OrderPaymentErrorData(), orderSummaryPageViewModel.orderPayment.value.errorData)
        coVerify { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `GoCicil Installment Options Failed`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 2, selectedTerm = OrderPaymentGoCicilTerms(isActive = true), availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = true)))))

        coEvery { goCicilInstallmentOptionUseCase.executeSuspend(any()) } throws IOException()

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0,
                totalItemPriceAndShippingFee = 1500.0, totalPriceWithoutDiscountsAndPaymentFees = 1500.0, totalPriceWithoutPaymentFees = 1500.0),
                OccButtonState.DISABLE, OccButtonType.PAY), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentGoCicilData(selectedTenure = 2, availableTerms = emptyList(),
                selectedTerm = OrderPaymentGoCicilTerms(isActive = true)),
                orderSummaryPageViewModel.orderPayment.value.walletData.goCicilData)
        assertEquals(OccGlobalEvent.AdjustAdminFeeError, orderSummaryPageViewModel.globalEvent.value)
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `GoCicil Installment Options Below Minimum`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, minimumAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 2, selectedTerm = OrderPaymentGoCicilTerms(isActive = true), availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = true)))))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        coVerify(inverse = true) {
            goCicilInstallmentOptionUseCase.executeSuspend(any())
        }
    }

    @Test
    fun `GoCicil Installment Options Above Maximum`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1, walletAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 2, selectedTerm = OrderPaymentGoCicilTerms(isActive = true), availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = true)))))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        coVerify(inverse = true) {
            goCicilInstallmentOptionUseCase.executeSuspend(any())
        }
    }

    @Test
    fun `GoCicil Installment Options Above Wallet Amount`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletAmount = 1, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 2, selectedTerm = OrderPaymentGoCicilTerms(isActive = true), availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = true)))))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        coVerify(inverse = true) {
            goCicilInstallmentOptionUseCase.executeSuspend(any())
        }
    }

    @Test
    fun `Choose GoCicil Installment Options Silently`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 2, selectedTerm = OrderPaymentGoCicilTerms(isActive = true), availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = true)))))

        // When
        orderSummaryPageViewModel.chooseInstallment(OrderPaymentGoCicilTerms(), listOf(OrderPaymentGoCicilTerms()), true)

        // Then
        coVerify(inverse = true) {
            goCicilInstallmentOptionUseCase.executeSuspend(any())
        }
        coVerify(inverse = true) {
            updateCartOccUseCase.executeSuspend(any())
        }
    }

    @Test
    fun `Choose GoCicil Installment Options With Invalid Profile`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(address = OrderProfileAddress())
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 1000000, walletAmount = 1000000, walletData = OrderPaymentWalletAdditionalData(walletType = 4,
                goCicilData = OrderPaymentGoCicilData(selectedTenure = 2, selectedTerm = OrderPaymentGoCicilTerms(isActive = true), availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = true)))))

        // When
        orderSummaryPageViewModel.chooseInstallment(OrderPaymentGoCicilTerms(), listOf(OrderPaymentGoCicilTerms()), false)

        // Then
        coVerify(inverse = true) {
            goCicilInstallmentOptionUseCase.executeSuspend(any())
        }
        coVerify(inverse = true) {
            updateCartOccUseCase.executeSuspend(any())
        }
    }
}