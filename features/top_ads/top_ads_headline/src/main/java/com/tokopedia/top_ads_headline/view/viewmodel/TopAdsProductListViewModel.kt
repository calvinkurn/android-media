package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.GetRecommendedHeadlineProductsData
import com.tokopedia.top_ads_headline.data.TopAdsHeadlineTabModel
import com.tokopedia.top_ads_headline.usecase.GetRecommendedHeadlineProductsUseCase
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import javax.inject.Inject

const val DEFAULT_RECOMMENDATION_TAB_ID = 0
const val DEFAULT_ALL_PRODUCTS_TAB_ID = 1

class TopAdsProductListViewModel @Inject constructor(private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase,
                                                     private val getRecommendedHeadlineProductsUseCase: GetRecommendedHeadlineProductsUseCase) : ViewModel() {

    fun getTopAdsCategoryList(): ArrayList<TopAdsHeadlineTabModel> {
        return ArrayList<TopAdsHeadlineTabModel>().apply {
            add(TopAdsHeadlineTabModel(id = DEFAULT_RECOMMENDATION_TAB_ID, name = R.string.topads_headline_recommendation_category, isSelected = true))
            add(TopAdsHeadlineTabModel(id = DEFAULT_ALL_PRODUCTS_TAB_ID, name = R.string.topads_headline_all_products_category))
        }
    }

    fun getTopAdsProductList(shopId: Int, keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int, tabId: Int?,
                             onSuccess: ((List<TopAdsProductModel>, eof: Boolean) -> Unit),
                             onError: ((Throwable) -> Unit)) {
        viewModelScope.launchCatchError(
                block = {
                    if (tabId == DEFAULT_RECOMMENDATION_TAB_ID) {
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

    private fun getResponseInProductModel(response: GetRecommendedHeadlineProductsData): List<TopAdsProductModel> {
        val list = ArrayList<TopAdsProductModel>()
        response.topadsGetRecommendedHeadlineProducts.recommendedProducts.products.forEach {
            list.add(TopAdsProductModel(productID = it.id,
                    productName = it.name, productPrice = it.priceFmt, productPriceNum = it.price, productImage = it.imageURL, productRating = it.rating.toIntOrZero(),
                    productReviewCount = it.reviewCount.toIntOrZero(), departmentID = it.category.id.toIntOrZero(), departmentName = it.category.name, isRecommended = true
            ))
        }
        return list
    }
}