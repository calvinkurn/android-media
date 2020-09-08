package com.tokopedia.oneclickcheckout.order.view

import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
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
import com.tokopedia.oneclickcheckout.order.data.checkout.*
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccCartRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccProfileRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.domain.UpdateCartOccUseCase
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageCartProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageCheckoutProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageLogisticProcessor
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase.Companion.PARAM_VALUE_MARKETPLACE
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.PARAM_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.PARAM_OCC
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.param.EditAddressParam
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_GLOBAL
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_OFFICIAL_STORE
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_POWER_MERCHANT
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONException
import rx.Observer
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import kotlin.math.ceil

class OrderSummaryPageViewModel @Inject constructor(private val executorDispatchers: ExecutorDispatchers,
                                                    private val executorSchedulers: ExecutorSchedulers,
                                                    private val cartProcessor: OrderSummaryPageCartProcessor,
                                                    private val logisticProcessor: OrderSummaryPageLogisticProcessor,
                                                    val getPreferenceListUseCase: GetPreferenceListUseCase,
                                                    private val updateCartOccUseCase: UpdateCartOccUseCase,
                                                    private val ratesResponseStateConverter: RatesResponseStateConverter,
                                                    private val editAddressUseCase: EditAddressUseCase,
                                                    private val checkoutProcessor: OrderSummaryPageCheckoutProcessor,
                                                    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                                    private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
                                                    private val userSessionInterface: UserSessionInterface,
                                                    private val orderSummaryAnalytics: OrderSummaryAnalytics) : BaseViewModel(executorDispatchers.main) {

    var orderCart: OrderCart = OrderCart()
    val orderProduct: OrderProduct
        get() = orderCart.product
    val orderShop: OrderShop
        get() = orderCart.shop
    private val orderKero: OrderKero
        get() = orderCart.kero

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

    private val compositeSubscription = CompositeSubscription()
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
//        compositeSubscription.add(
//                atcOccExternalUseCase.createObservable(
//                        RequestParams().apply {
//                            putString(AddToCartOccExternalUseCase.REQUEST_PARAM_KEY_PRODUCT_ID, productId)
//                        })
//                        .subscribeOn(executorSchedulers.io)
//                        .observeOn(executorSchedulers.main)
//                        .subscribe(object : Observer<AddToCartDataModel> {
//                            override fun onError(e: Throwable) {
//                                globalEvent.value = OccGlobalEvent.AtcError(e)
//                            }
//
//                            override fun onNext(result: AddToCartDataModel) {
//                                if (result.isDataError()) {
//                                    globalEvent.value = OccGlobalEvent.AtcError(errorMessage = result.getAtcErrorMessage()
//                                            ?: "")
//                                } else {
//                                    globalEvent.value = OccGlobalEvent.AtcSuccess(result.data.message.firstOrNull()
//                                            ?: "")
//                                }
//                            }
//
//                            override fun onCompleted() {
//                                // do nothing
//                            }
//                        })
//        )
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
//        getOccCartUseCase.execute({ orderData: OrderData ->
//            orderCart = orderData.cart
//            _orderPreference = OrderPreference(orderData.ticker, orderData.onboarding, orderData.profileIndex, orderData.profileRecommendation, orderData.preference, true)
//            orderPreference.value = OccState.FirstLoad(_orderPreference)
//            if (isFullRefresh) {
//                _orderShipment = OrderShipment()
//                orderShipment.value = _orderShipment
//            }
//            _orderPayment = orderData.payment
//            orderPayment.value = _orderPayment
//            validateUsePromoRevampUiModel = null
//            lastValidateUsePromoRequest = null
//            orderPromo.value = orderData.promo.copy(state = ButtonBayarState.NORMAL)
//            if (orderData.prompt.shouldShowPrompt()) {
//                globalEvent.value = OccGlobalEvent.Prompt(orderData.prompt)
//            }
//            if (orderProduct.productId > 0 && orderData.preference.shipment.serviceId > 0) {
//                orderTotal.value = orderTotal.value.copy(buttonState = ButtonBayarState.LOADING)
//                getRates()
//            } else {
//                orderTotal.value = orderTotal.value.copy(buttonState = ButtonBayarState.DISABLE)
//                sendViewOspEe()
//            }
//            OccIdlingResource.decrement()
//        }, { throwable: Throwable ->
//            Timber.d(throwable)
//            _orderPreference = OrderPreference()
//            orderCart = OrderCart()
//            validateUsePromoRevampUiModel = null
//            lastValidateUsePromoRequest = null
//            orderPreference.value = OccState.Failed(Failure(throwable))
//            _orderShipment = OrderShipment()
//            orderShipment.value = _orderShipment
//            _orderPayment = OrderPayment()
//            orderPayment.value = _orderPayment
//            OccIdlingResource.decrement()
//        }, getOccCartUseCase.createRequestParams(source))
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
            _orderShipment = result.orderShipment
            orderShipment.value = _orderShipment
            if (result.clearOldPromoCode != null) {
                clearOldLogisticPromo(result.clearOldPromoCode)
                if (result.autoApplyPromo != null) {
                    autoApplyLogisticPromo(result.autoApplyPromo, result.clearOldPromoCode, result.orderShipment)
                    return@launch
                }
            }
            sendViewOspEe()
            if (result.orderShipment.serviceErrorMessage.isNullOrEmpty()) {
                validateUsePromo()
            } else {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
            }
            calculateTotal()
        }
    }

//    fun getRates() {
//        OccIdlingResource.increment()
//        compositeSubscription.add(
//                ratesUseCase.execute(generateRatesParam())
//                        .map(::mapShippingRecommendationData)
//                        .subscribe(object : Observer<ShippingRecommendationData> {
//                            override fun onError(e: Throwable?) {
//                                _orderShipment = OrderShipment(
//                                        serviceName = _orderPreference.preference.shipment.serviceName,
//                                        serviceDuration = _orderPreference.preference.shipment.serviceDuration,
//                                        serviceErrorMessage = NO_COURIER_SUPPORTED_ERROR_MESSAGE,
//                                        shippingRecommendationData = null
//                                )
//                                orderShipment.value = _orderShipment
//                                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
//                                sendViewOspEe()
//                                OccIdlingResource.decrement()
//                            }
//
//                            override fun onNext(shippingRecommendationData: ShippingRecommendationData) {
//                                val value = _orderPreference
//                                if (value.isValid) {
//                                    val curShip = value.preference.shipment
//                                    var shipping = _orderShipment
//                                    val currPromo = if (shipping.isApplyLogisticPromo) shipping.logisticPromoViewModel?.promoCode
//                                            ?: "" else ""
//                                    var shippingErrorId: String? = null
//                                    var preselectedSpId: String? = null
//
//                                    if (!shippingRecommendationData.errorId.isNullOrEmpty() && !shippingRecommendationData.errorMessage.isNullOrEmpty()) {
//                                        shipping = OrderShipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = shippingRecommendationData.errorMessage, shippingRecommendationData = null)
//                                    } else {
//                                        if (shipping.serviceId != null && shipping.shipperProductId != null) {
//                                            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
//                                            var selectedShippingDurationViewModel: ShippingDurationUiModel? = null
//                                            for (shippingDurationViewModel in shippingDurationViewModels) {
//                                                if (shippingDurationViewModel.serviceData.serviceId == shipping.serviceId) {
//                                                    shippingDurationViewModel.isSelected = true
//                                                    selectedShippingDurationViewModel = shippingDurationViewModel
//                                                    val durationError = shippingDurationViewModel.serviceData.error
//                                                    if (durationError.errorId != null && durationError.errorId.isNotBlank() && durationError.errorMessage.isNotBlank()) {
//                                                        shippingErrorId = durationError.errorId
//                                                        shipping = OrderShipment(
//                                                                serviceId = shippingDurationViewModel.serviceData.serviceId,
//                                                                serviceDuration = shippingDurationViewModel.serviceData.serviceName,
//                                                                serviceName = shippingDurationViewModel.serviceData.serviceName,
//                                                                needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
//                                                                serviceErrorMessage = durationError.errorMessage,
//                                                                shippingRecommendationData = shippingRecommendationData)
//                                                    } else {
//                                                        val shippingCourierViewModelList = shippingDurationViewModel.shippingCourierViewModelList
//                                                        var selectedShippingCourierUiModel = shippingCourierViewModelList.first()
//                                                        for (shippingCourierUiModel in shippingCourierViewModelList) {
//                                                            if (shippingCourierUiModel.productData.shipperProductId == shipping.shipperProductId) {
//                                                                shippingCourierUiModel.isSelected = true
//                                                                selectedShippingCourierUiModel = shippingCourierUiModel
//                                                            } else {
//                                                                shippingCourierUiModel.isSelected = false
//                                                            }
//                                                        }
//                                                        var flagNeedToSetPinpoint = false
//                                                        var errorMessage: String? = null
//                                                        if (selectedShippingCourierUiModel.productData.error != null && selectedShippingCourierUiModel.productData.error.errorMessage != null && selectedShippingCourierUiModel.productData.error.errorId != null) {
//                                                            shippingErrorId = selectedShippingCourierUiModel.productData.error.errorId
//                                                            errorMessage = selectedShippingCourierUiModel.productData.error.errorMessage
//                                                            if (selectedShippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
//                                                                flagNeedToSetPinpoint = true
//                                                            }
//                                                        }
//                                                        shipping = shipping.copy(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
//                                                                shipperId = selectedShippingCourierUiModel.productData.shipperId,
//                                                                ratesId = selectedShippingCourierUiModel.ratesId,
//                                                                ut = selectedShippingCourierUiModel.productData.unixTime,
//                                                                checksum = selectedShippingCourierUiModel.productData.checkSum,
//                                                                shipperName = selectedShippingCourierUiModel.productData.shipperName,
//                                                                needPinpoint = flagNeedToSetPinpoint,
//                                                                serviceErrorMessage = if (flagNeedToSetPinpoint) NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
//                                                                insuranceData = selectedShippingCourierUiModel.productData.insurance,
//                                                                serviceId = shippingDurationViewModel.serviceData.serviceId,
//                                                                serviceDuration = shippingDurationViewModel.serviceData.serviceName,
//                                                                serviceName = shippingDurationViewModel.serviceData.serviceName,
//                                                                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
//                                                                isApplyLogisticPromo = false,
//                                                                logisticPromoViewModel = null,
//                                                                logisticPromoShipping = null,
//                                                                shippingRecommendationData = shippingRecommendationData)
//                                                    }
//                                                } else {
//                                                    shippingDurationViewModel.isSelected = false
//                                                }
//                                            }
//                                            shipping = setupShippingError(selectedShippingDurationViewModel, shippingRecommendationData, shipping, curShip)
//                                        } else {
//                                            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
//                                            var selectedShippingDurationViewModel: ShippingDurationUiModel? = null
//                                            for (shippingDurationViewModel in shippingDurationViewModels) {
//                                                if (shippingDurationViewModel.serviceData.serviceId == curShip.serviceId) {
//                                                    shippingDurationViewModel.isSelected = true
//                                                    selectedShippingDurationViewModel = shippingDurationViewModel
//                                                    val durationError: ErrorServiceData? = shippingDurationViewModel.serviceData.error
//                                                    if (durationError?.errorId != null && durationError.errorId.isNotBlank() && durationError.errorMessage.isNotBlank()) {
//                                                        shippingErrorId = durationError.errorId
//                                                        shipping = OrderShipment(
//                                                                serviceId = shippingDurationViewModel.serviceData.serviceId,
//                                                                serviceDuration = shippingDurationViewModel.serviceData.serviceName,
//                                                                serviceName = shippingDurationViewModel.serviceData.serviceName,
//                                                                needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
//                                                                serviceErrorMessage = durationError.errorMessage,
//                                                                shippingRecommendationData = shippingRecommendationData)
//                                                    } else {
//                                                        val shippingCourierViewModelList = shippingDurationViewModel.shippingCourierViewModelList
//                                                        var selectedShippingCourierUiModel = shippingCourierViewModelList.first()
//                                                        for (shippingCourierUiModel in shippingCourierViewModelList) {
//                                                            if (shippingCourierUiModel.isSelected) {
//                                                                selectedShippingCourierUiModel = shippingCourierUiModel
//                                                            }
//                                                        }
//                                                        selectedShippingCourierUiModel.isSelected = true
//                                                        var flagNeedToSetPinpoint = false
//                                                        var errorMessage: String? = null
//                                                        if (selectedShippingCourierUiModel.productData.error != null && selectedShippingCourierUiModel.productData.error.errorMessage != null && selectedShippingCourierUiModel.productData.error.errorId != null) {
//                                                            shippingErrorId = selectedShippingCourierUiModel.productData.error.errorId
//                                                            errorMessage = selectedShippingCourierUiModel.productData.error.errorMessage
//                                                            if (selectedShippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
//                                                                flagNeedToSetPinpoint = true
//                                                            }
//                                                        }
//                                                        shipping = OrderShipment(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
//                                                                shipperId = selectedShippingCourierUiModel.productData.shipperId,
//                                                                ratesId = selectedShippingCourierUiModel.ratesId,
//                                                                ut = selectedShippingCourierUiModel.productData.unixTime,
//                                                                checksum = selectedShippingCourierUiModel.productData.checkSum,
//                                                                shipperName = selectedShippingCourierUiModel.productData.shipperName,
//                                                                needPinpoint = flagNeedToSetPinpoint,
//                                                                serviceErrorMessage = if (flagNeedToSetPinpoint) NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
//                                                                insuranceData = selectedShippingCourierUiModel.productData.insurance,
//                                                                serviceId = shippingDurationViewModel.serviceData.serviceId,
//                                                                serviceDuration = shippingDurationViewModel.serviceData.serviceName,
//                                                                serviceName = shippingDurationViewModel.serviceData.serviceName,
//                                                                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
//                                                                shippingRecommendationData = shippingRecommendationData)
//
//                                                        if (shipping.serviceErrorMessage.isNullOrEmpty()) {
//                                                            preselectedSpId = selectedShippingCourierUiModel.productData.shipperProductId.toString()
//                                                        }
//                                                    }
//                                                } else {
//                                                    shippingDurationViewModel.isSelected = false
//                                                }
//                                            }
//                                            shipping = setupShippingError(selectedShippingDurationViewModel, shippingRecommendationData, shipping, curShip)
//                                        }
//                                    }
//
//                                    val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
//                                    if (logisticPromo != null && !logisticPromo.disabled) {
//                                        shipping = shipping.copy(logisticPromoViewModel = logisticPromo)
//                                        if (currPromo.isNotEmpty()) {
//                                            if (logisticPromo.promoCode != currPromo) {
//                                                clearOldLogisticPromo(currPromo)
//                                            }
//                                            autoApplyLogisticPromo(logisticPromo, currPromo, shipping)
//                                            return
//                                        } else {
//                                            shipping = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromo.title}" else null,
//                                                    logisticPromoViewModel = logisticPromo, logisticPromoShipping = null, isApplyLogisticPromo = false)
//                                        }
//                                    } else {
//                                        shipping = shipping.copy(logisticPromoTickerMessage = null, logisticPromoViewModel = null, logisticPromoShipping = null, isApplyLogisticPromo = false)
//                                        if (currPromo.isNotEmpty()) {
//                                            clearOldLogisticPromo(currPromo)
//                                        }
//                                    }
//
//                                    _orderShipment = shipping
//                                    orderShipment.value = _orderShipment
//                                    sendViewOspEe()
//                                    if (shipping.serviceErrorMessage.isNullOrEmpty()) {
//                                        validateUsePromo()
//                                    } else {
//                                        orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
//                                        sendViewShippingErrorMessage(shippingErrorId)
//                                    }
//                                    sendPreselectedCourierOption(preselectedSpId)
//                                    calculateTotal()
//                                }
//                            }
//
//                            override fun onCompleted() {
//                                OccIdlingResource.decrement()
//                            }
//                        })
//        )
//    }

    private fun generateRatesParam(): RatesParam {
        return RatesParam.Builder(generateListShopShipment(), generateShippingParam()).build().apply {
            occ = "1"
        }
    }

    fun generateShippingParam(): ShippingParam {
        return ShippingParam().apply {
            val address = _orderPreference.preference.address

            originDistrictId = orderShop.districtId.toString()
            originPostalCode = orderShop.postalCode
            originLatitude = orderShop.latitude
            originLongitude = orderShop.longitude
            destinationDistrictId = address.districtId.toString()
            destinationPostalCode = address.postalCode
            destinationLatitude = address.latitude
            destinationLongitude = address.longitude
            shopId = orderShop.shopId.toString()
            token = orderKero.keroToken
            ut = orderKero.keroUT
            insurance = 1
            isPreorder = orderProduct.isPreorder != 0
            categoryIds = orderProduct.categoryId.toString()
            uniqueId = orderCart.cartString
            addressId = address.addressId
            products = listOf(Product(orderProduct.productId.toLong(), orderProduct.isFreeOngkir))
            weightInKilograms = orderProduct.quantity.orderQuantity * orderProduct.weight / 1000.0
            productInsurance = orderProduct.productFinsurance
            orderValue = orderProduct.quantity.orderQuantity * orderProduct.getPrice()
        }
    }

    fun generateListShopShipment(): List<ShopShipment> {
        return orderShop.shopShipment
    }

    private fun mapShippingRecommendationData(shippingRecommendationData: ShippingRecommendationData): ShippingRecommendationData {
        val data = ratesResponseStateConverter.fillState(shippingRecommendationData, generateListShopShipment(), _orderShipment.shipperProductId.toZeroIfNull(), _orderShipment.serviceId.toZeroIfNull())
        if (data.shippingDurationViewModels != null) {
            val logisticPromo = data.logisticPromo
            if (logisticPromo != null) {
                // validate army courier
                val serviceData: ShippingDurationUiModel? = getRatesDataFromLogisticPromo(logisticPromo.serviceId, data.shippingDurationViewModels)
                if (serviceData == null) {
                    data.logisticPromo = null
                } else if (getCourierDataBySpId(logisticPromo.shipperProductId, serviceData.shippingCourierViewModelList) == null) {
                    data.logisticPromo = null
                }
            }
        }
        return data
    }

    private fun getCourierDataBySpId(spId: Int, shippingCourierViewModels: List<ShippingCourierUiModel>): ShippingCourierUiModel? {
        return shippingCourierViewModels.firstOrNull { it.productData.shipperProductId == spId }
    }

    private fun getRatesDataFromLogisticPromo(serviceId: Int, list: List<ShippingDurationUiModel>): ShippingDurationUiModel? {
        return list.firstOrNull { it.serviceData.serviceId == serviceId }
    }

    private fun setupShippingError(selectedShippingDurationViewModel: ShippingDurationUiModel?, shippingRecommendationData: ShippingRecommendationData, shipping: OrderShipment, curShip: OrderProfileShipment): OrderShipment {
        if (selectedShippingDurationViewModel == null && shippingRecommendationData.shippingDurationViewModels.isNotEmpty()) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE)
            return OrderShipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = NO_DURATION_AVAILABLE, shippingRecommendationData = shippingRecommendationData)
        } else if (shippingRecommendationData.shippingDurationViewModels.isEmpty()) {
            return OrderShipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = NO_COURIER_SUPPORTED_ERROR_MESSAGE, shippingRecommendationData = null)
        }
        return shipping
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
        clearCacheAutoApplyStackUseCase.setParams(PARAM_VALUE_MARKETPLACE, arrayListOf(oldPromoCode), true)
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY)
                        .subscribeOn(executorSchedulers.io)
                        .observeOn(executorSchedulers.main)
                        .subscribe(object : Observer<ClearPromoUiModel?> {
                            override fun onError(e: Throwable?) {
                                // do nothing, promocode directly removed
                            }

                            override fun onNext(t: ClearPromoUiModel?) {
                                // do nothing, promocode directly removed
                            }

                            override fun onCompleted() {
                                // do nothing, promocode directly removed
                            }
                        })
        )
        val orders = lastValidateUsePromoRequest?.orders ?: emptyList()
        if (orders.isNotEmpty()) {
            orders[0]?.codes?.remove(oldPromoCode)
        }
    }

    private fun autoApplyLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String, shipping: OrderShipment) {
        orderPromo.value = orderPromo.value.copy(state = OccButtonState.LOADING)
        val requestParams = RequestParams.create()
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, generateValidateUsePromoRequestWithBbo(logisticPromoUiModel, oldCode))
        OccIdlingResource.increment()
        compositeSubscription.add(
                validateUsePromoRevampUseCase.createObservable(requestParams)
                        .subscribeOn(executorSchedulers.io)
                        .observeOn(executorSchedulers.main)
                        .subscribe(object : Observer<ValidateUsePromoRevampUiModel> {
                            override fun onError(e: Throwable) {
                                orderPromo.value = orderPromo.value.copy(state = OccButtonState.DISABLE)
                                globalEvent.value = OccGlobalEvent.Error(e)
                                _orderShipment = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromoUiModel.title}" else null, isApplyLogisticPromo = false, logisticPromoShipping = null)
                                orderShipment.value = _orderShipment
                                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                                calculateTotal()
                                OccIdlingResource.decrement()
                            }

                            override fun onNext(response: ValidateUsePromoRevampUiModel) {
                                validateUsePromoRevampUiModel = response
                                if (!onApplyBbo(shipping, logisticPromoUiModel, response)) {
                                    _orderShipment = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromoUiModel.title}" else null, isApplyLogisticPromo = false, logisticPromoShipping = null)
                                    orderShipment.value = _orderShipment
                                    globalEvent.value = OccGlobalEvent.Normal
                                    updatePromoState(response.promoUiModel)
                                }
                            }

                            override fun onCompleted() {
                                OccIdlingResource.decrement()
                            }
                        })
        )
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
        val params = AuthHelper.generateParamsNetwork(userSessionInterface.userId, userSessionInterface.deviceId, TKPDMapParam())
        val op = _orderPreference
        if (op.isValid) {
            params[EditAddressParam.ADDRESS_ID] = op.preference.address.addressId.toString()
            params[EditAddressParam.ADDRESS_NAME] = op.preference.address.addressName
            params[EditAddressParam.ADDRESS_STREET] = op.preference.address.addressStreet
            params[EditAddressParam.POSTAL_CODE] = op.preference.address.postalCode
            params[EditAddressParam.DISTRICT_ID] = op.preference.address.districtId.toString()
            params[EditAddressParam.CITY_ID] = op.preference.address.cityId.toString()
            params[EditAddressParam.PROVINCE_ID] = op.preference.address.provinceId.toString()
            params[EditAddressParam.LATITUDE] = latitude
            params[EditAddressParam.LONGITUDE] = longitude
            params[EditAddressParam.RECEIVER_NAME] = op.preference.address.receiverName
            params[EditAddressParam.RECEIVER_PHONE] = op.preference.address.phone

            val requestParams = RequestParams.create()
            requestParams.putAllString(params)

            globalEvent.value = OccGlobalEvent.Loading
            compositeSubscription.add(
                    editAddressUseCase.createObservable(requestParams)
                            .subscribeOn(executorSchedulers.io)
                            .observeOn(executorSchedulers.main)
                            .subscribe(object : Observer<String> {
                                override fun onError(e: Throwable) {
                                    globalEvent.value = OccGlobalEvent.Error(e)
                                }

                                override fun onNext(stringResponse: String) {
                                    var messageError: String? = null
                                    var statusSuccess: Boolean
                                    try {
                                        val response = JsonParser().parse(stringResponse).asJsonObject
                                        val statusCode = response.getAsJsonObject(EditAddressUseCase.RESPONSE_DATA)
                                                .get(EditAddressUseCase.RESPONSE_IS_SUCCESS).asInt
                                        statusSuccess = statusCode == 1
                                        if (!statusSuccess) {
                                            messageError = response.getAsJsonArray("message_error").get(0).asString
                                        }
                                    } catch (e: JSONException) {
                                        statusSuccess = false
                                    }

                                    if (statusSuccess) {
                                        globalEvent.value = OccGlobalEvent.TriggerRefresh(false)
                                    } else {
                                        if (messageError.isNullOrBlank()) {
                                            messageError = DEFAULT_ERROR_MESSAGE
                                        }
                                        globalEvent.value = OccGlobalEvent.Error(errorMessage = messageError)
                                    }
                                }

                                override fun onCompleted() {
                                    // do nothing
                                }
                            })
            )
        }
    }

    fun chooseLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel) {
        val shipping = _orderShipment
        val shippingRecommendationData = _orderShipment.shippingRecommendationData
        if (shippingRecommendationData != null) {
            OccIdlingResource.increment()
            globalEvent.value = OccGlobalEvent.Loading
            val requestParams = RequestParams.create()
            requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, generateValidateUsePromoRequestWithBbo(logisticPromoUiModel))
            compositeSubscription.add(
                    validateUsePromoRevampUseCase.createObservable(requestParams)
                            .subscribeOn(executorSchedulers.io)
                            .observeOn(executorSchedulers.main)
                            .subscribe(object : Observer<ValidateUsePromoRevampUiModel> {
                                override fun onError(e: Throwable) {
                                    globalEvent.value = OccGlobalEvent.Error(e)
                                    OccIdlingResource.decrement()
                                }

                                override fun onNext(response: ValidateUsePromoRevampUiModel) {
                                    validateUsePromoRevampUiModel = response
                                    if (!onApplyBbo(shipping, logisticPromoUiModel, response)) {
                                        updatePromoState(response.promoUiModel)
                                        globalEvent.value = OccGlobalEvent.Error(errorMessage = FAIL_APPLY_BBO_ERROR_MESSAGE)
                                    }
                                }

                                override fun onCompleted() {
                                    OccIdlingResource.decrement()
                                }
                            })
            )
        }
    }

    private fun onApplyBbo(shipping: OrderShipment, logisticPromoUiModel: LogisticPromoUiModel, response: ValidateUsePromoRevampUiModel): Boolean {
        if (response.status.equals(STATUS_OK, true)) {
            val voucherOrderUiModel = response.promoUiModel.voucherOrderUiModels.firstOrNull { it?.code == logisticPromoUiModel.promoCode }
            if (voucherOrderUiModel != null && voucherOrderUiModel.messageUiModel.state != "red") {
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
                        updatePromoState(response.promoUiModel)
                        return true
                    }
                }
            }
        }
        return false
    }

    fun updateCart() {
        launch(executorDispatchers.main) {
            cartProcessor.updateCartIgnoreResult(orderCart, _orderPreference, _orderShipment, _orderPayment)
        }
//        val param = generateUpdateCartParam()
//        if (param != null) {
//            updateCartOccUseCase.execute(param, {
//                //do nothing
//            }, {
//                //do nothing
//            })
//        }
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
        var param = generateUpdateCartParam()
        if (param == null) {
            globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
            return
        }
        param = param.copy(profile = UpdateCartOccProfileRequest(
                profileId = preference.profileId.toString(),
                addressId = preference.addressModel.addressId.toString(),
                serviceId = preference.shipmentModel.serviceId,
                gatewayCode = preference.paymentModel.gatewayCode,
                metadata = preference.paymentModel.metadata
        ))
        globalEvent.value = OccGlobalEvent.Loading
        updateCartOccUseCase.execute(param, {
            clearBboIfExist()
            globalEvent.value = OccGlobalEvent.TriggerRefresh()
        }, { throwable: Throwable ->
            if (throwable is MessageErrorException) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = throwable.message
                        ?: DEFAULT_ERROR_MESSAGE)
            } else {
                globalEvent.value = OccGlobalEvent.Error(throwable)
            }
        })
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
                        OccIdlingResource.increment()
                        updateCartOccUseCase.execute(param, {
                            finalValidateUse(product, shop, pref, onSuccessCheckout, skipCheckIneligiblePromo)
                            OccIdlingResource.decrement()
                        }, { throwable: Throwable ->
                            if (throwable is MessageErrorException && throwable.message != null) {
                                globalEvent.value = OccGlobalEvent.TriggerRefresh(errorMessage = throwable.message
                                        ?: DEFAULT_ERROR_MESSAGE)
                            } else {
                                globalEvent.value = OccGlobalEvent.TriggerRefresh(throwable = throwable)
                            }
                            OccIdlingResource.decrement()
                        })
                    }
                    return
                }
            }
            globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
        }
    }

    private fun finalValidateUse(product: OrderProduct, shop: OrderShop, pref: OrderPreference, onSuccessCheckout: (CheckoutOccResult) -> Unit, skipCheckIneligiblePromo: Boolean) {
        if (!skipCheckIneligiblePromo) {
            val requestParams = RequestParams.create()
            requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, generateValidateUsePromoRequest())
            OccIdlingResource.increment()
            compositeSubscription.add(
                    validateUsePromoRevampUseCase.createObservable(requestParams)
                            .subscribeOn(executorSchedulers.io)
                            .observeOn(executorSchedulers.main)
                            .subscribe(object : Observer<ValidateUsePromoRevampUiModel> {
                                override fun onError(e: Throwable) {
                                    globalEvent.value = OccGlobalEvent.TriggerRefresh(throwable = e)
                                    OccIdlingResource.decrement()
                                }

                                override fun onNext(t: ValidateUsePromoRevampUiModel) {
                                    validateUsePromoRevampUiModel = t
                                    updatePromoState(t.promoUiModel)
                                    if (checkIneligiblePromo()) {
                                        doCheckout(product, shop, pref, onSuccessCheckout)
                                    }
                                }

                                override fun onCompleted() {
                                    OccIdlingResource.decrement()
                                }
                            })
            )
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
//        val shopPromos = generateShopPromos()
//        val checkoutPromos = generateCheckoutPromos()
//        val allPromoCodes = checkoutPromos.map { it.code } + shopPromos.map { it.code }
//        val param = CheckoutOccRequest(Profile(pref.preference.profileId), ParamCart(data = listOf(ParamData(
//                pref.preference.address.addressId,
//                listOf(
//                        ShopProduct(
//                                shopId = shop.shopId,
//                                isPreorder = product.isPreorder,
//                                warehouseId = product.warehouseId,
//                                finsurance = if (_orderShipment.isCheckInsurance) 1 else 0,
//                                productData = listOf(
//                                        ProductData(
//                                                product.productId,
//                                                product.quantity.orderQuantity,
//                                                product.notes
//                                        )
//                                ),
//                                shippingInfo = ShippingInfo(
//                                        _orderShipment.getRealShipperId(),
//                                        _orderShipment.getRealShipperProductId(),
//                                        _orderShipment.getRealRatesId(),
//                                        _orderShipment.getRealUt(),
//                                        _orderShipment.getRealChecksum()
//                                ),
//                                promos = shopPromos
//                        )
//                )
//        )), promos = checkoutPromos, mode = if (orderTotal.value.isButtonChoosePayment) 1 else 0))
//        OccIdlingResource.increment()
//        checkoutOccUseCase.execute(param, { checkoutOccData: CheckoutOccData ->
//            if (checkoutOccData.status.equals(STATUS_OK, true)) {
//                if (checkoutOccData.result.success == 1 || checkoutOccData.result.paymentParameter.redirectParam.url.isNotEmpty()) {
//                    onSuccessCheckout(checkoutOccData.result)
//                    orderSummaryAnalytics.eventClickBayarSuccess(orderTotal.value.isButtonChoosePayment, getTransactionId(checkoutOccData.result.paymentParameter.redirectParam.form), generateOspEe(OrderSummaryPageEnhanceECommerce.STEP_2, OrderSummaryPageEnhanceECommerce.STEP_2_OPTION, allPromoCodes))
//                } else {
//                    val error = checkoutOccData.result.error
//                    val errorCode = error.code
//                    orderSummaryAnalytics.eventClickBayarNotSuccess(orderTotal.value.isButtonChoosePayment, errorCode)
//                    if (checkoutOccData.result.prompt.shouldShowPrompt()) {
//                        globalEvent.value = OccGlobalEvent.Prompt(checkoutOccData.result.prompt)
//                    } else if (errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_ERROR || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_SHOP_CLOSED) {
//                        globalEvent.value = OccGlobalEvent.CheckoutError(error)
//                    } else if (errorCode == ERROR_CODE_PRICE_CHANGE) {
//                        globalEvent.value = OccGlobalEvent.PriceChangeError(PriceChangeMessage(PRICE_CHANGE_ERROR_MESSAGE, error.message, PRICE_CHANGE_ACTION_MESSAGE))
//                    } else if (error.message.isNotBlank()) {
//                        globalEvent.value = OccGlobalEvent.TriggerRefresh(errorMessage = error.message)
//                    } else {
//                        globalEvent.value = OccGlobalEvent.TriggerRefresh(errorMessage = "Terjadi kesalahan dengan kode $errorCode")
//                    }
//                }
//            } else {
//                globalEvent.value = OccGlobalEvent.TriggerRefresh(errorMessage = checkoutOccData.headerMessage ?: DEFAULT_ERROR_MESSAGE)
//            }
//            OccIdlingResource.decrement()
//        }, { throwable: Throwable ->
//            globalEvent.value = OccGlobalEvent.Error(throwable)
//            OccIdlingResource.decrement()
//        })
    }

    private fun generateShopPromos(): List<com.tokopedia.oneclickcheckout.order.data.checkout.PromoRequest> {
        val finalPromo = validateUsePromoRevampUiModel
        if (finalPromo != null) {
            val list: ArrayList<com.tokopedia.oneclickcheckout.order.data.checkout.PromoRequest> = ArrayList()
            for (voucherOrder in finalPromo.promoUiModel.voucherOrderUiModels) {
                if (orderCart.cartString == voucherOrder?.uniqueId && voucherOrder.messageUiModel.state != "red" &&
                        voucherOrder.code.isNotEmpty() && voucherOrder.type.isNotEmpty()) {
                    list.add(PromoRequest(voucherOrder.type, voucherOrder.code))
                }
            }
            return list
        }
        return emptyList()
    }

    private fun generateCheckoutPromos(): List<com.tokopedia.oneclickcheckout.order.data.checkout.PromoRequest> {
        val list = ArrayList<com.tokopedia.oneclickcheckout.order.data.checkout.PromoRequest>()
        val finalPromo = validateUsePromoRevampUiModel
        if (finalPromo != null && finalPromo.promoUiModel.codes.isNotEmpty() && finalPromo.promoUiModel.messageUiModel.state != "red") {
            for (code in finalPromo.promoUiModel.codes) {
                list.add(PromoRequest("global", code))
            }
        }
        return list
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
        val promoCodeList = ArrayList(notEligiblePromoHolderdataList.map { it.promoCode })
        clearCacheAutoApplyStackUseCase.setParams(PARAM_VALUE_MARKETPLACE, promoCodeList, true)
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY)
                        .subscribeOn(executorSchedulers.io)
                        .observeOn(executorSchedulers.main)
                        .subscribe(object : Observer<ClearPromoUiModel?> {
                            override fun onError(e: Throwable?) {
                                globalEvent.value = OccGlobalEvent.Error(e)
                            }

                            override fun onNext(t: ClearPromoUiModel?) {
                                if (_orderPreference.isValid) {
                                    finalUpdate(onSuccessCheckout, true)
                                }
                            }

                            override fun onCompleted() {
                                //do nothing
                            }
                        })
        )
    }

    fun updateCartPromo(onSuccess: (ValidateUsePromoRequest, PromoRequest, ArrayList<String>) -> Unit) {
        val param = generateUpdateCartParam()
        if (param == null) {
            globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
            return
        }
        globalEvent.value = OccGlobalEvent.Loading
        updateCartOccUseCase.execute(param, {
            globalEvent.value = OccGlobalEvent.Normal
            onSuccess(generateValidateUsePromoRequest(), generatePromoRequest(), generateBboPromoCodes())
        }, { throwable: Throwable ->
            if (throwable is MessageErrorException) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = throwable.message
                        ?: DEFAULT_ERROR_MESSAGE)
            } else {
                globalEvent.value = OccGlobalEvent.Error(throwable)
            }
        })
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
        orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
        orderPromo.value = orderPromo.value.copy(state = OccButtonState.LOADING)
        val validateUsePromoRequest = generateValidateUsePromoRequest()
        val requestParams = RequestParams.create()
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest)
        OccIdlingResource.increment()
        compositeSubscription.add(
                validateUsePromoRevampUseCase.createObservable(requestParams)
                        .subscribeOn(executorSchedulers.io)
                        .observeOn(executorSchedulers.main)
                        .subscribe(object : Observer<ValidateUsePromoRevampUiModel> {
                            override fun onError(e: Throwable) {
                                orderPromo.value = orderPromo.value.copy(state = OccButtonState.DISABLE)
                                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                                OccIdlingResource.decrement()
                            }

                            override fun onNext(result: ValidateUsePromoRevampUiModel) {
                                var isPromoReleased = false
                                val lastResult = validateUsePromoRevampUiModel
                                if (!lastResult?.promoUiModel?.codes.isNullOrEmpty() && result.promoUiModel.codes.isNotEmpty() && result.promoUiModel.messageUiModel.state == "red") {
                                    isPromoReleased = true
                                } else {
                                    result.promoUiModel.voucherOrderUiModels.firstOrNull { it?.messageUiModel?.state == "red" }?.let {
                                        isPromoReleased = true
                                    }
                                }
                                if (isPromoReleased) {
                                    orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
                                } else if (lastResult != null && result.promoUiModel.benefitSummaryInfoUiModel.finalBenefitAmount < lastResult.promoUiModel.benefitSummaryInfoUiModel.finalBenefitAmount) {
                                    orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(false)
                                }

                                validateUsePromoRevampUiModel = result
                                updatePromoState(result.promoUiModel)
                            }

                            override fun onCompleted() {
                                OccIdlingResource.decrement()
                            }
                        })
        )
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
        var param = generateUpdateCartParam()
        val creditCard = _orderPayment.creditCard
        if (param == null) {
            globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
            return
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
            return
        }
        updateCartOccUseCase.execute(param, {
            val availableTerms = creditCard.availableTerms
            availableTerms.forEach {
                it.isSelected = it.term == selectedInstallmentTerm.term
                it.isError = false
            }
            _orderPayment = _orderPayment.copy(creditCard = creditCard.copy(selectedTerm = selectedInstallmentTerm, availableTerms = availableTerms))
            orderTotal.value = orderTotal.value.copy(buttonState = if (shouldButtonStateEnable(_orderShipment)) OccButtonState.NORMAL else OccButtonState.DISABLE)
            calculateTotal()
            globalEvent.value = OccGlobalEvent.Normal
        }, {
            if (it is MessageErrorException) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = it.message
                        ?: DEFAULT_ERROR_MESSAGE)
            } else {
                globalEvent.value = OccGlobalEvent.Error(it)
            }
        })
    }

    fun updateCreditCard(metadata: String) {
        var param = generateUpdateCartParam()
        if (param == null) {
            globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
            return
        }
        param = param.copy(profile = param.profile.copy(metadata = metadata))
        globalEvent.value = OccGlobalEvent.Loading
        updateCartOccUseCase.execute(param, {
            clearBboIfExist()
            globalEvent.value = OccGlobalEvent.TriggerRefresh()
        }, { throwable: Throwable ->
            if (throwable is MessageErrorException) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = throwable.message
                        ?: DEFAULT_ERROR_MESSAGE)
            } else {
                globalEvent.value = OccGlobalEvent.Error(throwable)
            }
        })
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
        compositeSubscription.clear()
        debounceJob?.cancel()
    }

    private fun sendViewOspEe() {
        if (!hasSentViewOspEe) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(generateOspEe(OrderSummaryPageEnhanceECommerce.STEP_1, OrderSummaryPageEnhanceECommerce.STEP_1_OPTION))
            hasSentViewOspEe = true
        }
    }

    private fun generateOspEe(step: Int, option: String, promoCodes: List<String> = emptyList()): Map<String, Any> {
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
        }.build(step, option)
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

    private fun getTransactionId(query: String): String {
        val keyLength = TRANSACTION_ID_KEY.length
        val keyIndex = query.indexOf(TRANSACTION_ID_KEY)
        if (keyIndex > -1 && query[keyIndex + keyLength] == '=') {
            val nextAmpersand = query.indexOf('&', keyIndex)
            val end = if (nextAmpersand > -1) nextAmpersand else query.length
            val start = keyIndex + keyLength + 1

            if (end > start) {
                return query.substring(start, end)
            }
        }
        return ""
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