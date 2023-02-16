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

    @SerializedName("socialProofID")
    @Expose
    val socialProofId: String = "",

    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("subtitle")
    @Expose
    val subtitle: String = "",

    @SerializedName("icon")
    @Expose
    val icon: String = "",

    @SerializedName("applink")
    @Expose
    val appLink: EduLinkData = EduLinkData()
) {

    companion object {
        const val CHIP = "chip"
        const val TEXT = "text"
        const val ORANGE_CHIP = "orange_chip"

        const val TALK_ID = "talk"
        const val RATING_ID = "rating"
        const val MEDIA_ID = "media"
        const val SHOP_RATING_ID = "shop_rating"
        const val NEW_PRODUCT_ID = "new_product"
    }
}
