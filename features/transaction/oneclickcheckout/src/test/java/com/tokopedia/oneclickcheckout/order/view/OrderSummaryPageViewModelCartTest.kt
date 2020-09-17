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
        // Given
        every { addToCartOccExternalUseCase.createObservable(any()) } returns Observable.just(AddToCartDataModel(data = DataModel(success = 1)))

        // When
        orderSummaryPageViewModel.atcOcc("1")

        // Then
        assertEquals(OccGlobalEvent.AtcSuccess(), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Atc Occ External Error`() {
        // Given
        val errorMessage = "error"
        every { addToCartOccExternalUseCase.createObservable(any()) } returns Observable.just(AddToCartDataModel(errorMessage = arrayListOf(errorMessage), data = DataModel(success = 0)))

        // When
        orderSummaryPageViewModel.atcOcc("1")

        // Then
        assertEquals(OccGlobalEvent.AtcError(errorMessage = errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Atc Occ External Failed`() {
        // Given
        val response = Throwable()
        every { addToCartOccExternalUseCase.createObservable(any()) } returns Observable.error(response)

        // When
        orderSummaryPageViewModel.atcOcc("1")

        // Then
        assertEquals(OccGlobalEvent.AtcError(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Get Occ Cart Success With No Preference`() {
        // Given
        val response = OrderData()
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }

        // When
        orderSummaryPageViewModel.getOccCart(true, "")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(profileIndex = "", profileRecommendation = "", isValid = true)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success Twice Should Trigger Analytics Once`() {
        // Given
        val response = OrderData()
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }

        // When
        orderSummaryPageViewModel.getOccCart(true, "")
        orderSummaryPageViewModel.getOccCart(true, "")

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
    }

    @Test
    fun `Get Occ Cart Failed`() {
        // Given
        val response = Throwable()
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (secondArg() as ((Throwable) -> Unit)).invoke(response) }

        // When
        orderSummaryPageViewModel.getOccCart(true, "")

        // Then
        assertEquals(OccState.Failed(Failure(response)), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With Preference`() {
        // Given
        val shipment = OrderProfileShipment(serviceId = 1)
        val profile = OrderProfile(shipment = shipment)
        val response = OrderData(cart = OrderCart(product = OrderProduct(productId = 1)), preference = profile)
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }

        // When
        orderSummaryPageViewModel.getOccCart(true, "")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(profileIndex = "", profileRecommendation = "", preference = profile, isValid = true)),
                orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With Prompt`() {
        // Given
        val shipment = OrderProfileShipment(serviceId = 1)
        val profile = OrderProfile(shipment = shipment)
        val prompt = OccPrompt(OccPrompt.FROM_CART, OccPrompt.TYPE_DIALOG)
        val response = OrderData(cart = OrderCart(product = OrderProduct(productId = 1)), preference = profile, prompt = prompt)
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }

        // When
        orderSummaryPageViewModel.getOccCart(true, "")

        // Then
        assertEquals(OccState.FirstLoad(OrderPreference(profileIndex = "", profileRecommendation = "", preference = profile, isValid = true)),
                orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Prompt(prompt), orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Occ Cart Success With Preference And Rates`() {
        // Given
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

        // When
        orderSummaryPageViewModel.getOccCart(true, "")

        // Then
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
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        // Then
        assertEquals(10, orderSummaryPageViewModel.orderProduct.quantity.orderQuantity)
        assertEquals(OccButtonState.LOADING, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify { ratesUseCase.execute(any()) }
        verify { updateCartOccUseCase.execute(any(), any(), any(), any()) }
    }

    @Test
    fun `Update Product Debounce Should Not Reload Rates`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)), false)
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        // Then
        verify(inverse = true) { ratesUseCase.execute(any()) }
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any(), any()) }
    }

    @Test
    fun `Update Product Debounce With Error Quantity Should Not Reload Rates`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10, isStateError = true)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        // Then
        verify(inverse = true) { ratesUseCase.execute(any()) }
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any(), any()) }
    }

    @Test
    fun `Update Product Debounce Within Delay Time`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceTimeBy(500)
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 20)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        // Then
        verify(exactly = 1) { ratesUseCase.execute(any()) }
        verify(exactly = 1) { updateCartOccUseCase.execute(match { it.cart.first().quantity == 20 }, any(), any(), any()) }
    }

    @Test
    fun `Consume Force Show Onboarding`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(onboarding = OccMainOnboarding(isForceShowCoachMark = true))

        // When
        orderSummaryPageViewModel.consumeForceShowOnboarding()

        // Then
        assertEquals(OccState.Success(OrderPreference(onboarding = OccMainOnboarding(isForceShowCoachMark = false))), orderSummaryPageViewModel.orderPreference.value)
    }

    @Test
    fun `Consume Force Show Onboarding On Invalid State`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(onboarding = OccMainOnboarding(isForceShowCoachMark = false))

        // When
        orderSummaryPageViewModel.consumeForceShowOnboarding()

        // Then
        assertEquals(OccState.Loading, orderSummaryPageViewModel.orderPreference.value)
    }

    @Test
    fun `Update Cart Params`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        // When
        orderSummaryPageViewModel.updateCart()

        // Then
        verify { updateCartOccUseCase.execute(withArg { assertEquals(UpdateCartOccRequest(arrayListOf(UpdateCartOccCartRequest(cartId = "0", quantity = 1, productId = "1", spId = 1, shippingId = 1)), UpdateCartOccProfileRequest(profileId = "1", serviceId = 1, addressId = "0")), it) }, any(), any(), any()) }
    }

    @Test
    fun `Update Cart Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        orderSummaryPageViewModel.updateCart()

        // Then
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any(), any()) }
    }

    @Test
    fun `Update Cart Invalid Profile Id`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = OrderProfile(), isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        orderSummaryPageViewModel.updateCart()

        // Then
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any(), any()) }
    }

    @Test
    fun `Update Preference Success Should Trigger Refresh`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        // When
        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(true), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Preference Got Prompt`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val occPrompt = OccPrompt()
        every { updateCartOccUseCase.execute(any(), any(), any(),any()) } answers {
            (thirdArg() as ((OccPrompt) -> Unit)).invoke(occPrompt)
        }

        // When
        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Preference Failed`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        every { updateCartOccUseCase.execute(any(), any(), any(),any()) } answers {
            (arg(3) as ((Throwable) -> Unit)).invoke(response)
        }

        // When
        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = responseMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Preference Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (arg(3) as ((Throwable) -> Unit)).invoke(response)
        }

        // When
        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Preference On Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (arg(3) as ((Throwable) -> Unit)).invoke(response)
        }

        // When
        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
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
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        // When
        orderSummaryPageViewModel.chooseInstallment(term2)

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
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        val occPrompt = OccPrompt()
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (thirdArg() as ((OccPrompt) -> Unit)).invoke(occPrompt)
        }

        // When
        orderSummaryPageViewModel.chooseInstallment(term2)

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
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (arg(3) as ((Throwable) -> Unit)).invoke(response)
        }

        // When
        orderSummaryPageViewModel.chooseInstallment(term2)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = responseMessage), orderSummaryPageViewModel.globalEvent.value)
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
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (arg(3) as ((Throwable) -> Unit)).invoke(response)
        }

        // When
        orderSummaryPageViewModel.chooseInstallment(term2)

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
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
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        val term1 = OrderPaymentInstallmentTerm(term = 1, isEnable = true, isSelected = true)
        val term2 = OrderPaymentInstallmentTerm(term = 2, isEnable = true, isSelected = false)
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(availableTerms = listOf(term1, term2), selectedTerm = term1))

        // When
        orderSummaryPageViewModel.chooseInstallment(term2)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Success`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val metadata = "metadata"
        every { updateCartOccUseCase.execute(match { it.profile.metadata == metadata }, any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        // When
        orderSummaryPageViewModel.updateCreditCard(metadata)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Failed`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val metadata = "metadata"
        val responseMessage = "message"
        val response = MessageErrorException(responseMessage)
        every { updateCartOccUseCase.execute(match { it.profile.metadata == metadata }, any(), any(), any()) } answers {
            (arg(3) as ((Throwable) -> Unit)).invoke(response)
        }

        // When
        orderSummaryPageViewModel.updateCreditCard(metadata)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = responseMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val metadata = "metadata"
        val response = Exception()
        every { updateCartOccUseCase.execute(match { it.profile.metadata == metadata }, any(), any(), any()) } answers {
            (arg(3) as ((Throwable) -> Unit)).invoke(response)
        }

        // When
        orderSummaryPageViewModel.updateCreditCard(metadata)

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Credit Card On Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)

        // When
        orderSummaryPageViewModel.updateCreditCard("")

        // Then
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any(), any()) }
    }

    @Test
    fun `Update Credit Card Got Prompt`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val metadata = "metadata"
        val occPrompt = OccPrompt()
        every { updateCartOccUseCase.execute(match { it.profile.metadata == metadata }, any(), any(), any()) } answers {
            (thirdArg() as ((OccPrompt) -> Unit)).invoke(occPrompt)
        }

        // When
        orderSummaryPageViewModel.updateCreditCard(metadata)

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Update Got Prompt`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val occPrompt = OccPrompt()
        every { updateCartOccUseCase.execute(any(), any(), any(), any()) } answers {
            (thirdArg() as ((OccPrompt) -> Unit)).invoke(occPrompt)
        }

        // When
        var isSuccess = false
        orderSummaryPageViewModel.finalUpdate({
            isSuccess = true
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
        assertEquals(false, isSuccess)
    }
}