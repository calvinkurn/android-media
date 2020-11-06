package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.data.GetRecommendedHeadlineProductsData
import com.tokopedia.top_ads_headline.data.TopAdsCategoryDataModel
import com.tokopedia.top_ads_headline.usecase.GetRecommendedHeadlineProductsUseCase
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.domain.usecase.GetEtalaseListUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import javax.inject.Inject

private const val DEFAULT_RECOMMENDATION_CATEGORY_NAME = "Rekomendasi"
const val DEFAULT_RECOMMENDATION_CATEGORY_ID = "-1"

class TopAdsProductListViewModel @Inject constructor(private val getEtalaseListUseCase: GetEtalaseListUseCase,
                                                     private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase,
                                                     private val getRecommendedHeadlineProductsUseCase: GetRecommendedHeadlineProductsUseCase) : ViewModel() {

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
            //adding first category of rekomendasi
            add(TopAdsCategoryDataModel(count = 0, id = DEFAULT_RECOMMENDATION_CATEGORY_ID, name = DEFAULT_RECOMMENDATION_CATEGORY_NAME, type = 1))
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
        viewModelScope.launchCatchError(
                block = {
                    if (etalaseId == DEFAULT_RECOMMENDATION_CATEGORY_ID && keyword.isBlank()) {
                        getRecommendedHeadlineProductsUseCase.setParams(shopId = shopId.toString())
                        val response = getRecommendedHeadlineProductsUseCase.executeOnBackground()
                        if (response.topadsGetRecommendedHeadlineProducts.errors.isNotEmpty()) {
                            onError(Throwable(response.topadsGetRecommendedHeadlineProducts.errors.first().detail))
                        } else {
                            val responseInProductListMode = getResponseInProductModel(response)
                            onSuccess(responseInProductListMode, false)
                        }
                    } else {
                        topAdsGetListProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start, shopId)
                        val response = topAdsGetListProductUseCase.executeOnBackground()
                        onSuccess(response.topadsGetListProduct.data, response.topadsGetListProduct.eof)
                    }
                },
                onError = {
                    onError(it)
                    it.printStackTrace()
                }
        )
    }

    private fun getResponseInProductModel(response: GetRecommendedHeadlineProductsData.Data): List<ResponseProductList.Result.TopadsGetListProduct.Data> {
        val list = ArrayList<ResponseProductList.Result.TopadsGetListProduct.Data>()
        response.topadsGetRecommendedHeadlineProducts.recommendedProducts.products.forEach {
            list.add(ResponseProductList.Result.TopadsGetListProduct.Data(productID = it.id.toIntOrZero(),
                    productName = it.name, productPrice = it.priceFmt, productPriceNum = it.price, productImage = it.imageURL, productRating = it.rating.toIntOrZero(),
                    productReviewCount = it.reviewCount.toIntOrZero(), departmentId = it.category.id.toIntOrZero(), departmentName = it.category.name, isRecommended = true
            ))
        }
        return list
    }
}