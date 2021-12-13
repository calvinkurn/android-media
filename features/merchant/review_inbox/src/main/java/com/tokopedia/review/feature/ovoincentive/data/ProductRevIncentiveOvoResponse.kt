package com.tokopedia.review.feature.ovoincentive.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero

data class ProductRevIncentiveOvoResponse(
    @SerializedName("ticker")
    @Expose
    var ticker: TickerResponse = TickerResponse(),
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("subtitle")
    @Expose
    var subtitle: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("numbered_list")
    @Expose
    var numberedList: List<String> = listOf(),
    @SerializedName("cta_text")
    @Expose
    var ctaText: String = "",
    @SerializedName("amount")
    @Expose
    val amount: Int = 0,
    @SerializedName("bottom_sheet")
    @Expose
    val bottomSheetText: String = "",
    @SerializedName("image_url")
    @Expose
    val thankYouImage: String = ""
) {
    fun hasIncentive(): Boolean {
        return amount.isMoreThanZero()
    }

    fun hasOngoingPromo(): Boolean {
        return amount.isZero()
    }
}