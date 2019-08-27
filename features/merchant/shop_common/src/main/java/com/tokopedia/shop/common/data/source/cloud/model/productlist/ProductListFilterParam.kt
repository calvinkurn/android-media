package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductListFilterParam(

        @SerializedName("page")
        @Expose
        var page: Int = 0,

        @SerializedName("perPage")
        @Expose
        var perPage: Int = 20,

        @SerializedName("fkeyword")
        @Expose
        var fKeyword: String = "",

        @SerializedName("fmenu")
        @Expose
        var fMenu: String = "",

        @SerializedName("fcategory")
        @Expose
        var fCategory: Int = 0,

        @SerializedName("fcondition")
        @Expose
        var fCondition: Int = 0,

        @SerializedName("fcatalog")
        @Expose
        var fCatalog: Int = 0,

        @SerializedName("fpicture")
        @Expose
        var fPicture: Int = 0,

        @SerializedName("sort")
        @Expose
        var sort: Int = 0
)
