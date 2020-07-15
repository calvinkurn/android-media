package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
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
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class OrderSummaryPageViewModelLogisticTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Get Rates Failed`() {
        setUpCartAndRatesMocks()
        every { ratesUseCase.execute(any()) } returns Observable.error(Throwable())

        orderSummaryPageViewModel.getOccCart(true, "")

        assertEquals(
                OrderShipment(
                        serviceName = "",
                        serviceDuration = "",
                        serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                        shippingRecommendationData = null),
                (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Error`() {
        setUpCartAndRatesMocks()
        val shippingErrorMessage = "error"
        every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData().apply {
            errorId = "1"
            errorMessage = shippingErrorMessage
        })

        orderSummaryPageViewModel.getOccCart(true, "")

        assertEquals(
                OrderShipment(
                        serviceName = "",
                        serviceDuration = "",
                        serviceErrorMessage = shippingErrorMessage,
                        shippingRecommendationData = null),
                (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Get Rates Without Preference Duration`() {
        val shippingDurationViewModels = helper.shippingRecommendationData.shippingDurationViewModels.toMutableList()
        shippingDurationViewModels.removeIf { it.serviceData.serviceId == helper.shipment.serviceId }
        helper.shippingRecommendationData.shippingDurationViewModels = shippingDurationViewModels
        setUpCartAndRatesMocks()

        orderSummaryPageViewModel.getOccCart(true, "")

        assertEquals(
                OrderShipment(
                        serviceName = helper.preference.shipment.serviceName,
                        serviceDuration = helper.preference.shipment.serviceDuration,
                        serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE,
                        shippingRecommendationData = helper.shippingRecommendationData,
                        logisticPromoViewModel = helper.logisticPromo),
                (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Choose Courier`() {
        setUpCartAndRates()

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
                (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Duration`() {
        setUpCartAndRates()

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
                (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Duration With Error`() {
        setUpCartAndRates()

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
                (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping)
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Choose Logistic Promo Success`() {
        setUpCartAndRates()

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping!!
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Red State`() {
        setUpCartAndRates()

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "red"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping!!
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(null, OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Choose Logistic Promo Error`() {
        setUpCartAndRates()

        val response = Throwable()
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(response)
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        val shipping = (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping!!
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Autoapply Logistic Promo Success`() {
        setUpCartAndRates()

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)

        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(response = UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        orderSummaryPageViewModel.updateProduct(OrderProduct(quantity = QuantityUiModel(orderQuantity = 10)))
        (testDispatchers.main as TestCoroutineDispatcher).advanceUntilIdle()

        val shipping = (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping!!
        assertEquals(true, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierSecondDuration, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierSecondDuration.productData.insurance, shipping.insuranceData)
        assertEquals(helper.firstCourierSecondDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Autoapply Logistic Promo Red State`() {
        setUpCartAndRates()

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

        val shipping = (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping!!
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(null, shipping.logisticPromoShipping)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Normal, orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Autoapply Logistic Promo Error`() {
        setUpCartAndRates()

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

        val shipping = (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping!!
        assertEquals(false, shipping.isApplyLogisticPromo)
        assertEquals(helper.firstCourierFirstDuration.productData.shipperProductId, shipping.getRealShipperProductId())
        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Set Insurance Check`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(shipping = OrderShipment(isCheckInsurance = false, insuranceData = InsuranceData()))

        orderSummaryPageViewModel.setInsuranceCheck(true)

        assertEquals(true, orderSummaryPageViewModel._orderPreference!!.shipping!!.isCheckInsurance)
    }

    @Test
    fun `Change Pinpoint`() {
        orderSummaryPageViewModel._orderPreference = OrderPreference(shipping = OrderShipment(needPinpoint = true))

        orderSummaryPageViewModel.changePinpoint()

        assertEquals(false, (orderSummaryPageViewModel.orderPreference.value as OccState.Success<OrderPreference>).data.shipping!!.needPinpoint)
    }

    @Test
    fun `Save Pinpoint Success`() {
        setUpCartAndRates()

        every { editAddressUseCase.createObservable(any()) } returns Observable.just("{\"data\": {\"is_success\": 1}}")

        orderSummaryPageViewModel.savePinpoint("", "")

        assertEquals(OccGlobalEvent.TriggerRefresh(false), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Save Pinpoint Error`() {
        setUpCartAndRates()

        every { editAddressUseCase.createObservable(any()) } returns Observable.just("{\"data\": {\"is_success\": 0},\"message_error\": [\"error\"]}")

        orderSummaryPageViewModel.savePinpoint("", "")

        assertEquals(OccGlobalEvent.Error(errorMessage = "error"), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Save Pinpoint Failed`() {
        setUpCartAndRates()
        val throwable = Throwable()
        every { editAddressUseCase.createObservable(any()) } returns Observable.error(throwable)

        orderSummaryPageViewModel.savePinpoint("", "")

        assertEquals(OccGlobalEvent.Error(throwable), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Clear Bbo If Exist`() {
        setUpCartAndRates()
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))

        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        orderSummaryPageViewModel.clearBboIfExist()

        assertEquals(true, orderSummaryPageViewModel.lastValidateUsePromoRequest?.orders?.get(0)?.codes?.isEmpty())
    }
}