package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoodsFilterInput(
    @Expose
    @SerializedName("id")
    var id: String = "",
    @Expose
    @SerializedName("value")
    var value: List<String> = listOf()
){
    companion object {
        const val FILTER_ID_PAGE = "page"
        const val FILTER_ID_PAGE_SIZE = "pageSize"
        const val FILTER_ID_KEYWORD = "keyword"
        const val FILTER_ID_MENU = "menu"
        const val FILTER_ID_CATEGORY = "category"
    }
}
