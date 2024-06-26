package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.akamai_bot_lib.ERROR_MESSAGE_AKAMAI
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PriceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OrderInsurance
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfilePayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.DetailsItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import rx.Observable

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelLogisticTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Reload Rates`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
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
        orderSummaryPageViewModel.orderTotal.value =
            OrderTotal(buttonState = OccButtonState.LOADING)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.reloadRates()

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Reload Rates On Invalid Preference`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value =
            helper.preference.copy(payment = OrderProfilePayment())
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.reloadRates()

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Reload Rates On Invalid Address`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value =
            helper.preference.copy(address = OrderProfileAddress())
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.reloadRates()

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
        verify(inverse = true) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Reload Rates While Debounce`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceTimeBy(100)
        orderSummaryPageViewModel.reloadRates()
        testDispatchers.main.advanceUntilIdle()

        // Then
        coVerify(exactly = 2) { updateCartOccUseCase.executeSuspend(any()) }
        verify(exactly = 1) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Reload Rates After Debounce`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceUntilIdle()
        orderSummaryPageViewModel.reloadRates()

        // Then
        coVerify(exactly = 4) { updateCartOccUseCase.executeSuspend(any()) }
        verify(exactly = 2) { ratesUseCase.execute(any()) }
    }

    @Test
    fun `Get Rates Failed`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = "",
                serviceDuration = "",
                serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE,
                shippingRecommendationData = null
            ),
            (orderSummaryPageViewModel.orderShipment.value)
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val shippingErrorMessage = "error"
        every { ratesUseCase.execute(any()) } returns Observable.just(
            ShippingRecommendationData().apply {
                errorId = "1"
                errorMessage = shippingErrorMessage
            }
        )

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = "",
                serviceDuration = "",
                serviceErrorMessage = shippingErrorMessage,
                shippingRecommendationData = null
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Error Akamai`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        val response = AkamaiErrorException(ERROR_MESSAGE_AKAMAI)

        every { ratesUseCase.execute(any()) } throws response

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Without Preference Duration`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        shippingDurationViewModels.removeIf { it.serviceData.serviceId == helper.shipment.serviceId.toIntOrZero() }
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference

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
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Without Any Duration`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        shippingDurationViewModels.clear()
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.preference.shipment.serviceName,
                serviceDuration = helper.preference.shipment.serviceDuration,
                serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                shippingRecommendationData = null
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Duration Error Revamp With Recommendation`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val errorMessage = "error"
        val service = shippingDurationViewModels[0]
        service.serviceData = service.serviceData.copy(
            error = ErrorServiceData(
                errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED,
                errorMessage = errorMessage
            )
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
            shipment = helper.shipment.copy(
                recommendationSpId = helper.firstCourierSecondDuration.productData.shipperProductId.toString(),
                recommendationServiceId = helper.secondDuration.serviceData.serviceId.toString()
            )
        )

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
                insurance = OrderInsurance(helper.firstCourierSecondDuration.productData.insurance)
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
    }

    @Test
    fun `Get Rates Duration Error Revamp With Recommendation Product Hidden`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val errorMessage = "error"
        val service = shippingDurationViewModels[0]
        service.serviceData = service.serviceData.copy(
            error = ErrorServiceData(
                errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED,
                errorMessage = errorMessage
            )
        )
        val secondDurationCouriers =
            ArrayList(shippingDurationViewModels[1].shippingCourierViewModelList)
        secondDurationCouriers.add(
            ShippingCourierUiModel().apply {
                productData = ProductData(
                    shipperName = "pakirim",
                    shipperId = 2,
                    shipperProductId = 4,
                    insurance = InsuranceData(),
                    price = PriceData(
                        price = 2000
                    )
                )
                ratesId = "0"
            }
        )
        val product = secondDurationCouriers[0]
        product.productData = product.productData.copy(isUiRatesHidden = true)
        shippingDurationViewModels[1].shippingCourierViewModelList = secondDurationCouriers
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
            shipment = helper.shipment.copy(
                recommendationSpId = helper.firstCourierSecondDuration.productData.shipperProductId.toString(),
                recommendationServiceId = helper.secondDuration.serviceData.serviceId.toString()
            )
        )

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.secondDuration.serviceData.serviceName,
                serviceDuration = helper.secondDuration.serviceData.serviceName,
                serviceId = helper.secondDuration.serviceData.serviceId,
                shipperName = secondDurationCouriers[1].productData.shipperName,
                shipperId = secondDurationCouriers[1].productData.shipperId,
                shipperProductId = secondDurationCouriers[1].productData.shipperProductId,
                ratesId = secondDurationCouriers[1].ratesId,
                shippingPrice = secondDurationCouriers[1].productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo,
                insurance = OrderInsurance(secondDurationCouriers[1].productData.insurance)
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
    }

    @Test
    fun `Get Rates Courier Error Revamp With Recommendation`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val errorMessage = "error"
        val product = shippingDurationViewModels[0].shippingCourierViewModelList[0]
        product.productData = product.productData.copy(
            error =
            ErrorProductData().apply {
                this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
                this.errorMessage = errorMessage
            }
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
            shipment = helper.shipment.copy(
                recommendationSpId = helper.firstCourierSecondDuration.productData.shipperProductId.toString(),
                recommendationServiceId = helper.secondDuration.serviceData.serviceId.toString()
            )
        )

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.secondDuration.serviceData.serviceName,
                serviceDuration = helper.secondDuration.serviceData.serviceName,
                serviceId = helper.secondDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.secondDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.firstCourierSecondDuration.productData.shipperName,
                shipperId = helper.firstCourierSecondDuration.productData.shipperId,
                shipperProductId = helper.firstCourierSecondDuration.productData.shipperProductId,
                ratesId = helper.firstCourierSecondDuration.ratesId,
                shippingPrice = helper.firstCourierSecondDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo,
                insurance = OrderInsurance(helper.firstCourierSecondDuration.productData.insurance)
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
    }

    @Test
    fun `Get Rates With Courier Recommendation Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val errorMessage = "error"
        val firstDurationCouriers =
            ArrayList(shippingDurationViewModels[0].shippingCourierViewModelList)
        firstDurationCouriers[1].productData =
            firstDurationCouriers[1].productData.copy(isUiRatesHidden = true)
        firstDurationCouriers[0].productData.isRecommend = false
        firstDurationCouriers[0].productData = firstDurationCouriers[0].productData.copy(
            error =
            ErrorProductData().apply {
                this.errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
                this.errorMessage = errorMessage
            }
        )
        firstDurationCouriers.add(
            ShippingCourierUiModel().apply {
                productData = ProductData(
                    shipperName = "pakirim",
                    shipperId = 2,
                    shipperProductId = 4,
                    insurance = InsuranceData(),
                    price = PriceData(
                        price = 2000
                    )
                )
                ratesId = "0"
            }
        )
        helper.shippingRecommendationData.shippingDurationUiModels[0].shippingCourierViewModelList =
            firstDurationCouriers
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
            shipment = helper.shipment.copy(
                recommendationSpId = helper.secondCourierFirstDuration.productData.shipperProductId.toString(),
                recommendationServiceId = helper.firstDuration.serviceData.serviceId.toString()
            )
        )

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = firstDurationCouriers[2].productData.shipperName,
                shipperId = firstDurationCouriers[2].productData.shipperId,
                shipperProductId = firstDurationCouriers[2].productData.shipperProductId,
                ratesId = firstDurationCouriers[2].ratesId,
                shippingPrice = firstDurationCouriers[2].productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo,
                insurance = OrderInsurance(firstDurationCouriers[2].productData.insurance)
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
    }

    @Test
    fun `Get Rates Duration Not Match Revamp With No Recommendation`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value =
            helper.preference.copy(shipment = OrderProfileShipment())

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = "",
                serviceDuration = "",
                serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
    }

    @Test
    fun `Get Rates Courier Revamp With Second Courier Recommended`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        shippingDurationViewModels[0].shippingCourierViewModelList[1].productData.isRecommend = true
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.lastValidateUsePromoRequest =
            ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                ratesId = helper.secondCourierFirstDuration.ratesId,
                ut = helper.secondCourierFirstDuration.productData.unixTime,
                checksum = helper.secondCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.secondCourierFirstDuration.productData.insurance),
                shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        coVerify(exactly = 1) {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        }
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(
                helper.secondCourierFirstDuration.productData.shipperProductId.toString(),
                any()
            )
        }
    }

    @Test
    fun `Get Rates Courier Product Hidden`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val product = shippingDurationViewModels[0].shippingCourierViewModelList[1]
        product.productData = product.productData.copy(isUiRatesHidden = true)
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
            shipment = helper.shipment.copy(
                spId = helper.secondCourierFirstDuration.productData.shipperProductId.toString()
            )
        )

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                ratesId = helper.firstCourierFirstDuration.ratesId,
                ut = helper.firstCourierFirstDuration.productData.unixTime,
                checksum = helper.firstCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.firstCourierFirstDuration.productData.insurance),
                shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
    }

    @Test
    fun `Get Rates Courier With Selected SpId from Rates`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val service = shippingDurationViewModels[0]
        service.serviceData = service.serviceData.copy(
            selectedShipperProductId =
            helper.firstCourierFirstDuration.productData.shipperProductId
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
            shipment = helper.shipment.copy(
                spId = helper.secondCourierFirstDuration.productData.shipperProductId.toString()
            )
        )

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                ratesId = helper.firstCourierFirstDuration.ratesId,
                ut = helper.firstCourierFirstDuration.productData.unixTime,
                checksum = helper.firstCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.firstCourierFirstDuration.productData.insurance),
                shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
    }

    @Test
    fun `Get Rates Courier With Selected SpId from Rates and Different Selected Order Shipment`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val service = shippingDurationViewModels[0]
        service.serviceData = service.serviceData.copy(
            selectedShipperProductId =
            helper.secondCourierFirstDuration.productData.shipperProductId
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                ratesId = helper.secondCourierFirstDuration.ratesId,
                ut = helper.secondCourierFirstDuration.productData.unixTime,
                checksum = helper.secondCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.secondCourierFirstDuration.productData.insurance),
                shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
    }

    @Test
    fun `Get Rates Duration Error Distance Exceed With Selected Order Shipment`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val errorMessage = "error"
        val service = shippingDurationViewModels[0]
        service.serviceData = service.serviceData.copy(
            error = ErrorServiceData(
                errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED,
                errorMessage = errorMessage
            )
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                serviceId = helper.firstDuration.serviceData.serviceId,
                serviceErrorMessage = errorMessage,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED) }
    }

    @Test
    fun `Get Rates Duration Error Weight Exceed With Selected Order Shipment`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val errorMessage = "error"
        val service = shippingDurationViewModels[0]
        service.serviceData = service.serviceData.copy(
            error = ErrorServiceData(
                errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED,
                errorMessage = errorMessage
            )
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                serviceErrorMessage = errorMessage,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Distance Exceed With Selected Order Shipment`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val errorMessage = "error"
        val product = shippingDurationViewModels[0].shippingCourierViewModelList[0]
        product.productData = product.productData.copy(
            error =
            ErrorProductData().apply {
                this.errorId = ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
                this.errorMessage = errorMessage
            }
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                serviceErrorMessage = errorMessage,
                shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                ratesId = helper.firstCourierFirstDuration.ratesId,
                ut = helper.firstCourierFirstDuration.productData.unixTime,
                checksum = helper.firstCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.firstCourierFirstDuration.productData.insurance),
                shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Weight Exceed With Selected Order Shipment`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val errorMessage = "error"
        val courier = shippingDurationViewModels[0].shippingCourierViewModelList[0]
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply {
                this.errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
                this.errorMessage = errorMessage
            }
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                serviceId = helper.firstDuration.serviceData.serviceId,
                serviceErrorMessage = errorMessage,
                shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                ratesId = helper.firstCourierFirstDuration.ratesId,
                ut = helper.firstCourierFirstDuration.productData.unixTime,
                checksum = helper.firstCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.firstCourierFirstDuration.productData.insurance),
                shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
        verify(exactly = 1) { orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED) }
    }

    @Test
    fun `Get Rates Courier Error Pinpoint With Selected Order Shipment`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val courier = shippingDurationViewModels[0].shippingCourierViewModelList[0]
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply {
                this.errorId = ErrorProductData.ERROR_PINPOINT_NEEDED
                this.errorMessage = "error"
            }
        )
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                serviceErrorMessage = OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE,
                shipperName = helper.firstCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.firstCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.firstCourierFirstDuration.productData.shipperId,
                ratesId = helper.firstCourierFirstDuration.ratesId,
                ut = helper.firstCourierFirstDuration.productData.unixTime,
                checksum = helper.firstCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.firstCourierFirstDuration.productData.insurance),
                shippingPrice = helper.firstCourierFirstDuration.productData.price.price,
                needPinpoint = true,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Courier With Selected Order Shipment Second Courier`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId)
        orderSummaryPageViewModel.lastValidateUsePromoRequest =
            ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                ratesId = helper.secondCourierFirstDuration.ratesId,
                ut = helper.secondCourierFirstDuration.productData.unixTime,
                checksum = helper.secondCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.secondCourierFirstDuration.productData.insurance),
                shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        coVerify(exactly = 1) {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        }
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(
                helper.secondCourierFirstDuration.productData.shipperProductId.toString(),
                any()
            )
        }
    }

    @Test
    fun `Get Rates Courier With Selected Order Shipment Second Courier With Different Promo Code`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo.copy(promoCode = "bob")
        )
        orderSummaryPageViewModel.lastValidateUsePromoRequest =
            ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")
        coEvery {
            clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel()

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                ratesId = helper.secondCourierFirstDuration.ratesId,
                ut = helper.secondCourierFirstDuration.productData.unixTime,
                checksum = helper.secondCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.secondCourierFirstDuration.productData.insurance),
                shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        coVerify(exactly = 1) {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        }
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(
                helper.secondCourierFirstDuration.productData.shipperProductId.toString(),
                any()
            )
        }
    }

    @Test
    fun `Get Rates Courier With Selected Order Shipment No Courier`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val shippingCourierViewModels =
            shippingDurationViewModels[0].shippingCourierViewModelList.toMutableList()
        shippingCourierViewModels.removeAt(0)
        shippingDurationViewModels[0].shippingCourierViewModelList = shippingCourierViewModels
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.lastValidateUsePromoRequest =
            ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                ratesId = helper.secondCourierFirstDuration.ratesId,
                ut = helper.secondCourierFirstDuration.productData.unixTime,
                checksum = helper.secondCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.secondCourierFirstDuration.productData.insurance),
                shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        coVerify(exactly = 1) {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        }
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(
                helper.secondCourierFirstDuration.productData.shipperProductId.toString(),
                any()
            )
        }
    }

    @Test
    fun `Get Rates Courier With Selected Order Shipment Courier Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val shippingDurationViewModels =
            helper.shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val shippingCourierViewModels =
            shippingDurationViewModels[0].shippingCourierViewModelList.toMutableList()
        shippingCourierViewModels.removeAt(0)
        shippingCourierViewModels.add(
            0,
            ShippingCourierUiModel().apply {
                productData = ProductData(
                    shipperName = "pakirim",
                    shipperId = 2,
                    shipperProductId = 4,
                    insurance = InsuranceData(),
                    price = PriceData(
                        price = 2000
                    ),
                    error = ErrorProductData().apply {
                        errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
                        errorMessage = "error"
                    }
                )
                ratesId = "0"
            }
        )
        shippingDurationViewModels[0].shippingCourierViewModelList = shippingCourierViewModels
        helper.shippingRecommendationData.shippingDurationUiModels = shippingDurationViewModels
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.lastValidateUsePromoRequest =
            ValidateUsePromoRequest(mutableListOf("promo"))

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                ratesId = helper.secondCourierFirstDuration.ratesId,
                ut = helper.secondCourierFirstDuration.productData.unixTime,
                checksum = helper.secondCourierFirstDuration.productData.checkSum,
                insurance = OrderInsurance(helper.secondCourierFirstDuration.productData.insurance),
                shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                logisticPromoViewModel = helper.logisticPromo
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        coVerify(exactly = 1) {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        }
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(
                helper.secondCourierFirstDuration.productData.shipperProductId.toString(),
                any()
            )
        }
    }

    @Test
    fun `Get Rates On Error Order`() {
        // Given
        orderSummaryPageViewModel.orderCart =
            helper.orderData.cart.copy(shop = helper.orderData.cart.shop.copy(errors = listOf("error")))
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        orderSummaryPageViewModel.orderProfile.value = helper.preference

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(
            OrderShipment(
                isDisabled = true,
                isLoading = false,
                serviceName = helper.shipment.serviceName,
                serviceDuration = helper.shipment.serviceDuration,
                serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OrderTotal(), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Get Rates Overweight`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart.copy(
            shop = helper.orderData.cart.shop.copy(
                maximumWeight = 10,
                maximumWeightWording = "max"
            ),
            products = arrayListOf(helper.product.copy(weight = 100))
        )
        orderSummaryPageViewModel.orderProfile.value = helper.preference

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(false, orderSummaryPageViewModel.orderProfile.value.enable)
        assertEquals(90.0, orderSummaryPageViewModel.orderShop.value.overweight, 0.0)
        assertEquals(
            OrderShipment(
                isLoading = false,
                serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OrderTotal(), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Get Rates With No Pinpoint And Disable Change Courier`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
            address = helper.address.copy(latitude = "", longitude = ""),
            shipment = helper.shipment.copy(isDisableChangeCourier = true)
        )

        // When
        orderSummaryPageViewModel.getRates()

        // Then
        assertEquals(true, orderSummaryPageViewModel.orderProfile.value.enable)
        assertEquals(
            OrderShipment(
                serviceName = helper.shipment.serviceName,
                serviceDuration = helper.shipment.serviceDuration,
                serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE,
                needPinpoint = true
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OrderTotal(), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Choose Courier`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            shippingRecommendationData = helper.shippingRecommendationData.apply {
                shippingDurationUiModels = shippingDurationUiModels.reversed()
            }
        )

        val courier = helper.secondCourierFirstDuration
        courier.productData = courier.productData.copy(error = helper.errorProductData)

        // When
        orderSummaryPageViewModel.chooseCourier(courier)

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                ratesId = helper.secondCourierFirstDuration.ratesId,
                shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                isShowLogisticPromoTickerMessage = false,
                logisticPromoViewModel = helper.logisticPromo,
                needPinpoint = false,
                serviceErrorMessage = helper.secondCourierFirstDuration.productData.error.errorMessage,
                insurance = OrderInsurance(helper.secondCourierFirstDuration.productData.insurance)
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Courier With Error Pinpoint`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            shippingRecommendationData = helper.shippingRecommendationData.apply {
                shippingDurationUiModels = shippingDurationUiModels.reversed()
            }
        )

        val courier = helper.secondCourierFirstDuration
        val error = ErrorProductData().apply {
            errorId = ErrorProductData.ERROR_PINPOINT_NEEDED
        }

        courier.productData = courier.productData.copy(error = error)

        // When
        orderSummaryPageViewModel.chooseCourier(courier)

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.firstDuration.serviceData.serviceName,
                serviceDuration = helper.firstDuration.serviceData.serviceName,
                serviceId = helper.firstDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.secondCourierFirstDuration.productData.shipperName,
                shipperId = helper.secondCourierFirstDuration.productData.shipperId,
                shipperProductId = helper.secondCourierFirstDuration.productData.shipperProductId,
                ratesId = helper.secondCourierFirstDuration.ratesId,
                shippingPrice = helper.secondCourierFirstDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                isShowLogisticPromoTickerMessage = false,
                logisticPromoViewModel = helper.logisticPromo,
                insurance = OrderInsurance(helper.secondCourierFirstDuration.productData.insurance),
                serviceErrorMessage = OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE,
                needPinpoint = true
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Courier With No Corresponding Courier`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        orderSummaryPageViewModel.chooseCourier(
            ShippingCourierUiModel().apply {
                productData = ProductData(
                    shipperProductId = -1
                )
            }
        )

        // Then
        assertEquals(helper.orderShipment, orderSummaryPageViewModel.orderShipment.value)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Duration`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart

        // When
        orderSummaryPageViewModel.chooseDuration(
            helper.secondDuration.serviceData.serviceId,
            helper.firstCourierSecondDuration,
            false
        )

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.secondDuration.serviceData.serviceName,
                serviceDuration = helper.secondDuration.serviceData.serviceName,
                serviceId = helper.secondDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.firstDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.firstCourierSecondDuration.productData.shipperName,
                shipperId = helper.firstCourierSecondDuration.productData.shipperId,
                shipperProductId = helper.firstCourierSecondDuration.productData.shipperProductId,
                ratesId = helper.firstCourierSecondDuration.ratesId,
                shippingPrice = helper.firstCourierSecondDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                logisticPromoTickerMessage = "Tersedia bbo",
                isShowLogisticPromoTickerMessage = false,
                logisticPromoViewModel = helper.logisticPromo,
                isServicePickerEnable = true,
                insurance = OrderInsurance(helper.firstCourierSecondDuration.productData.insurance),
                serviceErrorMessage = ""
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(
                helper.firstCourierSecondDuration.productData.shipperProductId.toString(),
                any()
            )
        }
    }

    @Test
    fun `Choose Duration With No Bbo`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val shippingRecommendationData = helper.shippingRecommendationData
        shippingRecommendationData.logisticPromo = null
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)
        orderSummaryPageViewModel.orderCart = helper.orderData.cart

        // When
        orderSummaryPageViewModel.chooseDuration(
            helper.secondDuration.serviceData.serviceId,
            helper.firstCourierSecondDuration,
            false
        )

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.secondDuration.serviceData.serviceName,
                serviceDuration = helper.secondDuration.serviceData.serviceName,
                serviceId = helper.secondDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.secondDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.firstCourierSecondDuration.productData.shipperName,
                shipperId = helper.firstCourierSecondDuration.productData.shipperId,
                shipperProductId = helper.firstCourierSecondDuration.productData.shipperProductId,
                ratesId = helper.firstCourierSecondDuration.ratesId,
                shippingPrice = helper.firstCourierSecondDuration.productData.price.price,
                shippingRecommendationData = shippingRecommendationData,
                isShowLogisticPromoTickerMessage = false,
                isServicePickerEnable = true,
                insurance = OrderInsurance(helper.firstCourierSecondDuration.productData.insurance),
                serviceErrorMessage = ""
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(
                helper.firstCourierSecondDuration.productData.shipperProductId.toString(),
                any()
            )
        }
    }

    @Test
    fun `Choose Duration With Error`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo

        val error = "error"
        val courier = helper.firstCourierSecondDuration
        courier.productData = courier.productData.copy(
            error = ErrorProductData().apply {
                errorId = "1"
                errorMessage = error
            }
        )

        // When
        orderSummaryPageViewModel.chooseDuration(
            helper.secondDuration.serviceData.serviceId,
            helper.firstCourierSecondDuration,
            false
        )

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.secondDuration.serviceData.serviceName,
                serviceDuration = helper.secondDuration.serviceData.serviceName,
                serviceId = helper.secondDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.secondDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.firstCourierSecondDuration.productData.shipperName,
                shipperId = helper.firstCourierSecondDuration.productData.shipperId,
                shipperProductId = helper.firstCourierSecondDuration.productData.shipperProductId,
                ratesId = helper.firstCourierSecondDuration.ratesId,
                shippingPrice = helper.firstCourierSecondDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                serviceErrorMessage = error,
                isServicePickerEnable = true,
                insurance = OrderInsurance(helper.firstCourierSecondDuration.productData.insurance),
                isShowLogisticPromoTickerMessage = false
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Choose Duration With Pinpoint Error`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderPromo.value = helper.orderPromo

        // When
        orderSummaryPageViewModel.chooseDuration(
            helper.secondDuration.serviceData.serviceId,
            helper.firstCourierSecondDuration,
            true
        )

        // Then
        assertEquals(
            OrderShipment(
                serviceName = helper.secondDuration.serviceData.serviceName,
                serviceDuration = helper.secondDuration.serviceData.serviceName,
                serviceId = helper.secondDuration.serviceData.serviceId,
                isHideChangeCourierCard = helper.secondDuration.serviceData.selectedShipperProductId > 0,
                shipperName = helper.firstCourierSecondDuration.productData.shipperName,
                shipperId = helper.firstCourierSecondDuration.productData.shipperId,
                shipperProductId = helper.firstCourierSecondDuration.productData.shipperProductId,
                ratesId = helper.firstCourierSecondDuration.ratesId,
                shippingPrice = helper.firstCourierSecondDuration.productData.price.price,
                shippingRecommendationData = helper.shippingRecommendationData,
                serviceErrorMessage = OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE,
                needPinpoint = true,
                isServicePickerEnable = false,
                insurance = OrderInsurance(helper.firstCourierSecondDuration.productData.insurance),
                isShowLogisticPromoTickerMessage = false
            ),
            orderSummaryPageViewModel.orderShipment.value
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Choose Duration With No Shipment Data`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference

        // When
        orderSummaryPageViewModel.chooseDuration(
            helper.secondDuration.serviceData.serviceId,
            helper.firstCourierSecondDuration,
            true
        )

        // Then
        assertEquals(OrderShipment(), orderSummaryPageViewModel.orderShipment.value)
    }

    @Test
    fun `Choose Logistic Promo Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(serviceErrorMessage = "")

        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.isServicePickerEnable)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            shipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo From List of Promo Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(serviceErrorMessage = "")
        val promoSelected = helper.logisticPromoEko

        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = promoSelected.promoCode,
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(promoSelected)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        val promoSelectedAfterApplied = promoSelected.copy(isApplied = true)

        assertEquals(promoSelected, shipping.logisticPromoViewModel)
        assertEquals(
            promoSelectedAfterApplied,
            shipping.shippingRecommendationData?.listLogisticPromo?.find { it.isApplied }
        )
        assertEquals(null, shipping.logisticPromoTickerMessage)

        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.isServicePickerEnable)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            shipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Service Picker Enabled`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(isServicePickerEnable = true)

        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.isServicePickerEnable)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            shipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Previous Shipping In Error State`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(serviceErrorMessage = "error")

        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.isServicePickerEnable)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            shipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Pinpoint Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val shippingRecommendationData = helper.shippingRecommendationData
        val courier =
            shippingRecommendationData.shippingDurationUiModels[1].shippingCourierViewModelList[0]
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply {
                errorId = ErrorProductData.ERROR_PINPOINT_NEEDED
            }
        )
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)

        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(true, shipping.needPinpoint)
        assertEquals(
            OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE,
            shipping.serviceErrorMessage
        )
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            shipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Courier Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val shippingRecommendationData = helper.shippingRecommendationData
        val errorMessage = "error"
        val courier =
            shippingRecommendationData.shippingDurationUiModels[1].shippingCourierViewModelList[0]
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply {
                this.errorId = "1"
                this.errorMessage = errorMessage
            }
        )
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)

        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(errorMessage, shipping.serviceErrorMessage)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            shipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Success With Multiple Voucher`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "123",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            shipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Without Corresponding Duration`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val shippingRecommendationData = helper.shippingRecommendationData
        val durations = shippingRecommendationData.shippingDurationUiModels.toMutableList()
        durations.removeAt(1)
        shippingRecommendationData.shippingDurationUiModels = durations
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(
            OccGlobalEvent.Error(
                null,
                OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE
            ),
            orderSummaryPageViewModel.globalEvent.value
        )
    }

    @Test
    fun `Choose Logistic Promo Without Corresponding Courier`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val shippingRecommendationData = helper.shippingRecommendationData
        val durations = shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val courier = durations[1].shippingCourierViewModelList[0]
        courier.productData = courier.productData.copy(shipperProductId = 0)
        shippingRecommendationData.shippingDurationUiModels = durations
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(
            OccGlobalEvent.Error(
                null,
                OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE
            ),
            orderSummaryPageViewModel.globalEvent.value
        )
    }

    @Test
    fun `Choose Logistic Promo Red State`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        val promoErrorMessage = "error promo"
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "red", text = promoErrorMessage)
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(
            OccGlobalEvent.Error(null, promoErrorMessage),
            orderSummaryPageViewModel.globalEvent.value
        )
    }

    @Test
    fun `Choose Logistic Promo No State`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(
            OccGlobalEvent.Error(
                null,
                OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE
            ),
            orderSummaryPageViewModel.globalEvent.value
        )
    }

    @Test
    fun `Choose Logistic Promo Server Error Status`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "ERROR")

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(
            OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE),
            orderSummaryPageViewModel.globalEvent.value
        )
    }

    @Test
    fun `Choose Logistic Promo Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        val response = Throwable()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } throws response

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Error Akamai`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        val response = AkamaiErrorException("")
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } throws response
        coEvery {
            clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel()

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(
            OrderPromo(state = OccButtonState.NORMAL),
            orderSummaryPageViewModel.orderPromo.value
        )
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo With No Shipping Data`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then
        coVerify(inverse = true) {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        }
    }

    @Test
    fun `Unchoose Logistic Promo Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(serviceErrorMessage = "")

        val discountAmount = 100
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(
                    summaries = listOf(
                        SummariesItemUiModel(
                            type = SummariesUiModel.TYPE_DISCOUNT,
                            details = listOf(
                                DetailsItemUiModel(
                                    amount = discountAmount,
                                    type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT
                                )
                            )
                        )
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )
        coEvery {
            clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel()

        // When Choose Logistic Promo
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        // Then Have Logistic Discount
        val firstShipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, firstShipping.isApplyLogisticPromo)

        val firstTotal = orderSummaryPageViewModel.orderTotal.value
        assertEquals(discountAmount, firstTotal.orderCost.shippingDiscountAmount)
        assertEquals(2000.0, firstTotal.orderCost.shippingFee, 0.0)

        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        // When Unchoose Logistic Promo
        orderSummaryPageViewModel.chooseDuration(
            helper.firstDuration.serviceData.serviceId,
            helper.secondCourierFirstDuration,
            false
        )

        // Then Have No Logistic Discount
        val secondShipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, secondShipping.isApplyLogisticPromo)
        assertEquals(true, secondShipping.isServicePickerEnable)
        assertEquals(null, secondShipping.logisticPromoShipping)
        assertEquals(
            helper.secondCourierFirstDuration.productData.insurance,
            secondShipping.insurance.insuranceData
        )
        assertEquals(
            helper.secondCourierFirstDuration.productData.shipperProductId,
            secondShipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)

        val secondTotal = orderSummaryPageViewModel.orderTotal.value
        assertEquals(0, secondTotal.orderCost.shippingDiscountAmount)
        assertEquals(1500.0, secondTotal.orderCost.shippingFee, 0.0)
    }

    @Test
    fun `Auto Apply Logistic Promo Success`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            shipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Success With Different Benefit`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        val firstDiscountAmount = 100
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(
                    summaries = listOf(
                        SummariesItemUiModel(
                            type = SummariesUiModel.TYPE_DISCOUNT,
                            details = listOf(
                                DetailsItemUiModel(
                                    amount = firstDiscountAmount,
                                    type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT
                                )
                            )
                        )
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

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
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(
                    summaries = listOf(
                        SummariesItemUiModel(
                            type = SummariesUiModel.TYPE_DISCOUNT,
                            details = listOf(
                                DetailsItemUiModel(
                                    amount = secondDiscountAmount,
                                    type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT
                                )
                            )
                        )
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When Update Quantity
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceUntilIdle()

        // Then
        val secondShipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(true, secondShipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, secondShipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierSecondDuration.productData.insurance,
            secondShipping.insurance.insuranceData
        )
        assertEquals(
            helper.firstCourierSecondDuration.productData.shipperProductId,
            secondShipping.getRealShipperProductId()
        )

        val secondTotal = orderSummaryPageViewModel.orderTotal.value
        assertEquals(secondDiscountAmount, secondTotal.orderCost.shippingDiscountAmount)
        assertEquals(2000.0, secondTotal.orderCost.shippingFee, 0.0)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Without Corresponding Courier`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart

        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        val shippingRecommendationData = helper.shippingRecommendationData
        val durations = shippingRecommendationData.shippingDurationUiModels.toMutableList()
        val courier = durations[1].shippingCourierViewModelList[0]
        courier.productData = courier.productData.copy(shipperProductId = 0)
        shippingRecommendationData.shippingDurationUiModels = durations
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(null, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Red State`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "red")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(null, shipping.logisticPromoShipping)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Error`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        val throwable = Throwable()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } throws throwable

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Error On Disable Change Courier`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value =
            helper.preference.copy(shipment = helper.shipment.copy(isDisableChangeCourier = true))
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            ),
            status = "OK",
            errorCode = "200"
        )
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        orderSummaryPageViewModel.getRates()
        val throwable = Throwable()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } throws throwable

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(false, shipping.isDisabled)
        assertEquals(
            OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE,
            shipping.serviceErrorMessage
        )
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto Apply Logistic Promo Error Akamai`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        every { ratesUseCase.execute(any()) } returns Observable.just(helper.shippingRecommendationData)
        orderSummaryPageViewModel.getRates()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        assertEquals(true, orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        val throwable = AkamaiErrorException("")
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } throws throwable
        coEvery {
            clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel()

        // When
        orderSummaryPageViewModel.updateProduct(OrderProduct(orderQuantity = 10), 0)
        testDispatchers.main.advanceUntilIdle()

        // Then
        val shipping = orderSummaryPageViewModel.orderShipment.value
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(
            helper.firstCourierFirstDuration.productData.shipperProductId,
            shipping.getRealShipperProductId()
        )
        assertEquals(
            OrderPromo(state = OccButtonState.NORMAL),
            orderSummaryPageViewModel.orderPromo.value
        )
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Set Insurance Check`() {
        // Given
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(
            insurance = OrderInsurance(InsuranceData(), isCheckInsurance = false),
            shipperProductId = 1,
            serviceName = "service"
        )

        // When
        orderSummaryPageViewModel.setInsuranceCheck(true)

        // Then
        assertEquals(true, orderSummaryPageViewModel.orderShipment.value.insurance.isCheckInsurance)
    }

    @Test
    fun `Set Insurance Uncheck`() {
        // Given
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(
            insurance = OrderInsurance(InsuranceData(), isCheckInsurance = true),
            shipperProductId = 1,
            serviceName = "service"
        )

        // When
        orderSummaryPageViewModel.setInsuranceCheck(false)

        // Then
        assertEquals(
            false,
            orderSummaryPageViewModel.orderShipment.value.insurance.isCheckInsurance
        )
    }

    @Test
    fun `Set Insurance Check Using Same State`() {
        // Given
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(
            insurance = OrderInsurance(InsuranceData(), isCheckInsurance = false),
            shipperProductId = 1,
            serviceName = "service"
        )

        // When
        orderSummaryPageViewModel.setInsuranceCheck(false)

        // Then
        assertEquals(
            false,
            orderSummaryPageViewModel.orderShipment.value.insurance.isCheckInsurance
        )
    }

    @Test
    fun `Set Insurance Check On Invalid SpId`() {
        // Given
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(
            insurance = OrderInsurance(InsuranceData(), isCheckInsurance = false),
            shipperProductId = 0,
            serviceName = "service"
        )

        // When
        orderSummaryPageViewModel.setInsuranceCheck(true)

        // Then
        assertEquals(
            false,
            orderSummaryPageViewModel.orderShipment.value.insurance.isCheckInsurance
        )
    }

    @Test
    fun `Change Pinpoint`() {
        // Given
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(needPinpoint = true)

        // When
        orderSummaryPageViewModel.changePinpoint()

        // Then
        assertEquals(false, orderSummaryPageViewModel.orderShipment.value.needPinpoint)
    }

    @Test
    fun `Change Pinpoint On Invalid State`() {
        // Given
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(needPinpoint = false)

        // When
        orderSummaryPageViewModel.changePinpoint()

        // Then
        assertEquals(false, orderSummaryPageViewModel.orderShipment.value.needPinpoint)
    }

    @Test
    fun `Save Pinpoint Success`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        coEvery { editAddressUseCase(any()) } returns KeroEditAddressResponse.Data(keroEditAddress = KeroEditAddressResponse.Data.KeroEditAddress(KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse(isSuccess = 1)))

        // When
        orderSummaryPageViewModel.savePinpoint("", "")

        // Then
        assertEquals(
            OccGlobalEvent.TriggerRefresh(successMessage = OrderSummaryPageViewModel.SAVE_PINPOINT_SUCCESS_MESSAGE),
            orderSummaryPageViewModel.globalEvent.value
        )
    }

    @Test
    fun `Save Pinpoint On Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value =
            helper.preference.copy(address = OrderProfileAddress())

        // When
        orderSummaryPageViewModel.savePinpoint("", "")

        // Then
        coVerify(inverse = true) { editAddressUseCase(any()) }
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Save Pinpoint Error`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        coEvery { editAddressUseCase(any()) } returns KeroEditAddressResponse.Data(keroEditAddress = KeroEditAddressResponse.Data.KeroEditAddress(KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse(isSuccess = 0)))

        // When
        orderSummaryPageViewModel.savePinpoint("", "")

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Save Pinpoint Failed`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val throwable = Throwable()
        coEvery { editAddressUseCase(any()) } throws throwable

        // When
        orderSummaryPageViewModel.savePinpoint("", "")

        // Then
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Clear Bbo If Exist Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        coEvery {
            clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel()
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                globalSuccess = true
            ),
            status = "OK",
            errorCode = "200"
        )

        // When
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        assertEquals(
            true,
            orderSummaryPageViewModel.lastValidateUsePromoRequest?.orders?.get(0)?.codes?.isEmpty()
        )
    }

    @Test
    fun `Clear Bbo If Exist When Bbo Not Applied`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        verify(inverse = true) { clearCacheAutoApplyStackUseCase.get().setParams(any()) }
    }

    @Test
    fun `Clear Bbo If Exist When No Bbo Shipping`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            ),
            status = "OK",
            errorCode = "200"
        )
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        orderSummaryPageViewModel.orderShipment.value =
            orderSummaryPageViewModel.orderShipment.value.copy(logisticPromoShipping = null)

        // When
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        verify(inverse = true) { clearCacheAutoApplyStackUseCase.get().setParams(any()) }
    }

    @Test
    fun `Clear Bbo If Exist When No Bbo Available`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val shippingRecommendationData = helper.shippingRecommendationData
        shippingRecommendationData.logisticPromo = null
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(shippingRecommendationData = shippingRecommendationData)

        // When
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        verify(inverse = true) { clearCacheAutoApplyStackUseCase.get().setParams(any()) }
    }

    @Test
    fun `Clear Bbo If Exist When No Shipping Courier Available`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = LogisticPromoUiModel(),
            logisticPromoShipping = null
        )

        // When
        orderSummaryPageViewModel.clearBboIfExist()

        // Then
        verify(inverse = true) { clearCacheAutoApplyStackUseCase.get().setParams(any()) }
    }

    @Test
    fun `Get normal shipping duration param`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        val result = orderSummaryPageViewModel.getShippingBottomsheetParam()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `Get shipping duration param overweight`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart.copy(
            shop = helper.orderData.cart.shop.copy(
                maximumWeight = 10,
                maximumWeightWording = "max"
            ),
            products = arrayListOf(helper.product.copy(weight = 100))
        )
        orderSummaryPageViewModel.orderProfile.value = helper.preference

        // When
        val result = orderSummaryPageViewModel.getShippingBottomsheetParam()

        // Then
        assertNull(result)
    }

    @Test
    fun `Get shipping duration param after choose BO`() {
        // Given
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(
                isApplyLogisticPromo = true,
                logisticPromoViewModel = helper.logisticPromo,
                logisticPromoShipping = helper.firstCourierSecondDuration
            )

        // When
        val result = orderSummaryPageViewModel.getShippingBottomsheetParam()

        // Then
        assert(result?.psl_code == helper.logisticPromo.promoCode)
    }
}
