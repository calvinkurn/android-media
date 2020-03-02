package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderTotal
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderProduct
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
        val ratesParamBuilder = RatesParam.Builder(listOf(), shippingParam)
        ratesUseCase.execute(ratesParamBuilder.build())
                .map {
                    val data = ratesResponseStateConverter.fillState(it, listOf(), 1, 0)
                    if (data.shippingDurationViewModels != null) {
                        val logisticPromo = data.logisticPromo
                        if (logisticPromo != null) {
                            // validate army courier
                            val serviceData: ShippingDurationViewModel? = getRatesDataFromLogisticPromo(logisticPromo.serviceId, data.shippingDurationViewModels)
                            if (serviceData == null) {
                                data.logisticPromo = null
                            } else {
                                val courierData: ShippingCourierViewModel? = getCourierDatabySpId(logisticPromo.shipperProductId, serviceData.shippingCourierViewModelList)
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
    }

    private fun getCourierDatabySpId(spId: Int, shippingCourierViewModels: List<ShippingCourierViewModel>): ShippingCourierViewModel? {
        return shippingCourierViewModels.firstOrNull { it.productData.shipperProductId == spId }
//        for (shippingCourierViewModel in shippingCourierViewModels) {
//            if (shippingCourierViewModel.productData.shipperProductId == spId) {
//                return ShippingCourierConverter().convertToCourierItemData(shippingCourierViewModel)
//            }
//        }
//        return null
    }

    private fun getRatesDataFromLogisticPromo(serviceId: Int, list: List<ShippingDurationViewModel>): ShippingDurationViewModel? {
        list.firstOrNull { it.serviceData.serviceId == serviceId }
                ?.let {
                    return it
                }
        return null
    }

    fun calculateTotal() {

    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.clear()
    }
}