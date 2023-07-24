package com.tokopedia.tokochat.common.view.chatlist.uimodel

import com.tokopedia.tokochat.common.util.TokoChatValueUtil.GOFOOD
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.TOKOFOOD_SERVICE_TYPE

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
) {
    fun getStringOrderType(): String {
        return when (serviceType) {
            TOKOFOOD_SERVICE_TYPE -> GOFOOD
            else -> ""
        }
    }
}
