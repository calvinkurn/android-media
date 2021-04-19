package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelLogisticTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Reload Rates`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.reloadRates()

        // Then
        coVerify(exactly = 2) { updateCartOccUseCase.executeSuspend(any()) }
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Reload Rates On Invalid Button State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.LOADING)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.reloadRates()

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Rates Failed`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference)
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = "",
                        serviceDuration = "",
                        serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                        shippingRecommendationData = null),
                (orderSummaryPageViewModel.orderShipment.value))
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingErrorMessage = "error"
        every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData().apply {
            errorId = "1"
            errorMessage = shippingErrorMessage
        })

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = "",
                        serviceDuration = "",
                        serviceErrorMessage = shippingErrorMessage,
                        shippingRecommendationData = null),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Without Preference Duration`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels.removeIf { it.serviceData.serviceId == helper.shipment.serviceId }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.preference.shipment.serviceName,
                        serviceDuration = helper.preference.shipment.serviceDuration,
                        serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Without Any Duration`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels.clear()
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.preference.shipment.serviceName,
                        serviceDuration = helper.preference.shipment.serviceDuration,
                        serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                        shippingRecommendationData = null),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Duration Error Revamp With Recommendation`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].serviceData.error = ErrorServiceData().apply {
            this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference.copy(
                shipment = helper.shipment.copy(recommendationSpId = helper.firstCourierSecondDuration.productData.shipperProductId, recommendationServiceId = helper.secondDuration.serviceData.serviceId)
        ), isValid = true)
        orderSummaryPageViewModel.revampData = OccRevampData(true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
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
                        insuranceData = helper.firstCourierSecondDuration.productData.insurance),
                orderSummaryPageViewModel.orderShipment.value)
    }

    @Test
    fun `Get Rates Duration Error Distance Exceed`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].serviceData.error = ErrorServiceData().apply {
            this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED) }
    }

    @Test
    fun `Get Rates Duration Error Weight Exceed`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].serviceData.error = ErrorServiceData().apply {
            this.errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Revamp With Recommendation`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference.copy(
                shipment = helper.shipment.copy(recommendationSpId = helper.firstCourierSecondDuration.productData.shipperProductId, recommendationServiceId = helper.secondDuration.serviceData.serviceId)
        ), isValid = true)
        orderSummaryPageViewModel.revampData = OccRevampData(true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
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
                        insuranceData = helper.firstCourierSecondDuration.productData.insurance),
                orderSummaryPageViewModel.orderShipment.value)
    }

    @Test
    fun `Get Rates Courier Error Distance Exceed`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                        ratesId = helper.firstCourierFirstDuration.ratesId,
                        ut = helper.firstCourierFirstDuration.productData.unixTime,
                        checksum = helper.firstCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.firstCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Weight Exceed`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            this.errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                        ratesId = helper.firstCourierFirstDuration.ratesId,
                        ut = helper.firstCourierFirstDuration.productData.unixTime,
                        checksum = helper.firstCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.firstCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Pinpoint`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            this.errorId = ErrorProductData.ERROR_PINPOINT_NEEDED
            this.errorMessage = "error"
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE,
                        shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                        ratesId = helper.firstCourierFirstDuration.ratesId,
                        ut = helper.firstCourierFirstDuration.productData.unixTime,
                        checksum = helper.firstCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.firstCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                        needPinpoint = true,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Courier Revamp With Second Courier Recommended`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels[0].shippingCourierViewModelList[1].productData.isRecommend = true
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                        ratesId = helper.secondCourierFirstDuration.ratesId,
                        ut = helper.secondCourierFirstDuration.productData.unixTime,
                        checksum = helper.secondCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.secondCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoTickerMessage = "Tersedia bbo",
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        verify(exactly = 1) { validateUsePromoRevampUseCase.get().createObservable(any()) }
        verify(exactly = 1) { orderSummaryAnalytics.eventViewPreselectedCourierOption(helper.secondCourierFirstDuration.productData.shipperProductId.toString(), any()) }
    }

    @Test
    fun `Get Rates Courier Second Courier Recommended`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels[0].shippingCourierViewModelList[1].productData.isRecommend = true
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                        ratesId = helper.secondCourierFirstDuration.ratesId,
                        ut = helper.secondCourierFirstDuration.productData.unixTime,
                        checksum = helper.secondCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.secondCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoTickerMessage = "Tersedia bbo",
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        verify(exactly = 1) { validateUsePromoRevampUseCase.get().createObservable(any()) }
        verify(exactly = 1) { orderSummaryAnalytics.eventViewPreselectedCourierOption(helper.secondCourierFirstDuration.productData.shipperProductId.toString(), any()) }
    }

    @Test
    fun `Get Rates Duration Error Distance Exceed With Selected Order Shipment`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].serviceData.error = ErrorServiceData().apply {
            this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED) }
    }

    @Test
    fun `Get Rates Duration Error Weight Exceed With Selected Order Shipment`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].serviceData.error = ErrorServiceData().apply {
            this.errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Distance Exceed With Selected Order Shipment`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                        ratesId = helper.firstCourierFirstDuration.ratesId,
                        ut = helper.firstCourierFirstDuration.productData.unixTime,
                        checksum = helper.firstCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.firstCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Weight Exceed With Selected Order Shipment`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val errorMessage = "error"
        shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            this.errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
            this.errorMessage = errorMessage
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = errorMessage,
                        shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                        ratesId = helper.firstCourierFirstDuration.ratesId,
                        ut = helper.firstCourierFirstDuration.productData.unixTime,
                        checksum = helper.firstCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.firstCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Pinpoint With Selected Order Shipment`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels[0].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            this.errorId = ErrorProductData.ERROR_PINPOINT_NEEDED
            this.errorMessage = "error"
        }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        serviceErrorMessage = OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE,
                        shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                        ratesId = helper.firstCourierFirstDuration.ratesId,
                        ut = helper.firstCourierFirstDuration.productData.unixTime,
                        checksum = helper.firstCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.firstCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                        needPinpoint = true,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Courier With Selected Order Shipment Second Courier`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                        ratesId = helper.secondCourierFirstDuration.ratesId,
                        ut = helper.secondCourierFirstDuration.productData.unixTime,
                        checksum = helper.secondCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.secondCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoTickerMessage = "Tersedia bbo",
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        verify(exactly = 1) { validateUsePromoRevampUseCase.get().createObservable(any()) }
        verify(inverse = true) { orderSummaryAnalytics.eventViewPreselectedCourierOption(helper.secondCourierFirstDuration.productData.shipperProductId.toString(), any()) }
    }

    @Test
    fun `Get Rates Courier With Selected Order Shipment No Courier`() {
        // Given
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        val shippingCourierViewModels = shippingDurationViewModels[0].shippingCourierViewModelList.toMutableList()
        shippingCourierViewModels.removeAt(0)
        shippingDurationViewModels[0].shippingCourierViewModelList = shippingCourierViewModels
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
                OrderShipment(
                        serviceName = helper.firstDuration.serviceData.serviceName,
                        serviceDuration = helper.firstDuration.serviceData.serviceName,
                        serviceId = helper.firstDuration.serviceData.serviceId,
                        shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                        shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                        shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                        ratesId = helper.secondCourierFirstDuration.ratesId,
                        ut = helper.secondCourierFirstDuration.productData.unixTime,
                        checksum = helper.secondCourierFirstDuration.productData.checkSum,
                        insuranceData = helper.secondCourierFirstDuration.productData.insurance,
                        shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoTickerMessage = "Tersedia bbo",
                        logisticPromoViewModel = helper.logisticPromo),
                orderSummaryPageViewModel.orderShipment.value)
        verify(exactly = 1) { validateUsePromoRevampUseCase.get().createObservable(any()) }
        verify(inverse = true) { orderSummaryAnalytics.eventViewPreselectedCourierOption(helper.secondCourierFirstDuration.productData.shipperProductId.toString(), any()) }
    }

    @Test
    fun `Choose Courier`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(shippingRecommendationData = helper.shippingRecommendationData.apply {
            shippingDurationViewModels = shippingDurationViewModels.reversed()
        })

        // When
        orderSummaryPageViewModel.chooseCourier(helper.secondCourierFirstDuration)

        // Then
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
    fun `Choose Courier With No Corresponding Courier`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        orderSummaryPageViewModel.chooseCourier(ShippingCourierUiModel().apply {
            productData = ProductData().apply {
                shipperProductId = -1
            }
        })

        // Then
        assertEquals(helper.orderShipment, orderSummaryPageViewModel._orderShipment)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Duration`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        orderSummaryPageViewModel.chooseDuration(helper.secondDuration.serviceData.serviceId, helper.firstCourierSecondDuration, false)

        // Then
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
    fun `Choose Duration With No Bbo`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        shippingRecommendationData.logisticPromo = null
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)

        // When
        orderSummaryPageViewModel.chooseDuration(helper.secondDuration.serviceData.serviceId, helper.firstCourierSecondDuration, false)

        // Then
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
                        shippingRecommendationData = shippingRecommendationData,
                        isServicePickerEnable = true,
                        insuranceData = helper.firstCourierSecondDuration.productData.insurance),
                orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewPreselectedCourierOption(helper.firstCourierSecondDuration.productData.shipperProductId.toString(), any()) }
    }

    @Test
    fun `Choose Duration With Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        val error = "error"
        helper.firstCourierSecondDuration.productData.error = ErrorProductData().apply {
            errorId = "1"
            errorMessage = error
        }

        // When
        orderSummaryPageViewModel.chooseDuration(helper.secondDuration.serviceData.serviceId, helper.firstCourierSecondDuration, false)

        // Then
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
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Choose Duration With Pinpoint Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        orderSummaryPageViewModel.chooseDuration(helper.secondDuration.serviceData.serviceId, helper.firstCourierSecondDuration, true)

        // Then
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
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Choose Logistic Promo Success`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(serviceErrorMessage = "")

        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(false, shipping.isServicePickerEnable)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Service Picker Enabled`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isServicePickerEnable = true)

        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
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
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(serviceErrorMessage = "error")

        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.isServicePickerEnable)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Pinpoint Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        shippingRecommendationData.shippingDurationViewModels[1].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            errorId = ErrorProductData.ERROR_PINPOINT_NEEDED
        }
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)

        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.needPinpoint)
        assertEquals(OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE, shipping.serviceErrorMessage)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Courier Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        val errorMessage = "error"
        shippingRecommendationData.shippingDurationViewModels[1].shippingCourierViewModelList[0].productData.error = ErrorProductData().apply {
            this.errorId = "1"
            this.errorMessage = errorMessage
        }
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)

        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(errorMessage, shipping.serviceErrorMessage)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Multiple Voucher`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green")),
                PromoCheckoutVoucherOrdersItemUiModel(code = "123", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Without Corresponding Duration`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        val durations = shippingRecommendationData.shippingDurationViewModels.toMutableList()
        durations.removeAt(1)
        shippingRecommendationData.shippingDurationViewModels = durations
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Without Corresponding Courier`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        val durations = shippingRecommendationData.shippingDurationViewModels.toMutableList()
        durations[1].shippingCourierViewModelList[0].productData.shipperProductId = 0
        shippingRecommendationData.shippingDurationViewModels = durations
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Red State`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "red"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo No State`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Server Error Status`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(status = "ERROR"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        val response = Throwable()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.error(response)

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Error Akamai`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        val response = AkamaiErrorException("")
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.error(response)
        every { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.get().createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OrderPromo(state = OccButtonState.NORMAL), orderSummaryPageViewModel.orderPromo.value)
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Unchoose Logistic Promo Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(serviceErrorMessage = "")

        val discountAmount = 100
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        ), benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(summaries = listOf(
                SummariesItemUiModel(type = SummariesUiModel.TYPE_DISCOUNT, details = listOf(
                        DetailsItemUiModel(amount = discountAmount, type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT)
                ))
        ))), status = "OK"))
        every { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.get().createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // When Choose Logistic Promo
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then Have Logistic Discount
        val firstShipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, firstShipping.isApplyLogisticPromo)

        val firstTotal = orderSummaryPageViewModel.orderTotal.value
        assertEquals(discountAmount, firstTotal.orderCost.shippingDiscountAmount)
        assertEquals(2000.0, firstTotal.orderCost.shippingFee, 0.0)

        // When Unchoose Logistic Promo
        orderSummaryPageViewModel.chooseDuration(helper.firstDuration.serviceData.serviceId, helper.secondCourierFirstDuration, false)

        // Then Have No Logistic Discount
        val secondShipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, secondShipping.isApplyLogisticPromo)
        assertEquals(true, secondShipping.isServicePickerEnable)
        assertEquals(null, secondShipping.logisticPromoShipping)
        assertEquals(helper.secondCourierFirstDuration.productData.insurance, secondShipping.insuranceData)
        assertEquals(helper.secondCourierFirstDuration.productData.shipperProductId, secondShipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)

        val secondTotal = orderSummaryPageViewModel.orderTotal.value
        assertEquals(0, secondTotal.orderCost.shippingDiscountAmount)
        assertEquals(1500.0, secondTotal.orderCost.shippingFee, 0.0)
    }

    @Test
    fun `Auto Apply Logistic Promo Success`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Success With Different Benefit`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        val firstDiscountAmount = 100
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        ), benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(summaries = listOf(
                SummariesItemUiModel(type = SummariesUiModel.TYPE_DISCOUNT, details = listOf(
                        DetailsItemUiModel(amount = firstDiscountAmount, type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT)
                ))
        ))), status = "OK"))

        // When Choose Logistic Promo
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then Have First Discount Amount
        val firstShipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, firstShipping.isApplyLogisticPromo)

        val firstTotal = orderSummaryPageViewModel.orderTotal.value
        assertEquals(firstDiscountAmount, firstTotal.orderCost.shippingDiscountAmount)
        assertEquals(2000.0, firstTotal.orderCost.shippingFee, 0.0)

        // Given Second Discount Amount
        val secondDiscountAmount = 50
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        ), benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(summaries = listOf(
                SummariesItemUiModel(type = SummariesUiModel.TYPE_DISCOUNT, details = listOf(
                        DetailsItemUiModel(amount = secondDiscountAmount, type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT)
                ))
        ))), status = "OK"))

        // When Update Quantity
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        testDispatchers.main.advanceUntilIdle()

        // Then
        val secondShipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, secondShipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, secondShipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, secondShipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, secondShipping.getRealShipperProductId())

        val secondTotal = orderSummaryPageViewModel.orderTotal.value
        assertEquals(secondDiscountAmount, secondTotal.orderCost.shippingDiscountAmount)
        assertEquals(2000.0, secondTotal.orderCost.shippingFee, 0.0)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Without Corresponding Courier`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        val shippingRecommendationData = helper.shippingRecommendationData
        val durations = shippingRecommendationData.shippingDurationViewModels.toMutableList()
        durations[1].shippingCourierViewModelList[0].productData.shipperProductId = 0
        shippingRecommendationData.shippingDurationViewModels = durations
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(null, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Red State`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "red"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(null, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        val throwable = Throwable()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.error(throwable)

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Error Akamai`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        val throwable = AkamaiErrorException("")
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.error(throwable)
        every { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.get().createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OrderPromo(state = OccButtonState.NORMAL), orderSummaryPageViewModel.orderPromo.value)
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Set Insurance Check`() {
        // Given
        orderSummaryPageViewModel._orderShipment = OrderShipment(isCheckInsurance = false, insuranceData = InsuranceData(),
                shipperProductId = 1, serviceName = "service")

        // When
        orderSummaryPageViewModel.setInsuranceCheck(true)

        // Then
        assertEquals(true, orderSummaryPageViewModel._orderShipment.isCheckInsurance)
    }

    @Test
    fun `Set Insurance Uncheck`() {
        // Given
        orderSummaryPageViewModel._orderShipment = OrderShipment(isCheckInsurance = true, insuranceData = InsuranceData(),
                shipperProductId = 1, serviceName = "service")

        // When
        orderSummaryPageViewModel.setInsuranceCheck(false)

        // Then
        assertEquals(false, orderSummaryPageViewModel._orderShipment.isCheckInsurance)
    }

    @Test
    fun `Set Insurance Check Using Same State`() {
        // Given
        orderSummaryPageViewModel._orderShipment = OrderShipment(isCheckInsurance = false, insuranceData = InsuranceData(),
                shipperProductId = 1, serviceName = "service")

        // When
        orderSummaryPageViewModel.setInsuranceCheck(false)

        // Then
        assertEquals(false, orderSummaryPageViewModel._orderShipment.isCheckInsurance)
    }

    @Test
    fun `Set Insurance Check On Invalid SpId`() {
        // Given
        orderSummaryPageViewModel._orderShipment = OrderShipment(isCheckInsurance = false, insuranceData = InsuranceData(),
                shipperProductId = 0, serviceName = "service")

        // When
        orderSummaryPageViewModel.setInsuranceCheck(true)

        // Then
        assertEquals(false, orderSummaryPageViewModel._orderShipment.isCheckInsurance)
    }

    @Test
    fun `Change Pinpoint`() {
        // Given
        orderSummaryPageViewModel._orderShipment = OrderShipment(needPinpoint = true)

        // When
        orderSummaryPageViewModel.changePinpoint()

        // Then
        assertEquals(false, orderSummaryPageViewModel._orderShipment.needPinpoint)
    }

    @Test
    fun `Change Pinpoint On Invalid State`() {
        // Given
        orderSummaryPageViewModel._orderShipment = OrderShipment(needPinpoint = false)

        // When
        orderSummaryPageViewModel.changePinpoint()

        // Then
        assertEquals(false, orderSummaryPageViewModel._orderShipment.needPinpoint)
    }

    @Test
    fun `Save Pinpoint Success`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { editAddressUseCase.get().createObservable(any()) } returns Observable.just("{\"data\": {\"is_success\": 1}}")

        // When
        orderSummaryPageViewModel.savePinpoint("", "")

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Save Pinpoint On Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)

        // When
        orderSummaryPageViewModel.savePinpoint("", "")

        // Then
        verify(inverse = true) { editAddressUseCase.get().createObservable(any()) }
    }

    @Test
    fun `Save Pinpoint Error`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        every { editAddressUseCase.get().createObservable(any()) } returns Observable.just("{\"data\": {\"is_success\": 0},\"message_error\": [\"error\"]}")

        // When
        orderSummaryPageViewModel.savePinpoint("", "")

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = "error"), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Save Pinpoint Failed`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val throwable = Throwable()
        every { editAddressUseCase.get().createObservable(any()) } returns Observable.error(throwable)

        // When
        orderSummaryPageViewModel.savePinpoint("", "")

        // Then
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Clear Bbo If Exist Success`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.get().createObservable(any()) } returns Observable.just(ClearPromoUiModel())
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        assertEquals(true, orderSummaryPageViewModel.lastValidateUsePromoRequest?.orders?.get(0)?.codes?.isEmpty())
    }

    @Test
    fun `Clear Bbo If Exist When Bbo Not Applied`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        verify(inverse = true) { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) }
    }

    @Test
    fun `Clear Bbo If Exist When No Bbo Shipping`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        orderSummaryPageViewModel._orderShipment = orderSummaryPageViewModel._orderShipment.copy(logisticPromoShipping = null)

        // When
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        verify(inverse = true) { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) }
    }

    @Test
    fun `Clear Bbo If Exist When No Bbo Available`() {
        // Given
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val shippingRecommendationData = helper.shippingRecommendationData
        shippingRecommendationData.logisticPromo = null
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)

        // When
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        verify(inverse = true) { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) }
    }
}