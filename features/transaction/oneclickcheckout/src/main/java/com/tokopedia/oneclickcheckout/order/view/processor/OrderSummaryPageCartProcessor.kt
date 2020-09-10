package com.tokopedia.oneclickcheckout.order.view.processor

import com.google.gson.JsonParser
import com.tokopedia.atc_common.domain.usecase.AddToCartOccExternalUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccCartRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccProfileRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.domain.GetOccCartUseCase
import com.tokopedia.oneclickcheckout.order.domain.UpdateCartOccUseCase
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderSummaryPageCartProcessor @Inject constructor(private val atcOccExternalUseCase: AddToCartOccExternalUseCase,
                                                        private val getOccCartUseCase: GetOccCartUseCase,
                                                        private val updateCartOccUseCase: UpdateCartOccUseCase,
                                                        private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                        private val executorDispatchers: ExecutorDispatchers) {

    suspend fun atcOcc(productId: String): OccGlobalEvent {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val response = atcOccExternalUseCase.createObservable(
                        RequestParams().apply {
                            putString(AddToCartOccExternalUseCase.REQUEST_PARAM_KEY_PRODUCT_ID, productId)
                        }).toBlocking().single()
                if (response.isDataError()) {
                    OccGlobalEvent.AtcError(errorMessage = response.getAtcErrorMessage() ?: "")
                } else {
                    OccGlobalEvent.AtcSuccess(response.data.message.firstOrNull() ?: "")
                }
            } catch (t: Throwable) {
                val cause = t.cause
                if (cause != null) {
                    OccGlobalEvent.AtcError(cause)
                } else {
                    OccGlobalEvent.AtcError(t)
                }
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    suspend fun getOccCart(source: String): ResultGetOccCart {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val orderData = getOccCartUseCase.executeSuspend(getOccCartUseCase.createRequestParams(source))
                ResultGetOccCart(
                        orderCart = orderData.cart,
                        orderPreference = OrderPreference(orderData.ticker, orderData.onboarding, orderData.profileIndex, orderData.profileRecommendation, orderData.preference, true),
                        orderPayment = orderData.payment,
                        orderPromo = orderData.promo.copy(state = OccButtonState.NORMAL),
                        globalEvent = if (orderData.prompt.shouldShowPrompt()) OccGlobalEvent.Prompt(orderData.prompt) else null,
                        throwable = null
                )
            } catch (t: Throwable) {
                Timber.d(t)
                t.printStackTrace()
                ResultGetOccCart(
                        orderCart = OrderCart(),
                        orderPreference = OrderPreference(),
                        orderPayment = OrderPayment(),
                        orderPromo = OrderPromo(),
                        globalEvent = null,
                        throwable = t
                )
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    fun generateUpdateCartParam(orderCart: OrderCart, orderPreference: OrderPreference, orderShipment: OrderShipment, orderPayment: OrderPayment): UpdateCartOccRequest? {
        val orderProduct = orderCart.product
        if (orderPreference.isValid && orderPreference.preference.profileId > 0) {
            val cart = UpdateCartOccCartRequest(
                    orderCart.cartId.toString(),
                    orderProduct.quantity.orderQuantity,
                    orderProduct.notes,
                    orderProduct.productId.toString(),
                    orderShipment.getRealShipperId(),
                    orderShipment.getRealShipperProductId()
            )
            var metadata = orderPreference.preference.payment.metadata
            val selectedTerm = orderPayment.creditCard.selectedTerm
            if (selectedTerm != null) {
                try {
                    val parse = JsonParser().parse(metadata)
                    val expressCheckoutParams = parse.asJsonObject.getAsJsonObject(UpdateCartOccProfileRequest.EXPRESS_CHECKOUT_PARAM)
                    if (expressCheckoutParams.get(UpdateCartOccProfileRequest.INSTALLMENT_TERM) == null) {
                        throw Exception()
                    }
                    expressCheckoutParams.addProperty(UpdateCartOccProfileRequest.INSTALLMENT_TERM, selectedTerm.term.toString())
                    metadata = parse.toString()
                } catch (e: Exception) {
                    return null
                }
            }
            val profile = UpdateCartOccProfileRequest(
                    orderPreference.preference.profileId.toString(),
                    orderPreference.preference.payment.gatewayCode,
                    metadata,
                    orderPreference.preference.shipment.serviceId,
                    orderPreference.preference.address.addressId.toString()
            )
            return UpdateCartOccRequest(arrayListOf(cart), profile)
        }
        return null
    }

    suspend fun updateCartIgnoreResult(orderCart: OrderCart, orderPreference: OrderPreference, orderShipment: OrderShipment, orderPayment: OrderPayment) {
        withContext(executorDispatchers.io) {
            try {
                val param = generateUpdateCartParam(orderCart, orderPreference, orderShipment, orderPayment)
                if (param != null) {
                    // ignore result
                    updateCartOccUseCase.executeSuspend(param)
                }
            } catch (t: Throwable) {
                //ignore throwable
            }
        }
    }

    suspend fun updatePreference(param: UpdateCartOccRequest): Pair<Boolean, OccGlobalEvent> {
        return withContext(executorDispatchers.io) {
            try {
                updateCartOccUseCase.executeSuspend(param)
                return@withContext true to OccGlobalEvent.TriggerRefresh()
            } catch (t: Throwable) {
                if (t is MessageErrorException) {
                    return@withContext false to OccGlobalEvent.Error(errorMessage = t.message
                            ?: DEFAULT_ERROR_MESSAGE)
                }
                return@withContext false to OccGlobalEvent.Error(t)
            }
        }
    }
}

class ResultGetOccCart(
        var orderCart: OrderCart,
        var orderPreference: OrderPreference,
        var orderPayment: OrderPayment,
        var orderPromo: OrderPromo,
        var globalEvent: OccGlobalEvent?,
        var throwable: Throwable?
)