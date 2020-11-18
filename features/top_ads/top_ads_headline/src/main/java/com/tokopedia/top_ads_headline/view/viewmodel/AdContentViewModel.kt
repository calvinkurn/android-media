package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import javax.inject.Inject

class AdContentViewModel @Inject constructor(private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase) : ViewModel() {

    fun getTopAdsProductList(shopId: Int, keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int,
                             onSuccess: ((List<ResponseProductList.Result.TopadsGetListProduct.Data>) -> Unit),
                             onError: ((String) -> Unit)) {
        viewModelScope.launchCatchError(
                block = {
                    topAdsGetListProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start, shopId)
                    val response = topAdsGetListProductUseCase.executeOnBackground()
                    onSuccess(response.topadsGetListProduct.data)
                },
                onError = {
                    onError(it.message ?: "")
                    it.printStackTrace()
                })
    }
}