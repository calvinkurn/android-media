package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlusSavings(
    @SerializedName("ticker")
    @Expose
    val plusTicker: PlusTicker = PlusTicker(),
    @SerializedName("components")
    @Expose
    val plusComponents: PlusComponent = PlusComponent()
) {
    fun shouldHide(): Boolean {
        return plusTicker.imageUrl.isEmpty() &&
                plusTicker.leftText.isEmpty() &&
                plusTicker.rightText.isEmpty()
    }
}

data class PlusComponent(
    @SerializedName("details")
    @Expose
    val plusDetailComponents: List<PlusDetail> = listOf(),
    @SerializedName("footer")
    @Expose
    val plusFooter: PlusFooter = PlusFooter()
)

data class PlusDetail(
    @SerializedName("label")
    @Expose
    val label: String = "",
    @SerializedName("value")
    @Expose
    val value: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
)

data class PlusTicker(
    @SerializedName("left")
    @Expose
    val leftText: String = "",
    @SerializedName("right")
    @Expose
    val rightText: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
)

data class PlusFooter(
    @SerializedName("total")
    @Expose
    val plusFooterTotal: PlusTotal = PlusTotal()
)

data class PlusTotal(
    @SerializedName("label")
    @Expose
    val footerLabel: String = "",
    @SerializedName("value")
    @Expose
    val footerValue: String = "",
)