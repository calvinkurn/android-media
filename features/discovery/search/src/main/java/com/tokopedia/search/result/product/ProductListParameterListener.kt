package com.tokopedia.search.result.product

interface ProductListParameterListener {

    fun reloadData()

    fun refreshSearchParameter(queryParams: Map<String, String>)
}