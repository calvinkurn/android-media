package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartDataOcc
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class OrderSummaryPageViewModelPromoTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Update Cart Promo Success Without Promo`() {
        setUpCartAndRates()
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
        setUpCartAndRates()
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
        setUpCartAndRates()
        val response = Throwable()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Validate Use Promo Success`() {

    }

    @Test
    fun `Validate Use Promo Error`() {

    }

    @Test
    fun `Validate Use Promo Red State`() {

    }
}