package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.domain.GetOccCartUseCase
import com.tokopedia.oneclickcheckout.order.view.model.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderSummaryPageCartProcessor @Inject constructor(private val getOccCartUseCase: GetOccCartUseCase,
                                                        private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                        private val executorDispatchers: ExecutorDispatchers) {



    suspend fun getOccCart(isFullRefresh: Boolean, source: String, oldOrderTotal: OrderTotal): ResultGetOccCart {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val orderData = getOccCartUseCase.executeSuspend(getOccCartUseCase.createRequestParams(source))
                ResultGetOccCart(
                        orderCart = orderData.cart,
                        orderPreference = OrderPreference(orderData.ticker, orderData.onboarding, orderData.profileIndex, orderData.profileRecommendation, orderData.preference, true),
                        orderShipment = if (isFullRefresh) OrderShipment() else null,
                        orderPayment = orderData.payment,
                        orderPromo = orderData.promo.copy(state = OccButtonState.NORMAL),
                        orderTotal = if (orderData.cart.product.productId > 0 && orderData.preference.shipment.serviceId > 0) {
                            oldOrderTotal.copy(buttonState = OccButtonState.LOADING)
                        } else {
                            oldOrderTotal.copy(buttonState = OccButtonState.DISABLE)
                        },
                        globalEvent = if (orderData.prompt.shouldShowPrompt()) OccGlobalEvent.Prompt(orderData.prompt) else null,
                        throwable = null,
                        reaction = if (orderData.cart.product.productId > 0 && orderData.preference.shipment.serviceId > 0) {
                            CartReaction.GET_RATES
                        } else {
                            CartReaction.SEND_VIEW_OSP
                        }
                )
            } catch (t: Throwable) {
                Timber.d(t)
                t.printStackTrace()
                ResultGetOccCart(
                        orderCart = OrderCart(),
                        orderPreference = OrderPreference(),
                        orderShipment = OrderShipment(),
                        orderPayment = OrderPayment(),
                        orderPromo = OrderPromo(),
                        orderTotal = oldOrderTotal,
                        globalEvent = null,
                        throwable = t,
                        reaction = CartReaction.NOTHING
                )
            }
        }
        OccIdlingResource.decrement()
        return result
    }
}

class ResultGetOccCart(
        var orderCart: OrderCart,
        var orderPreference: OrderPreference,
        var orderShipment: OrderShipment?,
        var orderPayment: OrderPayment,
        var orderPromo: OrderPromo,
        var orderTotal: OrderTotal,
        var globalEvent: OccGlobalEvent?,
        var throwable: Throwable?,
        var reaction: CartReaction
)

enum class CartReaction {
    GET_RATES,
    SEND_VIEW_OSP,
    NOTHING
}