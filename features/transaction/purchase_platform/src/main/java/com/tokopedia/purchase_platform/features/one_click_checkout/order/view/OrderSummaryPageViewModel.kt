package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.data.model.request.checkout.*
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.domain.GetOccCartUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.*
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationViewModel
import kotlinx.coroutines.*
import rx.Observer
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class OrderSummaryPageViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                    private val getOccCartUseCase: GetOccCartUseCase,
                                                    private val ratesUseCase: GetRatesUseCase,
                                                    val getPreferenceListUseCase: GetPreferenceListUseCase,
                                                    private val ratesResponseStateConverter: RatesResponseStateConverter) : BaseViewModel(dispatcher) {

    var orderProduct: OrderProduct = OrderProduct()
    var orderShop: OrderShop = OrderShop()

    var orderPreference: MutableLiveData<OrderPreference> = MutableLiveData()

    var orderTotal: MutableLiveData<OrderTotal> = MutableLiveData(OrderTotal())

    private var compositeSubscription = CompositeSubscription()

    private var debounceJob: Job? = null

    fun getOccCart() {
        getOccCartUseCase.execute({ orderData: OrderData ->
            val o = orderData
            orderProduct = orderData.cart.product
            orderShop = orderData.cart.shop
            var preference = orderData.preference
            preference = preference.copy(shipment = preference.shipment.copy(serviceId = 1104))
            orderPreference.value = OrderPreference(preference)
        }, { throwable: Throwable ->
            throwable.printStackTrace()
        })
    }

    fun updateProduct(product: OrderProduct, shouldReloadRates: Boolean = true) {
        orderProduct = product
        orderTotal.value = orderTotal.value?.copy(buttonState = ButtonBayarState.LOADING)
        if (shouldReloadRates) {
            debounce()
        }
    }

    fun updatePreference(preference: Preference) {
//        this.orderPreference.value = orderPreference.value?.copy(preference = preference)
        debounceJob?.cancel()
        getRates()
    }

    fun updateCourier() {
        calculateTotal()
    }

    fun updateDuration() {
        calculateTotal()
    }

    fun loadOrder() {
        // get order
        orderProduct = OrderProduct()
//        orderPreference.value = OrderPreference(preference = Preference())
        orderTotal.value = OrderTotal(buttonState = ButtonBayarState.LOADING)
    }

    fun debounce() {
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
        ratesParam.occ = 1
        compositeSubscription.add(
                ratesUseCase.execute(ratesParam)
                        .map {
                            val value = orderPreference.value
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

                            }

                            override fun onNext(shippingRecommendationData: ShippingRecommendationData) {
                                val value = orderPreference.value
                                if (value != null) {
                                    val curShip = value.preference.shipment
                                    var shipping = value.shipping
                                    if (shipping != null) {
                                        shipping = shipping.copy(serviceId = curShip.serviceId, serviceDuration = curShip.serviceDuration, shippingRecommendationData = shippingRecommendationData)
                                    } else {
                                        val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
                                        var selectedShippingDurationViewModel: ShippingDurationUiModel? = null
                                        for (shippingDurationViewModel in shippingDurationViewModels) {
                                            if (shippingDurationViewModel.serviceData.serviceId == curShip.serviceId) {
                                                shippingDurationViewModel.isSelected = true
                                                selectedShippingDurationViewModel = shippingDurationViewModel
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
                                                    shipping = Shipment(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                                                            shipperName = selectedShippingCourierUiModel.productData.shipperName,
                                                            insuranceData = selectedShippingCourierUiModel.productData.insurance,
                                                            serviceId = shippingDurationViewModel.serviceData.serviceId,
                                                            serviceDuration = shippingDurationViewModel.serviceData.texts.textEtd,
                                                            serviceName = shippingDurationViewModel.serviceData.serviceName,
                                                            shippingRecommendationData = shippingRecommendationData)
                                                }
                                            } else {
                                                shippingDurationViewModel.isSelected = false
                                            }
                                        }
                                        if (selectedShippingDurationViewModel == null) {
                                            shipping = Shipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = "durasi tidak tersedia", shippingRecommendationData = shippingRecommendationData)
                                        }
                                    }
                                    orderPreference.value = value.copy(shipping = shipping)
                                }
                                orderTotal.value = OrderTotal(subTotal = 0L, buttonState = ButtonBayarState.NORMAL)
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
        shippingParam.destinationDistrictId = orderPreference.value?.preference?.address?.districtId?.toString()
        shippingParam.destinationPostalCode = orderPreference.value?.preference?.address?.postalCode
        shippingParam.destinationLatitude = orderPreference.value?.preference?.address?.latitude
        shippingParam.destinationLongitude = orderPreference.value?.preference?.address?.longitude
        shippingParam.shopId = orderShop.shopId.toString()
        shippingParam.token = "Tokopedia+Kero:LKlL31rEgWx6r0eJBx+GXVrJ+7Q\\u003d"
        shippingParam.ut = "1583825797"
        shippingParam.insurance = 1
        shippingParam.categoryIds = orderShop.cartResponse.product.categoryId.toString()
//        shippingParam.uniqueId = "478925-0-1493-4646989"
        shippingParam.uniqueId = orderShop.cartResponse.cartId.toString()
        shippingParam.addressId = orderPreference.value?.preference?.address?.addressId ?: 0
        shippingParam.products = listOf(Product(orderProduct.productId.toLong(), orderProduct.isFreeOngkir))

        shippingParam.weightInKilograms = orderProduct.quantity!!.orderQuantity * orderProduct.weight / 1000.0
        shippingParam.productInsurance = 0
        shippingParam.orderValue = orderProduct.quantity!!.orderQuantity * orderProduct.productPrice.toLong()
        return shippingParam
    }

    fun generateListShopShipment(): List<ShopShipment> {
        return orderShop.shopShipment
//        val element = ShopShipment()
//        element.shipProds =
//        return listOf(element)
    }

    private fun getCourierDatabySpId(spId: Int, shippingCourierViewModels: List<ShippingCourierUiModel>): ShippingCourierUiModel? {
        return shippingCourierViewModels.firstOrNull { it.productData.shipperProductId == spId }
//        for (shippingCourierViewModel in shippingCourierViewModels) {
//            if (shippingCourierViewModel.productData.shipperProductId == spId) {
//                return ShippingCourierConverter().convertToCourierItemData(shippingCourierViewModel)
//            }
//        }
//        return null
    }

    private fun getRatesDataFromLogisticPromo(serviceId: Int, list: List<ShippingDurationUiModel>): ShippingDurationUiModel? {
        list.firstOrNull { it.serviceData.serviceId == serviceId }
                ?.let {
                    return it
                }
        return null
    }

    fun calculateTotal() {

    }

    fun generateCheckoutRequest() {
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
    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.clear()
        debounceJob?.cancel()
    }
}