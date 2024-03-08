package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ThematicCampaign (
    @SerializedName("campaignName")
    val campaignName: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("background")
    val background: String = "",
    @SerializedName("additionalInfo")
    val additionalInfo: String = "",
    @SerializedName("productID")
    @Expose
    val productId: String = "",
    @SerializedName("campaignLogo")
    @Expose
    val campaignLogo: String = "",
    @SerializedName("applink")
    @Expose
    val applink: String = "",
    @SerializedName("superGraphicURL")
    @Expose
    val superGraphicURL: String = ""
) {

    val isMegaType
        get() = superGraphicURL.isNotBlank()
}
