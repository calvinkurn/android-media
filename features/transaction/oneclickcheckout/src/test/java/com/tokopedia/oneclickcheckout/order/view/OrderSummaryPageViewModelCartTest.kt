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
import com.tokopedia.oneclickcheckout.common.data.model.Shipment
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.data.get.ProfileResponse
import com.tokopedia.oneclickcheckout.order.data.update.*
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

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

        assertEquals(OccState.FirstLoad(OrderPreference(profileIndex = "", profileRecommendation = "")), orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
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
    }

    @Test
    fun `Get Occ Cart Success With Preference`() {
        val shipment = Shipment(serviceId = 1)
        val profile = ProfileResponse(shipment = shipment)
        val response = OrderData(cart = OrderCart(product = OrderProduct(productId = 1)), preference = profile)
        every { getOccCartUseCase.createRequestParams(any()) } returns RequestParams.EMPTY
        every { getOccCartUseCase.execute(any(), any(), any()) } answers { (firstArg() as ((OrderData) -> Unit)).invoke(response) }
        every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData().apply {
            shippingDurationViewModels = ArrayList()
        })

        orderSummaryPageViewModel.getOccCart(true, "")

        assertEquals(OccState.Success(OrderPreference(profileIndex = "", profileRecommendation = "", preference = profile,
                shipping = OrderShipment("", serviceDuration = "", serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE))),
                orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
    }

    @Test
    fun `Get Occ Cart Success With Preference And Rates`() {
        val shipment = Shipment(serviceId = 1)
        val profile = ProfileResponse(shipment = shipment, profileId = 1)
        val response = OrderData(cart = OrderCart(product = OrderProduct(productId = 1, quantity = QuantityUiModel(orderQuantity = 1))), preference = profile)
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

        assertEquals(OccState.Success(OrderPreference(profileIndex = "", profileRecommendation = "", preference = profile,
                shipping = OrderShipment(serviceName = "kirimaja (2 hari)", serviceDuration = "kirimaja (2 hari)", serviceId = 1, shipperName = "kirimin",
                        shipperId = 0, shipperProductId = 1, ratesId = "0", shippingPrice = 0, shippingRecommendationData = shippingRecommendationData,
                        insuranceData = shippingRecommendationData.shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.insurance))),
                orderSummaryPageViewModel.orderPreference.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(any())
        }
    }

    @Test
    fun `Update Product Debounce Success`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, shipping = helper.orderShipment)
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
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, shipping = helper.orderShipment)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)), false)

        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        verify(inverse = true) { ratesUseCase.execute(any()) }
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Update Product Debounce With Error Quantity Should Not Reload Rates`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, shipping = helper.orderShipment)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10, isStateError = true)), false)

        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        verify(inverse = true) { ratesUseCase.execute(any()) }
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Consume Force Show Onboarding`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(onboarding = OccMainOnboarding(isForceShowCoachMark = true))

        orderSummaryPageViewModel.consumeForceShowOnboarding()

        assertEquals(OccState.Success(OrderPreference(onboarding = OccMainOnboarding(isForceShowCoachMark = false))), orderSummaryPageViewModel.orderPreference.value)
    }

    @Test
    fun `Update Cart Params`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, shipping = helper.orderShipment)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.updateCart()

        verify { updateCartOccUseCase.execute(withArg { assertEquals(UpdateCartOccRequest(arrayListOf(UpdateCartOccCartRequest(cartId = "0", quantity = 1, productId = "1", spId = 1, shippingId = 1)), UpdateCartOccProfileRequest(profileId = "1", serviceId = 1, addressId = "0")), it) }, any(), any()) }
    }

    @Test
    fun `Update Preference Success Should Trigger Refresh`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, shipping = helper.orderShipment)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        assertEquals(OccGlobalEvent.TriggerRefresh(true), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Preference Failed`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, shipping = helper.orderShipment)
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
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, shipping = helper.orderShipment)
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.updatePreference(ProfilesItemModel())

        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }
}