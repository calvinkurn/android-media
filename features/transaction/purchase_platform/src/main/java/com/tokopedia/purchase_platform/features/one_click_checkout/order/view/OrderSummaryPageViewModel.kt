package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.purchase_platform.common.data.model.param.EditAddressParam
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccGlobalEvent
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccCartRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccGqlResponse
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
                                                    val updateCartOccUseCase: UpdateCartOccUseCase,
                                                    private val ratesResponseStateConverter: RatesResponseStateConverter,
                                                    private val editAddressUseCase: EditAddressUseCase,
                                                    private val checkoutOccUseCase: CheckoutOccUseCase,
                                                    private val userSessionInterface: UserSessionInterface) : BaseViewModel(dispatcher) {

    var orderProduct: OrderProduct = OrderProduct()
    var orderShop: OrderShop = OrderShop()
    var kero: Kero = Kero()
    var _orderPreference: OrderPreference? = null

    var orderPreference: MutableLiveData<OccState<OrderPreference>> = MutableLiveData(OccState.Loading)

    var orderTotal: MutableLiveData<OrderTotal> = MutableLiveData(OrderTotal())

    var globalEvent: MutableLiveData<OccGlobalEvent> = MutableLiveData(OccGlobalEvent.Normal)

    private var compositeSubscription = CompositeSubscription()

    private var debounceJob: Job? = null

    fun getOccCart(isFullRefresh: Boolean = true) {
        globalEvent.value = OccGlobalEvent.Normal
        getOccCartUseCase.execute({ orderData: OrderData ->
            orderProduct = orderData.cart.product
            orderShop = orderData.cart.shop
            kero = orderData.cart.kero
            val preference = orderData.preference
            _orderPreference = if (isFullRefresh || _orderPreference == null) {
                OrderPreference(preference)
            } else {
                _orderPreference?.copy(preference = preference)
            }
            orderPreference.value = OccState.FirstLoad(_orderPreference!!)
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

    fun updateProduct(product: OrderProduct, shouldReloadRates: Boolean = true) {
        orderProduct = product
        if (shouldReloadRates) {
            calculateTotal()
            orderTotal.value = orderTotal.value?.copy(buttonState = ButtonBayarState.LOADING)
            debounce()
        }
    }

    private fun debounce() {
        debounceJob?.cancel()
        Log.i("OSP DEBOUNCE", "CANCEL")
        debounceJob = launch {
            delay(1000)
            Log.i("OSP DEBOUNCE", "delay FINISH")
            if (isActive) {
                Log.i("OSP DEBOUNCE", "ACTIVE")
                getRates()
            }
        }
    }

    private fun getRates() {
        // get rates
        val shippingParam = generateShippingParam()
        val ratesParamBuilder = RatesParam.Builder(generateListShopShipment(), shippingParam)
        val ratesParam = ratesParamBuilder.build()
        ratesParam.occ = "1"
        compositeSubscription.add(
                ratesUseCase.execute(ratesParam)
                        .map {
                            Log.i("OSP", Thread.currentThread().name)
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
                                                                insuranceData = selectedShippingCourierUiModel.productData.insurance,
                                                                serviceId = shippingDurationViewModel.serviceData.serviceId,
                                                                serviceDuration = serviceDur,
                                                                serviceName = shippingDurationViewModel.serviceData.serviceName,
                                                                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                                                shippingRecommendationData = shippingRecommendationData)
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
                                    _orderPreference = value.copy(shipping = shipping)
                                    orderPreference.value = OccState.Success(_orderPreference!!)
                                    orderTotal.value = orderTotal.value?.copy(buttonState = if (shipping?.serviceErrorMessage.isNullOrEmpty()) ButtonBayarState.NORMAL else ButtonBayarState.DISABLE)
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
            if (shipping?.shippingPrice != null) {
                val totalShippingPrice = shipping.shippingPrice.toDouble()
                var insurancePrice = 0.0
                if (shipping.isCheckInsurance && shipping.insuranceData != null) {
                    insurancePrice = shipping.insuranceData.insurancePrice.toDouble()
                }
                val subtotal = totalProductPrice + totalShippingPrice + insurancePrice
                val minimumAmount = _orderPreference?.preference?.payment?.minimumAmount ?: 0
//                val minimumAmount = 500000
                val maximumAmount = _orderPreference?.preference?.payment?.maximumAmount ?: 0
//                val maximumAmount = 700000
                val fee = _orderPreference?.preference?.payment?.fee?.toDouble() ?: 0.0
                val orderCost = OrderCost(totalProductPrice, subtotal, totalShippingPrice, insurancePrice, fee)
                if (minimumAmount > subtotal) {
                    orderTotal.value = orderTotal.value?.copy(orderCost = orderCost,
                            paymentErrorMessage = "minimum pembayaran adalah ${CurrencyFormatUtil.convertPriceValueToIdrFormat(minimumAmount, false)}",
                            isButtonChoosePayment = true)
                } else if (maximumAmount > 0 && maximumAmount < subtotal) {
                    orderTotal.value = orderTotal.value?.copy(orderCost = orderCost,
                            paymentErrorMessage = "maximum pembayaran adalah ${CurrencyFormatUtil.convertPriceValueToIdrFormat(minimumAmount, false)}",
                            isButtonChoosePayment = true)
                } else if (_orderPreference?.preference?.payment?.gatewayCode?.contains("OVO") == true && subtotal > _orderPreference?.preference?.payment?.walletAmount ?: 0) {
                    orderTotal.value = orderTotal.value?.copy(orderCost = orderCost, paymentErrorMessage = "OVO kamu tidak cukup", isButtonChoosePayment = true)
                } else {
                    orderTotal.value = orderTotal.value?.copy(orderCost = orderCost, paymentErrorMessage = null, isButtonChoosePayment = false)
                }
                return
            }
        }
        orderTotal.value = orderTotal.value?.copy(orderCost = OrderCost(), buttonState = ButtonBayarState.DISABLE)
    }

    /*fun generateCheckoutRequest() {
        val dataRequest = DataCheckoutRequest.Builder()
                .addressId(1)
                .shopProducts(listOf(
                        generateShopProductRequest()
                ))
    }

    private fun generateShopProductRequest(): ShopProductCheckoutRequest {
        val shopProductCheckoutBuilder = ShopProductCheckoutRequest.Builder()
                .shippingInfo(ShippingInfoCheckoutRequest.Builder()
                        .shippingId(1)
                        .spId(2)
                        .ratesId("")
                        .checksum("")
                        .ut("")
                        .analyticsDataShippingCourierPrice("")
                        .ratesFeature(generateRatesFeature())
                        .build())
//                                .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0)
//                .finsurance((shipmentDetailData.getUseInsurance() != null && shipmentDetailData.getUseInsurance()) ? 1 : 0)
//        .isOrderPriority((shipmentDetailData.isOrderPriority() != null && shipmentDetailData.isOrderPriority() ? 1 : 0))
//        .isPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0)
//        .shopId(shipmentCartItemModel.getShopId())
//                .warehouseId(shipmentCartItemModel.getFulfillmentId())
//                .cartString(shipmentCartItemModel.getCartString())
                .productData(listOf(generateProductDataCheckout()))
//                .build()

        val promoCodes = ArrayList<String>()
        val promoRequests = ArrayList<PromoRequest>()
//        if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
//            promoCodes.add(shipmentCartItemModel.getVoucherOrdersItemUiModel().code)
//            val promoRequest = PromoRequest()
//            promoRequest.code = shipmentCartItemModel.getVoucherOrdersItemUiModel().code
//            promoRequest.type = PromoRequest.TYPE_MERCHANT
//            promoRequests.add(promoRequest)
//        }

//        if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
//            promoCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().code)
//            val promoRequest = PromoRequest()
//            promoRequest.code = shipmentCartItemModel.getVoucherLogisticItemUiModel().code
//            promoRequest.type = PromoRequest.TYPE_LOGISTIC
//            promoRequests.add(promoRequest)
//        }
//        shopProductCheckoutBuilder.promos(promoRequests)

//        if (promoCodes.size > 0) {
//            shopProductCheckoutBuilder.promoCodes(promoCodes)
//        }

//        if (shipmentDetailData.getUseDropshipper() != null && shipmentDetailData.getUseDropshipper()) {
//            shopProductCheckoutBuilder.isDropship(1)
//                    .dropshipData(DropshipDataCheckoutRequest.Builder()
//                            .name(shipmentDetailData.getDropshipperName())
//                            .telpNo(shipmentDetailData.getDropshipperPhone())
//                            .build())
//        } else {
//            shopProductCheckoutBuilder.isDropship(0)
//        }

        return shopProductCheckoutBuilder.build()
    }

    private fun generateProductDataCheckout(): ProductDataCheckoutRequest {
//        var courierId = ""
//        var serviceId = ""
//        var shippingPrice = ""
//        if (shipmentDetailData != null && shipmentDetailData.selectedCourier != null) {
//            courierId = shipmentDetailData.selectedCourier.shipperProductId.toString()
//            serviceId = shipmentDetailData.selectedCourier.serviceId.toString()
//            shippingPrice = shipmentDetailData.selectedCourier.shipperPrice.toString()
//        }
        return ProductDataCheckoutRequest.Builder()
//                .productId(cartItem.productId)
//                .purchaseProtection(cartItem.isProtectionOptIn)
//                .productName(cartItem.analyticsProductCheckoutData.productName)
//                .productPrice(cartItem.analyticsProductCheckoutData.productPrice)
//                .productBrand(cartItem.analyticsProductCheckoutData.productBrand)
//                .productCategory(cartItem.analyticsProductCheckoutData.productCategory)
//                .productVariant(cartItem.analyticsProductCheckoutData.productVariant)
//                .productQuantity(cartItem.analyticsProductCheckoutData.productQuantity)
//                .productShopId(cartItem.analyticsProductCheckoutData.productShopId)
//                .productShopType(cartItem.analyticsProductCheckoutData.productShopType)
//                .productShopName(cartItem.analyticsProductCheckoutData.productShopName)
//                .productCategoryId(cartItem.analyticsProductCheckoutData.productCategoryId)
//                .productListName(cartItem.analyticsProductCheckoutData.productListName)
//                .productAttribution(cartItem.analyticsProductCheckoutData.productAttribution)
//                .cartId(cartItem.cartId)
//                .warehouseId(cartItem.analyticsProductCheckoutData.warehouseId)
//                .productWeight(cartItem.analyticsProductCheckoutData.productWeight)
//                .promoCode(cartItem.analyticsProductCheckoutData.promoCode)
//                .promoDetails(cartItem.analyticsProductCheckoutData.promoDetails)
//                .buyerAddressId(cartItem.analyticsProductCheckoutData.buyerAddressId)
//                .shippingDuration(serviceId)
//                .courier(courierId)
//                .shippingPrice(shippingPrice)
//                .codFlag(cartItem.analyticsProductCheckoutData.codFlag)
//                .tokopediaCornerFlag(cartItem.analyticsProductCheckoutData.tokopediaCornerFlag)
//                .isFulfillment(cartItem.analyticsProductCheckoutData.isFulfillment)
//                .setDiscountedPrice(cartItem.analyticsProductCheckoutData.isDiscountedPrice)
//                .isFreeShipping(cartItem.isFreeShipping)
                .build()
    }

    private fun generateRatesFeature(): RatesFeature {
        val result = RatesFeature()
        val otdg = OntimeDeliveryGuarantee()
//        if (courierItemData.getOntimeDelivery() != null) {
//            otdg.setAvailable(courierItemData.getOntimeDelivery().available)
//            otdg.setDuration(courierItemData.getOntimeDelivery().value)
//        }
        result.ontimeDeliveryGuarantee = otdg
        return result
    }*/

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
            for (shippingDurationViewModel in shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == shipping.serviceId) {
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
                                shippingPrice = selectedShippingCourierUiModel.productData.price.price))
                        orderPreference.value = OccState.Success(_orderPreference!!)
                        calculateTotal()
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
            val shipping1 = shipping.copy(
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
                    shippingRecommendationData = shippingRecommendationData)
            _orderPreference = _orderPreference?.copy(shipping = shipping1)
            orderPreference.value = OccState.Success(_orderPreference!!)
            orderTotal.value = orderTotal.value?.copy(buttonState = if (shipping1.serviceErrorMessage.isNullOrEmpty()) ButtonBayarState.NORMAL else ButtonBayarState.DISABLE)
            calculateTotal()
//            }
        }
    }

    fun changePinpoint() {
        val op = _orderPreference
        if (op?.shipping != null) {
            orderPreference.value = OccState.Success(op.copy(shipping = op.shipping.copy(needPinpoint = false)))
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

    fun updateCart() {
        val param = generateUpdateCartParam()
        if (param != null) {
            updateCartOccUseCase.execute(param, { updateCartOccGqlResponse: UpdateCartOccGqlResponse ->
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
                    pref.shipping?.shipperId.toZeroIfNull(),
                    pref.shipping?.shipperProductId.toZeroIfNull()
            )
            val profile = UpdateCartOccProfileRequest(
                    pref.preference.profileId.toString(),
                    pref.preference.payment.gatewayCode,
                    "",
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
                    gatewayCode = preference.paymentModel?.gatewayCode ?: ""
            ))
            globalEvent.value = OccGlobalEvent.Loading
            updateCartOccUseCase.execute(param, { updateCartOccGqlResponse: UpdateCartOccGqlResponse ->
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

    fun finalUpdate(onSuccessCheckout: (PaymentParameter) -> Unit) {
        val product = orderProduct
        val shop = orderShop
        val pref = _orderPreference
        if (orderTotal.value?.buttonState == ButtonBayarState.NORMAL && pref != null && pref.shipping?.shipperProductId ?: 0 > 0) {
            val param = generateUpdateCartParam()
            if (param != null) {
                globalEvent.value = OccGlobalEvent.Loading
                updateCartOccUseCase.execute(param, { updateCartOccGqlResponse: UpdateCartOccGqlResponse ->
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

    private fun doCheckout(product: OrderProduct, shop: OrderShop, pref: OrderPreference, onSuccessCheckout: (PaymentParameter) -> Unit) {
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
                                        pref.shipping.shipperId!!,
                                        pref.shipping.shipperProductId!!,
                                        pref.shipping.ratesId!!,
                                        pref.shipping.ut!!,
                                        pref.shipping.checksum!!
                                )
                        )
                )
        ))))
        checkoutOccUseCase.execute(param, { checkoutOccGqlResponse: CheckoutOccGqlResponse ->
            if (checkoutOccGqlResponse.response.status.equals("OK", true)) {
                if (checkoutOccGqlResponse.response.data.success == 1) {
                    val paymentParameter = checkoutOccGqlResponse.response.data.paymentParameter
                    globalEvent.value = OccGlobalEvent.Normal
                    onSuccessCheckout(paymentParameter)
                } else {
                    val errorCode = checkoutOccGqlResponse.response.data.error.code
                    if (errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_SHOP_CLOSED) {
                        globalEvent.value = OccGlobalEvent.CheckoutError(checkoutOccGqlResponse.response.data.error)
                    } else if (checkoutOccGqlResponse.response.data.error.additionalInfo?.priceValidation?.isUpdated == true && checkoutOccGqlResponse.response.data.error.additionalInfo.priceValidation.message != null) {
                        globalEvent.value = OccGlobalEvent.PriceChangeError(checkoutOccGqlResponse.response.data.error.additionalInfo.priceValidation)
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

    fun updateCartPromo(onSuccess: (PromoRequest, ValidateUsePromoRequest) -> Unit) {
        val param = generateUpdateCartParam()
        if (param != null) {
            globalEvent.value = OccGlobalEvent.Loading
            updateCartOccUseCase.execute(param, { updateCartOccGqlResponse: UpdateCartOccGqlResponse ->
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

    private fun generatePromoRequest(): PromoRequest {
        val promoRequest = PromoRequest()
        promoRequest.orders = listOf(
                Order(orderShop.shopId.toLong(), orderShop.cartResponse.cartId.toString(), listOf(
                        ProductDetail(orderProduct.productId.toLong(), orderProduct.quantity?.orderQuantity.toZeroIfNull())
                ), ArrayList())
        )
        return promoRequest
    }

    private fun generateValidateUsePromoRequest(): ValidateUsePromoRequest {
        val validateUsePromoRequest = ValidateUsePromoRequest()
        val ordersItem = OrdersItem()
        ordersItem.shopId = orderShop.shopId
        ordersItem.uniqueId = orderShop.cartResponse.cartId.toString()
        ordersItem.productDetails = listOf(ProductDetailsItem(orderProduct.quantity?.orderQuantity.toZeroIfNull(), orderProduct.productId))
        val shipping = _orderPreference?.shipping
        if (shipping?.shipperProductId != null && shipping.shipperId != null) {
            ordersItem.shippingId = shipping.shipperId
            ordersItem.spId = shipping.shipperProductId
        }
        validateUsePromoRequest.orders = listOf(ordersItem)
        return validateUsePromoRequest
    }
}