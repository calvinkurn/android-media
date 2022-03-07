package com.tokopedia.product.addedit.detail.domain.mapper

import com.tokopedia.product.addedit.detail.domain.UniverseSearchResponse
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_SUGGESTION_NAME
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIVERSE_SEARCH_TYPE

object ProductNameRecommendationMapper {

    fun getProductNameAutoComplete(universeSearchResponse: UniverseSearchResponse): List<UniverseSearchResponse.UniverseSearch.Data> {
        return universeSearchResponse.universeSearch.data.filter { it.name == UNIVERSE_SEARCH_TYPE }
    }

    fun getFinalProductName(getProductNameAutoComplete: List<UniverseSearchResponse.UniverseSearch.Data>): List<String> {
        val productNameList: MutableList<String> = mutableListOf()
        getProductNameAutoComplete.map {  data ->
            data.items.forEach {
                if(productNameList.size <= MAX_SUGGESTION_NAME) {
                    productNameList.add(it.keyword)
                }
            }
        }

        return productNameList.toList()
    }
}