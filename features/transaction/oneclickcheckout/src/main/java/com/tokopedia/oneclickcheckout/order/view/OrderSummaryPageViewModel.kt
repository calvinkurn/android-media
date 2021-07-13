package com.tokopedia.oneclickcheckout.order.view

import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccMutableLiveData
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryPageEnhanceECommerce
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
import kotlinx.coroutines.flow.collect
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

    init {
        initCalculator()
    }

    var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = null
    var lastValidateUsePromoRequest: ValidateUsePromoRequest? = null
    val orderPromo: OccMutableLiveData<OrderPromo> = OccMutableLiveData(OrderPromo())

    var orderCart: OrderCart = OrderCart()
    val orderShop: OccMutableLiveData<OrderShop> = OccMutableLiveData(OrderShop())
    val orderProducts: OccMutableLiveData<List<OrderProduct>> = OccMutableLiveData(emptyList())

    var orderPreferenceData: OrderPreference = OrderPreference()
    val orderPreference: OccMutableLiveData<OccState<OrderPreference>> = OccMutableLiveData(OccState.Loading)

    val orderProfile: OccMutableLiveData<OrderProfile> = OccMutableLiveData(OrderProfile(enable = false))
    val orderShipment: OccMutableLiveData<OrderShipment> = OccMutableLiveData(OrderShipment())
    val orderPayment: OccMutableLiveData<OrderPayment> = OccMutableLiveData(OrderPayment())

    val orderTotal: OccMutableLiveData<OrderTotal> = OccMutableLiveData(OrderTotal())
    val globalEvent: OccMutableLiveData<OccGlobalEvent> = OccMutableLiveData(OccGlobalEvent.Normal)

    val addressState: OccMutableLiveData<AddressState> = OccMutableLiveData(AddressState())

    private var getCartJob: Job? = null
    private var debounceJob: Job? = null
    private var finalUpdateJob: Job? = null

    private var hasSentViewOspEe = false

    fun getPaymentProfile(): String {
        return orderCart.paymentProfile
    }

    fun getActivationData(): OrderPaymentWalletActionData {
        return orderPayment.value.walletData.activation
    }

    fun atcOcc(productId: String) {
        launch(executorDispatchers.immediate) {
            globalEvent.value = OccGlobalEvent.Loading
            globalEvent.value = cartProcessor.atcOcc(productId, userSession.userId)
        }
    }

    private fun isInvalidAddressState(profile: OrderProfile, addressState: AddressState): Boolean {
        return profile.address.addressId <= 0 && addressState.errorCode != AddressState.ERROR_CODE_OPEN_ANA
    }

    fun getOccCart(isFullRefresh: Boolean, source: String) {
        getCartJob?.cancel()
        getCartJob = launch(executorDispatchers.immediate) {
            globalEvent.value = OccGlobalEvent.Normal
            val result = cartProcessor.getOccCart(source)
            addressState.value = result.addressState
            orderCart = result.orderCart
            orderShop.value = orderCart.shop
            orderProducts.value = orderCart.products
            orderProfile.value = result.orderProfile
            orderPreferenceData = result.orderPreference
            orderPreference.value = if (result.throwable == null && !isInvalidAddressState(result.orderProfile, result.addressState)) {
                OccState.FirstLoad(result.orderPreference)
            } else {
                OccState.Failed(Failure(result.throwable))
            }
            if (isFullRefresh) {
                orderShipment.value = OrderShipment()
            }
            orderPayment.value = result.orderPayment
            validateUsePromoRevampUiModel = null
            lastValidateUsePromoRequest = null
            orderPromo.value = result.orderPromo
            result.globalEvent?.also {
                globalEvent.value = it
            }
            if (orderCart.products.isNotEmpty() && result.orderProfile.isValidProfile) {
                if (result.orderProfile.isDisableChangeCourierAndNeedPinpoint()) {
                    orderShipment.value = orderShipment.value.copy(isLoading = false, serviceName = "", needPinpoint = true)
                    orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                } else {
                    orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
                    getRatesSuspend()
                }
            } else {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
            }
        }
    }

    fun updateProduct(product: OrderProduct, productIndex: Int, shouldReloadRates: Boolean = true) {
        orderCart.products[productIndex] = product
        if (shouldReloadRates) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            orderShipment.value = orderShipment.value.copy(isLoading = true)
            debounce()
        }
    }

    private fun debounce() {
        debounceJob?.cancel()
        debounceJob = launch(executorDispatchers.immediate) {
            delay(DEBOUNCE_TIME)
            if (isActive) {
                updateCart()
                if (orderProfile.value.isDisableChangeCourierAndNeedPinpoint()) {
                    orderShipment.value = orderShipment.value.copy(isLoading = false, serviceName = "", needPinpoint = true)
                    orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                } else if (orderProfile.value.isValidProfile) {
                    getRates()
                }
            }
        }
    }

    fun reloadRates() {
        if (orderProfile.value.isValidProfile && orderTotal.value.buttonState != OccButtonState.LOADING) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            debounceJob?.cancel()
            updateCart()
            getRates()
        }
    }

    fun getRates() {
        launch(executorDispatchers.immediate) {
            orderShipment.value = orderShipment.value.copy(isLoading = true)
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            getRatesSuspend()
        }
    }

    private suspend fun getRatesSuspend() {
        val result = if (cartProcessor.isOrderNormal(orderCart)) {
            logisticProcessor.getRates(orderCart, orderProfile.value, orderShipment.value, orderShop.value.shopShipment)
        } else {
            logisticProcessor.generateOrderErrorResultRates(orderProfile.value)
        }
        if (result.clearOldPromoCode.isNotEmpty()) {
            clearOldLogisticPromo(result.clearOldPromoCode)
        }
        if (result.autoApplyPromo != null) {
            autoApplyLogisticPromo(result.autoApplyPromo, result.clearOldPromoCode, result.orderShipment)
            return
        }
        orderShipment.value = result.orderShipment
        sendViewOspEe()
        sendPreselectedCourierOption(result.preselectedSpId)
        if (result.overweight != null) {
            orderShop.value = orderShop.value.copy(overweight = result.overweight)
            orderProfile.value = orderProfile.value.copy(enable = false)
            orderTotal.value = OrderTotal()
            orderPromo.value = orderPromo.value.copy(isDisabled = true)
        } else {
            orderShop.value = orderShop.value.copy(overweight = 0.0)
            orderProfile.value = orderProfile.value.copy(enable = true)
            orderPromo.value = orderPromo.value.copy(isDisabled = result.orderShipment.isDisabled)
            if (result.orderShipment.serviceErrorMessage.isNullOrEmpty()) {
                validateUsePromo()
            } else {
                sendViewShippingErrorMessage(result.shippingErrorId)
                calculateTotal()
            }
        }
        updateCart()
        configureForceShowOnboarding()
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
        promoProcessor.clearOldLogisticPromoFromLastRequest(lastValidateUsePromoRequest, oldPromoCode)
    }

    private fun autoApplyLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String, shipping: OrderShipment) {
        launch(executorDispatchers.immediate) {
            updateCartWithCustomShipment(shipping)
            orderPromo.value = orderPromo.value.copy(state = OccButtonState.LOADING)
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            val (isApplied, resultValidateUse, newGlobalEvent) = promoProcessor.validateUseLogisticPromo(generateValidateUsePromoRequestWithBbo(logisticPromoUiModel, oldCode), logisticPromoUiModel.promoCode)
            if (isApplied && resultValidateUse != null) {
                val (newShipment, _) = logisticProcessor.onApplyBbo(shipping, logisticPromoUiModel)
                if (newShipment != null) {
                    orderShipment.value = newShipment
                    validateUsePromoRevampUiModel = resultValidateUse
                    globalEvent.value = OccGlobalEvent.Normal
                    updatePromoState(resultValidateUse.promoUiModel)
                    updateCart()
                    return@launch
                }
            }
            orderShipment.value = if (orderProfile.value.shipment.isDisableChangeCourier) {
                shipping.copy(serviceErrorMessage = FAIL_GET_RATES_ERROR_MESSAGE, isApplyLogisticPromo = false, logisticPromoShipping = null)
            } else {
                shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromoUiModel.title}" else null, isApplyLogisticPromo = false, logisticPromoShipping = null)
            }
            if (resultValidateUse != null) {
                validateUsePromoRevampUiModel = resultValidateUse
                globalEvent.value = OccGlobalEvent.Normal
                updatePromoState(resultValidateUse.promoUiModel)
                updateCart()
                return@launch
            }
            clearAllPromoFromLastRequest()
            calculateTotal()
            globalEvent.value = newGlobalEvent
            updateCart()
        }
    }

    fun clearBboIfExist() {
        val logisticPromoViewModel = orderShipment.value.logisticPromoViewModel
        if (logisticPromoViewModel != null && orderShipment.value.isApplyLogisticPromo && orderShipment.value.logisticPromoShipping != null) {
            clearOldLogisticPromo(logisticPromoViewModel.promoCode)
        }
    }

    private fun resetBbo() {
        orderShipment.value = logisticProcessor.resetBbo(orderShipment.value)
    }

    fun chooseCourier(chosenShippingCourierViewModel: ShippingCourierUiModel) {
        val newOrderShipment = logisticProcessor.chooseCourier(chosenShippingCourierViewModel, orderShipment.value)
        newOrderShipment?.let {
            clearBboIfExist()
            orderShipment.value = it
            validateUsePromo()
            updateCart()
        }
    }

    fun setInsuranceCheck(checked: Boolean) {
        if (orderShipment.value.getRealShipperProductId() > 0 && orderShipment.value.insurance.isCheckInsurance != checked) {
            orderShipment.value.insurance.isCheckInsurance = checked
            orderShipment.value.insurance.isFirstLoad = false
            calculateTotal()
        }
    }

    fun chooseDuration(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
        val newOrderShipment = logisticProcessor.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint, orderShipment.value)
        newOrderShipment?.let {
            clearBboIfExist()
            orderShipment.value = it
            sendPreselectedCourierOption(selectedShippingCourierUiModel.productData.shipperProductId.toString())
            if (it.serviceErrorMessage.isNullOrEmpty()) {
                validateUsePromo()
                updateCart()
            } else {
                calculateTotal()
            }
        }
    }

    fun changePinpoint() {
        if (orderShipment.value.needPinpoint) {
            orderShipment.value.needPinpoint = false
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
            if (!orderProfile.value.isValidProfile) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            val result = logisticProcessor.savePinpoint(orderProfile.value.address, longitude, latitude, userSession.userId, userSession.deviceId)
            globalEvent.value = result
        }
    }

    fun chooseLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel) {
        launch(executorDispatchers.immediate) {
            val shipping = orderShipment.value
            val shippingRecommendationData = orderShipment.value.shippingRecommendationData
            if (shippingRecommendationData != null) {
                globalEvent.value = OccGlobalEvent.Loading
                val (isApplied, resultValidateUse, newGlobalEvent) = promoProcessor.validateUseLogisticPromo(generateValidateUsePromoRequestWithBbo(logisticPromoUiModel), logisticPromoUiModel.promoCode)
                if (isApplied && resultValidateUse != null) {
                    val (newShipment, newEvent) = logisticProcessor.onApplyBbo(shipping, logisticPromoUiModel)
                    if (newShipment != null) {
                        orderShipment.value = newShipment
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
                    calculateTotal()
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
            globalEvent.value = OccGlobalEvent.Loading
            val newChosenAddress = logisticProcessor.setChosenAddress(addressModel)
            if (newChosenAddress == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_ERROR_MESSAGE)
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
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
                globalEvent.value = OccGlobalEvent.UpdateLocalCacheAddress(newChosenAddress)
                clearBboIfExist()
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun updateCart() {
        launch(executorDispatchers.immediate) {
            cartProcessor.updateCartIgnoreResult(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
        }
    }

    private fun updateCartWithCustomShipment(orderShipment: OrderShipment) {
        launch(executorDispatchers.immediate) {
            cartProcessor.updateCartIgnoreResult(orderCart, orderProfile.value, orderShipment, orderPayment.value)
        }
    }

    private fun generateUpdateCartParam(): UpdateCartOccRequest? {
        return cartProcessor.generateUpdateCartParam(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
    }

    private fun shouldSkipShippingValidationWhenUpdateCart(): Boolean {
        return cartProcessor.shouldSkipShippingValidationWhenUpdateCart(orderShipment.value)
    }

    fun finalUpdate(onSuccessCheckout: (CheckoutOccResult) -> Unit, skipCheckIneligiblePromo: Boolean) {
        if (orderTotal.value.buttonState == OccButtonState.NORMAL) {
            globalEvent.value = OccGlobalEvent.Loading
            val shop = orderShop.value
            if (orderProfile.value.isValidProfile && orderShipment.value.getRealShipperProductId() > 0) {
                val param = generateUpdateCartParam()
                if (param != null) {
                    if (validateSelectedTerm()) {
                        finalUpdateJob?.cancel()
                        finalUpdateJob = launch(executorDispatchers.immediate) {
                            val (isSuccess, errorGlobalEvent) = cartProcessor.finalUpdateCart(param)
                            if (isSuccess) {
                                finalValidateUse(orderCart.products, shop, orderProfile.value, onSuccessCheckout, skipCheckIneligiblePromo)
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

    private fun finalValidateUse(products: List<OrderProduct>, shop: OrderShop, profile: OrderProfile, onSuccessCheckout: (CheckoutOccResult) -> Unit, skipCheckIneligiblePromo: Boolean) {
        val validateUsePromoRequest = generateValidateUsePromoRequest()
        if (!skipCheckIneligiblePromo && promoProcessor.hasPromo(validateUsePromoRequest)) {
            launch(executorDispatchers.immediate) {
                val (resultValidateUse, isSuccess, newGlobalEvent) = promoProcessor.finalValidateUse(validateUsePromoRequest, orderCart)
                if (resultValidateUse != null) {
                    validateUsePromoRevampUiModel = resultValidateUse
                    updatePromoState(resultValidateUse.promoUiModel)
                    if (isSuccess) {
                        doCheckout(products, shop, profile, onSuccessCheckout)
                        return@launch
                    }
                }
                globalEvent.value = newGlobalEvent
            }
        } else {
            doCheckout(products, shop, profile, onSuccessCheckout)
        }
    }

    private fun doCheckout(products: List<OrderProduct>, shop: OrderShop, profile: OrderProfile, onSuccessCheckout: (CheckoutOccResult) -> Unit) {
        launch(executorDispatchers.immediate) {
            val (checkoutOccResult, globalEventResult) = checkoutProcessor.doCheckout(validateUsePromoRevampUiModel, orderCart, products, shop, profile, orderShipment.value, orderTotal.value, userSession.userId, generateOspEeBody(emptyList()))
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
            if (isSuccess && orderProfile.value.isValidProfile) {
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
        return promoProcessor.generatePromoRequest(orderCart, orderShipment.value, lastValidateUsePromoRequest, orderPromo.value)
    }

    fun generateValidateUsePromoRequest(shouldAddLogisticPromo: Boolean = true): ValidateUsePromoRequest {
        val validateUsePromoRequest = promoProcessor.generateValidateUsePromoRequest(shouldAddLogisticPromo, lastValidateUsePromoRequest, orderCart, orderShipment.value, orderPromo.value)
        lastValidateUsePromoRequest = validateUsePromoRequest
        return validateUsePromoRequest
    }

    private fun generateValidateUsePromoRequestWithBbo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String? = null): ValidateUsePromoRequest {
        val validateUsePromoRequest = promoProcessor.generateValidateUsePromoRequestWithBbo(logisticPromoUiModel, oldCode, lastValidateUsePromoRequest, orderCart, orderShipment.value, orderPromo.value)
        lastValidateUsePromoRequest = validateUsePromoRequest
        return validateUsePromoRequest
    }

    fun generateBboPromoCodes(): ArrayList<String> {
        return promoProcessor.generateBboPromoCodes(orderShipment.value)
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
                    calculateTotal()
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
                    calculateTotal()
                }
            }
        }
    }

    private fun clearAllPromoFromLastRequest() {
        validateUsePromoRevampUiModel = null
        lastValidateUsePromoRequest = promoProcessor.clearAllPromoFromLastRequest(lastValidateUsePromoRequest)
        orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
    }

    fun updatePromoState(promoUiModel: PromoUiModel) {
        orderPromo.value = orderPromo.value.copy(lastApply = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel), isDisabled = false, state = OccButtonState.NORMAL)
        calculateTotal()
    }

    fun calculateTotal() {
        launch(executorDispatchers.immediate) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            calculator.calculateTotal(orderCart, orderProfile.value, orderShipment.value,
                    validateUsePromoRevampUiModel, orderPayment.value, orderTotal.value)
        }
    }

    private fun initCalculator() {
        launch(executorDispatchers.immediate) {
            calculator.total.collect { (newOrderPayment, newOrderTotal) ->
                orderPayment.value = newOrderPayment
                orderTotal.value = newOrderTotal
            }
        }
    }

    fun chooseInstallment(selectedInstallmentTerm: OrderPaymentInstallmentTerm) {
        launch(executorDispatchers.immediate) {
            var param = generateUpdateCartParam()
            val creditCard = orderPayment.value.creditCard
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
                    throw IllegalStateException()
                }
                expressCheckoutParams.addProperty(UpdateCartOccProfileRequest.INSTALLMENT_TERM, selectedInstallmentTerm.term.toString())
                param = param.copy(profile = param.profile.copy(metadata = metadata.toString()),
                        skipShippingValidation = shouldSkipShippingValidationWhenUpdateCart())
            } catch (e: RuntimeException) {
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
                orderPayment.value = orderPayment.value.copy(creditCard = creditCard.copy(selectedTerm = selectedInstallmentTerm, availableTerms = availableTerms))
                calculateTotal()
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
        val creditCard = orderPayment.value.creditCard
        val selectedTerm = creditCard.selectedTerm
        val hasEnableTerm = creditCard.availableTerms.indexOfFirst { it.isEnable } > -1
        if (selectedTerm != null && !selectedTerm.isEnable && hasEnableTerm) {
            val availableTerms = creditCard.availableTerms
            availableTerms.forEach { it.isError = true }
            selectedTerm.isError = true
            orderPayment.value = orderPayment.value.copy(creditCard = creditCard.copy(selectedTerm = selectedTerm, availableTerms = availableTerms))
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
            orderSummaryAnalytics.eventViewOrderSummaryPage(userSession.userId, orderProfile.value.payment.gatewayName, generateOspEeBody().build(OrderSummaryPageEnhanceECommerce.STEP_1, OrderSummaryPageEnhanceECommerce.STEP_1_OPTION))
            for (product in orderCart.products) {
                if (product.purchaseProtectionPlanData.isProtectionAvailable) {
                    orderSummaryAnalytics.eventPPImpressionOnInsuranceSection(userSession.userId, product.categoryId, "", product.purchaseProtectionPlanData.protectionTitle)
                }
            }
            hasSentViewOspEe = true
        }
    }

    private fun generateOspEeBody(promoCodes: List<String> = emptyList()): OrderSummaryPageEnhanceECommerce {
        val products = orderProducts.value
        return OrderSummaryPageEnhanceECommerce().apply {
            for (orderProduct in products) {
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
                setCartId(orderProduct.cartId)
                setBuyerAddressId(orderProfile.value.address.addressId.toString())
                setSpid(orderShipment.value.getRealShipperProductId().toString())
                setCodFlag(false)
                setCornerFlag(false)
                setIsFullfilment(orderShop.value.isFulfillment)
                setShopIdDimension(orderShop.value.shopId.toString())
                setShopNameDimension(orderShop.value.shopName)
                setShopTypeDimension(orderShop.value.isOfficial, orderShop.value.isGold)
                setCategoryId(orderProduct.categoryId)
                if (orderShipment.value.getRealShipperProductId() > 0) {
                    setShippingPrice(orderShipment.value.getRealShippingPrice().toString())
                } else {
                    setShippingPrice("")
                }
                setShippingDuration(orderShipment.value.serviceDuration)
                setCampaignId(orderProduct.campaignId)
                saveData()
            }
        }
    }

    fun consumeForceShowOnboarding() {
        val onboarding = orderPreferenceData.onboarding
        if (onboarding.isForceShowCoachMark) {
            orderPreferenceData = orderPreferenceData.copy(onboarding = onboarding.copy(isForceShowCoachMark = false))
            globalEvent.value = OccGlobalEvent.Normal
        }
    }

    private fun configureForceShowOnboarding() {
        val onboarding = orderPreferenceData.onboarding
        if (onboarding.isForceShowCoachMark && orderProfile.value.isValidProfile && orderShipment.value.isValid()) {
            forceShowOnboarding(onboarding)
        }
    }

    private fun forceShowOnboarding(onboarding: OccOnboarding) {
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