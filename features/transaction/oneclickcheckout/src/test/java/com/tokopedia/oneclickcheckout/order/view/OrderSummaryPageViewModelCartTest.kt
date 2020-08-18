package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.PriceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.data.update.*
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelCartTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Atc Occ External Success`() {
        every { addToCartOccExternalUseCase.createObservable(any()) } returns Observable.just(AddToCartDataModel(data = DataModel(success = 1)))

        orderSummaryPageViewModel.atcOcc("1")

        assertEquals(OccGlobalEvent.AtcSuccess(), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Atc Occ External Error`() {
        val errorMessage = "error"
        every { addToCartOccExternalUseCase.createObservable(any()) } returns Observable.just(AddToCartDataModel(errorMessage = arrayListOf(errorMessage), data = DataModel(success = 0)))

        orderSummaryPageViewModel.atcOcc("1")

        assertEquals(OccGlobalEvent.AtcError(errorMessage = errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Atc Occ External Failed`() {
        val response = Throwable()
        every { addToCartOccExternalUseCase.createObservable(any()) } returns Observable.error(response)

        orderSummaryPageViewModel.atcOcc("1")

        assertEquals(OccGlobalEvent.AtcError(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Get Occ Cart Success With No Preference`() {
        val response = OrderData()
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }

        orderSummaryPageViewModel.getOccCart(true, "")

        assertEquals(OccState.FirstLoad(OrderPreference(profileIndex = "", profileRecommendation = "", isValid = true)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success Twice Should Trigger Analytics Once`() {
        val response = OrderData()
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }

        orderSummaryPageViewModel.getOccCart(true, "")
        orderSummaryPageViewModel.getOccCart(true, "")

        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
    }

    @Test
    fun `Get Occ Cart Failed`() {
        val response = Throwable()
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (secondArg() as ((Throwable) -> Unit)).invoke(response) }

        orderSummaryPageViewModel.getOccCart(true, "")

        assertEquals(OccState.Failed(Failure(response)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With Preference`() {
        val shipment = OrderProfileShipment(serviceId = 1)
        val profile = OrderProfile(shipment = shipment)
        val response = OrderData(cart = OrderCart(product = OrderProduct(productId = 1)), preference = profile)
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }

        orderSummaryPageViewModel.getOccCart(true, "")

        assertEquals(OccState.FirstLoad(OrderPreference(profileIndex = "", profileRecommendation = "", preference = profile, isValid = true)),
                orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With Preference And Rates`() {
        val shipment = OrderProfileShipment(serviceId = 1)
        val profile = OrderProfile(shipment = shipment, profileId = 1)
        val cart = OrderCart(product = OrderProduct(productId = 1, quantity = QuantityUiModel(orderQuantity = 1)))
        val response = OrderData(cart = cart, preference = profile)
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }

        val shippingRecommendationData = ShippingRecommendationData().apply {
            shippingDurationViewModels = listOf(
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
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())

        orderSummaryPageViewModel.getOccCart(true, "")

        assertEquals(OccState.FirstLoad(OrderPreference(profileIndex = "", profileRecommendation = "", preference = profile, isValid = true)),
                orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OrderShipment(serviceName = "kirimaja (2 hari)", serviceDuration = "kirimaja (2 hari)", serviceId = 1, shipperName = "kirimin",
                shipperId = 1, shipperProductId = 1, ratesId = "0", shippingPrice = 0, shippingRecommendationData = shippingRecommendationData,
                insuranceData = shippingRecommendationData.shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.insurance),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
        assertEquals(cart, orderSummaryPageViewModel.orderCart)
        assertEquals(1, orderSummaryPageViewModel.getCurrentShipperId())
        assertEquals(1, orderSummaryPageViewModel.getCurrentProfileId())
        verify(exactly = 1) { ratesUseCase.execute(any()) }
        verify(exactly = 1) { validateUsePromoRevampUseCase.createObservable(any()) }
    }

    @Test
    fun `Update Product Debounce Success`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)

        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        assertEquals(10, orderSummaryPageViewModel.orderProduct.quantity.orderQuantity)
        assertEquals(ButtonBayarState.LOADING, orderSummaryPageViewModel.orderTotal.value.buttonState)

        verify { ratesUseCase.execute(any()) }
        verify { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Update Product Debounce Should Not Reload Rates`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)

        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)), false)
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        verify(inverse = true) { ratesUseCase.execute(any()) }
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Update Product Debounce With Error Quantity Should Not Reload Rates`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)

        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10, isStateError = true)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        verify(inverse = true) { ratesUseCase.execute(any()) }
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Update Product Debounce Within Delay Time`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)

        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceTimeBy(500)
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 20)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        verify(exactly = 1) { ratesUseCase.execute(any()) }
        verify(exactly = 1) { updateCartOccUseCase.execute(match { it.cart.first().quantity == 20 }, any(), any()) }
    }

    @Test
    fun `Consume Force Show Onboarding`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(onboarding = OccMainOnboarding(isForceShowCoachMark = true))

        orderSummaryPageViewModel.consumeForceShowOnboarding()

        assertEquals(OccState.Success(OrderPreference(onboarding = OccMainOnboarding(isForceShowCoachMark = false))), orderSummaryPageViewModel.orderPreference.value)
    }

    @Test
    fun `Consume Force Show Onboarding On Invalid State`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(onboarding = OccMainOnboarding(isForceShowCoachMark = false))

        orderSummaryPageViewModel.consumeForceShowOnboarding()

        assertEquals(OccState.Loading, orderSummaryPageViewModel.orderPreference.value)
    }

    @Test
    fun `Update Cart Params`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.updateCart()

        verify { updateCartOccUseCase.execute(withArg { assertEquals(UpdateCartOccRequest(arrayListOf(UpdateCartOccCartRequest(cartId = "0", quantity = 1, productId = "1", spId = 1, shippingId = 1)), UpdateCartOccProfileRequest(profileId = "1", serviceId = 1, addressId = "0")), it) }, any(), any()) }
    }

    @Test
    fun `Update Cart Invalid Preference State`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        orderSummaryPageViewModel.updateCart()

        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Update Cart Invalid Profile Id`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = OrderProfile(), isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        orderSummaryPageViewModel.updateCart()

        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Update Preference Success Should Trigger Refresh`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        assertEquals(OccGlobalEvent.TriggerRefresh(true), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Preference Failed`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        assertEquals(OccGlobalEvent.Error(errorMessage = responseMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Preference Error`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Preference On Invalid Preference State`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Installment Success`() {
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {"installment_term": "1"}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.chooseInstallment(term2)

        assertEquals(term2.copy(isSelected = true), orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
        assertEquals(listOf(term1.copy(isSelected = false), term2.copy(isSelected = true)), orderSummaryPageViewModel.orderPayment.value.creditCard.availableTerms)
    }

    @Test
    fun `Choose Installment Failed`() {
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {"installment_term": "1"}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.chooseInstallment(term2)

        assertEquals(OccGlobalEvent.Error(errorMessage = responseMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Installment Error`() {
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {"installment_term": "1"}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.chooseInstallment(term2)

        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Installment Using Invalid Metadata`() {
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))

        orderSummaryPageViewModel.chooseInstallment(term2)

        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Success`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val metadata = "metadata"
        every { updateCartOccUseCase.execute(match { it.profile.metadata == metadata }, any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.updateCreditCard(metadata)

        assertEquals(OccGlobalEvent.TriggerRefresh(), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Failed`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val metadata = "metadata"
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        every { updateCartOccUseCase.execute(match { it.profile.metadata == metadata }, any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.updateCreditCard(metadata)

        assertEquals(OccGlobalEvent.Error(errorMessage = responseMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Error`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val metadata = "metadata"
        val response = Exception()
        every { updateCartOccUseCase.execute(match { it.profile.metadata == metadata }, any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.updateCreditCard(metadata)

        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card On Invalid Preference State`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)

        orderSummaryPageViewModel.updateCreditCard("")

        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }
}