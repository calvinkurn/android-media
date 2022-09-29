package com.tokopedia.watch.ordersummary.model

import com.google.gson.annotations.SerializedName

data class DataKeyParameterModel(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("parameters")
    val parameters: String = "{}",
) {
    companion object {
        const val KEY_NEW_ORDER = "newOrder"
        const val KEY_READY_TO_SHIP = "readyToShipOrder"
        const val KEY_UNREAD_CHAT = "unreadChat"
    }
}