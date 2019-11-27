package com.tokopedia.salam.umrah.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.common.data.UmrahOption
import com.tokopedia.salam.umrah.common.data.PriceRangeLimit
/**
 * @author by M on 18/10/2019
 */
data class Filter(
        @SerializedName("departurePeriods")
        @Expose
        var departurePeriods: List<UmrahOption> = listOf(),

        @SerializedName("departureCities")
        @Expose
        var departureCities: List<UmrahOption> = listOf(),

        @SerializedName("durationDaysRangeLimit")
        @Expose
        var durationDaysRangeLimit: PriceRangeLimit = PriceRangeLimit(),

        @SerializedName("priceRangeLimit")
        @Expose
        var priceRangeLimit: PriceRangeLimit = PriceRangeLimit()
)