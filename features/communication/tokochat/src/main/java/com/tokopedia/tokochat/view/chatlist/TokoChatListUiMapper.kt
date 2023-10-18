package com.tokopedia.tokochat.view.chatlist

import android.util.ArrayMap
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.getSource
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import javax.inject.Inject

class TokoChatListUiMapper@Inject constructor() {

    fun mapToListChat(listChannel: List<ConversationsChannel>): List<TokoChatListItemUiModel> {
        val rawResult = listChannel.map {
            mapToChatListItem(it)
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
        val result = ArrayMap<String, Int>()
        channelList.forEach {
            val serviceTypeName = getSource(
                it.metadata?.orderInfo?.serviceType ?: Int.ZERO
            )
            val lastCounter: Int = result.getOrDefault(serviceTypeName, Int.ZERO)
            result[serviceTypeName] = lastCounter + it.unreadCount
        }
        return result
    }
}
