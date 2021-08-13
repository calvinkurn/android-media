package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
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
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class OrderSummaryPageViewModelPromoTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Generate Promo Request With Insurance`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isCheckInsurance = true, insuranceData = helper.firstCourierFirstDuration.productData.insurance)

        // When
        val promoRequest = orderSummaryPageViewModel.generatePromoRequest()

        // Then
        assertEquals(1, promoRequest.orders.first().isInsurancePrice)
    }

    @Test
    fun `Generate Promo Request With Insurance Checked And No Data`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isCheckInsurance = true)

        // When
        val promoRequest = orderSummaryPageViewModel.generatePromoRequest()

        // Then
        assertEquals(0, promoRequest.orders.first().isInsurancePrice)
    }

    @Test
    fun `Generate Validate Use Promo Request With Last Apply And Bbo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(lastApply = LastApplyUiModel(codes = listOf(promoCode),
                voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode))))
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isApplyLogisticPromo = true, logisticPromoViewModel = helper.logisticPromo, logisticPromoShipping = helper.firstCourierSecondDuration)

        // When
        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(promoCode, promoRequest.codes.first())
        assertEquals(listOf(promoCode, helper.logisticPromo.promoCode), promoRequest.orders.first()!!.codes)
    }

    @Test
    fun `Generate Validate Use Promo Request With Invalid Last Apply And No Bbo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(lastApply = LastApplyUiModel(voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode, uniqueId = promoCode))))
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(logisticPromoViewModel = null, logisticPromoTickerMessage = null)

        // When
        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(true, promoRequest.codes.isEmpty())
        assertEquals(true, promoRequest.orders.first()!!.codes.isEmpty())
    }

    @Test
    fun `Generate Validate Use Promo Request With Multiple Last Apply`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(lastApply = LastApplyUiModel(
                voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode), LastApplyVoucherOrdersItemUiModel(code = promoCode))))
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(true, promoRequest.codes.isEmpty())
        assertEquals(listOf(promoCode), promoRequest.orders.first()!!.codes)
    }

    @Test
    fun `Update Cart Promo Success Without Promo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.updateCartPromo { validateUsePromoRequest, promoRequest, bboCodes ->
            // Then
            assertEquals(ValidateUsePromoRequest(isSuggested = 0, skipApply = 0, cartType = "occ", state = "checkout",
                    orders = listOf(OrdersItem(shippingId = helper.firstCourierFirstDuration.productData.shipperId, spId = helper.firstCourierFirstDuration.productData.shipperProductId,
                            productDetails = listOf(ProductDetailsItem(helper.product.quantity.orderQuantity, helper.product.productId))))), validateUsePromoRequest)
            assertEquals(PromoRequest(cartType = "occ", state = "checkout",
                    orders = listOf(Order(isChecked = true, shippingId = helper.firstCourierFirstDuration.productData.shipperId, spId = helper.firstCourierFirstDuration.productData.shipperProductId,
                            product_details = listOf(ProductDetail(helper.product.productId, helper.product.quantity.orderQuantity))))), promoRequest)
            assertEquals(0, bboCodes.size)
        }
    }

    @Test
    fun `Update Cart Promo Success With BBO`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(PromoUiModel(voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
        )), status = "OK"))
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.updateCartPromo { validateUsePromoRequest, promoRequest, bboCodes ->
            // Then
            assertEquals(ValidateUsePromoRequest(isSuggested = 0, skipApply = 0, cartType = "occ", state = "checkout",
                    orders = listOf(OrdersItem(shippingId = helper.logisticPromo.shipperId, spId = helper.logisticPromo.shipperProductId,
                            codes = mutableListOf(helper.logisticPromo.promoCode),
                            productDetails = listOf(ProductDetailsItem(helper.product.quantity.orderQuantity, helper.product.productId))))), validateUsePromoRequest)
            assertEquals(PromoRequest(cartType = "occ", state = "checkout",
                    orders = listOf(Order(isChecked = true, shippingId = helper.logisticPromo.shipperId, spId = helper.logisticPromo.shipperProductId,
                            codes = mutableListOf(helper.logisticPromo.promoCode),
                            product_details = listOf(ProductDetail(helper.product.productId, helper.product.quantity.orderQuantity))))), promoRequest)
            assertEquals(1, bboCodes.size)
        }
    }

    @Test
    fun `Update Cart Promo Got Prompt`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val occPrompt = OccPrompt()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns occPrompt

        // When
        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Cart Promo Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val response = Throwable()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } throws response

        // When
        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Cart Promo Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Validate Use Promo Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "green")))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.error(Throwable())

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderPromo.value.state)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Error Akamai`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        val exception = AkamaiErrorException("")
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.error(exception)
        every { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.get().createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        assertEquals(OrderPromo(state = OccButtonState.NORMAL), orderSummaryPageViewModel.orderPromo.value)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
        assertEquals(false, orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertEquals(OccGlobalEvent.Error(exception), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Validate Use Promo Red State Released`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "red")))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Benefit Decreased`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "green"), benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(finalBenefitAmount = 10),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(messageUiModel = MessageUiModel(state = "green")))))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = PromoUiModel(messageUiModel = MessageUiModel(state = "green"), benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(finalBenefitAmount = 1000),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(messageUiModel = MessageUiModel(state = "green")))))

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(false)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Final Validate Use Promo Global Code Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "green")))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        coVerify {
            checkoutOccUseCase.executeSuspend(match {
                val globalCode = it.carts.promos.first()
                globalCode.code == promoCode && globalCode.type == "global" && it.carts.data.first().shopProducts.first().promos.isEmpty()
            })
        }
    }

    @Test
    fun `Final Validate Use Promo Voucher Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        val promoCode = "abc"
        val promoType = "type"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(code = promoCode, type = promoType, messageUiModel = MessageUiModel(state = "green")))))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        coVerify {
            checkoutOccUseCase.executeSuspend(match {
                val voucherCode = it.carts.data.first().shopProducts.first().promos.first()
                voucherCode.code == promoCode && voucherCode.type == promoType && it.carts.promos.isEmpty()
            })
        }
    }

    @Test
    fun `Final Validate Use Promo Red State`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "red")))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(NotEligiblePromoHolderdata(
                promoCode = promoCode, shopName = "Kode promo", iconType = 1, showShopSection = true
        ))), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Validate Use Promo Red State Voucher`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(codes = listOf(promoCode),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                )), messageUiModel = MessageUiModel(state = "red")))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(NotEligiblePromoHolderdata(
                promoCode = promoCode, shopName = "Kode promo", iconType = 1, showShopSection = true
        ), NotEligiblePromoHolderdata(showShopSection = true))), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Validate Use Promo Red State Multiple Voucher`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                ), PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                ))))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(
                NotEligiblePromoHolderdata(showShopSection = true), NotEligiblePromoHolderdata(showShopSection = false)
        )), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Validate Use Promo Red State With Shop Badge`() {
        // Given
        val shopBadge = "shop_badge.png"
        orderSummaryPageViewModel.orderCart = helper.orderData.cart.copy(shop = OrderShop(shopBadge = shopBadge))
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        val response = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "red")
                ))))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        assertEquals(OccGlobalEvent.PromoClashing(arrayListOf(
                NotEligiblePromoHolderdata(showShopSection = true, shopBadge = shopBadge)
        )), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Final Validate Use Promo Error Akamai`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart.copy(shop = OrderShop(isGold = 1))
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        val lastResponse = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "green")
                ))))
        val response = AkamaiErrorException("")
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.error(response)
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = lastResponse
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.get().createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(throwable = response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Cancel Ineligible Promo Checkout Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        every { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.get().createObservable(any()) } returns Observable.just(ClearPromoUiModel())
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isSuccess = false
        orderSummaryPageViewModel.cancelIneligiblePromoCheckout(arrayListOf()) {
            isSuccess = true
        }

        // Then
        assertEquals(true, isSuccess)
    }

    @Test
    fun `Cancel Ineligible Promo Checkout Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { clearCacheAutoApplyStackUseCase.get().setParams(any(), any(), any()) } just Runs
        val response = Throwable()
        every { clearCacheAutoApplyStackUseCase.get().createObservable(any()) } returns Observable.error(response)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isSuccess = false
        orderSummaryPageViewModel.cancelIneligiblePromoCheckout(arrayListOf()) {
            isSuccess = true
        }

        // Then
        assertEquals(false, isSuccess)
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }
}