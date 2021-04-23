package com.tokopedia.oneclickcheckout.preference.edit.view.shipping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.GetShippingDurationUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.mapper.ShippingDurationModelWithPriceMapper
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ServicesItemModel
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ServicesItemModelNoPrice
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ShippingListModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Observer
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ShippingDurationViewModel @Inject constructor(private val useCase: GetShippingDurationUseCase,
                                                    private val useCaseRates: GetRatesUseCase,
                                                    private val mapperPrice: ShippingDurationModelWithPriceMapper,
                                                    private val dispatchers: CoroutineDispatchers) : BaseViewModel(dispatchers.immediate) {

    var selectedId = -1
    private var shippingDurationModel: ShippingListModel? = null

    private val _shippingDuration = MutableLiveData<OccState<ShippingListModel>>()
    val shippingDuration: LiveData<OccState<ShippingListModel>>
        get() = _shippingDuration

    private val compositeSubscription = CompositeSubscription()

    fun getShippingDuration() {
        _shippingDuration.value = OccState.Loading
        OccIdlingResource.increment()
        useCase.execute(onSuccess = {
            logicSelection(it)
            OccIdlingResource.decrement()
        }, onError = {
            _shippingDuration.value = OccState.Failed(Failure(it))
            OccIdlingResource.decrement()
        })

    }

    private fun logicSelection(shippingDurationModel: ShippingListModel) {
        launch {
            OccIdlingResource.increment()
            withContext(dispatchers.default) {
                val shippingList = shippingDurationModel.services
                for (item in shippingList) {
                    if (item is ServicesItemModelNoPrice) {
                        item.isSelected = item.serviceId == selectedId
                    } else if (item is ServicesItemModel) {
                        item.isSelected = item.servicesId == selectedId
                    }
                }
                shippingDurationModel.services = shippingList
            }
            this@ShippingDurationViewModel.shippingDurationModel = shippingDurationModel
            _shippingDuration.value = OccState.Success(shippingDurationModel)
            OccIdlingResource.decrement()
        }
    }

    fun setSelectedShipping(shippingId: Int) {
        val shippingModel = shippingDurationModel
        if (shippingModel != null && _shippingDuration.value is OccState.Success) {
            selectedId = shippingId
            logicSelection(shippingModel)
        }
    }


    /*With Price*/
    fun getRates(listShopShipment: ArrayList<ShopShipment>?, shippingParam: ShippingParam?, isNewLayout: Boolean) {
        _shippingDuration.value = OccState.Loading
        OccIdlingResource.increment()
        val ratesParam = shippingParam?.let { listShopShipment?.let { list -> RatesParam.Builder(list, it) } }?.build()
        ratesParam?.let {
            it.occ = "1"
            compositeSubscription.add(
                    useCaseRates.execute(it)
                            .subscribe(object : Observer<ShippingRecommendationData> {
                                override fun onError(e: Throwable?) {
                                    _shippingDuration.value = OccState.Failed(Failure(e))
                                    OccIdlingResource.decrement()
                                }

                                override fun onNext(shippingRecomendationData: ShippingRecommendationData) {
                                    if (!shippingRecomendationData.errorMessage.isNullOrEmpty()) {
                                        _shippingDuration.value = OccState.Failed(Failure(MessageErrorException(shippingRecomendationData.errorMessage)))
                                    } else {
                                        logicSelection(mapToModelPrice(shippingRecomendationData, isNewLayout))
                                    }
                                }

                                override fun onCompleted() {
                                    OccIdlingResource.decrement()
                                }
                            })
            )
        }
    }

    private fun mapToModelPrice(responses: ShippingRecommendationData, isNewLayout: Boolean): ShippingListModel {
        return mapperPrice.convertToDomainModelWithPrice(responses, isNewLayout)
    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.clear()
    }
}
