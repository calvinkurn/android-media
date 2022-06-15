package com.tokopedia.search.result.product.searchparameterprovider

import com.tokopedia.discovery.common.model.SearchParameter

interface SearchParameterProvider {
    fun getSearchParameter(): SearchParameter?
}