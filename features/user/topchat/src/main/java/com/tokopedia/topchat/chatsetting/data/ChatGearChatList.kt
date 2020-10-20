package com.tokopedia.topchat.chatsetting.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingBuyerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingSellerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingUtilsUiModel

data class ChatGearChatList(
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Boolean = false,
        @SerializedName("listBuyer")
        @Expose
        val listBuyer: List<ChatSettingBuyerUiModel> = listOf(),
        @SerializedName("listSeller")
        @Expose
        val listSeller: List<ChatSettingSellerUiModel> = listOf(),
        @SerializedName("listUtils")
        @Expose
        val listUtils: List<ChatSettingUtilsUiModel> = listOf()
)