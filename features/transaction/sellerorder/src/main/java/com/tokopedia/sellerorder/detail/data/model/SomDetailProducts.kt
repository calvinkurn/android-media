package com.tokopedia.sellerorder.detail.data.model

/**
 * Created by fwidjaja on 2019-10-04.
 */
data class SomDetailProducts (
        val listProducts: List<SomDetailOrder.Data.GetSomDetail.Products> = listOf(),
        val isTopAds: Boolean = false
)