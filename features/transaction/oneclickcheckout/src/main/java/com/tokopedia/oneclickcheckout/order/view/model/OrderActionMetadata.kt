package com.tokopedia.oneclickcheckout.order.view.model

import com.google.gson.annotations.SerializedName

data class OrderActionMetadata(
    @SerializedName("funnel")
    val funnel: String = "occ",
    @SerializedName("action")
    val listAction: List<Action> = emptyList()
) {
    data class Action(
        @SerializedName("unique_id")
        val uniqueId: String = "",
        @SerializedName("metadata")
        val listMetadata: List<Metadata> = emptyList()
    ) {
        data class Metadata(
            @SerializedName("key")
            val key: String = "",
            @SerializedName("value")
            val value: String = ""
        )
    }
}
