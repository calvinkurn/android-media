package com.tokopedia.topchat.chatroom.domain.pojo.tokonow


import com.google.gson.annotations.SerializedName

data class ChatTokoNowWarehouse(
    @SerializedName("warehouseId")
    var warehouseId: String = "0"
)