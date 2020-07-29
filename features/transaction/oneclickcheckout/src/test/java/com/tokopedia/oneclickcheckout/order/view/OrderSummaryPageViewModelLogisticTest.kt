package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartDataOcc
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccResponse
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class OrderSummaryPageViewModelLogisticTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Get Rates Failed`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference)
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        orderSummaryPageViewModel.getRates()

        assertEquals(
                OrderShipment(
                        serviceName = "",
                        serviceDuration = "",
                        serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                        shippingRecommendationData = null),
                (orderSummaryPageViewModel.orderShipment.value))
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Error`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingErrorMessage = "error"
        every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData().apply {
            errorId = "1"
            errorMessage = shippingErrorMessage
        })

        orderSummaryPageViewModel.getRates()

        assertEquals(
                OrderShipment(
                        serviceName = "",
                        serviceDuration = "",
                        serviceErrorMessage = shippingErrorMessage,
                        shippingRecommendationData = null),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Without Preference Duration`() {
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels.removeIf { it.serviceData.serviceId == helper.shipment.serviceId }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        orderSummaryPageViewModel.getRates()

        assertEquals(
                OrderShipment(
                        serviceName = helper.preference.shipment.serviceName,
                        serviceDuration = helper.preference.shipment.serviceDuration,
                        serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Without Any Duration`() {
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels.clear()
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        orderSummaryPageViewModel.getRates()

        assertEquals(
                OrderShipment(
                        serviceName = helper.preference.shipment.serviceName,
                        serviceDuration = helper.preference.shipment.serviceDuration,
                        serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                        shippingRecommendationData = null),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Error Distance Exceed`() {
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].serviceData.error = ErrorServiceData().apply {
            this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        orderSummaryPageViewModel.getRates()

        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED) }
    }

    @Test
    fun `Get Rates Error Weight Exceed`() {
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].serviceData.error = ErrorServiceData().apply {
            this.errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        orderSummaryPageViewModel.getRates()

        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED) }
    }

    @Test
    fun `Choose Courier`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        orderSummaryPageViewModel.chooseCourier(helper.secondCourierFirstDuration)

        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                        shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                        shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                        ratesId = helper.secondCourierFirstDuration.ratesId,
                        shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoTickerMessage = "Tersedia bbo",
                        logisticPromoViewModel = helper.logisticPromo,
                        insuranceData = helper.secondCourierFirstDuration.productData.insurance),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Duration`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        orderSummaryPageViewModel.chooseDuration(helper.secondDuration.serviceData.serviceId, helper.firstCourierSecondDuration, false)

        assertEquals(
                OrderShipment(
                        serviceName = helper.secondDuration.serviceData.serviceName,
                        serviceDuration = helper.secondDuration.serviceData.serviceName,
                        serviceId = helper.secondDuration.serviceData.serviceId,
                        shipperName = helper.firstCourierSecondDuration.productData.shipperName,
                        shipperId = helper.firstCourierSecondDuration.productData.shipperId,
                        shipperProductId = helper.firstCourierSecondDuration.productData.shipperProductId,
                        ratesId = helper.firstCourierSecondDuration.ratesId,
                        shippingPrice = helper.firstCourierSecondDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoTickerMessage = "Tersedia bbo",
                        logisticPromoViewModel = helper.logisticPromo,
                        isServicePickerEnable = true,
                        insuranceData = helper.firstCourierSecondDuration.productData.insurance),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewPreselectedCourierOption(helper.firstCourierSecondDuration.productData.shipperProductId.toString(), any()) }
    }

    @Test
    fun `Choose Duration With Error`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        val error = "error"
        helper.firstCourierSecondDuration.productData.error = ErrorProductData().apply {
            errorId = "1"
            errorMessage = error
        }
        orderSummaryPageViewModel.chooseDuration(helper.secondDuration.serviceData.serviceId, helper.firstCourierSecondDuration, false)

        assertEquals(
                OrderShipment(
                        serviceName = helper.secondDuration.serviceData.serviceName,
                        serviceDuration = helper.secondDuration.serviceData.serviceName,
                        serviceId = helper.secondDuration.serviceData.serviceId,
                        shipperName = helper.firstCourierSecondDuration.productData.shipperName,
                        shipperId = helper.firstCourierSecondDuration.productData.shipperId,
                        shipperProductId = helper.firstCourierSecondDuration.productData.shipperProductId,
                        ratesId = helper.firstCourierSecondDuration.ratesId,
                        shippingPrice = helper.firstCourierSecondDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        serviceErrorMessage = error,
                        isServicePickerEnable = true,
                        insuranceData = helper.firstCourierSecondDuration.productData.insurance),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Choose Duration With Pinpoint Error`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        orderSummaryPageViewModel.chooseDuration(helper.secondDuration.serviceData.serviceId, helper.firstCourierSecondDuration, true)

        assertEquals(
                OrderShipment(
                        serviceName = helper.secondDuration.serviceData.serviceName,
                        serviceDuration = helper.secondDuration.serviceData.serviceName,
                        serviceId = helper.secondDuration.serviceData.serviceId,
                        shipperName = helper.firstCourierSecondDuration.productData.shipperName,
                        shipperId = helper.firstCourierSecondDuration.productData.shipperId,
                        shipperProductId = helper.firstCourierSecondDuration.productData.shipperProductId,
                        ratesId = helper.firstCourierSecondDuration.ratesId,
                        shippingPrice = helper.firstCourierSecondDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        serviceErrorMessage = OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE,
                        needPinpoint = true,
                        isServicePickerEnable = false,
                        insuranceData = helper.firstCourierSecondDuration.productData.insurance),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Choose Logistic Promo Success`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(serviceErrorMessage = "")

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Service Picker Enabled`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isServicePickerEnable = true)

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.isServicePickerEnable)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Previous Shipping In Error State`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(serviceErrorMessage = "error")

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.isServicePickerEnable)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Without Corresponding Duration`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        val durations = shippingRecommendationData.shippingDurationViewModels.toMutableList()
        durations.removeAt(1)
        shippingRecommendationData.shippingDurationViewModels = durations
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Without Corresponding Courier`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        val durations = shippingRecommendationData.shippingDurationViewModels.toMutableList()
        durations[1].shippingCourierViewModelList[0].productData.shipperProductId = 0
        shippingRecommendationData.shippingDurationViewModels = durations
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Red State`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "red"))
        )), status = "OK"))

        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo No State`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(status = "OK"))

        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Server Error Status`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        val response = Throwable()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(status = "ERROR"))

        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Error`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        val response = Throwable()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(response)

        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Autoapply Logistic Promo Success`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(response = UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Autoapply Logistic Promo Red State`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(response = UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "red"))
        )), status = "OK"))

        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(null, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Autoapply Logistic Promo Error`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(response = UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        val throwable = Throwable()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(throwable)

        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Set Insurance Check`() {
        orderSummaryPageViewModel._orderShipment = OrderShipment(isCheckInsurance = false, insuranceData = InsuranceData(),
                shipperProductId = 1, serviceName = "service")

        orderSummaryPageViewModel.setInsuranceCheck(true)

        assertEquals(true, orderSummaryPageViewModel._orderShipment.isCheckInsurance)
    }

    @Test
    fun `Change Pinpoint`() {
        orderSummaryPageViewModel._orderShipment = OrderShipment(needPinpoint = true)

        orderSummaryPageViewModel.changePinpoint()

        assertEquals(false, orderSummaryPageViewModel._orderShipment.needPinpoint)
    }

    @Test
    fun `Change Pinpoint On Invalid State`() {
        orderSummaryPageViewModel._orderShipment = OrderShipment(needPinpoint = false)

        orderSummaryPageViewModel.changePinpoint()

        assertEquals(false, orderSummaryPageViewModel._orderShipment.needPinpoint)
    }

    @Test
    fun `Save Pinpoint Success`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { editAddressUseCase.createObservable(any()) } returns Observable.just("{\"data\": {\"is_success\": 1}}")

        orderSummaryPageViewModel.savePinpoint("", "")

        assertEquals(OccGlobalEvent.TriggerRefresh(false), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Save Pinpoint On Invalid State`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)

        orderSummaryPageViewModel.savePinpoint("", "")

        verify(inverse = true) { editAddressUseCase.createObservable(any()) }
    }

    @Test
    fun `Save Pinpoint Error`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { editAddressUseCase.createObservable(any()) } returns Observable.just("{\"data\": {\"is_success\": 0},\"message_error\": [\"error\"]}")

        orderSummaryPageViewModel.savePinpoint("", "")

        assertEquals(OccGlobalEvent.Error(errorMessage = "error"), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Save Pinpoint Failed`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val throwable = Throwable()
        every { editAddressUseCase.createObservable(any()) } returns Observable.error(throwable)

        orderSummaryPageViewModel.savePinpoint("", "")

        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Clear Bbo If Exist`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        orderSummaryPageViewModel.clearBboIfExist()

        assertEquals(true, orderSummaryPageViewModel.lastValidateUsePromoRequest?.orders?.get(0)?.codes?.isEmpty())
    }

    @Test
    fun `Clear Bbo If Exist When No Bbo Shipping`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        orderSummaryPageViewModel._orderShipment = orderSummaryPageViewModel._orderShipment.copy(logisticPromoShipping = null)

        orderSummaryPageViewModel.clearBboIfExist()

        verify(inverse = true) { clearCacheAutoApplyStackUseCase.setParams(any(), any(), any()) }
    }

    @Test
    fun `Clear Bbo If Exist When No Bbo`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        shippingRecommendationData.logisticPromo = null
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)

        orderSummaryPageViewModel.clearBboIfExist()

        verify(inverse = true) { clearCacheAutoApplyStackUseCase.setParams(any(), any(), any()) }
    }
}