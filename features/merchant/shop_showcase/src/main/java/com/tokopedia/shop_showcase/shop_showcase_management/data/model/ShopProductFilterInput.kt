package com.tokopedia.shop_showcase.shop_showcase_management.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopProductFilterInput (
        @SerializedName("page")
        @Expose
        var page: Int = 1,

        @SerializedName("perPage")
        @Expose
        var perPage: Int = 1,

        @SerializedName("fkeyword")
        @Expose
        var searchKeyword: String = "",

        @SerializedName("fmenu")
        @Expose
        var etalaseMenu: String = "",

        @SerializedName("sort")
        @Expose
        var sort: Int = 0
)