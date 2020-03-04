package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.data.model.request.checkout.*
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderTotal
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderProduct
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationViewModel
import kotlinx.coroutines.*
import rx.Observer
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class OrderSummaryPageViewModel @Inject constructor(dispatcher: CoroutineDispatcher, private val ratesUseCase: GetRatesUseCase, private val ratesResponseStateConverter: RatesResponseStateConverter) : BaseViewModel(dispatcher) {

    private var orderProduct: OrderProduct = OrderProduct()

    private var orderPreference: MutableLiveData<OrderPreference> = MutableLiveData()

    private var orderTotal: MutableLiveData<OrderTotal> = MutableLiveData()

    private var compositeSubscription = CompositeSubscription()

    private var debounceJob: Job? = null

    fun updateProduct(product: OrderProduct, shouldReloadRates: Boolean = true) {
        orderProduct = product
        orderTotal.value = orderTotal.value?.copy(btnState = 1)
        if (shouldReloadRates) {
            debounce()
        }
    }

    fun updatePreference(preference: Preference) {
        this.orderPreference.value = orderPreference.value?.copy(preference = preference)
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
        orderPreference.value = OrderPreference(preference = Preference())
        orderTotal.value = OrderTotal(btnState = 1)
    }

    fun debounce() {
        debounceJob?.cancel()
        debounceJob = launch {
            delay(700)
            if (isActive) {
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
                            val data = ratesResponseStateConverter.fillState(it, listOf(), 1, 0)
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
                                orderPreference.value = orderPreference.value?.copy(shipping = OrderShipping())
                                orderTotal.value = OrderTotal(subtotal = 0.0, btnState = 0)
                            }

                            override fun onCompleted() {

                            }
                        })
        )
    }

    fun generateShippingParam(): ShippingParam {
        val shippingParam = ShippingParam()
        shippingParam.originDistrictId = ""
        shippingParam.originPostalCode = ""
        shippingParam.originLatitude = ""
        shippingParam.originLongitude = ""
        shippingParam.destinationDistrictId = ""
        shippingParam.destinationPostalCode = ""
        shippingParam.destinationLatitude = ""
        shippingParam.destinationLongitude = ""
        shippingParam.shopId = ""
        shippingParam.token = ""
        shippingParam.ut = ""
        shippingParam.insurance = 1
        shippingParam.categoryIds = ""

        shippingParam.weightInKilograms = 1 * 0 / 1000.0
        shippingParam.productInsurance = 0
        shippingParam.orderValue = 5000 * 1
        return shippingParam
    }

    fun generateListShopShipment(): ArrayList<ShopShipment> {
        return arrayListOf()
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