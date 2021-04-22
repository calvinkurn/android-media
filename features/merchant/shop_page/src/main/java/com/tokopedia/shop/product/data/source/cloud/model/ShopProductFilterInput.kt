package com.tokopedia.shop.product.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopProductFilterInput(
        @SerializedName("page")
        @Expose
        var page: Int = 1,

        @SerializedName("perPage")
        @Expose
        var perPage: Int = 10,

        @SerializedName("fkeyword")
        @Expose
        var searchKeyword: String = "",

        @SerializedName("fmenu")
        @Expose
        var etalaseMenu: String = "",

        @SerializedName("sort")
        @Expose
        var sort: Int = 0,

        @SerializedName("rating")
        @Expose
        var rating: String = "",

        @SerializedName("pmax")
        @Expose
        var pmax: Int = 0,

        @SerializedName("pmin")
        @Expose
        var pmin: Int = 0,

        @SerializedName("user_districtId")
        @Expose
        var userDistrictId: String = "",

        @SerializedName("user_cityId")
        @Expose
        var userCityId: String = "",

        @SerializedName("user_lat")
        @Expose
        var userLat: String = "",

        @SerializedName("user_long")
        @Expose
        var userLong: String = ""
)