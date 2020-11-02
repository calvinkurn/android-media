package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.top_ads_headline.data.TopAdsCategoryDataModel
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.domain.usecase.GetEtalaseListUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import javax.inject.Inject


class TopAdsProductListViewModel @Inject constructor(private val getEtalaseListUseCase: GetEtalaseListUseCase,
                                                     private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase) : ViewModel() {

    private var etalaseListLiveData: MutableLiveData<ArrayList<TopAdsCategoryDataModel>> = MutableLiveData()

    fun getEtalaseListLiveData(): LiveData<ArrayList<TopAdsCategoryDataModel>> = etalaseListLiveData

    fun getEtalaseList(shopId: String) {
        getEtalaseListUseCase.setParams(shopId)
        viewModelScope.launchCatchError(
                block = {
                    val response = getEtalaseListUseCase.executeOnBackground()
                    etalaseListLiveData.value = getTopAdsCategoryList(response.shopShowcasesByShopID.result)
                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    private fun getTopAdsCategoryList(list: List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>): ArrayList<TopAdsCategoryDataModel>? {
        return ArrayList<TopAdsCategoryDataModel>().apply {
            list.forEach {
                if (it.count != 0) {
                    add(TopAdsCategoryDataModel(it.count, it.id, it.name, it.type))
                }
            }
            first().isSelected = true
        }
    }

    fun getTopAdsProductList(shopId: Int, keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int,
                             onSuccess: ((List<ResponseProductList.Result.TopadsGetListProduct.Data>, eof: Boolean) -> Unit),
                             onError: ((Throwable) -> Unit)) {
        topAdsGetListProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start, shopId)
        viewModelScope.launchCatchError(
                block = {
                    val response = topAdsGetListProductUseCase.executeOnBackground()
                    onSuccess(response.topadsGetListProduct.data, response.topadsGetListProduct.eof)
                },
                onError = {
                    onError(it)
                    it.printStackTrace()
                }
        )
    }
}