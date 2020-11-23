package com.tokopedia.product.addedit.detail.domain.mapper

import com.tokopedia.product.addedit.detail.domain.UniverseSearchResponse
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIVERSE_SEARCH_TYPE

object AddEditProductDetailMapper {

    fun getProductNameAutoComplete(universeSearchResponse: UniverseSearchResponse): List<UniverseSearchResponse.UniverseSearch.Data> {
        return universeSearchResponse.universeSearch.data.filter { it.name == UNIVERSE_SEARCH_TYPE }
    }

    fun getFinalProductName(getProductNameAutoComplete: List<UniverseSearchResponse.UniverseSearch.Data>): List<String> {
        val productNameList: MutableList<String> = mutableListOf()
        val maxSuggestionName = 5
        val maxLengthKeyword = 70
        getProductNameAutoComplete.map {  data ->
            data.items.forEach {
                if(productNameList.size <= maxSuggestionName) {
                    if(it.keyword.length > maxLengthKeyword) {
                        productNameList.add(it.keyword.take(maxLengthKeyword))
                    } else {
                        productNameList.add(it.keyword)
                    }
                }
            }
        }

        return productNameList.toList()
    }
}