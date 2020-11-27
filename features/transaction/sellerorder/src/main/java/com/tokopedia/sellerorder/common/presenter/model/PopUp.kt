package com.tokopedia.sellerorder.common.presenter.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PopUp(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("body")
        @Expose
        val body: String = "",

        @SerializedName("actionButton")
        @Expose
        val actionButtons: List<ActionButton> = emptyList()
) {
    data class ActionButton(
            @SerializedName("displayName")
            @Expose
            val displayName: String = "",

            @SerializedName("color")
            @Expose
            val color: String = "",

            @SerializedName("type")
            @Expose
            val type: String = ""
    )
}