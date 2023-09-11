package com.tokopedia.oneclickcheckout.order.view.processor

import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiExternalUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccCartRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccProfileRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest.Companion.SOURCE_UPDATE_QTY_NOTES
import com.tokopedia.oneclickcheckout.order.domain.GetOccCartUseCase
import com.tokopedia.oneclickcheckout.order.domain.UpdateCartOccUseCase
import com.tokopedia.oneclickcheckout.order.view.mapper.PrescriptionMapper
import com.tokopedia.oneclickcheckout.order.view.mapper.SaveAddOnStateMapper.generateSaveAddOnStateRequestParams
import com.tokopedia.oneclickcheckout.order.view.mapper.SaveAddOnStateMapper.generateSaveAllAddOnsStateRequestParams
import com.tokopedia.oneclickcheckout.order.view.model.AddressState
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt
import com.tokopedia.oneclickcheckout.order.view.model.OccToasterAction
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPreference
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.SaveAddOnState
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EpharmacyPrescriptionDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.ImageUploadDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCaseCoroutine
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderSummaryPageCartProcessor @Inject constructor(
    private val atcOccMultiExternalUseCase: Lazy<AddToCartOccMultiExternalUseCase>,
    private val getOccCartUseCase: GetOccCartUseCase,
    private val updateCartOccUseCase: UpdateCartOccUseCase,
    private val getPrescriptionIdsUseCase: GetPrescriptionIdsUseCaseCoroutine,
    private val saveAddOnStateUseCase: SaveAddOnStateUseCase,
    private val executorDispatchers: CoroutineDispatchers
) {
    suspend fun atcOcc(productIds: String, userId: String): OccGlobalEvent {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val listProductId = productIds.split(",").filter { it.isNotBlank() }
                val response = atcOccMultiExternalUseCase.get().setParams(listProductId, userId).executeOnBackground()
                if (response.isStatusError()) {
                    return@withContext OccGlobalEvent.AtcError(
                        errorMessage = response.getAtcErrorMessage()
                            ?: ""
                    )
                }
                return@withContext OccGlobalEvent.AtcSuccess(
                    response.data.message.firstOrNull()
                        ?: ""
                )
            } catch (t: Throwable) {
                return@withContext OccGlobalEvent.AtcError(t.cause ?: t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    suspend fun getOccCart(
        source: String,
        gatewayCode: String,
        tenor: Int,
        isCartReimagine: Boolean
    ): ResultGetOccCart {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val orderData = getOccCartUseCase
                    .executeSuspend(getOccCartUseCase.createRequestParams(source, gatewayCode, tenor, isCartReimagine))
                return@withContext ResultGetOccCart(
                    orderCart = orderData.cart,
                    orderPreference = OrderPreference(orderData.ticker, orderData.onboarding, orderData.preference.isValidProfile),
                    orderProfile = orderData.preference,
                    orderPayment = orderData.payment,
                    orderPromo = orderData.promo.copy(state = OccButtonState.NORMAL),
                    globalEvent = when {
                        orderData.prompt.shouldShowPrompt() -> {
                            OccGlobalEvent.Prompt(orderData.prompt)
                        }
                        orderData.popUp.isNeedToShowPopUp() -> {
                            OccGlobalEvent.PopUp(orderData.popUp)
                        }
                        else -> null
                    },
                    throwable = null,
                    addressState = AddressState(orderData.errorCode, orderData.preference.address, orderData.popUpMessage),
                    profileCode = orderData.profileCode,
                    imageUpload = orderData.imageUpload
                )
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext ResultGetOccCart(
                    orderCart = OrderCart(),
                    orderPreference = OrderPreference(),
                    orderProfile = OrderProfile(),
                    orderPayment = OrderPayment(),
                    orderPromo = OrderPromo(),
                    globalEvent = null,
                    throwable = t,
                    addressState = AddressState(),
                    profileCode = "",
                    imageUpload = ImageUploadDataModel()
                )
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    suspend fun getPrescriptionId(checkoutId: String): EpharmacyPrescriptionDataModel {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val prescriptionIds =
                    getPrescriptionIdsUseCase.setParams(checkoutId, GetPrescriptionIdsUseCaseCoroutine.SOURCE_OCC).executeOnBackground()
                return@withContext PrescriptionMapper.mapPrescriptionResponse(prescriptionIds)
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext EpharmacyPrescriptionDataModel()
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    fun isOrderNormal(orderCart: OrderCart): Boolean {
        return !orderCart.shop.isError && orderCart.products.any { !it.isError }
    }

    fun generateUpdateCartParam(orderCart: OrderCart, orderProfile: OrderProfile, orderShipment: OrderShipment, orderPayment: OrderPayment): UpdateCartOccRequest? {
        if (orderProfile.isValidProfile && orderCart.products.isNotEmpty()) {
            val cart = ArrayList<UpdateCartOccCartRequest>()
            orderCart.products.forEach {
                if (!it.isError) {
                    cart.add(
                        UpdateCartOccCartRequest(
                            it.cartId,
                            it.orderQuantity,
                            it.notes,
                            it.productId
                        )
                    )
                }
            }
            var metadata = orderProfile.payment.metadata
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
            val selectedGoCicilTerm = orderPayment.walletData.goCicilData.selectedTerm
            val profile = UpdateCartOccProfileRequest(
                gatewayCode = orderProfile.payment.gatewayCode,
                metadata = metadata,
                addressId = orderProfile.address.addressId,
                serviceId = if (realServiceId == 0) orderProfile.shipment.serviceId.toIntOrZero() else realServiceId,
                shippingId = orderShipment.getRealShipperId().toString(),
                spId = orderShipment.getRealShipperProductId().toString(),
                isFreeShippingSelected = orderShipment.isApplyLogisticPromo && orderShipment.logisticPromoShipping != null && orderShipment.logisticPromoViewModel != null,
                tenureType = selectedGoCicilTerm?.installmentTerm ?: 0,
                optionId = selectedGoCicilTerm?.optionId ?: ""
            )
            return UpdateCartOccRequest(cart, profile)
        }
        return null
    }

    internal fun shouldSkipShippingValidationWhenUpdateCart(orderShipment: OrderShipment): Boolean {
        return (orderShipment.getRealShipperId() <= 0 || orderShipment.getRealShipperProductId() <= 0)
    }

    suspend fun updateCartIgnoreResult(orderCart: OrderCart, orderProfile: OrderProfile, orderShipment: OrderShipment, orderPayment: OrderPayment) {
        withContext(executorDispatchers.io) {
            try {
                val param = generateUpdateCartParam(orderCart, orderProfile, orderShipment, orderPayment)?.copy(
                    skipShippingValidation = shouldSkipShippingValidationWhenUpdateCart(orderShipment),
                    source = SOURCE_UPDATE_QTY_NOTES
                )
                if (param != null) {
                    // ignore result
                    updateCartOccUseCase.executeSuspend(param)
                }
            } catch (t: Throwable) {
                // ignore throwable
            }
        }
    }

    suspend fun updatePreference(param: UpdateCartOccRequest): Pair<Boolean, OccGlobalEvent> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val uiMessage = updateCartOccUseCase.executeSuspend(param)
                if (uiMessage is OccPrompt) {
                    return@withContext false to OccGlobalEvent.Prompt(uiMessage)
                } else if (uiMessage is OccToasterAction) {
                    return@withContext false to OccGlobalEvent.TriggerRefresh(uiMessage = uiMessage)
                }
                return@withContext true to OccGlobalEvent.TriggerRefresh()
            } catch (t: Throwable) {
                if (t is MessageErrorException) {
                    return@withContext false to OccGlobalEvent.TriggerRefresh(
                        errorMessage = t.message
                            ?: DEFAULT_ERROR_MESSAGE,
                        shouldTriggerAnalytics = true
                    )
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
                val uiMessage = updateCartOccUseCase.executeSuspend(param)
                if (uiMessage is OccPrompt) {
                    return@withContext false to OccGlobalEvent.Prompt(uiMessage)
                } else if (uiMessage is OccToasterAction) {
                    return@withContext false to OccGlobalEvent.TriggerRefresh(uiMessage = uiMessage)
                }
                return@withContext true to OccGlobalEvent.Normal
            } catch (t: Throwable) {
                if (t is MessageErrorException) {
                    return@withContext false to OccGlobalEvent.TriggerRefresh(
                        errorMessage = t.message
                            ?: DEFAULT_ERROR_MESSAGE,
                        shouldTriggerAnalytics = true
                    )
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
                val uiMessage = updateCartOccUseCase.executeSuspend(param)
                if (uiMessage is OccPrompt) {
                    return@withContext false to OccGlobalEvent.Prompt(uiMessage)
                } else if (uiMessage is OccToasterAction) {
                    return@withContext false to OccGlobalEvent.TriggerRefresh(uiMessage = uiMessage)
                }
                return@withContext true to OccGlobalEvent.Loading
            } catch (t: Throwable) {
                if (t is MessageErrorException) {
                    return@withContext false to OccGlobalEvent.TriggerRefresh(
                        errorMessage = t.message
                            ?: DEFAULT_ERROR_MESSAGE,
                        shouldTriggerAnalytics = true
                    )
                }
                return@withContext false to OccGlobalEvent.TriggerRefresh(throwable = t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    suspend fun saveAddOnProductState(
        newAddOnProductData: AddOnsProductDataModel.Data,
        product: OrderProduct
    ): SaveAddOnState {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                saveAddOnStateUseCase.setParams(
                    saveAddOnStateRequest = generateSaveAddOnStateRequestParams(
                        newAddOnProductData = newAddOnProductData,
                        product = product
                    ),
                    isFireAndForget = true
                )
                val response = saveAddOnStateUseCase.executeOnBackground()
                val errorMessage: String = response.saveAddOns.errorMessage.joinToString(separator = ", ")
                return@withContext if (errorMessage.isBlank() && response.saveAddOns.status == STATUS_OK) {
                    SaveAddOnState(
                        isSuccess = true,
                        message = errorMessage
                    )
                } else {
                    SaveAddOnState(
                        isSuccess = false,
                        message = errorMessage
                    )
                }
            } catch (throwable: Throwable) {
                return@withContext SaveAddOnState(
                    isSuccess = false,
                    throwable = throwable
                )
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    suspend fun saveAllAddOnsAllProductsState(
        products: List<OrderProduct>
    ): SaveAddOnState {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                saveAddOnStateUseCase.setParams(
                    saveAddOnStateRequest = generateSaveAllAddOnsStateRequestParams(
                        products = products
                    ),
                    isFireAndForget = false
                )
                val response = saveAddOnStateUseCase.executeOnBackground()
                val errorMessage: String = response.saveAddOns.errorMessage.joinToString(separator = ", ")
                return@withContext if (errorMessage.isBlank() && response.saveAddOns.status == STATUS_OK) {
                    SaveAddOnState(
                        isSuccess = true,
                        message = errorMessage
                    )
                } else {
                    SaveAddOnState(
                        isSuccess = false,
                        message = errorMessage
                    )
                }
            } catch (throwable: Throwable) {
                return@withContext SaveAddOnState(
                    isSuccess = false,
                    throwable = throwable
                )
            }
        }
        OccIdlingResource.decrement()
        return result
    }
}

class ResultGetOccCart(
    val orderCart: OrderCart = OrderCart(),
    val orderPreference: OrderPreference = OrderPreference(),
    val orderProfile: OrderProfile = OrderProfile(),
    val orderPayment: OrderPayment = OrderPayment(),
    val orderPromo: OrderPromo = OrderPromo(),
    val globalEvent: OccGlobalEvent? = null,
    val throwable: Throwable? = null,
    val addressState: AddressState = AddressState(),
    val profileCode: String = "",
    val imageUpload: ImageUploadDataModel = ImageUploadDataModel()
)
