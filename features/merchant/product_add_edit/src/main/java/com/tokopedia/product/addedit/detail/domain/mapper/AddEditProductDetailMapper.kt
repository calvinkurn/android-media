package com.tokopedia.product.addedit.detail.domain.mapper

import com.tokopedia.product.addedit.detail.domain.UniverseSearchResponse
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIVERSE_SEARCH_TYPE

object AddEditProductDetailMapper {

    fun getProductNameAutoComplete(universeSearchResponse: UniverseSearchResponse): List<UniverseSearchResponse.UniverseSearch.Data> {
        return universeSearchResponse.universeSearch.data.filter { it.name == UNIVERSE_SEARCH_TYPE }
    }

    fun getProductNameStart(getProductNameAutoComplete: List<UniverseSearchResponse.UniverseSearch.Data>, keyword: String): List<String> {
        val listProductName: MutableList<String> = mutableListOf()
        val maxSuggestionName = 5
        getProductNameAutoComplete.map {  data ->
            data.items.map {
                if(listProductName.size <= maxSuggestionName) {
                    listProductName.add(it.keyword)
                }
            }
        }

        return listProductName.toList()
    }
}