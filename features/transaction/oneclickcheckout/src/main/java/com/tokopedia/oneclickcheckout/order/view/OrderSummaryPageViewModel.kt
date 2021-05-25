package com.tokopedia.oneclickcheckout.order.view

import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.util.ChosenAddress
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccMutableLiveData
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.AddressModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryPageEnhanceECommerce
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccProfileRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.order.view.processor.*
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderSummaryPageViewModel @Inject constructor(private val executorDispatchers: CoroutineDispatchers,
                                                    private val cartProcessor: OrderSummaryPageCartProcessor,
                                                    private val logisticProcessor: OrderSummaryPageLogisticProcessor,
                                                    private val checkoutProcessor: OrderSummaryPageCheckoutProcessor,
                                                    private val promoProcessor: OrderSummaryPagePromoProcessor,
                                                    private val calculator: OrderSummaryPageCalculator,
                                                    private val userSession: UserSessionInterface,
                                                    private val orderSummaryAnalytics: OrderSummaryAnalytics) : BaseViewModel(executorDispatchers.immediate) {

    var orderCart: OrderCart = OrderCart()
    val orderProduct: OrderProduct
        get() = orderCart.product
    val orderShop: OrderShop
        get() = orderCart.shop

    var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = null
    var lastValidateUsePromoRequest: ValidateUsePromoRequest? = null
    var orderPromo: OccMutableLiveData<OrderPromo> = OccMutableLiveData(OrderPromo())
        private set

    var _orderPreference: OrderPreference = OrderPreference()
    val orderPreference: OccMutableLiveData<OccState<OrderPreference>> = OccMutableLiveData(OccState.Loading)

    var _orderShipment: OrderShipment = OrderShipment()
    val orderShipment: OccMutableLiveData<OrderShipment> = OccMutableLiveData(OrderShipment())

    var _orderPayment: OrderPayment = OrderPayment()
    val orderPayment: OccMutableLiveData<OrderPayment> = OccMutableLiveData(OrderPayment())

    val orderTotal: OccMutableLiveData<OrderTotal> = OccMutableLiveData(OrderTotal())
    val globalEvent: OccMutableLiveData<OccGlobalEvent> = OccMutableLiveData(OccGlobalEvent.Normal)

    val addressState: OccMutableLiveData<AddressState> = OccMutableLiveData(AddressState())

    private var debounceJob: Job? = null
    private var finalUpdateJob: Job? = null
    private var getCartJob: Job? = null

    private var hasSentViewOspEe = false

    var revampData: OccRevampData = OccRevampData()

    fun getCurrentProfileId(): Int {
        return _orderPreference.preference.profileId
    }

    fun getCurrentShipperId(): Int {
        return _orderShipment.getRealShipperId()
    }

    fun getPaymentProfile(): String {
        return orderCart.paymentProfile
    }

    fun atcOcc(productId: String) {
        launch(executorDispatchers.immediate) {
            globalEvent.value = OccGlobalEvent.Loading
            globalEvent.value = cartProcessor.atcOcc(productId, userSession.userId)
        }
    }

    private fun isInvalidAddressState(preference: OrderProfile, addressState: AddressState): Boolean {
        return preference.address.addressId <= 0 && addressState.errorCode != AddressState.ERROR_CODE_OPEN_ANA
    }

    fun getOccCart(isFullRefresh: Boolean, source: String) {
        getCartJob?.cancel()
        getCartJob = launch(executorDispatchers.immediate) {
            globalEvent.value = OccGlobalEvent.Normal
            val result = cartProcessor.getOccCart(source)
            addressState.value = result.addressState
            revampData = result.revampData
            orderCart = result.orderCart
            _orderPreference = result.orderPreference
            orderPreference.value = if (result.throwable == null && !isInvalidAddressState(result.orderPreference.preference, result.addressState)) {
                OccState.FirstLoad(_orderPreference)
            } else {
                OccState.Failed(Failure(result.throwable))
            }
            if (isFullRefresh) {
                _orderShipment = OrderShipment()
                orderShipment.value = _orderShipment
            }
            _orderPayment = result.orderPayment
            orderPayment.value = _orderPayment
            validateUsePromoRevampUiModel = null
            lastValidateUsePromoRequest = null
            orderPromo.value = result.orderPromo
            result.globalEvent?.also {
                globalEvent.value = it
            }
            if (orderProduct.productId > 0 && _orderPreference.preference.address.addressId > 0) {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
                getRatesSuspend()
            } else if (result.throwable == null && !isInvalidAddressState(result.orderPreference.preference, result.addressState)) {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
            }
        }
    }

    fun updateProduct(product: OrderProduct, shouldReloadRates: Boolean = true) {
        orderCart.product = product
        if (shouldReloadRates) {
            if (!product.quantity.isStateError) {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
                debounce()
            } else {
                calculateTotal(forceButtonState = null)
            }
        }
    }

    private fun debounce() {
        debounceJob?.cancel()
        debounceJob = launch(executorDispatchers.immediate) {
            delay(DEBOUNCE_TIME)
            if (isActive) {
                updateCart()
                if (_orderPreference.isValid && _orderPreference.preference.address.addressId > 0) {
                    getRates()
                }
            }
        }
    }

    fun reloadRates() {
        if (_orderPreference.isValid && _orderPreference.preference.address.addressId > 0 && orderTotal.value.buttonState != OccButtonState.LOADING) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            debounceJob?.cancel()
            updateCart()
            getRates()
        }
    }

    fun getRates() {
        launch(executorDispatchers.immediate) {
            getRatesSuspend()
        }
    }

    private suspend fun getRatesSuspend() {
        val result = logisticProcessor.getRates(orderCart, _orderPreference, _orderShipment, generateListShopShipment())
        if (result.clearOldPromoCode.isNotEmpty()) {
            clearOldLogisticPromo(result.clearOldPromoCode)
        }
        if (result.autoApplyPromo != null) {
            autoApplyLogisticPromo(result.autoApplyPromo, result.clearOldPromoCode, result.orderShipment)
            return
        }
        _orderShipment = result.orderShipment
        orderShipment.value = _orderShipment
        sendViewOspEe()
        sendPreselectedCourierOption(result.preselectedSpId)
        if (result.orderShipment.serviceErrorMessage.isNullOrEmpty()) {
            validateUsePromo()
        } else {
            sendViewShippingErrorMessage(result.shippingErrorId)
            calculateTotal(forceButtonState = OccButtonState.DISABLE)
        }
        updateCart()
        configureForceShowOnboarding()
    }

    fun generateShippingParam(): ShippingParam {
        return logisticProcessor.generateShippingParam(orderCart, _orderPreference)
    }

    fun generateListShopShipment(): List<ShopShipment> {
        return orderShop.shopShipment
    }

    private fun sendViewShippingErrorMessage(shippingErrorId: String?) {
        if (shippingErrorId == ERROR_DISTANCE_LIMIT_EXCEEDED) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED)
        } else if (shippingErrorId == ERROR_WEIGHT_LIMIT_EXCEEDED) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED)
        }
    }

    private fun sendPreselectedCourierOption(preselectedSpId: String?) {
        if (preselectedSpId != null) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(preselectedSpId, userSession.userId)
        }
    }

    private fun clearOldLogisticPromo(oldPromoCode: String) {
        launch(executorDispatchers.io) {
            promoProcessor.clearOldLogisticPromo(oldPromoCode)
        }
        val orders = lastValidateUsePromoRequest?.orders ?: emptyList()
        if (orders.isNotEmpty()) {
            orders[0]?.codes?.remove(oldPromoCode)
        }
    }

    private fun autoApplyLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String, shipping: OrderShipment) {
        launch(executorDispatchers.immediate) {
            orderPromo.value = orderPromo.value.copy(state = OccButtonState.LOADING)
            val (isApplied, resultValidateUse, newGlobalEvent) = promoProcessor.validateUseLogisticPromo(generateValidateUsePromoRequestWithBbo(logisticPromoUiModel, oldCode), logisticPromoUiModel.promoCode)
            if (isApplied && resultValidateUse != null) {
                val (newShipment, _) = logisticProcessor.onApplyBbo(shipping, logisticPromoUiModel)
                if (newShipment != null) {
                    _orderShipment = newShipment
                    orderShipment.value = _orderShipment
                    validateUsePromoRevampUiModel = resultValidateUse
                    globalEvent.value = OccGlobalEvent.Normal
                    updatePromoState(resultValidateUse.promoUiModel)
                    updateCart()
                    return@launch
                }
            }
            _orderShipment = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromoUiModel.title}" else null, isApplyLogisticPromo = false, logisticPromoShipping = null)
            orderShipment.value = _orderShipment
            if (resultValidateUse != null) {
                validateUsePromoRevampUiModel = resultValidateUse
                globalEvent.value = OccGlobalEvent.Normal
                updatePromoState(resultValidateUse.promoUiModel)
                updateCart()
                return@launch
            }
            clearAllPromoFromLastRequest()
            calculateTotal(forceButtonState = OccButtonState.NORMAL)
            globalEvent.value = newGlobalEvent
            updateCart()
        }
    }

    fun clearBboIfExist() {
        val logisticPromoViewModel = _orderShipment.logisticPromoViewModel
        if (logisticPromoViewModel != null && _orderShipment.isApplyLogisticPromo && _orderShipment.logisticPromoShipping != null) {
            clearOldLogisticPromo(logisticPromoViewModel.promoCode)
        }
    }

    private fun resetBbo() {
        _orderShipment = logisticProcessor.resetBbo(_orderShipment)
        orderShipment.value = _orderShipment
    }

    fun chooseCourier(chosenShippingCourierViewModel: ShippingCourierUiModel) {
        val newOrderShipment = logisticProcessor.chooseCourier(chosenShippingCourierViewModel, _orderShipment)
        newOrderShipment?.let {
            clearBboIfExist()
            _orderShipment = it
            orderShipment.value = _orderShipment
            validateUsePromo()
            updateCart()
        }
    }

    fun setInsuranceCheck(checked: Boolean) {
        if (_orderShipment.getRealShipperProductId() > 0 && _orderShipment.isCheckInsurance != checked) {
            _orderShipment = _orderShipment.copy(isCheckInsurance = checked)
            calculateTotal(forceButtonState = null)
        }
    }

    fun chooseDuration(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
        val newOrderShipment = logisticProcessor.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint, _orderShipment)
        newOrderShipment?.let {
            clearBboIfExist()
            _orderShipment = it
            orderShipment.value = _orderShipment
            sendPreselectedCourierOption(selectedShippingCourierUiModel.productData.shipperProductId.toString())
            if (it.serviceErrorMessage.isNullOrEmpty()) {
                validateUsePromo()
                updateCart()
            } else {
                calculateTotal(forceButtonState = OccButtonState.DISABLE)
            }
        }
    }

    fun changePinpoint() {
        if (_orderShipment.needPinpoint) {
            _orderShipment = _orderShipment.copy(needPinpoint = false)
        }
    }

    fun savePinpoint(longitude: String, latitude: String) {
        launch(executorDispatchers.immediate) {
            if (getCartJob?.isCompleted == false) {
                getCartJob?.join()
                if (orderPreference.value !is OccState.FirstLoad) {
                    return@launch
                }
            }
            val op = _orderPreference
            if (!op.isValid) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            val result = logisticProcessor.savePinpoint(op.preference.address, longitude, latitude, userSession.userId, userSession.deviceId)
            globalEvent.value = result
        }
    }

    fun chooseLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel) {
        launch(executorDispatchers.immediate) {
            val shipping = _orderShipment
            val shippingRecommendationData = _orderShipment.shippingRecommendationData
            if (shippingRecommendationData != null) {
                globalEvent.value = OccGlobalEvent.Loading
                val (isApplied, resultValidateUse, newGlobalEvent) = promoProcessor.validateUseLogisticPromo(generateValidateUsePromoRequestWithBbo(logisticPromoUiModel), logisticPromoUiModel.promoCode)
                if (isApplied && resultValidateUse != null) {
                    val (newShipment, newEvent) = logisticProcessor.onApplyBbo(shipping, logisticPromoUiModel)
                    if (newShipment != null) {
                        _orderShipment = newShipment
                        orderShipment.value = _orderShipment
                    }
                    validateUsePromoRevampUiModel = resultValidateUse
                    updatePromoState(resultValidateUse.promoUiModel)
                    globalEvent.value = newEvent
                    updateCart()
                    return@launch
                }
                if (resultValidateUse != null) {
                    validateUsePromoRevampUiModel = resultValidateUse
                } else {
                    clearAllPromoFromLastRequest()
                    calculateTotal(forceButtonState = OccButtonState.NORMAL)
                }
                globalEvent.value = newGlobalEvent
                updateCart()
            }
        }
    }

    fun chooseAddress(addressModel: RecipientAddressModel) {
        launch(executorDispatchers.immediate) {
            var param = generateUpdateCartParam()
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            param = param.copy(profile = param.profile.copy(
                    addressId = addressModel.id
            ), skipShippingValidation = shouldSkipShippingValidationWhenUpdateCart())
            val chosenAddress = ChosenAddress(
                    addressId = addressModel.id,
                    districtId = addressModel.destinationDistrictId,
                    postalCode = addressModel.postalCode,
                    geolocation = if (addressModel.latitude.isNotBlank() && addressModel.longitude.isNotBlank()) addressModel.latitude + "," + addressModel.longitude else "",
                    mode = ChosenAddress.MODE_ADDRESS
            )
            param.chosenAddress = chosenAddress
            globalEvent.value = OccGlobalEvent.Loading
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
                globalEvent.value = OccGlobalEvent.UpdateLocalCacheAddress(AddressModel(
                        addressId = addressModel.id.toLongOrZero(),
                        cityId = addressModel.cityId.toLongOrZero(),
                        districtId = addressModel.destinationDistrictId.toLongOrZero(),
                        latitude = addressModel.latitude,
                        longitude = addressModel.longitude,
                        addressName = addressModel.addressName,
                        receiverName = addressModel.recipientName,
                        postalCode = addressModel.postalCode)
                )
                clearBboIfExist()
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun updateCart() {
        launch(executorDispatchers.immediate) {
            cartProcessor.updateCartIgnoreResult(orderCart, _orderPreference, _orderShipment, _orderPayment)
        }
    }

    fun generateUpdateCartParam(): UpdateCartOccRequest? {
        return cartProcessor.generateUpdateCartParam(orderCart, _orderPreference, _orderShipment, _orderPayment)
    }

    private fun shouldSkipShippingValidationWhenUpdateCart(): Boolean {
        return cartProcessor.shouldSkipShippingValidationWhenUpdateCart(_orderShipment)
    }

    fun updatePreference(preference: ProfilesItemModel) {
        launch(executorDispatchers.immediate) {
            var param = generateUpdateCartParam()
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            val recommendedShipping = logisticProcessor.getRecommendedShipmentFromServiceId(_orderShipment, preference.shipmentModel.serviceId)
            val cart = param.cart.first().copy(shippingId = recommendedShipping?.first
                    ?: 0, spId = recommendedShipping?.second ?: 0)
            param = param.copy(
                    cart = arrayListOf(cart),
                    profile = UpdateCartOccProfileRequest(
                            profileId = preference.profileId.toString(),
                            addressId = preference.addressModel.addressId.toString(),
                            serviceId = preference.shipmentModel.serviceId,
                            gatewayCode = preference.paymentModel.gatewayCode,
                            metadata = preference.paymentModel.metadata
                    ))
            val chosenAddress = ChosenAddress(
                    addressId = preference.addressModel.addressId.toString(),
                    districtId = preference.addressModel.districtId.toString(),
                    postalCode = preference.addressModel.postalCode,
                    geolocation = if (preference.addressModel.latitude.isNotBlank() && preference.addressModel.longitude.isNotBlank()) {
                        preference.addressModel.latitude + "," + preference.addressModel.longitude
                    } else "",
                    mode = ChosenAddress.MODE_ADDRESS
            )
            param.chosenAddress = chosenAddress
            globalEvent.value = OccGlobalEvent.Loading
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
                globalEvent.value = OccGlobalEvent.UpdateLocalCacheAddress(preference.addressModel)
                clearBboIfExist()
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun finalUpdate(onSuccessCheckout: (CheckoutOccResult) -> Unit, skipCheckIneligiblePromo: Boolean) {
        if (orderTotal.value.buttonState == OccButtonState.NORMAL) {
            globalEvent.value = OccGlobalEvent.Loading
            val product = orderProduct
            val shop = orderShop
            val pref = _orderPreference
            if (pref.isValid && _orderShipment.getRealShipperProductId() > 0) {
                val param = generateUpdateCartParam()
                if (param != null) {
                    if (validateSelectedTerm()) {
                        finalUpdateJob?.cancel()
                        finalUpdateJob = launch(executorDispatchers.immediate) {
                            val (isSuccess, errorGlobalEvent) = cartProcessor.finalUpdateCart(param)
                            if (isSuccess) {
                                finalValidateUse(product, shop, pref, onSuccessCheckout, skipCheckIneligiblePromo)
                                return@launch
                            }
                            globalEvent.value = errorGlobalEvent
                        }
                    }
                    return
                }
            }
            globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
        }
    }

    private fun finalValidateUse(product: OrderProduct, shop: OrderShop, pref: OrderPreference, onSuccessCheckout: (CheckoutOccResult) -> Unit, skipCheckIneligiblePromo: Boolean) {
        val validateUsePromoRequest = generateValidateUsePromoRequest()
        if (!skipCheckIneligiblePromo && promoProcessor.hasPromo(validateUsePromoRequest)) {
            launch(executorDispatchers.immediate) {
                val (resultValidateUse, isSuccess, newGlobalEvent) = promoProcessor.finalValidateUse(validateUsePromoRequest, orderCart)
                if (resultValidateUse != null) {
                    validateUsePromoRevampUiModel = resultValidateUse
                    updatePromoState(resultValidateUse.promoUiModel)
                    if (isSuccess) {
                        doCheckout(product, shop, pref, onSuccessCheckout)
                        return@launch
                    }
                }
                globalEvent.value = newGlobalEvent
            }
        } else {
            doCheckout(product, shop, pref, onSuccessCheckout)
        }
    }

    private fun doCheckout(product: OrderProduct, shop: OrderShop, pref: OrderPreference, onSuccessCheckout: (CheckoutOccResult) -> Unit) {
        launch(executorDispatchers.immediate) {
            val (checkoutOccResult, globalEventResult) = checkoutProcessor.doCheckout(validateUsePromoRevampUiModel, orderCart, product, shop, pref, _orderShipment, orderTotal.value, userSession.userId, generateOspEeBody(emptyList()))
            if (checkoutOccResult != null) {
                onSuccessCheckout(checkoutOccResult)
            } else if (globalEventResult != null) {
                globalEvent.value = globalEventResult
            }
        }
    }

    fun cancelIneligiblePromoCheckout(notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>, onSuccessCheckout: (CheckoutOccResult) -> Unit) {
        globalEvent.value = OccGlobalEvent.Loading
        launch(executorDispatchers.immediate) {
            val (isSuccess, newGlobalEvent) = promoProcessor.cancelIneligiblePromoCheckout(ArrayList(notEligiblePromoHolderdataList.map { it.promoCode }))
            if (isSuccess && _orderPreference.isValid) {
                finalUpdate(onSuccessCheckout, true)
                return@launch
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun updateCartPromo(onSuccess: (ValidateUsePromoRequest, PromoRequest, ArrayList<String>) -> Unit) {
        launch(executorDispatchers.immediate) {
            val param = generateUpdateCartParam()
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            globalEvent.value = OccGlobalEvent.Loading
            val (isSuccess, newGlobalEvent) = cartProcessor.updateCartPromo(param)
            globalEvent.value = newGlobalEvent
            if (isSuccess) {
                onSuccess(generateValidateUsePromoRequest(), generatePromoRequest(), generateBboPromoCodes())
            }
        }
    }

    fun generatePromoRequest(): PromoRequest {
        return promoProcessor.generatePromoRequest(orderCart, _orderShipment, lastValidateUsePromoRequest, orderPromo.value)
    }

    fun generateValidateUsePromoRequest(shouldAddLogisticPromo: Boolean = true): ValidateUsePromoRequest {
        val validateUsePromoRequest = promoProcessor.generateValidateUsePromoRequest(shouldAddLogisticPromo, lastValidateUsePromoRequest, orderCart, _orderShipment, orderPromo.value)
        lastValidateUsePromoRequest = validateUsePromoRequest
        return validateUsePromoRequest
    }

    private fun generateValidateUsePromoRequestWithBbo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String? = null): ValidateUsePromoRequest {
        val validateUsePromoRequest = promoProcessor.generateValidateUsePromoRequestWithBbo(logisticPromoUiModel, oldCode, lastValidateUsePromoRequest, orderCart, _orderShipment, orderPromo.value)
        lastValidateUsePromoRequest = validateUsePromoRequest
        return validateUsePromoRequest
    }

    fun generateBboPromoCodes(): ArrayList<String> {
        return promoProcessor.generateBboPromoCodes(_orderShipment)
    }

    fun validateUsePromo() {
        launch(executorDispatchers.immediate) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            orderPromo.value = orderPromo.value.copy(state = OccButtonState.LOADING)
            val (error, resultValidateUse, isAkamaiError) = promoProcessor.validateUsePromo(generateValidateUsePromoRequest(), validateUsePromoRevampUiModel)
            when {
                error != null && isAkamaiError -> {
                    resetBbo()
                    clearAllPromoFromLastRequest()
                    calculateTotal(forceButtonState = OccButtonState.NORMAL)
                    globalEvent.value = OccGlobalEvent.Error(error)
                }
                error != null && !isAkamaiError -> {
                    orderPromo.value = orderPromo.value.copy(state = OccButtonState.DISABLE)
                    orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                    globalEvent.value = OccGlobalEvent.Error(error)
                }
                resultValidateUse != null -> {
                    validateUsePromoRevampUiModel = resultValidateUse
                    updatePromoState(resultValidateUse.promoUiModel)
                }
                else -> {
                    validateUsePromoRevampUiModel = null
                    orderPromo.value = orderPromo.value.copy(state = OccButtonState.NORMAL)
                    calculateTotal(forceButtonState = OccButtonState.NORMAL)
                }
            }
        }
    }

    private fun clearAllPromoFromLastRequest() {
        validateUsePromoRevampUiModel = null
        val orders = lastValidateUsePromoRequest?.orders ?: emptyList()
        if (orders.isNotEmpty()) {
            orders[0]?.codes?.clear()
        }
        lastValidateUsePromoRequest?.codes?.clear()
        orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
    }

    fun updatePromoState(promoUiModel: PromoUiModel) {
        orderPromo.value = orderPromo.value.copy(lastApply = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel), state = OccButtonState.NORMAL)
        calculateTotal(forceButtonState = OccButtonState.NORMAL)
    }

    fun calculateTotal(forceButtonState: OccButtonState? = null) {
        launch(executorDispatchers.immediate) {
            val (newOrderPayment, newOrderTotal) = calculator.calculateTotal(orderCart, _orderPreference,
                    _orderShipment, validateUsePromoRevampUiModel, _orderPayment, orderTotal.value,
                    forceButtonState, orderPromo.value)
            _orderPayment = newOrderPayment
            orderPayment.value = _orderPayment
            orderTotal.value = newOrderTotal
        }
    }

    fun chooseInstallment(selectedInstallmentTerm: OrderPaymentInstallmentTerm) {
        launch(executorDispatchers.immediate) {
            var param = generateUpdateCartParam()
            val creditCard = _orderPayment.creditCard
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            globalEvent.value = OccGlobalEvent.Loading
            try {
                val metadata = JsonParser().parse(param.profile.metadata)
                val expressCheckoutParams = metadata.asJsonObject.getAsJsonObject(UpdateCartOccProfileRequest.EXPRESS_CHECKOUT_PARAM)
                if (expressCheckoutParams.get(UpdateCartOccProfileRequest.INSTALLMENT_TERM) == null) {
                    // unexpected null installment term param
                    throw Exception()
                }
                expressCheckoutParams.addProperty(UpdateCartOccProfileRequest.INSTALLMENT_TERM, selectedInstallmentTerm.term.toString())
                param = param.copy(profile = param.profile.copy(metadata = metadata.toString()),
                        skipShippingValidation = shouldSkipShippingValidationWhenUpdateCart())
            } catch (e: Exception) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
                val availableTerms = creditCard.availableTerms
                availableTerms.forEach {
                    it.isSelected = it.term == selectedInstallmentTerm.term
                    it.isError = false
                }
                _orderPayment = _orderPayment.copy(creditCard = creditCard.copy(selectedTerm = selectedInstallmentTerm, availableTerms = availableTerms))
                calculateTotal(forceButtonState = OccButtonState.NORMAL)
                globalEvent.value = OccGlobalEvent.Normal
                return@launch
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun choosePayment(gatewayCode: String, metadata: String) {
        launch(executorDispatchers.immediate) {
            if (getCartJob?.isCompleted == false) {
                getCartJob?.join()
                if (orderPreference.value !is OccState.FirstLoad) {
                    return@launch
                }
            }
            var param = generateUpdateCartParam()
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            param = param.copy(profile = param.profile.copy(gatewayCode = gatewayCode, metadata = metadata),
                    skipShippingValidation = shouldSkipShippingValidationWhenUpdateCart())
            globalEvent.value = OccGlobalEvent.Loading
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
                clearBboIfExist()
            }
            globalEvent.value = newGlobalEvent
        }
    }

    private fun validateSelectedTerm(): Boolean {
        val creditCard = _orderPayment.creditCard
        val selectedTerm = creditCard.selectedTerm
        val hasEnableTerm = creditCard.availableTerms.indexOfFirst { it.isEnable } > -1
        if (selectedTerm != null && !selectedTerm.isEnable && hasEnableTerm) {
            val availableTerms = creditCard.availableTerms
            availableTerms.forEach { it.isError = true }
            selectedTerm.isError = true
            _orderPayment = _orderPayment.copy(creditCard = creditCard.copy(selectedTerm = selectedTerm, availableTerms = availableTerms))
            orderPayment.value = _orderPayment
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
            globalEvent.value = OccGlobalEvent.Error(errorMessage = INSTALLMENT_INVALID_MIN_AMOUNT)
            return false
        }
        return true
    }

    override fun onCleared() {
        debounceJob?.cancel()
        finalUpdateJob?.cancel()
        getCartJob?.cancel()
        super.onCleared()
    }

    private fun sendViewOspEe() {
        if (!hasSentViewOspEe) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(userSession.userId, _orderPreference.preference.payment.gatewayName, generateOspEeBody().build(OrderSummaryPageEnhanceECommerce.STEP_1, OrderSummaryPageEnhanceECommerce.STEP_1_OPTION))
            if (orderProduct.purchaseProtectionPlanData.isProtectionAvailable) {
                orderSummaryAnalytics.eventPPImpressionOnInsuranceSection(userSession.userId, orderProduct.categoryId, "", orderProduct.purchaseProtectionPlanData.protectionTitle)
            }
            hasSentViewOspEe = true
        }
    }

    private fun generateOspEeBody(promoCodes: List<String> = emptyList()): OrderSummaryPageEnhanceECommerce {
        return OrderSummaryPageEnhanceECommerce().apply {
            setName(orderProduct.productName)
            setId(orderProduct.productId.toString())
            setPrice(orderProduct.productPrice.toString())
            setBrand(null)
            setCategory(orderProduct.category)
            setVariant(null)
            setQuantity(orderProduct.quantity.orderQuantity.toString())
            setListName(orderProduct.productTrackerData.trackerListName)
            setAttribution(orderProduct.productTrackerData.attribution)
            setDiscountedPrice(orderProduct.isSlashPrice)
            setWarehouseId(orderProduct.warehouseId.toString())
            setProductWeight(orderProduct.weight.toString())
            setPromoCode(promoCodes)
            setPromoDetails("")
            setProductType("")
            setCartId(orderCart.cartId)
            setBuyerAddressId(_orderPreference.preference.address.addressId.toString())
            setSpid(_orderShipment.getRealShipperProductId().toString())
            setCodFlag(false)
            setCornerFlag(false)
            setIsFullfilment(orderShop.isFulfillment)
            setShopIdDimension(orderShop.shopId.toString())
            setShopNameDimension(orderShop.shopName)
            setShopTypeDimension(orderShop.isOfficial, orderShop.isGold)
            setCategoryId(orderProduct.categoryId)
            if (_orderShipment.getRealShipperProductId() > 0) {
                setShippingPrice(_orderShipment.getRealShippingPrice().toString())
            } else {
                setShippingPrice("")
            }
            setShippingDuration(_orderShipment.serviceDuration)
            setCampaignId(orderProduct.campaignId)
        }
    }

    fun consumeForceShowOnboarding() {
        val onboarding = _orderPreference.onboarding
        if (onboarding.isForceShowCoachMark) {
            _orderPreference = _orderPreference.copy(onboarding = onboarding.copy(isForceShowCoachMark = false))
            globalEvent.value = OccGlobalEvent.Normal
        }
    }

    private fun configureForceShowOnboarding() {
        val onboarding = _orderPreference.onboarding
        if (onboarding.isForceShowCoachMark) {
            val preference = _orderPreference.preference
            if (preference.address.addressId > 0 && _orderShipment.isValid()) {
                forceShowOnboarding(onboarding)
            }
        }
    }

    private fun forceShowOnboarding(onboarding: OccMainOnboarding) {
        val currentGlobalEvent = globalEvent.value
        val hasBlockingGlobalEvent = currentGlobalEvent is OccGlobalEvent.Loading || currentGlobalEvent is OccGlobalEvent.Prompt
        if (!hasBlockingGlobalEvent) {
            globalEvent.value = OccGlobalEvent.ForceOnboarding(onboarding)
        }
    }

    companion object {
        const val DEBOUNCE_TIME = 1000L

        const val FAIL_GET_RATES_ERROR_MESSAGE = "Gagal menampilkan pengiriman"
        const val NO_COURIER_SUPPORTED_ERROR_MESSAGE = "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda."
        const val NO_DURATION_AVAILABLE = "Durasi pengiriman tidak tersedia"
        const val NEED_PINPOINT_ERROR_MESSAGE = "Butuh pinpoint lokasi"

        const val FAIL_APPLY_BBO_ERROR_MESSAGE = "Gagal mengaplikasikan bebas ongkir"

        const val ERROR_CODE_PRICE_CHANGE = "513"
        const val PRICE_CHANGE_ERROR_MESSAGE = "Harga telah berubah"
        const val PRICE_CHANGE_ACTION_MESSAGE = "Cek Belanjaan"

        const val OVO_GATEWAY_CODE = "OVO"

        const val MINIMUM_AMOUNT_ERROR_MESSAGE = "Belanjaanmu kurang dari min. transaksi"
        const val MAXIMUM_AMOUNT_ERROR_MESSAGE = "Belanjaanmu melebihi limit transaksi"

        const val CHANGE_PAYMENT_METHOD_MESSAGE = "Ubah"

        const val INSTALLMENT_INVALID_MIN_AMOUNT = "Oops, tidak bisa bayar dengan cicilan karena min. pembeliannya kurang."

        const val TRANSACTION_ID_KEY = "transaction_id"
    }
}