package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.promoprice.PromoPriceUiModel

// https://tokopedia.atlassian.net/wiki/spaces/PDP/pages/2421492033/PDP+Promo+Revamp#Campaign-Revamp
data class PromoPriceResponse(
    @SerializedName("iconURL")
    val iconUrl: String = "",
    @SerializedName("promoPriceFmt")
    val promoPriceFmt: String = "",
    @SerializedName("subtitle")
    val promoSubtitle: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("color")
    val mainTextColor: String = "",
    @SerializedName("background")
    val backgroundColor: String = "",
    @SerializedName("superGraphicURL")
    val superGraphicUrl: String = "",
    @SerializedName("separatorColor")
    val separatorColor: String = "",
    @SerializedName("priceAdditionalFmt")
    val priceAdditionalFmt: String = ""
)

fun PromoPriceResponse.mapIntoPromoPriceUiModel(
    slashPriceFmt: String
): PromoPriceUiModel? {
    if (promoPriceFmt.isEmpty()) return null
    return PromoPriceUiModel(
        priceAdditionalFmt = priceAdditionalFmt,
        promoPriceFmt = promoPriceFmt,
        promoSubtitle = promoSubtitle,
        slashPriceFmt = slashPriceFmt,
        separatorColor = separatorColor,
        mainTextColor = mainTextColor,
        cardBackgroundColor = backgroundColor,
        mainIconUrl = iconUrl,
        boIconUrl = "",
        superGraphicIconUrl = superGraphicUrl,
        applink = applink
    )
}