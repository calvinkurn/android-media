package com.tokopedia.shopdiscount.select.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING

data class FilterRequest(
    @SerializedName("page")
    val page: Int,

    @SerializedName("page_size")
    val pageSize: Int,

    @SerializedName("keyword")
    val keyword: String = EMPTY_STRING,

    @SerializedName("etalase_ids")
    val etalaseIds: List<String> = emptyList(),

    @SerializedName("category_ids")
    val categoryIds: List<String> = emptyList(),

    @SerializedName("warehouse_ids")
    val warehouseIds: List<String> = emptyList()
)