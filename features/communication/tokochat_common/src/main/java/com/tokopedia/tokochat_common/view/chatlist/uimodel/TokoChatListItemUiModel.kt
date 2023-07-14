package com.tokopedia.tokochat_common.view.chatlist.uimodel

data class TokoChatListItemUiModel(
    val orderId: String,
    val channelId: String,
    val driverName: String,
    val message: String,
    val business: String,
    val createAt: Long,
    val counter: Int,
    val serviceType: Int,
    val imageUrl: String
)
