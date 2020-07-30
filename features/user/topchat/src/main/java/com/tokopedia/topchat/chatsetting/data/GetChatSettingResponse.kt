package com.tokopedia.topchat.chatsetting.data


import com.google.gson.annotations.SerializedName

data class GetChatSettingResponse(
    @SerializedName("ChatGetGearList")
    val chatGetGearList: ChatGetGearList = ChatGetGearList()
)