package com.tokopedia.tokochat.common.view.chatlist.uimodel

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.GOFOOD
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.GOSEND_INSTANT_SERVICE_TYPE
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.GOSEND_SAMEDAY_SERVICE_TYPE
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.SHOPPING_LOGISTIC
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
) : Comparable<TokoChatListItemUiModel> {
    fun getStringOrderType(): String {
        return when (serviceType) {
            TOKOFOOD_SERVICE_TYPE -> GOFOOD
            GOSEND_INSTANT_SERVICE_TYPE -> SHOPPING_LOGISTIC
            GOSEND_SAMEDAY_SERVICE_TYPE -> SHOPPING_LOGISTIC
            else -> ""
        }
    }

    override fun compareTo(other: TokoChatListItemUiModel): Int {
        val result = createAt - other.createAt
        return when {
            (result > 1) -> Int.ONE
            (result == 0L) -> Int.ZERO
            (result < 0L) -> -1
            else -> -1
        }
    }
}
