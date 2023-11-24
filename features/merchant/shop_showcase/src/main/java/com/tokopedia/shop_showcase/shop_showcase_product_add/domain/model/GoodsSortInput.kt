package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoodsSortInput(
    @Expose
    @SerializedName("id")
    var id: String = "",
    @Expose
    @SerializedName("value")
    var value: String = ""
){
    companion object {
        const val SORT_ID_DEFAULT = "DEFAULT"
        const val SORT_VALUE_DEFAULT = "DESC"
    }
}
