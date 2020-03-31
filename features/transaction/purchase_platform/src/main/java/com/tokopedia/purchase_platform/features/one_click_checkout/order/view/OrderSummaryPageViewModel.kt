package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase.Companion.PARAM_VALUE_MARKETPLACE
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.PARAM_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.PARAM_OCC
import com.tokopedia.purchase_platform.common.data.model.param.EditAddressParam
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.Message
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.PriceValidation
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata.Companion.TYPE_ICON_GLOBAL
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata.Companion.TYPE_ICON_OFFICIAL_STORE
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata.Companion.TYPE_ICON_POWER_MERCHANT
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccGlobalEvent
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics.OrderSummaryPageEnhanceECommerce
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccCartRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccProfileRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.*
import com.tokopedia.purchase_platform.features.one_click_checkout.order.domain.CheckoutOccUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.domain.GetOccCartUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.domain.UpdateCartOccUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.ErrorCheckoutBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.*
import com.tokopedia.purchase_platform.features.promo.data.request.Order
import com.tokopedia.purchase_platform.features.promo.data.request.ProductDetail
import com.tokopedia.purchase_platform.features.promo.data.request.PromoRequest
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.OrdersItem
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ProductDetailsItem
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.features.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.PromoUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class OrderSummaryPageViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                    private val getOccCartUseCase: GetOccCartUseCase,
                                                    private val ratesUseCase: GetRatesUseCase,
                                                    val getPreferenceListUseCase: GetPreferenceListUseCase,
                                                    private val updateCartOccUseCase: UpdateCartOccUseCase,
                                                    private val ratesResponseStateConverter: RatesResponseStateConverter,
                                                    private val editAddressUseCase: EditAddressUseCase,
                                                    private val checkoutOccUseCase: CheckoutOccUseCase,
                                                    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                                    private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
                                                    private val userSessionInterface: UserSessionInterface,
                                                    val orderSummaryAnalytics: OrderSummaryAnalytics) : BaseViewModel(dispatcher) {

    var orderProduct: OrderProduct = OrderProduct()
    var orderShop: OrderShop = OrderShop()
    var kero: Kero = Kero()
    var profileIndex: String? = null
    var _orderPreference: OrderPreference? = null

    var orderPromo: MutableLiveData<OrderPromo> = MutableLiveData(OrderPromo())
    var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = null
    var lastValidateUsePromoRequest: ValidateUsePromoRequest? = null

    var orderPreference: MutableLiveData<OccState<OrderPreference>> = MutableLiveData(OccState.Loading)
    var orderTotal: MutableLiveData<OrderTotal> = MutableLiveData(OrderTotal())
    var globalEvent: MutableLiveData<OccGlobalEvent> = MutableLiveData(OccGlobalEvent.Normal)

    private var compositeSubscription = CompositeSubscription()
    private var debounceJob: Job? = null

    private var hasSentViewOspEe = false

    fun getOccCart(isFullRefresh: Boolean = true) {
        globalEvent.value = OccGlobalEvent.Normal
        getOccCartUseCase.execute({ orderData: OrderData ->
            orderProduct = orderData.cart.product
            orderShop = orderData.cart.shop
            kero = orderData.cart.kero
            profileIndex = orderData.profileIndex
            val preference = orderData.preference
            _orderPreference = if (isFullRefresh || _orderPreference == null) {
                OrderPreference(profileIndex, preference)
            } else {
                _orderPreference?.copy(preference = preference)
            }
//            _orderPreference = OrderPreference(preference)
            orderPreference.value = OccState.FirstLoad(_orderPreference!!)
            validateUsePromoRevampUiModel = null
            lastValidateUsePromoRequest = null
            orderPromo.value = orderData.promo.copy(state = ButtonBayarState.NORMAL)
            if (orderProduct.productId > 0 && preference.shipment.serviceId > 0) {
                orderTotal.value = orderTotal.value?.copy(buttonState = ButtonBayarState.LOADING)
                getRates()
            } else {
                orderTotal.value = orderTotal.value?.copy(buttonState = ButtonBayarState.DISABLE)
            }
        }, { throwable: Throwable ->
            _orderPreference = null
            orderPreference.value = OccState.Fail(false, throwable, "")
            throwable.printStackTrace()
        })
    }

    private fun generateOspEe(step: Int, option: String): Map<String, Any> {
        val orderSummaryPageEnhanceECommerce = OrderSummaryPageEnhanceECommerce()
        orderSummaryPageEnhanceECommerce.setName(orderProduct.productName)
        orderSummaryPageEnhanceECommerce.setId(orderProduct.productId)
        orderSummaryPageEnhanceECommerce.setPrice(orderProduct.productPrice)
        orderSummaryPageEnhanceECommerce.setBrand(null)
        orderSummaryPageEnhanceECommerce.setCategory(null)
        orderSummaryPageEnhanceECommerce.setVariant(null)
        orderSummaryPageEnhanceECommerce.setQuantity(orderProduct.quantity?.orderQuantity
                ?: orderProduct.minOrderQuantity)
        orderSummaryPageEnhanceECommerce.setListName(orderProduct.productResponse.productTrackerData.trackerListName)
        orderSummaryPageEnhanceECommerce.setAttribution(orderProduct.productResponse.productTrackerData.attribution)
        orderSummaryPageEnhanceECommerce.setDiscountedPrice(orderProduct.productResponse.isSlashPrice)
        orderSummaryPageEnhanceECommerce.setWarehouseId(orderProduct.productResponse.wareHouseId)
        orderSummaryPageEnhanceECommerce.setWarehouseId(orderProduct.weight)
        orderSummaryPageEnhanceECommerce.setPromoCode("")
        orderSummaryPageEnhanceECommerce.setPromoDetails("")
        orderSummaryPageEnhanceECommerce.setCartId(orderShop.cartResponse.cartId)
        orderSummaryPageEnhanceECommerce.setBuyerAddressId(_orderPreference?.preference?.address?.addressId
                ?: 0)
        orderSummaryPageEnhanceECommerce.setSpid(_orderPreference?.shipping?.getRealShipperProductId()
                ?: 0)
        orderSummaryPageEnhanceECommerce.setCodFlag(false)
        orderSummaryPageEnhanceECommerce.setCornerFlag(false)
        orderSummaryPageEnhanceECommerce.setIsFullfilment(false)
        orderSummaryPageEnhanceECommerce.setShopId(orderShop.shopId)
        orderSummaryPageEnhanceECommerce.setShopName(orderShop.shopName)
        orderSummaryPageEnhanceECommerce.setShopType(orderShop.isOfficial, orderShop.isGold)
        orderSummaryPageEnhanceECommerce.setCategoryId(orderProduct.productResponse.categoryId)
        return orderSummaryPageEnhanceECommerce.build(step, option)
    }

    fun updateProduct(product: OrderProduct, shouldReloadRates: Boolean = true) {
        orderProduct = product
        if (shouldReloadRates) {
            calculateTotal()
            if (product.quantity?.isStateError == false) {
                orderTotal.value = orderTotal.value?.copy(buttonState = ButtonBayarState.LOADING)
                debounce()
            }
        }
    }

    private fun debounce() {
        debounceJob?.cancel()
        debounceJob = launch {
            delay(1000)
            if (isActive) {
                getRates()
            }
        }
    }

    private fun getRates() {
        val s = _orderPreference?.shipping
        if (s != null && s.shippingRecommendationData?.logisticPromo?.isApplied == true && s.isApplyLogisticPromo) {
            clearCacheAutoApplyStackUseCase.setParams(PARAM_VALUE_MARKETPLACE, arrayListOf(s.shippingRecommendationData.logisticPromo?.promoCode
                    ?: ""))
            compositeSubscription.add(
                    clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY).subscribe(object : Observer<ClearPromoUiModel?> {
                        override fun onError(e: Throwable?) {
                            e?.printStackTrace()
                        }

                        override fun onNext(t: ClearPromoUiModel?) {
                        }

                        override fun onCompleted() {
                        }
                    })
            )
            val orders = lastValidateUsePromoRequest?.orders ?: emptyList()
            if (orders.isNotEmpty()) {
                orders[0]?.codes?.remove(s.logisticPromoViewModel?.promoCode)
            }
        }
        // get rates
        val shippingParam = generateShippingParam()
        val ratesParamBuilder = RatesParam.Builder(generateListShopShipment(), shippingParam)
        val ratesParam = ratesParamBuilder.build()
        ratesParam.occ = "1"
        compositeSubscription.add(
                ratesUseCase.execute(ratesParam)
                        .map {
                            val value = _orderPreference
                            val data = ratesResponseStateConverter.fillState(it, generateListShopShipment(), value?.shipping?.shipperProductId
                                    ?: 0, value?.shipping?.serviceId ?: 0)
                            if (data.shippingDurationViewModels != null) {
                                val logisticPromo = data.logisticPromo
                                if (logisticPromo != null) {
                                    // validate army courier
                                    val serviceData: ShippingDurationUiModel? = getRatesDataFromLogisticPromo(logisticPromo.serviceId, data.shippingDurationViewModels)
                                    if (serviceData == null) {
                                        data.logisticPromo = null
                                    } else {
                                        val courierData: ShippingCourierUiModel? = getCourierDatabySpId(logisticPromo.shipperProductId, serviceData.shippingCourierViewModelList)
                                        if (courierData == null) {
                                            data.logisticPromo = null
                                        }
                                    }
                                }
                            }
                            return@map data
                        }.subscribe(object : Observer<ShippingRecommendationData> {
                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                _orderPreference = _orderPreference?.copy(shipping = Shipment(
                                        serviceName = _orderPreference?.preference?.shipment?.serviceName,
                                        serviceDuration = _orderPreference?.preference?.shipment?.serviceDuration,
                                        serviceErrorMessage = "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda.",
                                        shippingRecommendationData = null
                                ))
                                orderPreference.value = OccState.Success(_orderPreference!!)
                                orderTotal.value = orderTotal.value?.copy(buttonState = ButtonBayarState.DISABLE)
                            }

                            override fun onNext(shippingRecommendationData: ShippingRecommendationData) {
                                val value = _orderPreference
                                if (value != null) {
                                    val curShip = value.preference.shipment
                                    var shipping = value.shipping

                                    if (!shippingRecommendationData.errorId.isNullOrEmpty() && !shippingRecommendationData.errorMessage.isNullOrEmpty()) {
                                        shipping = Shipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = shippingRecommendationData.errorMessage, shippingRecommendationData = null)
                                    } else {
                                        if (shipping?.serviceId != null && shipping.shipperProductId != null) {
                                            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
                                            var selectedShippingDurationViewModel: ShippingDurationUiModel? = null
                                            for (shippingDurationViewModel in shippingDurationViewModels) {
                                                if (shippingDurationViewModel.serviceData.serviceId == shipping!!.serviceId) {
                                                    shippingDurationViewModel.isSelected = true
                                                    selectedShippingDurationViewModel = shippingDurationViewModel
                                                    val durationError = shippingDurationViewModel.serviceData.error
                                                    if (durationError.errorId != null && durationError.errorId.isNotBlank() && durationError.errorMessage.isNotBlank()) {
                                                        val tempServiceDuration = shippingDurationViewModel.serviceData.serviceName
                                                        val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                                                            tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")"))
                                                        } else {
                                                            "Durasi tergantung kurir"
                                                        }
                                                        shipping = Shipment(
                                                                serviceId = shippingDurationViewModel.serviceData.serviceId,
                                                                serviceDuration = serviceDur,
                                                                serviceName = shippingDurationViewModel.serviceData.serviceName,
                                                                needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                                                                serviceErrorMessage = durationError.errorMessage,
                                                                shippingRecommendationData = shippingRecommendationData)
                                                    } else {
                                                        val shippingCourierViewModelList = shippingDurationViewModel.shippingCourierViewModelList
                                                        var selectedShippingCourierUiModel: ShippingCourierUiModel? = null
                                                        for (shippingCourierUiModel in shippingCourierViewModelList) {
                                                            if (shippingCourierUiModel.productData.shipperProductId == shipping.shipperProductId) {
                                                                shippingCourierUiModel.isSelected = true
                                                                selectedShippingCourierUiModel = shippingCourierUiModel
                                                            } else {
                                                                shippingCourierUiModel.isSelected = false
                                                            }
                                                        }
                                                        if (selectedShippingCourierUiModel != null) {
                                                            var flagNeedToSetPinpoint = false
                                                            var errorMessage: String? = null
                                                            if (selectedShippingCourierUiModel.productData.error != null && selectedShippingCourierUiModel.productData.error.errorMessage != null && selectedShippingCourierUiModel.productData.error.errorId != null) {
                                                                errorMessage = selectedShippingCourierUiModel.productData.error.errorMessage
                                                                if (selectedShippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                                                                    flagNeedToSetPinpoint = true
                                                                }
                                                            }
                                                            val tempServiceDuration = shippingDurationViewModel.serviceData.serviceName
                                                            val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                                                                tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")"))
                                                            } else {
                                                                "Durasi tergantung kurir"
                                                            }
                                                            shipping = shipping.copy(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                                                                    shipperId = selectedShippingCourierUiModel.productData.shipperId,
                                                                    ratesId = selectedShippingCourierUiModel.ratesId,
                                                                    ut = selectedShippingCourierUiModel.productData.unixTime,
                                                                    checksum = selectedShippingCourierUiModel.productData.checkSum,
                                                                    shipperName = selectedShippingCourierUiModel.productData.shipperName,
                                                                    needPinpoint = flagNeedToSetPinpoint,
                                                                    serviceErrorMessage = if (flagNeedToSetPinpoint) "Butuh pinpoint lokasi" else errorMessage,
                                                                    insuranceData = selectedShippingCourierUiModel.productData.insurance,
                                                                    serviceId = shippingDurationViewModel.serviceData.serviceId,
                                                                    serviceDuration = serviceDur,
                                                                    serviceName = shippingDurationViewModel.serviceData.serviceName,
                                                                    shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                                                    shippingRecommendationData = shippingRecommendationData)
                                                        }
                                                    }
                                                } else {
                                                    shippingDurationViewModel.isSelected = false
                                                }
                                            }
                                            if (selectedShippingDurationViewModel == null && shippingRecommendationData.shippingDurationViewModels.isNotEmpty()) {
                                                shipping = Shipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = "durasi tidak tersedia", shippingRecommendationData = shippingRecommendationData)
                                            } else if (shippingRecommendationData.shippingDurationViewModels.isEmpty()) {
                                                shipping = Shipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda.", shippingRecommendationData = null)
                                            }
                                        } else {
                                            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
                                            var selectedShippingDurationViewModel: ShippingDurationUiModel? = null
                                            for (shippingDurationViewModel in shippingDurationViewModels) {
                                                if (shippingDurationViewModel.serviceData.serviceId == curShip.serviceId) {
                                                    shippingDurationViewModel.isSelected = true
                                                    selectedShippingDurationViewModel = shippingDurationViewModel
                                                    val durationError = shippingDurationViewModel.serviceData.error
                                                    if (durationError.errorId != null && durationError.errorId.isNotBlank() && durationError.errorMessage.isNotBlank()) {
                                                        val tempServiceDuration = shippingDurationViewModel.serviceData.serviceName
                                                        val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                                                            tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")"))
                                                        } else {
                                                            "Durasi tergantung kurir"
                                                        }
                                                        shipping = Shipment(
                                                                serviceId = shippingDurationViewModel.serviceData.serviceId,
                                                                serviceDuration = serviceDur,
                                                                serviceName = shippingDurationViewModel.serviceData.serviceName,
                                                                needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                                                                serviceErrorMessage = durationError.errorMessage,
                                                                shippingRecommendationData = shippingRecommendationData)
                                                    } else {
                                                        val shippingCourierViewModelList = shippingDurationViewModel.shippingCourierViewModelList
                                                        var selectedShippingCourierUiModel: ShippingCourierUiModel? = null
                                                        for (shippingCourierUiModel in shippingCourierViewModelList) {
                                                            if (shippingCourierUiModel.isSelected) {
                                                                selectedShippingCourierUiModel = shippingCourierUiModel
                                                            }
                                                        }
                                                        if (selectedShippingCourierUiModel == null) {
                                                            selectedShippingCourierUiModel = shippingCourierViewModelList[0]
                                                        }
                                                        if (selectedShippingCourierUiModel != null) {
                                                            selectedShippingCourierUiModel.isSelected = true
                                                            var flagNeedToSetPinpoint = false
                                                            var errorMessage: String? = null
                                                            if (selectedShippingCourierUiModel.productData.error != null && selectedShippingCourierUiModel.productData.error.errorMessage != null && selectedShippingCourierUiModel.productData.error.errorId != null) {
                                                                errorMessage = selectedShippingCourierUiModel.productData.error.errorMessage
                                                                if (selectedShippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                                                                    flagNeedToSetPinpoint = true
                                                                }
                                                            }
                                                            val tempServiceDuration = shippingDurationViewModel.serviceData.serviceName
                                                            val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                                                                tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")"))
                                                            } else {
                                                                "Durasi tergantung kurir"
                                                            }
                                                            shipping = Shipment(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                                                                    shipperId = selectedShippingCourierUiModel.productData.shipperId,
                                                                    ratesId = selectedShippingCourierUiModel.ratesId,
                                                                    ut = selectedShippingCourierUiModel.productData.unixTime,
                                                                    checksum = selectedShippingCourierUiModel.productData.checkSum,
                                                                    shipperName = selectedShippingCourierUiModel.productData.shipperName,
                                                                    needPinpoint = flagNeedToSetPinpoint,
                                                                    serviceErrorMessage = if (flagNeedToSetPinpoint) "Butuh pinpoint lokasi" else errorMessage,
                                                                    insuranceData = selectedShippingCourierUiModel.productData.insurance,
                                                                    serviceId = shippingDurationViewModel.serviceData.serviceId,
                                                                    serviceDuration = serviceDur,
                                                                    serviceName = shippingDurationViewModel.serviceData.serviceName,
                                                                    shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                                                    shippingRecommendationData = shippingRecommendationData)
                                                        }
                                                    }
                                                } else {
                                                    shippingDurationViewModel.isSelected = false
                                                }
                                            }
                                            if (selectedShippingDurationViewModel == null && shippingRecommendationData.shippingDurationViewModels.isNotEmpty()) {
                                                shipping = Shipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = "durasi tidak tersedia", shippingRecommendationData = shippingRecommendationData)
                                            } else if (shippingRecommendationData.shippingDurationViewModels.isEmpty()) {
                                                shipping = Shipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda.", shippingRecommendationData = null)
                                            }
                                        }
                                    }

                                    //BBO
                                    //reset last BBO
                                    if (shipping?.logisticPromoViewModel != null) {
                                        shipping = shipping.copy(logisticPromoTickerMessage = null, logisticPromoViewModel = null, logisticPromoShipping = null, isApplyLogisticPromo = false)
                                    }
                                    if (shipping?.serviceErrorMessage?.isEmpty() == true) {
                                        val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                                        if (logisticPromo != null && !logisticPromo.disabled) {
                                            shipping = shipping.copy(logisticPromoTickerMessage = "Tersedia ${logisticPromo.title}", logisticPromoViewModel = logisticPromo, logisticPromoShipping = null)
                                        }
                                    } else if (shipping != null) {
                                        shipping = shipping.copy(logisticPromoTickerMessage = null, logisticPromoViewModel = null, logisticPromoShipping = null)
                                    }

                                    _orderPreference = value.copy(shipping = shipping)
                                    orderPreference.value = OccState.Success(_orderPreference!!)
                                    if (shipping?.serviceErrorMessage.isNullOrEmpty()) {
                                        validateUsePromo()
                                    } else {
                                        orderTotal.value = orderTotal.value?.copy(buttonState = if (shipping?.serviceErrorMessage.isNullOrEmpty() && orderProduct.quantity?.isStateError == false) ButtonBayarState.NORMAL else ButtonBayarState.DISABLE)
                                    }
                                    if (!hasSentViewOspEe) {
                                        orderSummaryAnalytics.eventViewOrderSummaryPage(generateOspEe(1, "order summary page loaded"))
                                        hasSentViewOspEe = true
                                    }
                                    calculateTotal()
                                }
                            }

                            override fun onCompleted() {

                            }
                        })
        )
    }

    fun generateShippingParam(): ShippingParam {
        val shippingParam = ShippingParam()
        shippingParam.originDistrictId = orderShop.districtId.toString()
        shippingParam.originPostalCode = orderShop.postalCode
        shippingParam.originLatitude = orderShop.latitude
        shippingParam.originLongitude = orderShop.longitude
        shippingParam.destinationDistrictId = _orderPreference?.preference?.address?.districtId?.toString()
        shippingParam.destinationPostalCode = _orderPreference?.preference?.address?.postalCode
        shippingParam.destinationLatitude = _orderPreference?.preference?.address?.latitude
        shippingParam.destinationLongitude = _orderPreference?.preference?.address?.longitude
        shippingParam.shopId = orderShop.shopId.toString()
        shippingParam.token = kero.keroToken
        shippingParam.ut = kero.keroUT
        shippingParam.insurance = 1
        shippingParam.isPreorder = orderShop.cartResponse.product.isPreorder != 0
        shippingParam.categoryIds = orderShop.cartResponse.product.categoryId.toString()
        shippingParam.uniqueId = orderShop.cartResponse.cartId.toString()
        shippingParam.addressId = _orderPreference?.preference?.address?.addressId ?: 0
        shippingParam.products = listOf(Product(orderProduct.productId.toLong(), orderProduct.isFreeOngkir))

        shippingParam.weightInKilograms = orderProduct.quantity!!.orderQuantity * orderProduct.weight / 1000.0
        shippingParam.productInsurance = orderShop.cartResponse.product.productFinsurance
        var productPrice = orderProduct.productPrice.toLong()
        if (orderProduct.wholesalePrice.isNotEmpty()) {
            for (wholesalePrice in orderProduct.wholesalePrice) {
                if (orderProduct.quantity!!.orderQuantity >= wholesalePrice.qtyMin) {
                    productPrice = wholesalePrice.prdPrc.toLong()
                }
            }
        }
        val totalProductPrice = orderProduct.quantity!!.orderQuantity * productPrice
        shippingParam.orderValue = totalProductPrice
        return shippingParam
    }

    fun generateListShopShipment(): List<ShopShipment> {
        return orderShop.shopShipment
    }

    private fun getCourierDatabySpId(spId: Int, shippingCourierViewModels: List<ShippingCourierUiModel>): ShippingCourierUiModel? {
        return shippingCourierViewModels.firstOrNull { it.productData.shipperProductId == spId }
    }

    private fun getRatesDataFromLogisticPromo(serviceId: Int, list: List<ShippingDurationUiModel>): ShippingDurationUiModel? {
        list.firstOrNull { it.serviceData.serviceId == serviceId }
                ?.let {
                    return it
                }
        return null
    }

    fun calculateTotal() {
        val quantity = orderProduct.quantity
        if (quantity != null) {
            var productPrice = orderProduct.productPrice.toDouble()
            if (orderProduct.wholesalePrice.isNotEmpty()) {
                for (wholesalePrice in orderProduct.wholesalePrice) {
                    if (quantity.orderQuantity >= wholesalePrice.qtyMin) {
                        productPrice = wholesalePrice.prdPrc.toDouble()
                    }
                }
            }
            val totalProductPrice = quantity.orderQuantity * productPrice
            val shipping = _orderPreference?.shipping
            val totalShippingPrice: Double = if (shipping?.logisticPromoShipping != null && shipping.isApplyLogisticPromo) {
                shipping.logisticPromoShipping.productData.price.price.toDouble()
            } else if (shipping?.shippingPrice != null) {
                shipping.shippingPrice.toDouble()
            } else 0.0
            var insurancePrice = 0.0
            if (shipping?.isCheckInsurance == true && shipping.insuranceData != null) {
                insurancePrice = shipping.insuranceData.insurancePrice.toDouble()
            }
            val benefitAmount = if (shipping?.logisticPromoViewModel != null && shipping.isApplyLogisticPromo) shipping.logisticPromoViewModel.benefitAmount else 0
            val fee = _orderPreference?.preference?.payment?.fee?.toDouble() ?: 0.0
            val finalShippingPrice = if ((totalShippingPrice - benefitAmount) < 0) 0.0 else (totalShippingPrice - benefitAmount)
            val subtotal = totalProductPrice + finalShippingPrice + insurancePrice + fee
            val minimumAmount = _orderPreference?.preference?.payment?.minimumAmount ?: 0
            val maximumAmount = _orderPreference?.preference?.payment?.maximumAmount ?: 0
            val orderCost = OrderCost(totalProductPrice, subtotal, totalShippingPrice, insurancePrice, fee, benefitAmount, validateUsePromoRevampUiModel?.promoUiModel?.benefitSummaryInfoUiModel?.finalBenefitText
                    ?: "", validateUsePromoRevampUiModel?.promoUiModel?.benefitSummaryInfoUiModel?.finalBenefitAmount
                    ?: 0)
            var currentState = orderTotal.value?.buttonState ?: ButtonBayarState.NORMAL
            if (currentState == ButtonBayarState.NORMAL && quantity.isStateError) {
                currentState = ButtonBayarState.DISABLE
            }
            if (minimumAmount > subtotal) {
                orderTotal.value = orderTotal.value?.copy(orderCost = orderCost,
                        paymentErrorMessage = "Belanjaanmu kurang dari min. transaksi ${_orderPreference?.preference?.payment?.gatewayName}. Silahkan pilih pembayaran lain.",
                        isButtonChoosePayment = true, buttonState = currentState)
            } else if (maximumAmount > 0 && maximumAmount < subtotal) {
                orderTotal.value = orderTotal.value?.copy(orderCost = orderCost,
                        paymentErrorMessage = "Belanjaanmu melebihi limit transaksi ${_orderPreference?.preference?.payment?.gatewayName}. Silahkan pilih pembayaran lain.",
                        isButtonChoosePayment = true, buttonState = currentState)
            } else if (_orderPreference?.preference?.payment?.gatewayCode?.contains("OVO") == true && subtotal > _orderPreference?.preference?.payment?.walletAmount ?: 0) {
                orderTotal.value = orderTotal.value?.copy(orderCost = orderCost,
                        paymentErrorMessage = "OVO Cash kamu tidak cukup. Silahkan pilih pembayaran lain.",
                        isButtonChoosePayment = true, buttonState = currentState)
            } else {
                orderTotal.value = orderTotal.value?.copy(orderCost = orderCost, paymentErrorMessage = null, isButtonChoosePayment = false, buttonState = currentState)
            }
            return
        }
        orderTotal.value = orderTotal.value?.copy(orderCost = OrderCost(), buttonState = ButtonBayarState.DISABLE)
    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.clear()
        debounceJob?.cancel()
    }

    fun chooseCourier(choosenShippingCourierViewModel: ShippingCourierUiModel) {
        val value = _orderPreference
        val shippingRecommendationData = value?.shipping?.shippingRecommendationData
        val curShip = value?.preference?.shipment
        val shipping = value?.shipping
        if (shippingRecommendationData != null && curShip != null && shipping != null) {
            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
            if (shippingRecommendationData.logisticPromo?.isApplied == true && shipping.isApplyLogisticPromo) {
                clearCacheAutoApplyStackUseCase.setParams(PARAM_VALUE_MARKETPLACE, arrayListOf(shippingRecommendationData.logisticPromo?.promoCode
                        ?: ""))
                compositeSubscription.add(
                        clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY).subscribe(object : Observer<ClearPromoUiModel?> {
                            override fun onError(e: Throwable?) {
                                e?.printStackTrace()
                            }

                            override fun onNext(t: ClearPromoUiModel?) {
                            }

                            override fun onCompleted() {
                            }
                        })
                )
                val orders = lastValidateUsePromoRequest?.orders ?: emptyList()
                if (orders.isNotEmpty()) {
                    orders[0]?.codes?.remove(shipping.logisticPromoViewModel?.promoCode)
                }
            }
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
                        _orderPreference = _orderPreference?.copy(shipping = shipping.copy(
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
                                isApplyLogisticPromo = false))
                        orderPreference.value = OccState.Success(_orderPreference!!)
                        calculateTotal()
                        validateUsePromo()
                    }
                }
            }
        }
    }

    fun setInsuranceCheck(checked: Boolean) {
        if (_orderPreference != null && _orderPreference?.shipping != null) {
            _orderPreference = _orderPreference?.copy(shipping = _orderPreference?.shipping?.copy(isCheckInsurance = checked))
            calculateTotal()
        }
    }

    fun chooseDuration(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
        val value = _orderPreference
        val shippingRecommendationData = value?.shipping?.shippingRecommendationData
        val curShip = value?.preference?.shipment
        val shipping = value?.shipping
        if (shippingRecommendationData != null && curShip != null && shipping != null) {
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
            val tempServiceDuration = selectedShippingDurationViewModel.serviceData.serviceName
            val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")"))
            } else {
                "Durasi tergantung kurir"
            }
            if (shippingRecommendationData.logisticPromo?.isApplied == true && shipping.isApplyLogisticPromo) {
                clearCacheAutoApplyStackUseCase.setParams(PARAM_VALUE_MARKETPLACE, arrayListOf(shippingRecommendationData.logisticPromo?.promoCode
                        ?: ""))
                compositeSubscription.add(
                        clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY).subscribe(object : Observer<ClearPromoUiModel?> {
                            override fun onError(e: Throwable?) {
                                e?.printStackTrace()
                            }

                            override fun onNext(t: ClearPromoUiModel?) {
                            }

                            override fun onCompleted() {
                            }
                        })
                )
                val orders = lastValidateUsePromoRequest?.orders ?: emptyList()
                if (orders.isNotEmpty()) {
                    orders[0]?.codes?.remove(shipping.logisticPromoViewModel?.promoCode)
                }
            }
            shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo?.copy(isApplied = false)
            var shipping1 = shipping.copy(
                    needPinpoint = flagNeedToSetPinpoint,
                    serviceErrorMessage = if (flagNeedToSetPinpoint) "Butuh pinpoint lokasi" else null,
                    isServicePickerEnable = !flagNeedToSetPinpoint,
                    serviceId = selectedShippingDurationViewModel.serviceData.serviceId,
                    serviceDuration = serviceDur,
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

            if (shipping1.serviceErrorMessage.isNullOrEmpty()) {
                val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                if (logisticPromo != null && !logisticPromo.disabled) {
                    shipping1 = shipping1.copy(logisticPromoTickerMessage = "Tersedia ${logisticPromo.title}", logisticPromoViewModel = logisticPromo, logisticPromoShipping = null)
                }
            }
            _orderPreference = _orderPreference?.copy(shipping = shipping1)
            orderPreference.value = OccState.Success(_orderPreference!!)
            if (shipping1.serviceErrorMessage.isNullOrEmpty()) {
                validateUsePromo()
            } else {
                orderTotal.value = orderTotal.value?.copy(buttonState = if (shipping1.serviceErrorMessage.isNullOrEmpty() && orderProduct.quantity?.isStateError == false) ButtonBayarState.NORMAL else ButtonBayarState.DISABLE)
            }
            calculateTotal()
        }
    }

    fun changePinpoint() {
        val op = _orderPreference
        if (op?.shipping != null) {
            _orderPreference = op.copy(shipping = op.shipping.copy(needPinpoint = false))
            orderPreference.value = OccState.Success(_orderPreference!!)
        }
    }

    fun savePinpoint(longitude: String, latitude: String) {
        val params = generateAuthParam()
        val op = _orderPreference
        if (op != null) {
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

            //loading
            globalEvent.value = OccGlobalEvent.Loading
            compositeSubscription.add(
                    editAddressUseCase.createObservable(requestParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(object : Observer<String> {
                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                    globalEvent.value = OccGlobalEvent.Error(e)
                                }

                                override fun onNext(stringResponse: String) {
                                    var response: JSONObject? = null
                                    var messageError: String? = null
                                    var statusSuccess: Boolean
                                    try {
                                        response = JSONObject(stringResponse)
                                        val statusCode = response.getJSONObject(EditAddressUseCase.RESPONSE_DATA)
                                                .getInt(EditAddressUseCase.RESPONSE_IS_SUCCESS)
                                        statusSuccess = statusCode == 1
                                        if (!statusSuccess) {
                                            messageError = response.getJSONArray("message_error").getString(0)
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                        statusSuccess = false
                                    }

                                    if (response != null && statusSuccess) {
                                        // trigger refresh
                                        globalEvent.value = OccGlobalEvent.TriggerRefresh(false)
                                    } else {
                                        //show error
                                        if (messageError.isNullOrBlank()) {
                                            messageError = "Terjadi kesalahan. Ulangi beberapa saat lagi"
                                        }
                                        globalEvent.value = OccGlobalEvent.Error(errorMessage = messageError)
                                    }
                                }

                                override fun onCompleted() {
                                }
                            })
            )
        }
    }

    private fun generateAuthParam(): MutableMap<String, String> {
        return AuthHelper.generateParamsNetwork(userSessionInterface.userId, userSessionInterface.deviceId, TKPDMapParam())
    }

    fun chooseLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel) {
        globalEvent.value = OccGlobalEvent.Loading
        val shipping = _orderPreference?.shipping
        if (shipping != null) {
            val validateUsePromoRequest = generateValidateUsePromoRequest(false)
            validateUsePromoRequest.orders[0]?.shippingId = logisticPromoUiModel.shipperId
            validateUsePromoRequest.orders[0]?.spId = logisticPromoUiModel.shipperProductId
            validateUsePromoRequest.orders[0]?.codes?.add(logisticPromoUiModel.promoCode)
            val requestParams = RequestParams.create()
            requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest)
            validateUsePromoRevampUseCase.createObservable(requestParams)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ValidateUsePromoRevampUiModel> {
                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            globalEvent.value = OccGlobalEvent.Error(e)
                        }

                        override fun onNext(response: ValidateUsePromoRevampUiModel) {
                            validateUsePromoRevampUiModel = response
                            if (response.status.equals("OK", true)) {
                                for (voucherOrderUiModel in response.promoUiModel.voucherOrderUiModels) {
                                    if (voucherOrderUiModel != null && voucherOrderUiModel.code == logisticPromoUiModel.promoCode && voucherOrderUiModel.messageUiModel.state != "red") {
                                        val shippingRecommendationData = _orderPreference?.shipping?.shippingRecommendationData
                                        var logisticPromoShipping: ShippingCourierUiModel? = null
                                        if (shippingRecommendationData != null) {
                                            for (shippingDurationViewModel in shippingRecommendationData.shippingDurationViewModels) {
                                                if (shippingDurationViewModel.isSelected) {
                                                    for (shippingCourierUiModel in shippingDurationViewModel.shippingCourierViewModelList) {
                                                        shippingCourierUiModel.isSelected = false
                                                    }
                                                }
                                                if (shippingDurationViewModel.serviceData.serviceId == logisticPromoUiModel.serviceId) {
                                                    for (shippingCourierUiModel in shippingDurationViewModel.shippingCourierViewModelList) {
                                                        if (shippingCourierUiModel.productData.shipperProductId == logisticPromoUiModel.shipperProductId) {
                                                            logisticPromoShipping = shippingCourierUiModel
                                                            break
                                                        }
                                                    }
                                                }
                                                if (_orderPreference?.shipping?.isServicePickerEnable == true) {
                                                    shippingDurationViewModel.isSelected = false
                                                }
                                            }
                                            shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo.copy(isApplied = true)
                                            _orderPreference = _orderPreference?.copy(shipping = shipping.copy(shippingRecommendationData = shippingRecommendationData,
                                                    insuranceData = logisticPromoShipping?.productData?.insurance,
                                                    logisticPromoTickerMessage = null, isApplyLogisticPromo = true, logisticPromoShipping = logisticPromoShipping))
                                            orderPreference.value = OccState.Success(_orderPreference!!)
                                            globalEvent.value = OccGlobalEvent.Normal
                                            calculateTotal()
                                            return
                                        }
                                    }
                                }
                            }
                            updatePromoState(response.promoUiModel)
                            globalEvent.value = OccGlobalEvent.Error(null, "Gagal mengaplikasikan bebas ongkir")
                        }

                        override fun onCompleted() {
                        }
                    })
        }
    }

    fun updateCart() {
        val param = generateUpdateCartParam()
        if (param != null) {
            updateCartOccUseCase.execute(param, {
                //do nothing
            }, { throwable: Throwable ->
                throwable.printStackTrace()
            })
        }
    }

    fun generateUpdateCartParam(): UpdateCartOccRequest? {
        val op = orderProduct
        val quantity = op.quantity
        val pref = _orderPreference
        if (quantity != null && pref != null && pref.preference.profileId > 0) {
            val cart = UpdateCartOccCartRequest(
                    orderShop.cartResponse.cartId.toString(),
                    quantity.orderQuantity,
                    op.notes,
                    op.productId.toString(),
                    pref.shipping?.getRealShipperId() ?: 0,
                    pref.shipping?.getRealShipperProductId() ?: 0
            )
            val profile = UpdateCartOccProfileRequest(
                    pref.preference.profileId.toString(),
                    pref.preference.payment.gatewayCode,
                    pref.preference.payment.metadata,
                    pref.preference.shipment.serviceId,
                    pref.preference.address.addressId.toString()
            )
            return UpdateCartOccRequest(arrayListOf(cart), profile)
        }
        return null
    }

    fun updatePreference(preference: ProfilesItemModel) {
        var param = generateUpdateCartParam()
        if (param != null) {
            param = param.copy(profile = UpdateCartOccProfileRequest(
                    profileId = preference.profileId.toString(),
                    addressId = preference.addressModel?.addressId.toString(),
                    serviceId = preference.shipmentModel?.serviceId ?: 0,
                    gatewayCode = preference.paymentModel?.gatewayCode ?: "",
                    metadata = preference.paymentModel?.metadata ?: ""
            ))
            globalEvent.value = OccGlobalEvent.Loading
            updateCartOccUseCase.execute(param, {
                globalEvent.value = OccGlobalEvent.TriggerRefresh(true)
            }, { throwable: Throwable ->
                throwable.printStackTrace()
                if (throwable is MessageErrorException && throwable.message != null) {
                    globalEvent.value = OccGlobalEvent.Error(errorMessage = throwable.message
                            ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
                } else {
                    globalEvent.value = OccGlobalEvent.Error(throwable)
                }
            })
        }
    }

    fun finalUpdate(onSuccessCheckout: (Data) -> Unit) {
        val product = orderProduct
        val shop = orderShop
        val pref = _orderPreference
        if (orderTotal.value?.buttonState == ButtonBayarState.NORMAL && pref != null && pref.shipping?.getRealShipperProductId() ?: 0 > 0) {
            val param = generateUpdateCartParam()
            if (param != null) {
                globalEvent.value = OccGlobalEvent.Loading
                updateCartOccUseCase.execute(param, {
                    doCheckout(product, shop, pref, onSuccessCheckout)
                }, { throwable: Throwable ->
                    throwable.printStackTrace()
                    if (throwable is MessageErrorException && throwable.message != null) {
                        globalEvent.value = OccGlobalEvent.Error(errorMessage = throwable.message
                                ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
                    } else {
                        globalEvent.value = OccGlobalEvent.Error(throwable)
                    }
                })
            }
        }
    }

    private fun doCheckout(product: OrderProduct, shop: OrderShop, pref: OrderPreference, onSuccessCheckout: (Data) -> Unit) {
        if (checkIneligiblePromo()) {
            val param = CheckoutOccRequest(Profile(pref.preference.profileId), ParamCart(data = listOf(ParamData(
                    pref.preference.address.addressId,
                    listOf(
                            ShopProduct(
                                    shopId = shop.shopId,
                                    isPreorder = shop.cartResponse.product.isPreorder,
                                    warehouseId = shop.cartResponse.product.wareHouseId,
                                    finsurance = if (pref.shipping!!.isCheckInsurance) 1 else 0,
                                    productData = listOf(
                                            ProductData(
                                                    product.productId,
                                                    product.quantity!!.orderQuantity,
                                                    product.notes
                                            )
                                    ),
                                    shippingInfo = ShippingInfo(
                                            pref.shipping.getRealShipperId(),
                                            pref.shipping.getRealShipperProductId(),
                                            pref.shipping.getRealRatesId(),
                                            pref.shipping.getRealUt(),
                                            pref.shipping.getRealChecksum()
                                    ),
                                    promos = generateShopPromos()
                            )
                    )
            )), promos = generateCheckoutPromos()))
            checkoutOccUseCase.execute(param, { checkoutOccGqlResponse: CheckoutOccGqlResponse ->
                if (checkoutOccGqlResponse.response.status.equals("OK", true)) {
                    if (checkoutOccGqlResponse.response.data.success == 1 || checkoutOccGqlResponse.response.data.paymentParameter.redirectParam.url.isNotEmpty()) {
                        globalEvent.value = OccGlobalEvent.Normal
                        onSuccessCheckout(checkoutOccGqlResponse.response.data)
                        orderSummaryAnalytics.eventClickBayarSuccess(generateOspEe(2, "click bayar success"))
                    } else {
                        val errorCode = checkoutOccGqlResponse.response.data.error.code
                        orderSummaryAnalytics.eventClickBayarNotSuccess(errorCode)
                        if (errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_ERROR || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_SHOP_CLOSED) {
                            globalEvent.value = OccGlobalEvent.CheckoutError(checkoutOccGqlResponse.response.data.error)
                        } else if (errorCode == "513") {
                            globalEvent.value = OccGlobalEvent.PriceChangeError(PriceValidation(true, Message("Harga telah berubah", checkoutOccGqlResponse.response.data.error.message, "Cek Belanjaan")))
                        } else if (checkoutOccGqlResponse.response.data.error.message.isNotBlank()) {
                            globalEvent.value = OccGlobalEvent.Error(errorMessage = checkoutOccGqlResponse.response.data.error.message)
                        } else {
                            globalEvent.value = OccGlobalEvent.Error(errorMessage = "Terjadi kesalahan dengan kode ${checkoutOccGqlResponse.response.data.error.message}")
                        }
                    }
                } else {
                    if (checkoutOccGqlResponse.response.header.messages.isNotEmpty()) {
                        globalEvent.value = OccGlobalEvent.Error(errorMessage = checkoutOccGqlResponse.response.header.messages[0])
                    } else {
                        globalEvent.value = OccGlobalEvent.Error(errorMessage = "Terjadi kesalahan")
                    }
                }
            }, { throwable: Throwable ->
                throwable.printStackTrace()
                globalEvent.value = OccGlobalEvent.Error(throwable)
            })
        }
    }

    private fun generateShopPromos(): List<com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.PromoRequest> {
        val finalPromo = validateUsePromoRevampUiModel
        if (finalPromo != null) {
            val list: ArrayList<com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.PromoRequest> = ArrayList()
            for (voucherOrder in finalPromo.promoUiModel.voucherOrderUiModels) {
                if (voucherOrder?.messageUiModel?.state != "red" && orderShop.cartResponse.cartString == voucherOrder?.uniqueId) {
                    if (voucherOrder.code.isNotEmpty() && voucherOrder.type.isNotEmpty()) {
                        list.add(PromoRequest(voucherOrder.type, voucherOrder.code))
                    }
                }
            }
            return list
        }
        return emptyList()
    }

    private fun checkIneligiblePromo(): Boolean {
        val notEligiblePromoHolderdataList = ArrayList<NotEligiblePromoHolderdata>()
        val validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = validateUsePromoRevampUiModel
        if (validateUsePromoRevampUiModel != null) {
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
            val voucherOrdersItemUiModels = validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels
            if (voucherOrdersItemUiModels.isNotEmpty()) {
                for (i in voucherOrdersItemUiModels.indices) {
                    val voucherOrdersItemUiModel = voucherOrdersItemUiModels[i]
                    if (voucherOrdersItemUiModel != null && voucherOrdersItemUiModel.messageUiModel.state == "red") {
                        val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
                        notEligiblePromoHolderdata.promoTitle = if (voucherOrdersItemUiModel.titleDescription != null) voucherOrdersItemUiModel.titleDescription else ""
                        if (validateUsePromoRevampUiModel.promoUiModel.codes.size > 0) {
                            notEligiblePromoHolderdata.promoCode = validateUsePromoRevampUiModel.promoUiModel.codes[0]
                        }
                        if (orderShop.cartResponse.cartString == voucherOrdersItemUiModel.uniqueId) {
                            notEligiblePromoHolderdata.shopName = orderShop.shopName
                            if (orderShop.cartResponse.shop.isOfficial == 1) {
                                notEligiblePromoHolderdata.iconType = TYPE_ICON_OFFICIAL_STORE
                            } else if (orderShop.cartResponse.shop.isGold == 1) {
                                notEligiblePromoHolderdata.iconType = TYPE_ICON_POWER_MERCHANT
                            }
                        }
                        notEligiblePromoHolderdata.errorMessage = validateUsePromoRevampUiModel.promoUiModel.messageUiModel.text
                        notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
                    }
                }
            }
        }

        if (notEligiblePromoHolderdataList.size > 0) {
            globalEvent.value = OccGlobalEvent.PromoClashing(notEligiblePromoHolderdataList)
            return false
        } else {
            return true
        }
    }

    fun cancelIneligiblePromoCheckout(notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>, onSuccessCheckout: (Data) -> Unit) {
        globalEvent.value = OccGlobalEvent.Loading
        val promoCodeList = ArrayList(notEligiblePromoHolderdataList.map { it.promoCode })
        clearCacheAutoApplyStackUseCase.setParams(PARAM_VALUE_MARKETPLACE, promoCodeList)
        compositeSubscription.add(
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY)
                        .subscribe(object : Observer<ClearPromoUiModel?> {
                            override fun onError(e: Throwable?) {
                                e?.printStackTrace()
                            }

                            override fun onNext(t: ClearPromoUiModel?) {
                                if (_orderPreference != null) {
                                    doCheckout(orderProduct, orderShop, _orderPreference!!, onSuccessCheckout)
                                }
                            }

                            override fun onCompleted() {
                            }
                        })
        )
    }

    private fun generateCheckoutPromos(): List<com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.PromoRequest> {
        val list = ArrayList<com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.PromoRequest>()
        val finalPromo = validateUsePromoRevampUiModel
        if (finalPromo != null && finalPromo.promoUiModel.codes.isNotEmpty() && finalPromo.promoUiModel.messageUiModel.state != "red") {
            for (code in finalPromo.promoUiModel.codes) {
                list.add(PromoRequest("global", code))
            }
        }
        return list
    }

    fun updateCartPromo(onSuccess: (PromoRequest, ValidateUsePromoRequest) -> Unit) {
        val param = generateUpdateCartParam()
        if (param != null) {
            globalEvent.value = OccGlobalEvent.Loading
            updateCartOccUseCase.execute(param, {
                globalEvent.value = OccGlobalEvent.Normal
                onSuccess(generatePromoRequest(), generateValidateUsePromoRequest())
            }, { throwable: Throwable ->
                throwable.printStackTrace()
                if (throwable is MessageErrorException && throwable.message != null) {
                    globalEvent.value = OccGlobalEvent.Error(errorMessage = throwable.message
                            ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
                } else {
                    globalEvent.value = OccGlobalEvent.Error(throwable)
                }
            })
        } else {
            globalEvent.value = OccGlobalEvent.Error(errorMessage = "Terjadi kesalahan. Ulangi beberapa saat lagi")
        }
    }

    fun generatePromoRequest(shouldAddLogisticPromo: Boolean = true): PromoRequest {
        val promoRequest = PromoRequest()

        val ordersItem = Order(orderShop.shopId.toLong(), orderShop.cartResponse.cartString, listOf(
                ProductDetail(orderProduct.productId.toLong(), orderProduct.quantity?.orderQuantity.toZeroIfNull())
        ), isChecked = true)

        val shipping = _orderPreference?.shipping
        if (shipping?.getRealShipperProductId() ?: 0 > 0 && shipping?.getRealShipperId() ?: 0 > 0) {
            ordersItem.shippingId = shipping!!.getRealShipperId()
            ordersItem.spId = shipping.getRealShipperProductId()
        }

        if (shipping?.isCheckInsurance == true && shipping.insuranceData != null) {
            ordersItem.isInsurancePrice = 1
        } else {
            ordersItem.isInsurancePrice = 0
        }

        val lastRequest = lastValidateUsePromoRequest
        var codes: MutableList<String> = ArrayList()
        if (lastRequest != null && lastRequest.orders.isNotEmpty() && lastRequest.orders[0] != null) {
            codes = lastRequest.orders[0]?.codes ?: ArrayList()
        } else {
            val voucherOrders = orderPromo.value?.lastApply?.voucherOrders ?: emptyList()
            for (voucherOrder in voucherOrders) {
                if (voucherOrder.uniqueId.equals(ordersItem.uniqueId, true)) {
                    codes.add(voucherOrder.code)
                    break
                }
            }
        }

        if (shouldAddLogisticPromo && shipping?.isApplyLogisticPromo == true && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            if (!codes.contains(shipping.logisticPromoViewModel.promoCode)) {
                codes.add(shipping.logisticPromoViewModel.promoCode)
            }
        } else if (shipping?.logisticPromoViewModel?.promoCode?.isNotEmpty() == true) {
            codes.remove(shipping.logisticPromoViewModel.promoCode)
        }

        ordersItem.codes = codes

        promoRequest.orders = listOf(ordersItem)
        promoRequest.state = PARAM_CHECKOUT
        promoRequest.cartType = PARAM_OCC

        if (lastRequest != null) {
            promoRequest.codes = ArrayList(lastRequest.codes.filterNotNull())
        } else {
            val globalCodes = orderPromo.value?.lastApply?.codes ?: emptyList()
            promoRequest.codes = ArrayList(globalCodes)
        }
        return promoRequest
    }

    fun generateValidateUsePromoRequest(shouldAddLogisticPromo: Boolean = true): ValidateUsePromoRequest {
        val validateUsePromoRequest = lastValidateUsePromoRequest ?: ValidateUsePromoRequest()

        val ordersItem = OrdersItem()
        ordersItem.shopId = orderShop.shopId
        ordersItem.uniqueId = orderShop.cartResponse.cartString

        ordersItem.productDetails = listOf(ProductDetailsItem(orderProduct.quantity?.orderQuantity.toZeroIfNull(), orderProduct.productId))

        val shipping = _orderPreference?.shipping
        if (shipping?.getRealShipperProductId() ?: 0 > 0 && shipping?.getRealShipperId() ?: 0 > 0) {
            ordersItem.shippingId = shipping!!.getRealShipperId()
            ordersItem.spId = shipping.getRealShipperProductId()
        }

        val lastRequest = lastValidateUsePromoRequest
        var codes: MutableList<String> = ArrayList()
        if (lastRequest != null && lastRequest.orders.isNotEmpty() && lastRequest.orders[0] != null) {
            codes = lastRequest.orders[0]?.codes ?: ArrayList()
        } else {
            val voucherOrders = orderPromo.value?.lastApply?.voucherOrders ?: emptyList()
            for (voucherOrder in voucherOrders) {
                if (voucherOrder.uniqueId.equals(ordersItem.uniqueId, true)) {
                    if (!codes.contains(voucherOrder.code)) {
                        codes.add(voucherOrder.code)
                    }
                    break
                }
            }
        }

        if (shouldAddLogisticPromo && shipping?.isApplyLogisticPromo == true && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            if (!codes.contains(shipping.logisticPromoViewModel.promoCode)) {
                codes.add(shipping.logisticPromoViewModel.promoCode)
            }
        } else if (shipping?.logisticPromoViewModel?.promoCode?.isNotEmpty() == true) {
            codes.remove(shipping.logisticPromoViewModel.promoCode)
        }
        ordersItem.codes = codes

        validateUsePromoRequest.orders = listOf(ordersItem)
        validateUsePromoRequest.state = PARAM_CHECKOUT
        validateUsePromoRequest.cartType = PARAM_OCC

        if (lastRequest != null) {
            validateUsePromoRequest.codes = lastRequest.codes
        } else {
            val globalCodes = orderPromo.value?.lastApply?.codes ?: emptyList()
            validateUsePromoRequest.codes = globalCodes.toMutableList()
        }

        lastValidateUsePromoRequest = validateUsePromoRequest

        return validateUsePromoRequest
    }

    fun validateUsePromo(validateUsePromoRequest: ValidateUsePromoRequest? = null) {
        orderTotal.value = orderTotal.value?.copy(buttonState = ButtonBayarState.LOADING)
        orderPromo.value = orderPromo.value?.copy(state = ButtonBayarState.LOADING)
        val requestParams = RequestParams.create()
        requestParams.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest
                ?: generateValidateUsePromoRequest())
        validateUsePromoRevampUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ValidateUsePromoRevampUiModel> {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        orderPromo.value = orderPromo.value?.copy(state = ButtonBayarState.DISABLE)
                    }

                    override fun onNext(t: ValidateUsePromoRevampUiModel) {
                        validateUsePromoRevampUiModel = t
                        updatePromoState(t.promoUiModel)
                        orderTotal.value = orderTotal.value?.copy(buttonState = if (_orderPreference?.shipping?.serviceErrorMessage.isNullOrEmpty() && orderProduct.quantity?.isStateError == false) ButtonBayarState.NORMAL else ButtonBayarState.DISABLE)
                    }

                    override fun onCompleted() {
                    }
                })
    }

    fun updatePromoState(promoUiModel: PromoUiModel) {
        orderPromo.value = orderPromo.value?.copy(lastApply = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel), state = ButtonBayarState.NORMAL)
        calculateTotal()
    }
}