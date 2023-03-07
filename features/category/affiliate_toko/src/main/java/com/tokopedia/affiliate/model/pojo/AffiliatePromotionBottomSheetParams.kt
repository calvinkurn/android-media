package com.tokopedia.affiliate.model.pojo

import java.io.Serializable

data class AffiliatePromotionBottomSheetParams(
    val idArray: ArrayList<Int>?,
    val itemId: String,
    val itemName: String,
    val itemImage: String,
    val itemUrl: String,
    val productIdentifier: String,
    val origin: Int = 1, // default origin promosikan = 1
    val isLinkGenerationEnabled: Boolean = true,
    val commission: String = "",
    val status: String = "",
    val type: String? = "pdp",
    val ssaInfo: SSAInfo? = null
) : Serializable {
    data class SSAInfo(
        val ssaStatus: Boolean,
        val ssaMessage: String,
        val message: String,
        val label: Label
    ) : Serializable {
        data class Label(
            val labelType: String,
            val labelText: String
        ) : Serializable
    }
}
