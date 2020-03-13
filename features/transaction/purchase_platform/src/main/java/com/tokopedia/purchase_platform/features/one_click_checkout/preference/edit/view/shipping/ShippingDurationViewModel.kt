package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.ShippingNoPriceResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceEditUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetShippingDurationUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper.ShippingDurationModelMapper
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper.ShippingDurationModelWithPriceMapper
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItemModelNoPrice
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ShippingDataModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Observer
import javax.inject.Inject

class ShippingDurationViewModel @Inject constructor(val useCase: GetShippingDurationUseCase,
                                                    val useCaseRates: GetRatesUseCase,
                                                    val mapper: ShippingDurationModelMapper,
                                                    val mapperPrice: ShippingDurationModelWithPriceMapper,
                                                    dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    var selectedId = -1
    private var shippingDurationModel: ShippingListModel? = null

    private val _shippingDuration = MutableLiveData<OccState<ShippingListModel>>()
    val shippingDuration: LiveData<OccState<ShippingListModel>>
        get() = _shippingDuration

    fun getShippingDuration(){
        _shippingDuration.value = OccState.Loading
        useCase.execute(onSuccess = {
//            _shippingDuration.value = OccState.Success(mapTomodel(it))
            logicSelection(mapTomodel(it))
        }, onError = {
            _shippingDuration.value = OccState.Fail(false, it, "")
        })

    }

    fun logicSelection(shippingDurationModel: ShippingListModel) {
        launch {
            withContext(Dispatchers.Default){
                val shippingList = shippingDurationModel.services
                for (item in shippingList){
                    if(item is ServicesItemModelNoPrice) {
                        item.isSelected = item.serviceId == selectedId
                    } else if (item is ServicesItemModel) {
                        item.isSelected = item.servicesId == selectedId
                    }
                }
                shippingDurationModel.services = shippingList
            }
            this@ShippingDurationViewModel.shippingDurationModel = shippingDurationModel
            _shippingDuration.value = OccState.Success(shippingDurationModel)
        }
    }

    private fun mapTomodel(responses: ShippingNoPriceResponse): ShippingListModel{
        return mapper.convertToDomainModel(responses)
    }

    fun consumeGetShippingDurationFail(){
        val value = _shippingDuration.value
        if(value is OccState.Fail){
            _shippingDuration.value = value.copy(isConsumed = true)
        }
    }

    fun setSelectedShipping(shippingId: Int){
        val shippingModel = shippingDurationModel
        if(shippingModel != null && _shippingDuration.value is OccState.Success) {
            selectedId = shippingId
            logicSelection(shippingModel)
//            shippingModel?.let { logicSelection(it) }
        }
    }


    /*With Price*/
    fun getRates(listShopShipment: ArrayList<ShopShipment>?, shippingParam: ShippingParam?){
        _shippingDuration.value = OccState.Loading
        val ratesParamBuilder = shippingParam?.let { listShopShipment?.let { list -> RatesParam.Builder(list, it) } }
        val ratesParam = ratesParamBuilder?.build()
        ratesParam?.occ = "1"
        ratesParam?.let {
            useCaseRates.execute(it)
                .subscribe(object : Observer<ShippingRecommendationData> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(shippingRecomendationData: ShippingRecommendationData) {
                        logicSelection(mapTomodelPrice(shippingRecomendationData))

                    }

                    override fun onCompleted() {
                    }
                })
        }
    }

    private fun mapTomodelPrice(responses: ShippingRecommendationData): ShippingListModel{
        return mapperPrice.convertToDomainModelWithPrice(responses)
    }

}


