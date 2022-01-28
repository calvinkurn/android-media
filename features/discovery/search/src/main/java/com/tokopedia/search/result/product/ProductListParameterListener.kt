package com.tokopedia.search.result.product

interface ProductListParameterListener {

    val queryKey: String

    fun reloadData()

    fun refreshSearchParameter(queryParams: Map<String, String>)
}