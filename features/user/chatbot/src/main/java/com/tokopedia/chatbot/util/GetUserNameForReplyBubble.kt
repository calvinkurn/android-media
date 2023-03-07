package com.tokopedia.chatbot.util

import com.google.gson.GsonBuilder
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chatbot.domain.pojo.senderinfo.SenderInfoData
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetUserNameForReplyBubble @Inject constructor(
    private val userSession : UserSessionInterface
){
    fun getUserName(messageUiModel: MessageUiModel) : String? {
        if (messageUiModel.fromUid == userSession.userId) {
            return userSession.name
        }
        val senderInfoData = convertToSenderInfo(messageUiModel.source)
        if (senderInfoData != null) {
            return senderInfoData.name
        }
        return ""
    }

    private fun convertToSenderInfo(source: String): SenderInfoData? {
        val senderInfoPrefix = CHATBOT_SOURCE_CONSTANT
        return if (source.isNotEmpty() && source.startsWith(senderInfoPrefix)) {
            val s = source.substring(senderInfoPrefix.length, source.length)
            GsonBuilder().create()
                .fromJson<SenderInfoData>(s,
                    SenderInfoData::class.java)
        } else null
    }

    companion object {
        const val CHATBOT_SOURCE_CONSTANT =  "chatbot_"
    }

}