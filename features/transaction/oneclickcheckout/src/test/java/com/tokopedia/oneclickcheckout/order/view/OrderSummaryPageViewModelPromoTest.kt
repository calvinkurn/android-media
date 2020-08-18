package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.data.checkout.*
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartDataOcc
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccResponse
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class OrderSummaryPageViewModelPromoTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Generate Promo Request With Insurance`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isCheckInsurance = true, insuranceData = helper.firstCourierFirstDuration.productData.insurance)

        val promoRequest = orderSummaryPageViewModel.generatePromoRequest()

        assertEquals(1, promoRequest.orders.first().isInsurancePrice)
    }

    @Test
    fun `Generate Promo Request With Insurance Checked And No Data`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isCheckInsurance = true)

        val promoRequest = orderSummaryPageViewModel.generatePromoRequest()

        assertEquals(0, promoRequest.orders.first().isInsurancePrice)
    }

    @Test
    fun `Generate Validate Use Promo Request With Last Apply And Bbo`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(lastApply = LastApplyUiModel(codes = listOf(promoCode),
                voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode))))
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isApplyLogisticPromo = true, logisticPromoViewModel = helper.logisticPromo, logisticPromoShipping = helper.firstCourierSecondDuration)

        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        assertEquals(promoCode, promoRequest.codes.first())
        assertEquals(listOf(promoCode, helper.logisticPromo.promoCode), promoRequest.orders.first()!!.codes)
    }

    @Test
    fun `Generate Validate Use Promo Request With Invalid Last Apply And No Bbo`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(lastApply = LastApplyUiModel(voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode, uniqueId = promoCode))))
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(logisticPromoViewModel = null, logisticPromoTickerMessage = null)

        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        assertEquals(true, promoRequest.codes.isEmpty())
        assertEquals(true, promoRequest.orders.first()!!.codes.isEmpty())
    }

    @Test
    fun `Generate Validate Use Promo Request With Multiple Last Apply`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(lastApply = LastApplyUiModel(
                voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode), LastApplyVoucherOrdersItemUiModel(code = promoCode))))
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        assertEquals(true, promoRequest.codes.isEmpty())
        assertEquals(listOf(promoCode), promoRequest.orders.first()!!.codes)
    }

    @Test
    fun `Update Cart Promo Success Without Promo`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.updateCartPromo { validateUsePromoRequest, promoRequest, bboCodes ->
            assertEquals(ValidateUsePromoRequest(isSuggested = 0, skipApply = 0, cartType = "occ", state = "checkout",
                    orders = listOf(OrdersItem(shippingId = helper.firstCourierFirstDuration.productData.shipperId, spId = helper.firstCourierFirstDuration.productData.shipperProductId,
                            productDetails = listOf(ProductDetailsItem(helper.product.quantity.orderQuantity, helper.product.productId))))), validateUsePromoRequest)
            assertEquals(PromoRequest(cartType = "occ", state = "checkout",
                    orders = listOf(Order(isChecked = true, shippingId = helper.firstCourierFirstDuration.productData.shipperId, spId = helper.firstCourierFirstDuration.productData.shipperProductId,
                            product_details = listOf(ProductDetail(helper.product.productId.toLong(), helper.product.quantity.orderQuantity))))), promoRequest)
            assertEquals(0, bboCodes.size)
        }
    }

    @Test
    fun `Update Cart Promo Success With BBO`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.updateCartPromo { validateUsePromoRequest, promoRequest, bboCodes ->
            assertEquals(ValidateUsePromoRequest(isSuggested = 0, skipApply = 0, cartType = "occ", state = "checkout",
                    orders = listOf(OrdersItem(shippingId = helper.logisticPromo.shipperId, spId = helper.logisticPromo.shipperProductId,
                            codes = mutableListOf(helper.logisticPromo.promoCode),
                            productDetails = listOf(ProductDetailsItem(helper.product.quantity.orderQuantity, helper.product.productId))))), validateUsePromoRequest)
            assertEquals(PromoRequest(cartType = "occ", state = "checkout",
                    orders = listOf(Order(isChecked = true, shippingId = helper.logisticPromo.shipperId, spId = helper.logisticPromo.shipperProductId,
                            codes = mutableListOf(helper.logisticPromo.promoCode),
                            product_details = listOf(ProductDetail(helper.product.productId.toLong(), helper.product.quantity.orderQuantity))))), promoRequest)
            assertEquals(1, bboCodes.size)
        }
    }

    @Test
    fun `Update Cart Promo Error`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }

        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Cart Promo Invalid Preference State`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }

        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Validate Use Promo Success`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "green")))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)

        orderSummaryPageViewModel.validateUsePromo()

        verify(inverse = true) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(ButtonBayarState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Error`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(Throwable())

        orderSummaryPageViewModel.validateUsePromo()

        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderPromo.value.state)
        assertEquals(ButtonBayarState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Red State Released`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "red")))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))

        orderSummaryPageViewModel.validateUsePromo()

        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(ButtonBayarState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Benefit Decreased`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "green"), benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(finalBenefitAmount = 10),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(messageUiModel = MessageUiModel(state = "green")))))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = PromoUiModel(messageUiModel = MessageUiModel(state = "green"), benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(finalBenefitAmount = 1000),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(messageUiModel = MessageUiModel(state = "green")))))

        orderSummaryPageViewModel.validateUsePromo()

        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(false)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(ButtonBayarState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Final Validate Use Promo Global Code Success`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "green")))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.finalUpdate({ }, false)

        verify {
            checkoutOccUseCase.execute(match {
                val globalCode = it.carts.promos.first()
                globalCode.code == promoCode && globalCode.type == "global" && it.carts.data.first().shopProducts.first().promos.isEmpty()
            }, any(), any())
        }
    }

    @Test
    fun `Final Validate Use Promo Voucher Success`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        val promoCode = "abc"
        val promoType = "type"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(code = promoCode, type = promoType, messageUiModel = MessageUiModel(state = "green")))))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.finalUpdate({ }, false)

        verify {
            checkoutOccUseCase.execute(match {
                val voucherCode = it.carts.data.first().shopProducts.first().promos.first()
                voucherCode.code == promoCode && voucherCode.type == promoType && it.carts.promos.isEmpty()
            }, any(), any())
        }
    }

    @Test
    fun `Final Validate Use Promo Red State`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "red")))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.finalUpdate({ }, false)

        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(NotEligiblePromoHolderdata(
                promoCode = promoCode, shopName = "Kode promo", iconType = 1, showShopSection = true
        ))), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Validate Use Promo Red State Voucher`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                )), messageUiModel = MessageUiModel(state = "red")))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.finalUpdate({ }, false)

        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(NotEligiblePromoHolderdata(
                promoCode = promoCode, shopName = "Kode promo", iconType = 1, showShopSection = true
        ), NotEligiblePromoHolderdata(showShopSection = true))), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Validate Use Promo Red State Multiple Voucher`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                ), PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                ))))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest()
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.finalUpdate({ }, false)

        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(
                NotEligiblePromoHolderdata(showShopSection = true), NotEligiblePromoHolderdata(showShopSection = false)
        )), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Validate Use Promo Red State Official Store`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart.copy(shop = OrderShop(isOfficial = 1))
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                ))))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest()
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.finalUpdate({ }, false)

        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(
                NotEligiblePromoHolderdata(showShopSection = true, iconType = NotEligiblePromoHolderdata.TYPE_ICON_OFFICIAL_STORE)
        )), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Validate Use Promo Red State Power Merchant`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart.copy(shop = OrderShop(isGold = 1))
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                ))))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest()
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        orderSummaryPageViewModel.finalUpdate({ }, false)

        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(
                NotEligiblePromoHolderdata(showShopSection = true, iconType = NotEligiblePromoHolderdata.TYPE_ICON_POWER_MERCHANT)
        )), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Cancel Ineligible Promo Checkout Success`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl"))))))
        }

        var isSuccess = false
        orderSummaryPageViewModel.cancelIneligiblePromoCheckout(arrayListOf()) {
            isSuccess = true
        }

        assertEquals(true, isSuccess)
    }

    @Test
    fun `Cancel Ineligible Promo Checkout Error`() {
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any(), any()) } just Runs
        val response = Throwable()
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(response)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl"))))))
        }

        var isSuccess = false
        orderSummaryPageViewModel.cancelIneligiblePromoCheckout(arrayListOf()) {
            isSuccess = true
        }

        assertEquals(false, isSuccess)
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }
}