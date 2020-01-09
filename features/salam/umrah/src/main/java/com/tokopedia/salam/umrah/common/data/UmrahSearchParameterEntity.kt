package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageModel
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactory

/**
 * @author by firman on 17/10/2019
 */

data class UmrahSearchParameterEntity(
        @SerializedName("umrahSearchParameter")
        @Expose
        val umrahSearchParameter: UmrahSearchParameter = UmrahSearchParameter()
) : UmrahHomepageModel() {

    companion object {
    }

    override fun type(typeFactory: UmrahHomepageFactory): Int {
        return typeFactory.type(this)
    }
}

data class UmrahSearchParameter(
        @SerializedName("departureCities")
        @Expose
        val depatureCities: DefaultOption = DefaultOption(),
        @SerializedName("departurePeriods")
        @Expose
        val departurePeriods: DefaultOption = DefaultOption(),
        @SerializedName("priceRangeOptions")
        @Expose
        val priceRangeOptions: DefaultOption = DefaultOption(),
        @SerializedName("sortMethods")
        @Expose
        val sortMethods: DefaultOption = DefaultOption(),
        @SerializedName("priceRangeLimit")
        @Expose
        val priceRangeLimit: PriceRangeLimit = PriceRangeLimit(),
        @SerializedName("durationDaysRangeLimit")
        @Expose
        val durationDaysRangeLimit: PriceRangeLimit = PriceRangeLimit()
)

data class DefaultOption(
        @SerializedName("defaultOption")
        @Expose
        val defaultOption: Int = 0,
        @SerializedName("options")
        @Expose
        val options: List<UmrahOption> = arrayListOf()
)

data class PriceRangeLimit(
        @SerializedName("minimum")
        @Expose
        var minimum: Int = 0,
        @SerializedName("maximum")
        @Expose
        var maximum: Int = 0
)