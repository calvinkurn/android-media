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
                    item.isSelected = item.serviceId == selectedId
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
    fun getRates(){
        val shippingParam = generateShippingParam()
        val ratesParamBuilder = RatesParam.Builder(generateListShopShipment(), shippingParam)
        val ratesParam = ratesParamBuilder.build()
        ratesParam.occ = 1
        useCaseRates.execute(ratesParam)
                .subscribe(object : Observer<ShippingRecommendationData> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(shippingRecomendationData: ShippingRecommendationData) {
                        logicSelectionPrice(mapTomodelPrice(shippingRecomendationData))
//                        _shippingDuration.value = mapTomodelPrice(shippingRecomendationData)
                       /* val value = _shippingDuration.value
                        if(value != null) {
                            val current = _shippingDuration.value
                        }
*/

//                        logicSelectionPrice(mapTomodelPrice(shippingRecomendationData))

/*
                        val shippingDataModel = shippingRecomendationData.shippingDurationViewModels
                        var selectedShippingData: ShippingDurationUiModel? = null
                        for(shippingData in shippingDataModel) {
                            shippingData.isSelected = true
                            selectedShippingData = shippingData
                        }*/

                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun mapTomodelPrice(responses: ShippingRecommendationData): ShippingListModel{
        return mapperPrice.convertToDomainModelWithPrice(responses)
    }

    fun logicSelectionPrice(shippingDurationModel: ShippingListModel) {
        launch {
            withContext(Dispatchers.Default){
                val shippingList = shippingDurationModel.servicesPrice
                for (item in shippingList){
                    item.isSelected = item.servicesId == selectedId
                }
                shippingDurationModel.servicesPrice = shippingList
            }
            this@ShippingDurationViewModel.shippingDurationModel = shippingDurationModel
            _shippingDuration.value = OccState.Success(shippingDurationModel)
        }
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
}


