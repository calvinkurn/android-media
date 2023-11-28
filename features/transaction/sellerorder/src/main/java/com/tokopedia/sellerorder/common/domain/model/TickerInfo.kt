package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerInfo(
        @SerializedName("text")
        @Expose
        val text: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("action_text")
        @Expose
        val actionText: String = "",

        @SerializedName("action_key")
        @Expose
        val actionKey: String = "",

        @SerializedName("action_url")
        @Expose
        val actionUrl: String = "",

        @SerializedName("cta_text")
        @Expose
        val ctaText: String = "",

        @SerializedName("cta_action_type")
        @Expose
        val ctaActionType: String = "",

        @SerializedName("cta_action_value")
        @Expose
        val ctaActionValue: String = ""
) {
    companion object {
        const val CTA_TYPE_ACTION = "CTA_ACTION"
        const val CTA_TYPE_URI = "CTA_URI"

        const val CTA_ACTION_VALUE_POF = "order_detail_pof"
    }
}
