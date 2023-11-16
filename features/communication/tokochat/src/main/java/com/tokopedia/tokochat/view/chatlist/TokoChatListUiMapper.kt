package com.tokopedia.tokochat.view.chatlist

import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.GOSEND_INSTANT_SERVICE_TYPE
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.GOSEND_SAMEDAY_SERVICE_TYPE
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.getSource
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.util.TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT
import com.tokopedia.tokochat.util.toggle.TokoChatAbPlatform
import javax.inject.Inject

class TokoChatListUiMapper@Inject constructor(
    private val abTestPlatform: TokoChatAbPlatform
) {

    fun mapToListChat(listChannel: List<ConversationsChannel>): List<TokoChatListItemUiModel> {
        val rawResult = listChannel.mapNotNull {
            val serviceType = it.metadata?.orderInfo?.serviceType ?: 0
            if ((
                serviceType != GOSEND_INSTANT_SERVICE_TYPE &&
                    serviceType != GOSEND_SAMEDAY_SERVICE_TYPE
                ) || isTokoChatLogisticEnabled()
            ) {
                mapToChatListItem(it)
            } else {
                // Skip chat list item when service type is logistic & rollence is off
                null
            }
        }
        return rawResult.sortedByDescending {
            it.createAt
        }
    }

    private fun mapToChatListItem(conversationsChannel: ConversationsChannel): TokoChatListItemUiModel {
        val orderInfo = conversationsChannel.metadata?.orderInfo
        val lastMessage = conversationsChannel.lastMessage
        return TokoChatListItemUiModel(
            orderId = conversationsChannel.name,
            channelId = orderInfo?.id ?: "",
            driverName = orderInfo?.driver?.driverName ?: "",
            message = lastMessage?.message ?: "",
            business = orderInfo?.descriptionInfo ?: "",
            createAt = lastMessage?.createdAt ?: conversationsChannel.createdAt,
            counter = conversationsChannel.unreadCount,
            serviceType = orderInfo?.serviceType ?: Int.ZERO,
            imageUrl = orderInfo?.driver?.driverPhoto ?: ""
        )
    }

    fun mapToTypeCounter(channelList: List<ConversationsChannel>): Map<String, Int> {
        val result = HashMap<String, Int>()
        channelList.forEach {
            val serviceType = it.metadata?.orderInfo?.serviceType ?: 0
            if ((
                serviceType != GOSEND_INSTANT_SERVICE_TYPE &&
                    serviceType != GOSEND_SAMEDAY_SERVICE_TYPE
                ) || isTokoChatLogisticEnabled()
            ) {
                val serviceTypeName = getSource(
                    it.metadata?.orderInfo?.serviceType ?: Int.ZERO
                )
                val lastCounter: Int = result[serviceTypeName] ?: Int.ZERO
                result[serviceTypeName] = lastCounter + it.unreadCount
            }
        }
        return result
    }

    private fun isTokoChatLogisticEnabled(): Boolean {
        return abTestPlatform.getString(
            ROLLENCE_LOGISTIC_CHAT,
            ""
        ) == ROLLENCE_LOGISTIC_CHAT
    }
}
