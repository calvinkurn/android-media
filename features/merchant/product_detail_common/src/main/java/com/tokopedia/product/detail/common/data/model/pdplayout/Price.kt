package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.percentFormatted
import com.tokopedia.product.detail.common.data.model.variant.VariantChild

data class Price(
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("lastUpdateUnix")
    val lastUpdateUnix: String = "",
    @SerializedName("value")
    val value: Double = 0.0,
    @SerializedName("priceFmt")
    val priceFmt: String = "",
    @SerializedName("slashPriceFmt")
    val slashPriceFmt: String = "",
    @SerializedName("discPercentage")
    val discPercentage: String = "",
    @SerializedName("isPriceMasked")
    val isPriceMasked: Boolean = false
) {

    fun updatePrice(variantChild: VariantChild?) = copy(
        value = variantChild?.price.orZero(),
        priceFmt = variantChild?.priceFmt.ifNullOrBlank {
            variantChild?.finalPrice?.getCurrencyFormatted().orEmpty()
        },
        slashPriceFmt = variantChild?.slashPriceFmt.ifNullOrBlank {
            variantChild?.finalMainPrice?.getCurrencyFormatted().orEmpty()
        },
        discPercentage = variantChild?.discPercentage.ifNullOrBlank {
            variantChild?.campaign?.discountedPercentage?.percentFormatted().orEmpty()
        },
        isPriceMasked = variantChild?.isPriceMasked.orFalse()
    )

    fun updatePriceFmt() = copy(
        priceFmt = priceFmt.ifNullOrBlank {
            value.getCurrencyFormatted()
        }
    )
}
