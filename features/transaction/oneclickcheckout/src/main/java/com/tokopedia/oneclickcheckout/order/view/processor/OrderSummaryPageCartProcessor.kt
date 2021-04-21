package com.tokopedia.oneclickcheckout.order.view.processor

import com.google.gson.JsonParser
import com.tokopedia.atc_common.domain.usecase.AddToCartOccExternalUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccCartRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccProfileRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.domain.GetOccCartUseCase
import com.tokopedia.oneclickcheckout.order.domain.UpdateCartOccUseCase
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.usecase.RequestParams
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderSummaryPageCartProcessor @Inject constructor(private val atcOccExternalUseCase: Lazy<AddToCartOccExternalUseCase>,
                                                        private val getOccCartUseCase: GetOccCartUseCase,
                                                        private val updateCartOccUseCase: UpdateCartOccUseCase,
                                                        private val executorDispatchers: CoroutineDispatchers) {

    suspend fun atcOcc(productId: String, userId: String): OccGlobalEvent {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val response = atcOccExternalUseCase.get().createObservable(
                        RequestParams().apply {
                            putString(AddToCartOccExternalUseCase.REQUEST_PARAM_KEY_PRODUCT_ID, productId)
                            putString(AddToCartOccExternalUseCase.REQUEST_PARAM_KEY_USER_ID, userId)
                        }).toBlocking().single()
                if (response.isDataError()) {
                    return@withContext OccGlobalEvent.AtcError(errorMessage = response.getAtcErrorMessage()
                            ?: "")
                }
                return@withContext OccGlobalEvent.AtcSuccess(response.data.message.firstOrNull()
                        ?: "")
            } catch (t: Throwable) {
                return@withContext OccGlobalEvent.AtcError(t.cause ?: t)
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
                return@withContext ResultGetOccCart(
                        orderCart = orderData.cart,
                        orderPreference = OrderPreference(orderData.ticker, orderData.onboarding, orderData.profileIndex,
                                orderData.profileRecommendation, orderData.preference, true, orderData.removeProfileData),
                        orderPayment = orderData.payment,
                        orderPromo = orderData.promo.copy(state = OccButtonState.NORMAL),
                        globalEvent = if (orderData.prompt.shouldShowPrompt()) OccGlobalEvent.Prompt(orderData.prompt) else null,
                        throwable = null,
                        revampData = orderData.revampData,
                        addressState = AddressState(orderData.errorCode, orderData.preference.address.state, orderData.popUpMessage)
                )
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext ResultGetOccCart(
                        orderCart = OrderCart(),
                        orderPreference = OrderPreference(),
                        orderPayment = OrderPayment(),
                        orderPromo = OrderPromo(),
                        globalEvent = null,
                        throwable = t,
                        revampData = OccRevampData(),
                        addressState = AddressState()
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
                    orderShipment.getRealShipperProductId(),
                    orderShipment.isApplyLogisticPromo && orderShipment.logisticPromoShipping != null && orderShipment.logisticPromoViewModel != null
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
            val realServiceId = orderShipment.getRealServiceId()
            val profile = UpdateCartOccProfileRequest(
                    orderPreference.preference.profileId.toString(),
                    orderPreference.preference.payment.gatewayCode,
                    metadata,
                    if (realServiceId == 0) orderPreference.preference.shipment.serviceId else realServiceId,
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
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val prompt = updateCartOccUseCase.executeSuspend(param)
                if (prompt != null) {
                    return@withContext false to OccGlobalEvent.Prompt(prompt)
                }
                return@withContext true to OccGlobalEvent.TriggerRefresh()
            } catch (t: Throwable) {
                if (t is MessageErrorException) {
                    return@withContext false to OccGlobalEvent.Error(errorMessage = t.message
                            ?: DEFAULT_ERROR_MESSAGE)
                }
                return@withContext false to OccGlobalEvent.Error(t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    suspend fun updateCartPromo(param: UpdateCartOccRequest): Pair<Boolean, OccGlobalEvent> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val prompt = updateCartOccUseCase.executeSuspend(param)
                if (prompt != null) {
                    return@withContext false to OccGlobalEvent.Prompt(prompt)
                }
                return@withContext true to OccGlobalEvent.Normal
            } catch (t: Throwable) {
                if (t is MessageErrorException) {
                    return@withContext false to OccGlobalEvent.Error(errorMessage = t.message
                            ?: DEFAULT_ERROR_MESSAGE)
                }
                return@withContext false to OccGlobalEvent.Error(t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    suspend fun finalUpdateCart(param: UpdateCartOccRequest): Pair<Boolean, OccGlobalEvent> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val prompt = updateCartOccUseCase.executeSuspend(param)
                if (prompt != null) {
                    return@withContext false to OccGlobalEvent.Prompt(prompt)
                }
                return@withContext true to OccGlobalEvent.Loading
            } catch (t: Throwable) {
                if (t is MessageErrorException) {
                    return@withContext false to OccGlobalEvent.TriggerRefresh(errorMessage = t.message
                            ?: DEFAULT_ERROR_MESSAGE)
                }
                return@withContext false to OccGlobalEvent.TriggerRefresh(throwable = t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }
}

class ResultGetOccCart(
        var orderCart: OrderCart,
        var orderPreference: OrderPreference,
        var orderPayment: OrderPayment,
        var orderPromo: OrderPromo,
        var globalEvent: OccGlobalEvent?,
        var throwable: Throwable?,
        var revampData: OccRevampData,
        var addressState: AddressState
)