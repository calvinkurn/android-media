package com.tokopedia.product.detail.data.model.social_proof

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.pdplayout.EduLinkData

/**
 * Created by yovi.putra on 15/02/23"
 * Project name: android-tokopedia-core
 **/

data class SocialProofData(
    @SerializedName("socialProofType")
    @Expose
    val socialProofType: String = "",

    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("subtitle")
    @Expose
    val subtitle: String = "",

    @SerializedName("icon")
    @Expose
    val icon: String = "",

    @SerializedName("shopAdditional")
    @Expose
    val eduLink: EduLinkData = EduLinkData()
) {

    companion object {
        const val CHIP = "chip"
        const val TEXT = "text"
        const val ORANGE_CHIP = "orange_chip"
    }
}
