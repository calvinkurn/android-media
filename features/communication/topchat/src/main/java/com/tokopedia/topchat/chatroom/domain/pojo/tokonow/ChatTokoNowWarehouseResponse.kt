package com.tokopedia.topchat.chatroom.domain.pojo.tokonow


import com.google.gson.annotations.SerializedName

data class ChatTokoNowWarehouseResponse(
    @SerializedName("chatTokoNowWarehouse")
    var chatTokoNowWarehouse: ChatTokoNowWarehouse = ChatTokoNowWarehouse()
)