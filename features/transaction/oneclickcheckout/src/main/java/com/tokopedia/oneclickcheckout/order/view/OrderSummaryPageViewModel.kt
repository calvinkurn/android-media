package com.tokopedia.oneclickcheckout.order.view

import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
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
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccCartRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccProfileRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageCartProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageCheckoutProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageLogisticProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPagePromoProcessor
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.PARAM_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.PARAM_OCC
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_GLOBAL
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_OFFICIAL_STORE
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_POWER_MERCHANT
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

class OrderSummaryPageViewModel @Inject constructor(private val executorDispatchers: ExecutorDispatchers,
                                                    val getPreferenceListUseCase: GetPreferenceListUseCase,
                                                    private val cartProcessor: OrderSummaryPageCartProcessor,
                                                    private val logisticProcessor: OrderSummaryPageLogisticProcessor,
                                                    private val checkoutProcessor: OrderSummaryPageCheckoutProcessor,
                                                    private val promoProcessor: OrderSummaryPagePromoProcessor,
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
                if (!onApplyBbo(shipping, logisticPromoUiModel, resultValidateUse)) {
                    updatePromoState(resultValidateUse.promoUiModel)
                    globalEvent.value = OccGlobalEvent.Error(errorMessage = FAIL_APPLY_BBO_ERROR_MESSAGE)
                }
                return@launch
            }
            if (resultValidateUse != null) {
                validateUsePromoRevampUiModel = resultValidateUse
                _orderShipment = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromoUiModel.title}" else null, isApplyLogisticPromo = false, logisticPromoShipping = null)
                orderShipment.value = _orderShipment
                globalEvent.value = OccGlobalEvent.Normal
                updatePromoState(resultValidateUse.promoUiModel)
                return@launch
            }
            orderPromo.value = orderPromo.value.copy(state = OccButtonState.DISABLE)
            globalEvent.value = newGlobalEvent
            _orderShipment = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromoUiModel.title}" else null, isApplyLogisticPromo = false, logisticPromoShipping = null)
            orderShipment.value = _orderShipment
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

    fun chooseCourier(choosenShippingCourierViewModel: ShippingCourierUiModel) {
        val shipping = _orderShipment
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
            clearBboIfExist()
            shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo?.copy(isApplied = false)
            for (shippingDurationViewModel in shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == shipping.serviceId) {
                    shippingDurationViewModel.isSelected = true
                    val shippingCourierViewModelList = shippingDurationViewModel.shippingCourierViewModelList
                    var selectedShippingCourierUiModel: ShippingCourierUiModel? = null
                    for (shippingCourierUiModel in shippingCourierViewModelList) {
                        if (shippingCourierUiModel.productData.shipperProductId == choosenShippingCourierViewModel.productData.shipperProductId) {
                            selectedShippingCourierUiModel = shippingCourierUiModel
                        } else {
                            shippingCourierUiModel.isSelected = false
                        }
                    }
                    if (selectedShippingCourierUiModel != null) {
                        selectedShippingCourierUiModel.isSelected = true
                        _orderShipment = shipping.copy(
                                shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                                ratesId = selectedShippingCourierUiModel.ratesId,
                                ut = selectedShippingCourierUiModel.productData.unixTime,
                                checksum = selectedShippingCourierUiModel.productData.checkSum,
                                shipperId = selectedShippingCourierUiModel.productData.shipperId,
                                shipperName = selectedShippingCourierUiModel.productData.shipperName,
                                insuranceData = selectedShippingCourierUiModel.productData.insurance,
                                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                shippingRecommendationData = shippingRecommendationData,
                                logisticPromoShipping = null,
                                isApplyLogisticPromo = false)
                        orderShipment.value = _orderShipment
                        calculateTotal()
                        validateUsePromo()
                        return
                    }
                }
            }
        }
    }

    fun setInsuranceCheck(checked: Boolean) {
        if (_orderShipment.getRealShipperProductId() > 0 && _orderShipment.isCheckInsurance != checked) {
            _orderShipment = _orderShipment.copy(isCheckInsurance = checked)
            calculateTotal()
        }
    }

    fun chooseDuration(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
        val shipping = _orderShipment
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
            var selectedShippingDurationViewModel = shippingDurationViewModels[0]
            for (shippingDurationViewModel in shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == selectedServiceId) {
                    shippingDurationViewModel.isSelected = true
                    selectedShippingDurationViewModel = shippingDurationViewModel
                } else {
                    shippingDurationViewModel.isSelected = false
                }
            }
            clearBboIfExist()
            shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo?.copy(isApplied = false)
            var newShipping = shipping.copy(
                    needPinpoint = flagNeedToSetPinpoint,
                    serviceErrorMessage = if (flagNeedToSetPinpoint) NEED_PINPOINT_ERROR_MESSAGE else selectedShippingCourierUiModel.productData.error?.errorMessage,
                    isServicePickerEnable = !flagNeedToSetPinpoint,
                    serviceId = selectedShippingDurationViewModel.serviceData.serviceId,
                    serviceDuration = selectedShippingDurationViewModel.serviceData.serviceName,
                    serviceName = selectedShippingDurationViewModel.serviceData.serviceName,
                    shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                    ratesId = selectedShippingCourierUiModel.ratesId,
                    ut = selectedShippingCourierUiModel.productData.unixTime,
                    checksum = selectedShippingCourierUiModel.productData.checkSum,
                    shipperId = selectedShippingCourierUiModel.productData.shipperId,
                    shipperName = selectedShippingCourierUiModel.productData.shipperName,
                    insuranceData = selectedShippingCourierUiModel.productData.insurance,
                    shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                    shippingRecommendationData = shippingRecommendationData,
                    logisticPromoTickerMessage = null,
                    logisticPromoViewModel = null,
                    logisticPromoShipping = null,
                    isApplyLogisticPromo = false)

            if (newShipping.serviceErrorMessage.isNullOrEmpty()) {
                val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                if (logisticPromo != null && !logisticPromo.disabled) {
                    newShipping = newShipping.copy(logisticPromoTickerMessage = "Tersedia ${logisticPromo.title}", logisticPromoViewModel = logisticPromo, logisticPromoShipping = null)
                }
            }
            _orderShipment = newShipping
            orderShipment.value = _orderShipment
            sendPreselectedCourierOption(selectedShippingCourierUiModel.productData.shipperProductId.toString())
            if (newShipping.serviceErrorMessage.isNullOrEmpty()) {
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
                    if (!onApplyBbo(shipping, logisticPromoUiModel, resultValidateUse)) {
                        updatePromoState(resultValidateUse.promoUiModel)
                        globalEvent.value = OccGlobalEvent.Error(errorMessage = FAIL_APPLY_BBO_ERROR_MESSAGE)
                    }
                    return@launch
                }
                resultValidateUse?.let {
                    validateUsePromoRevampUiModel = it
                }
                globalEvent.value = newGlobalEvent
            }
        }
    }

    private fun onApplyBbo(shipping: OrderShipment, logisticPromoUiModel: LogisticPromoUiModel, response: ValidateUsePromoRevampUiModel): Boolean {
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            var logisticPromoShipping: ShippingCourierUiModel? = null
            val shouldEnableServicePicker = shipping.isServicePickerEnable || !shipping.serviceErrorMessage.isNullOrEmpty()
            for (shippingDurationViewModel in shippingRecommendationData.shippingDurationViewModels) {
                if (shippingDurationViewModel.isSelected) {
                    for (shippingCourierUiModel in shippingDurationViewModel.shippingCourierViewModelList) {
                        shippingCourierUiModel.isSelected = false
                    }
                }
                if (shippingDurationViewModel.serviceData.serviceId == logisticPromoUiModel.serviceId) {
                    logisticPromoShipping = shippingDurationViewModel.shippingCourierViewModelList.firstOrNull { it.productData.shipperProductId == logisticPromoUiModel.shipperProductId }
                }
                if (shouldEnableServicePicker) {
                    shippingDurationViewModel.isSelected = false
                }
            }
            if (logisticPromoShipping != null) {
                shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo.copy(isApplied = true)
                val needPinpoint = logisticPromoShipping.productData?.error?.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED
                _orderShipment = shipping.copy(shippingRecommendationData = shippingRecommendationData, isServicePickerEnable = shouldEnableServicePicker,
                        insuranceData = logisticPromoShipping.productData?.insurance, serviceErrorMessage = if (needPinpoint) NEED_PINPOINT_ERROR_MESSAGE else logisticPromoShipping.productData?.error?.errorMessage,
                        needPinpoint = needPinpoint, logisticPromoTickerMessage = null, isApplyLogisticPromo = true, logisticPromoShipping = logisticPromoShipping)
                orderShipment.value = _orderShipment
                globalEvent.value = OccGlobalEvent.Normal
                validateUsePromoRevampUiModel = response
                updatePromoState(response.promoUiModel)
                return true
            }
        }
        return false
    }

    fun updateCart() {
        launch(executorDispatchers.main) {
            cartProcessor.updateCartIgnoreResult(orderCart, _orderPreference, _orderShipment, _orderPayment)
        }
    }

    fun generateUpdateCartParam(): UpdateCartOccRequest? {
        val op = orderProduct
        val quantity = op.quantity
        val pref = _orderPreference
        if (pref.isValid && pref.preference.profileId > 0) {
            val cart = UpdateCartOccCartRequest(
                    orderCart.cartId.toString(),
                    quantity.orderQuantity,
                    op.notes,
                    op.productId.toString(),
                    _orderShipment.getRealShipperId(),
                    _orderShipment.getRealShipperProductId()
            )
            var metadata = pref.preference.payment.metadata
            val selectedTerm = _orderPayment.creditCard.selectedTerm
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
                    pref.preference.profileId.toString(),
                    pref.preference.payment.gatewayCode,
                    metadata,
                    pref.preference.shipment.serviceId,
                    pref.preference.address.addressId.toString()
            )
            return UpdateCartOccRequest(arrayListOf(cart), profile)
        }
        return null
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
                val (resultValidateUse, newGlobalEvent) = promoProcessor.finalValidateUse(generateValidateUsePromoRequest())
                if (resultValidateUse != null) {
                    validateUsePromoRevampUiModel = resultValidateUse
                    updatePromoState(resultValidateUse.promoUiModel)
                    if (checkIneligiblePromo()) {
                        doCheckout(product, shop, pref, onSuccessCheckout)
                    }
                    return@launch
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

    private fun checkIneligiblePromo(): Boolean {
        var notEligiblePromoHolderdataList = ArrayList<NotEligiblePromoHolderdata>()
        val validateUsePromoRevampUiModel = validateUsePromoRevampUiModel
        if (validateUsePromoRevampUiModel != null) {
            notEligiblePromoHolderdataList = addIneligibleGlobalPromo(validateUsePromoRevampUiModel, notEligiblePromoHolderdataList)
            notEligiblePromoHolderdataList = addIneligibleVoucherPromo(validateUsePromoRevampUiModel, notEligiblePromoHolderdataList)
        }

        if (notEligiblePromoHolderdataList.size > 0) {
            globalEvent.value = OccGlobalEvent.PromoClashing(notEligiblePromoHolderdataList)
            return false
        }
        return true
    }

    private fun addIneligibleGlobalPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel, notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>): ArrayList<NotEligiblePromoHolderdata> {
        if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
            val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
            notEligiblePromoHolderdata.promoTitle = validateUsePromoRevampUiModel.promoUiModel.titleDescription
            if (validateUsePromoRevampUiModel.promoUiModel.codes.isNotEmpty()) {
                notEligiblePromoHolderdata.promoCode = validateUsePromoRevampUiModel.promoUiModel.codes[0]
            }
            notEligiblePromoHolderdata.shopName = "Kode promo"
            notEligiblePromoHolderdata.iconType = TYPE_ICON_GLOBAL
            notEligiblePromoHolderdata.showShopSection = true
            notEligiblePromoHolderdata.errorMessage = validateUsePromoRevampUiModel.promoUiModel.messageUiModel.text
            notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
        }
        return notEligiblePromoHolderdataList
    }

    private fun addIneligibleVoucherPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel, notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>): ArrayList<NotEligiblePromoHolderdata> {
        val voucherOrdersItemUiModels = validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels
        for (i in voucherOrdersItemUiModels.indices) {
            val voucherOrdersItemUiModel = voucherOrdersItemUiModels[i]
            if (voucherOrdersItemUiModel != null && voucherOrdersItemUiModel.messageUiModel.state == "red") {
                val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
                notEligiblePromoHolderdata.promoTitle = voucherOrdersItemUiModel.titleDescription
                notEligiblePromoHolderdata.promoCode = voucherOrdersItemUiModel.titleDescription
                if (orderCart.cartString == voucherOrdersItemUiModel.uniqueId) {
                    notEligiblePromoHolderdata.shopName = orderShop.shopName
                    if (orderShop.isOfficial == 1) {
                        notEligiblePromoHolderdata.iconType = TYPE_ICON_OFFICIAL_STORE
                    } else if (orderShop.isGold == 1) {
                        notEligiblePromoHolderdata.iconType = TYPE_ICON_POWER_MERCHANT
                    }
                }
                if (i == 0) {
                    notEligiblePromoHolderdata.showShopSection = true
                } else {
                    notEligiblePromoHolderdata.showShopSection = voucherOrdersItemUiModels[i - 1]?.uniqueId != voucherOrdersItemUiModel.uniqueId
                }

                notEligiblePromoHolderdata.errorMessage = voucherOrdersItemUiModel.messageUiModel.text
                notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
            }
        }
        return notEligiblePromoHolderdataList
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
        val promoRequest = PromoRequest()

        val ordersItem = Order()
        ordersItem.shopId = orderShop.shopId.toLong()
        ordersItem.uniqueId = orderCart.cartString
        ordersItem.product_details = listOf(ProductDetail(orderProduct.productId.toLong(), orderProduct.quantity.orderQuantity))
        ordersItem.isChecked = true

        val shipping = _orderShipment
        ordersItem.shippingId = shipping.getRealShipperId()
        ordersItem.spId = shipping.getRealShipperProductId()

        if (shipping.isCheckInsurance && shipping.insuranceData != null) {
            ordersItem.isInsurancePrice = 1
        } else {
            ordersItem.isInsurancePrice = 0
        }

        val lastRequest = lastValidateUsePromoRequest

        ordersItem.codes = generateOrderPromoCodes(lastRequest, ordersItem.uniqueId, shipping)

        promoRequest.orders = listOf(ordersItem)
        promoRequest.state = PARAM_CHECKOUT
        promoRequest.cartType = PARAM_OCC

        if (lastRequest != null) {
            promoRequest.codes = ArrayList(lastRequest.codes.filterNotNull())
        } else {
            val globalCodes = orderPromo.value.lastApply?.codes ?: emptyList()
            promoRequest.codes = ArrayList(globalCodes)
        }
        return promoRequest
    }

    private fun generateOrderPromoCodes(lastRequest: ValidateUsePromoRequest?, uniqueId: String, shipping: OrderShipment, shouldAddLogisticPromo: Boolean = true): MutableList<String> {
        var codes: MutableList<String> = ArrayList()
        val lastRequestOrderCodes = lastRequest?.orders?.firstOrNull()?.codes
        if (lastRequestOrderCodes != null) {
            codes = lastRequestOrderCodes
        } else {
            val voucherOrders = orderPromo.value.lastApply?.voucherOrders ?: emptyList()
            for (voucherOrder in voucherOrders) {
                if (voucherOrder.uniqueId.equals(uniqueId, true)) {
                    if (!codes.contains(voucherOrder.code)) {
                        codes.add(voucherOrder.code)
                    }
                }
            }
        }

        if (shouldAddLogisticPromo && shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            if (!codes.contains(shipping.logisticPromoViewModel.promoCode)) {
                codes.add(shipping.logisticPromoViewModel.promoCode)
            }
        } else if (shipping.logisticPromoViewModel?.promoCode?.isNotEmpty() == true) {
            codes.remove(shipping.logisticPromoViewModel.promoCode)
        }
        return codes
    }

    fun generateValidateUsePromoRequest(shouldAddLogisticPromo: Boolean = true): ValidateUsePromoRequest {
        val validateUsePromoRequest = lastValidateUsePromoRequest ?: ValidateUsePromoRequest()

        val ordersItem = OrdersItem()
        ordersItem.shopId = orderShop.shopId
        ordersItem.uniqueId = orderCart.cartString

        ordersItem.productDetails = listOf(ProductDetailsItem(orderProduct.quantity.orderQuantity, orderProduct.productId))

        val shipping = _orderShipment
        ordersItem.shippingId = shipping.getRealShipperId()
        ordersItem.spId = shipping.getRealShipperProductId()

        val lastRequest = lastValidateUsePromoRequest

        ordersItem.codes = generateOrderPromoCodes(lastRequest, ordersItem.uniqueId, shipping, shouldAddLogisticPromo)

        validateUsePromoRequest.orders = listOf(ordersItem)
        validateUsePromoRequest.state = PARAM_CHECKOUT
        validateUsePromoRequest.cartType = PARAM_OCC

        if (lastRequest != null) {
            validateUsePromoRequest.codes = lastRequest.codes
        } else {
            val globalCodes = orderPromo.value.lastApply?.codes ?: emptyList()
            validateUsePromoRequest.codes = globalCodes.toMutableList()
        }
        validateUsePromoRequest.skipApply = 0
        validateUsePromoRequest.isSuggested = 0

        lastValidateUsePromoRequest = validateUsePromoRequest

        return validateUsePromoRequest
    }

    private fun generateValidateUsePromoRequestWithBbo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String? = null): ValidateUsePromoRequest {
        return generateValidateUsePromoRequest(false).apply {
            orders[0]?.apply {
                shippingId = logisticPromoUiModel.shipperId
                spId = logisticPromoUiModel.shipperProductId
                if (oldCode != null) {
                    codes.remove(oldCode)
                }
                codes.add(logisticPromoUiModel.promoCode)
            }
        }
    }

    fun generateBboPromoCodes(): ArrayList<String> {
        val shipping = _orderShipment
        if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            return arrayListOf(shipping.logisticPromoViewModel.promoCode)
        }
        return ArrayList()
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
        val quantity = orderProduct.quantity
        var payment = _orderPayment
        if (quantity.orderQuantity <= 0 || !_orderPreference.isValid) {
            orderTotal.value = orderTotal.value.copy(orderCost = OrderCost(), buttonState = OccButtonState.DISABLE)
            return
        }
        val totalProductPrice = quantity.orderQuantity * orderProduct.getPrice().toDouble()
        val shipping = _orderShipment
        val totalShippingPrice = shipping.getRealOriginalPrice().toDouble()
        val insurancePrice = shipping.getRealInsurancePrice().toDouble()
        val (productDiscount, shippingDiscount, cashbacks) = calculatePromo()
        var subtotal = totalProductPrice + totalShippingPrice + insurancePrice
        payment = calculateInstallmentDetails(payment, subtotal, if (orderShop.isOfficial == 1) subtotal - productDiscount - shippingDiscount else 0.0, productDiscount + shippingDiscount)
        val fee = payment.getRealFee()
        subtotal += fee
        subtotal -= productDiscount
        subtotal -= shippingDiscount
        val orderCost = OrderCost(subtotal, totalProductPrice, totalShippingPrice, insurancePrice, fee, shippingDiscount, productDiscount, cashbacks)

        var currentState = orderTotal.value.buttonState
        if (currentState == OccButtonState.NORMAL && (!shouldButtonStateEnable(shipping))) {
            currentState = OccButtonState.DISABLE
        }
        if (payment.errorTickerMessage.isNotEmpty()) {
            _orderPayment = payment.copy(isCalculationError = false)
            orderPayment.value = _orderPayment
            orderTotal.value = orderTotal.value.copy(orderCost = orderCost, paymentErrorMessage = payment.errorTickerMessage, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
        } else if (payment.errorMessage.message.isNotEmpty() && payment.errorMessage.button.text.isNotEmpty()) {
            if (currentState == OccButtonState.NORMAL) {
                currentState = OccButtonState.DISABLE
            }
            _orderPayment = payment.copy(isCalculationError = false)
            orderPayment.value = _orderPayment
            orderTotal.value = orderTotal.value.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
        } else if (payment.minimumAmount > subtotal) {
            orderTotal.value = orderTotal.value.copy(orderCost = orderCost,
                    paymentErrorMessage = "Belanjaanmu kurang dari min. transaksi ${payment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(payment.minimumAmount, false).removeDecimalSuffix()}). Silahkan pilih pembayaran lain.",
                    buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            _orderPayment = payment.copy(isCalculationError = true)
            orderPayment.value = _orderPayment
        } else if (payment.maximumAmount > 0 && payment.maximumAmount < subtotal) {
            orderTotal.value = orderTotal.value.copy(orderCost = orderCost,
                    paymentErrorMessage = "Belanjaanmu melebihi limit transaksi ${payment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(payment.maximumAmount, false).removeDecimalSuffix()}). Silahkan pilih pembayaran lain.",
                    buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            _orderPayment = payment.copy(isCalculationError = true)
            orderPayment.value = _orderPayment
        } else if (payment.gatewayCode.contains(OVO_GATEWAY_CODE) && subtotal > payment.walletAmount) {
            orderTotal.value = orderTotal.value.copy(orderCost = orderCost,
                    paymentErrorMessage = OVO_INSUFFICIENT_ERROR_MESSAGE,
                    buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            _orderPayment = payment.copy(isCalculationError = true)
            orderPayment.value = _orderPayment
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_PAYMENT_OVO_BALANCE)
        } else {
            if (payment.creditCard.selectedTerm?.isError == true && currentState == OccButtonState.NORMAL) {
                currentState = OccButtonState.DISABLE
            }
            _orderPayment = payment.copy(isCalculationError = false)
            orderPayment.value = _orderPayment
            orderTotal.value = orderTotal.value.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
        }
    }

    private fun calculatePromo(): Triple<Int, Int, ArrayList<Pair<String, String>>> {
        var productDiscount = 0
        var shippingDiscount = 0
        val cashbacks = ArrayList<Pair<String, String>>()
        val summaries = validateUsePromoRevampUiModel?.promoUiModel?.benefitSummaryInfoUiModel?.summaries
                ?: emptyList()
        for (summary in summaries) {
            if (summary.type == SummariesUiModel.TYPE_DISCOUNT) {
                for (detail in summary.details) {
                    if (detail.type == SummariesUiModel.TYPE_SHIPPING_DISCOUNT) {
                        shippingDiscount += detail.amount
                    }
                    if (detail.type == SummariesUiModel.TYPE_PRODUCT_DISCOUNT) {
                        productDiscount += detail.amount
                    }
                }
            }
            if (summary.type == SummariesUiModel.TYPE_CASHBACK) {
                cashbacks.addAll(summary.details.map { it.description to it.amountStr })
            }
        }
        return Triple(productDiscount, shippingDiscount, cashbacks)
    }

    private fun calculateMdrFee(subTotal: Double, mdr: Float, subsidize: Double, mdrSubsidize: Float): Double {
        return ceil(subTotal * (mdr / 100.0) - subsidize * (mdrSubsidize / 100.0))
    }

    private fun calculateInstallmentDetails(payment: OrderPayment, subTotal: Double, subsidize: Double, discount: Int): OrderPayment {
        if (payment.creditCard.selectedTerm == null) {
            return payment
        }
        val installments = payment.creditCard.availableTerms
        var selectedInstallmentTerm: OrderPaymentInstallmentTerm? = null
        for (i in installments.lastIndex downTo 0) {
            val installment = installments[i]
            installment.isEnable = installment.minAmount <= (subTotal - discount)
            if (installment.isError) {
                installment.isError = !installment.isEnable
            }
            installment.fee = calculateMdrFee(subTotal, installment.mdr, subsidize, installment.mdrSubsidize)
            val total = subTotal + installment.fee - discount
            installment.monthlyAmount = if (installment.term > 0) ceil(total / installment.term) else total
            if (installment.isSelected) {
                selectedInstallmentTerm = installment
            }
        }
        return payment.copy(creditCard = payment.creditCard.copy(availableTerms = installments, selectedTerm = selectedInstallmentTerm))
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