package com.tokopedia.oneclickcheckout.order.view

import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccMutableLiveData
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderSummaryPageViewModel @Inject constructor(private val executorDispatchers: ExecutorDispatchers,
                                                    val getPreferenceListUseCase: GetPreferenceListUseCase,
                                                    private val cartProcessor: OrderSummaryPageCartProcessor,
                                                    private val logisticProcessor: OrderSummaryPageLogisticProcessor,
                                                    private val checkoutProcessor: OrderSummaryPageCheckoutProcessor,
                                                    private val promoProcessor: OrderSummaryPagePromoProcessor,
                                                    private val calculator: OrderSummaryPageCalculator,
                                                    private val userSessionInterface: UserSessionInterface,
                                                    private val orderSummaryAnalytics: OrderSummaryAnalytics) : BaseViewModel(executorDispatchers.main) {

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

    private var debounceJob: Job? = null

    private var hasSentViewOspEe = false

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
        launch(executorDispatchers.main) {
            globalEvent.value = OccGlobalEvent.Loading
            globalEvent.value = cartProcessor.atcOcc(productId)
        }
    }

    fun getOccCart(isFullRefresh: Boolean, source: String) {
        launch(executorDispatchers.main) {
            globalEvent.value = OccGlobalEvent.Normal
            val result = cartProcessor.getOccCart(source)
            orderCart = result.orderCart
            _orderPreference = result.orderPreference
            orderPreference.value = if (result.throwable == null) OccState.FirstLoad(_orderPreference) else OccState.Failed(Failure(result.throwable))
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
            if (orderProduct.productId > 0 && _orderPreference.preference.shipment.serviceId > 0) {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
                getRates()
            } else if (result.throwable == null) {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                sendViewOspEe()
            }
        }
    }

    fun updateProduct(product: OrderProduct, shouldReloadRates: Boolean = true) {
        orderCart.product = product
        if (shouldReloadRates) {
            calculateTotal()
            if (!product.quantity.isStateError) {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
                debounce()
            }
        }
    }

    private fun debounce() {
        debounceJob?.cancel()
        debounceJob = launch(executorDispatchers.main) {
            OccIdlingResource.increment()
            delay(1000)
            if (isActive) {
                updateCart()
                if (_orderPreference.isValid && _orderPreference.preference.shipment.serviceId > 0) {
                    getRates()
                }
                OccIdlingResource.decrement()
            }
        }
    }

    fun getRates() {
        launch(executorDispatchers.main) {
            val result = logisticProcessor.getRates(orderCart, _orderPreference, _orderShipment, generateListShopShipment())
            if (result.clearOldPromoCode.isNotEmpty()) {
                clearOldLogisticPromo(result.clearOldPromoCode)
            }
            if (result.autoApplyPromo != null) {
                autoApplyLogisticPromo(result.autoApplyPromo, result.clearOldPromoCode, result.orderShipment)
                return@launch
            }
            _orderShipment = result.orderShipment
            orderShipment.value = _orderShipment
            sendViewOspEe()
            if (result.orderShipment.serviceErrorMessage.isNullOrEmpty()) {
                validateUsePromo()
            } else {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                sendViewShippingErrorMessage(result.shippingErrorId)
            }
            sendPreselectedCourierOption(result.preselectedSpId)
            calculateTotal()
        }
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
            orderSummaryAnalytics.eventViewPreselectedCourierOption(preselectedSpId, userSessionInterface.userId)
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
        launch(executorDispatchers.main) {
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
                    return@launch
                }
            }
            _orderShipment = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromoUiModel.title}" else null, isApplyLogisticPromo = false, logisticPromoShipping = null)
            orderShipment.value = _orderShipment
            if (resultValidateUse != null) {
                validateUsePromoRevampUiModel = resultValidateUse
                globalEvent.value = OccGlobalEvent.Normal
                updatePromoState(resultValidateUse.promoUiModel)
                return@launch
            }
            orderPromo.value = orderPromo.value.copy(state = OccButtonState.DISABLE)
            globalEvent.value = newGlobalEvent
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
            calculateTotal()
        }
    }

    fun clearBboIfExist() {
        val logisticPromoViewModel = _orderShipment.logisticPromoViewModel
        if (logisticPromoViewModel != null && _orderShipment.isApplyLogisticPromo && _orderShipment.logisticPromoShipping != null) {
            clearOldLogisticPromo(logisticPromoViewModel.promoCode)
        }
    }

    fun chooseCourier(chosenShippingCourierViewModel: ShippingCourierUiModel) {
        val newOrderShipment = logisticProcessor.chooseCourier(chosenShippingCourierViewModel, _orderShipment)
        newOrderShipment?.let {
            clearBboIfExist()
            _orderShipment = it
            orderShipment.value = _orderShipment
            calculateTotal()
            validateUsePromo()
        }
    }

    fun setInsuranceCheck(checked: Boolean) {
        if (_orderShipment.getRealShipperProductId() > 0 && _orderShipment.isCheckInsurance != checked) {
            _orderShipment = _orderShipment.copy(isCheckInsurance = checked)
            calculateTotal()
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
            } else {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                calculateTotal()
            }
        }
    }

    fun changePinpoint() {
        if (_orderShipment.needPinpoint) {
            _orderShipment = _orderShipment.copy(needPinpoint = false)
        }
    }

    fun savePinpoint(longitude: String, latitude: String) {
        val op = _orderPreference
        if (!op.isValid) {
            globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
            return
        }
        launch(executorDispatchers.main) {
            val result = logisticProcessor.savePinpoint(op.preference.address, longitude, latitude)
            globalEvent.value = result
        }
    }

    fun chooseLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel) {
        launch(executorDispatchers.main) {
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
                    return@launch
                }
                resultValidateUse?.let {
                    validateUsePromoRevampUiModel = it
                }
                globalEvent.value = newGlobalEvent
            }
        }
    }

    fun updateCart() {
        launch(executorDispatchers.main) {
            cartProcessor.updateCartIgnoreResult(orderCart, _orderPreference, _orderShipment, _orderPayment)
        }
    }

    fun generateUpdateCartParam(): UpdateCartOccRequest? {
        return cartProcessor.generateUpdateCartParam(orderCart, _orderPreference, _orderShipment, _orderPayment)
    }

    fun updatePreference(preference: ProfilesItemModel) {
        launch(executorDispatchers.main) {
            var param = generateUpdateCartParam()
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            param = param.copy(profile = UpdateCartOccProfileRequest(
                    profileId = preference.profileId.toString(),
                    addressId = preference.addressModel.addressId.toString(),
                    serviceId = preference.shipmentModel.serviceId,
                    gatewayCode = preference.paymentModel.gatewayCode,
                    metadata = preference.paymentModel.metadata
            ))
            globalEvent.value = OccGlobalEvent.Loading
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
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
                        launch(executorDispatchers.main) {
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
        if (!skipCheckIneligiblePromo) {
            launch(executorDispatchers.main) {
                val (resultValidateUse, isSuccess, newGlobalEvent) = promoProcessor.finalValidateUse(generateValidateUsePromoRequest(), orderCart)
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
        launch(executorDispatchers.main) {
            val (checkoutOccResult, globalEventResult) = checkoutProcessor.doCheckout(validateUsePromoRevampUiModel, orderCart, product, shop, pref, _orderShipment, orderTotal.value, generateOspEeBody(emptyList()))
            if (checkoutOccResult != null) {
                onSuccessCheckout(checkoutOccResult)
            } else if (globalEventResult != null) {
                globalEvent.value = globalEventResult
            }
        }
    }

    fun cancelIneligiblePromoCheckout(notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>, onSuccessCheckout: (CheckoutOccResult) -> Unit) {
        globalEvent.value = OccGlobalEvent.Loading
        launch(executorDispatchers.main) {
            val (isSuccess, newGlobalEvent) = promoProcessor.cancelIneligiblePromoCheckout(ArrayList(notEligiblePromoHolderdataList.map { it.promoCode }))
            if (isSuccess && _orderPreference.isValid) {
                finalUpdate(onSuccessCheckout, true)
                return@launch
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun updateCartPromo(onSuccess: (ValidateUsePromoRequest, PromoRequest, ArrayList<String>) -> Unit) {
        launch(executorDispatchers.main) {
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
        launch(executorDispatchers.main) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            orderPromo.value = orderPromo.value.copy(state = OccButtonState.LOADING)
            val resultValidateUse = promoProcessor.validateUsePromo(generateValidateUsePromoRequest(), validateUsePromoRevampUiModel)
            if (resultValidateUse == null) {
                orderPromo.value = orderPromo.value.copy(state = OccButtonState.DISABLE)
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
            } else {
                validateUsePromoRevampUiModel = resultValidateUse
                updatePromoState(resultValidateUse.promoUiModel)
            }
        }
    }

    private fun shouldButtonStateEnable(orderShipment: OrderShipment): Boolean {
        return (orderShipment.isValid() && orderShipment.serviceErrorMessage.isNullOrEmpty() && orderShop.errors.isEmpty() && !orderProduct.quantity.isStateError)
    }

    fun updatePromoState(promoUiModel: PromoUiModel) {
        orderPromo.value = orderPromo.value.copy(lastApply = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel), state = OccButtonState.NORMAL)
        orderTotal.value = orderTotal.value.copy(buttonState = if (shouldButtonStateEnable(_orderShipment)) OccButtonState.NORMAL else OccButtonState.DISABLE)
        calculateTotal()
    }

    fun calculateTotal() {
        val (newOrderPayment, newOrderTotal) = calculator.calculateTotal(orderCart, _orderPreference, _orderShipment, validateUsePromoRevampUiModel, _orderPayment, orderTotal.value)
        _orderPayment = newOrderPayment
        orderPayment.value = _orderPayment
        orderTotal.value = newOrderTotal
    }

    fun chooseInstallment(selectedInstallmentTerm: OrderPaymentInstallmentTerm) {
        launch(executorDispatchers.main) {
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
                param = param.copy(profile = param.profile.copy(metadata = metadata.toString()))
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
                orderTotal.value = orderTotal.value.copy(buttonState = if (shouldButtonStateEnable(_orderShipment)) OccButtonState.NORMAL else OccButtonState.DISABLE)
                calculateTotal()
                globalEvent.value = OccGlobalEvent.Normal
                return@launch
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun updateCreditCard(metadata: String) {
        launch(executorDispatchers.main) {
            var param = generateUpdateCartParam()
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            param = param.copy(profile = param.profile.copy(metadata = metadata))
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
        if (selectedTerm != null && !selectedTerm.isEnable) {
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
        super.onCleared()
        debounceJob?.cancel()
    }

    private fun sendViewOspEe() {
        if (!hasSentViewOspEe) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(generateOspEeBody().build(OrderSummaryPageEnhanceECommerce.STEP_1, OrderSummaryPageEnhanceECommerce.STEP_1_OPTION))
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
            setCartId(orderCart.cartId.toString())
            setBuyerAddressId(_orderPreference.preference.address.addressId.toString())
            setSpid(_orderShipment.getRealShipperProductId().toString())
            setCodFlag(false)
            setCornerFlag(false)
            setIsFullfilment(false)
            setShopId(orderShop.shopId.toString())
            setShopName(orderShop.shopName)
            setShopType(orderShop.isOfficial, orderShop.isGold)
            setCategoryId(orderProduct.categoryId.toString())
        }
    }

    fun consumeForceShowOnboarding() {
        val onboarding = _orderPreference.onboarding
        if (onboarding.isForceShowCoachMark) {
            _orderPreference = _orderPreference.copy(onboarding = onboarding.copy(isForceShowCoachMark = false))
            orderPreference.value = OccState.Success(_orderPreference)
        }
    }

    companion object {
        const val NO_COURIER_SUPPORTED_ERROR_MESSAGE = "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda."
        const val NO_DURATION_AVAILABLE = "Durasi pengiriman tidak tersedia"
        const val NEED_PINPOINT_ERROR_MESSAGE = "Butuh pinpoint lokasi"

        const val FAIL_APPLY_BBO_ERROR_MESSAGE = "Gagal mengaplikasikan bebas ongkir"

        const val ERROR_CODE_PRICE_CHANGE = "513"
        const val PRICE_CHANGE_ERROR_MESSAGE = "Harga telah berubah"
        const val PRICE_CHANGE_ACTION_MESSAGE = "Cek Belanjaan"

        const val OVO_GATEWAY_CODE = "OVO"
        const val OVO_INSUFFICIENT_ERROR_MESSAGE = "OVO Cash kamu tidak cukup. Silahkan pilih pembayaran lain."

        const val INSTALLMENT_INVALID_MIN_AMOUNT = "Oops, tidak bisa bayar dengan cicilan karena min. pembeliannya kurang."

        const val TRANSACTION_ID_KEY = "transaction_id"
    }
}