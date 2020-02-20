package com.tokopedia.salam.umrah.search.data

import com.google.gson.annotations.SerializedName

/**
 * @author by firman on 18/10/2019
 */
data class UmrahSearchProductDataParam(
        @SerializedName("categorySlugName")
        var categorySlugName: String = "",
        @SerializedName("departurePeriod")
        var departurePeriod: String = "",
        @SerializedName("priceMin")
        var priceMin: Int = 0,
        @SerializedName("priceMax")
        var priceMax: Int  = 0,
        @SerializedName("departureCityId")
        var departureCityId: String  = "",
        @SerializedName("durationDaysMin")
        var durationDaysMin: Int  = 0,
        @SerializedName("durationDaysMax")
        var durationDaysMax: Int = 0,
        @SerializedName("airlineId")
        var airlineId: String  = "",
        @SerializedName("sortMethod")
        var sortMethod: String = "",
        @SerializedName("page")
        var page: Int = 1,
        @SerializedName("limit")
        var limit: Int  = 20)