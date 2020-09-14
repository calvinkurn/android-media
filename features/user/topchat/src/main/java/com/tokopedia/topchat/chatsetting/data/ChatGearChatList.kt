package com.tokopedia.topchat.chatsetting.data


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingBuyerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingSellerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingUtilsUiModel

data class ChatGearChatList(
        @SerializedName("isSuccess")
        val isSuccess: Boolean = false,
        @SerializedName("listBuyer")
        val listBuyer: List<ChatSettingBuyerUiModel> = listOf(),
        @SerializedName("listSeller")
        val listSeller: List<ChatSettingSellerUiModel> = listOf(),
        @SerializedName("listUtils")
        val listUtils: List<ChatSettingUtilsUiModel> = listOf()
)