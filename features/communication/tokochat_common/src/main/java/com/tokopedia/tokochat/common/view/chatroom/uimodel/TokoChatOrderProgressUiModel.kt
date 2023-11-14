package com.tokopedia.tokochat.common.view.chatroom.uimodel

import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.SOURCE_LOGISTIC
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.SOURCE_TOKOFOOD

data class TokoChatOrderProgressUiModel(
    val orderProgressType: Type,
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
    val additionalInfo: String,
    val buttonText: String,
    val buttonApplink: String,
    val buttonEnable: Boolean
) {
    enum class Type(val value: String) {
        TOKOFOOD(SOURCE_TOKOFOOD),
        LOGISTIC(SOURCE_LOGISTIC);

        companion object {
            fun sourceToType(value: String): Type {
                return Type.values().firstOrNull {
                    it.value.equals(value, ignoreCase = true)
                } ?: TOKOFOOD // Default to tokofood
            }
        }
    }
}
