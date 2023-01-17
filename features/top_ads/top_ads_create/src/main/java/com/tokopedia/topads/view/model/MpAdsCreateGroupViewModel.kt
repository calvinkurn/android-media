package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.Product
import com.tokopedia.topads.common.data.response.TopAdsProductResponse
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetProductUseCase
import javax.inject.Inject

class MpAdsCreateGroupViewModel@Inject constructor(
    private val bidInfoUseCase: BidInfoUseCase,
    private val topAdsGetProductUseCase: TopAdsGetProductUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    fun getBidInfo(suggestions: List<DataSuggestions>, sourceValue: String, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit) {
        bidInfoUseCase.setParams(suggestions, ParamObject.AUTO_BID_STATE, sourceValue)
        bidInfoUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topadsBidInfo.data)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    fun getProduct(productid: String, onSuccess: (TopAdsProductResponse) -> Unit){
        topAdsGetProductUseCase.getProduct(productid,
            {
                onSuccess(it)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    override fun onCleared() {
        super.onCleared()
        bidInfoUseCase.cancelJobs()
    }
}
