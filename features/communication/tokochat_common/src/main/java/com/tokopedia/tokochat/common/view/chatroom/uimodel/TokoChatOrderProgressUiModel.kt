package com.tokopedia.tokochat.common.view.chatroom.uimodel

data class TokoChatOrderProgressUiModel(
    val isEnable: Boolean,
    val imageUrl: String,
    val invoiceId: String,
    val labelTitle: String,
    val labelValue: String,
    val name: String,
    val orderId: String,
    val state: String,
    val status: String,
    val statusId: Long,
    val appLink: String,
)
