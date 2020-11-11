package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.top_ads_headline.data.CpmModelMapper
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import com.tokopedia.topads.sdk.domain.model.CpmModel
import javax.inject.Inject

class AdContentViewModel @Inject constructor(private val cpmModelMapper: CpmModelMapper,
                                             private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase) : ViewModel() {

    fun getTopAdsProductList(shopId: Int, keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int,
                             onSuccess: ((cpmModel: CpmModel) -> Unit),
                             onError: ((String) -> Unit)) {
        viewModelScope.launchCatchError(
                block = {
                    topAdsGetListProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start, shopId)
                    val response = topAdsGetListProductUseCase.executeOnBackground()
                    onSuccess(cpmModelMapper.getCpmModelResponse(response.topadsGetListProduct.data as ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>))
                },
                onError = {
                    onError(it.message ?: "")
                    it.printStackTrace()
                })
    }
}