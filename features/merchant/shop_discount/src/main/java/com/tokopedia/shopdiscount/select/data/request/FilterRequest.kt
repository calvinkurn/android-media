package com.tokopedia.shopdiscount.select.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING

data class FilterRequest(
    @SerializedName("page")
    @Expose
    val page: Int,

    @SerializedName("page_size")
    @Expose
    val pageSize: Int,

    @SerializedName("keyword")
    @Expose
    val keyword: String = EMPTY_STRING,

    @SerializedName("etalase_ids")
    @Expose
    val etalaseIds: List<String> = emptyList(),

    @SerializedName("category_ids")
    @Expose
    val categoryIds: List<String> = emptyList(),

    @SerializedName("warehouse_ids")
    @Expose
    val warehouseIds: List<String> = emptyList()
)