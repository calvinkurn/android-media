package com.tokopedia.tokochat.view.chatlist

import android.content.Context
import android.util.ArrayMap
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import javax.inject.Inject

class TokoChatListUiMapper@Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun mapToChatListItem(conversationsChannel: ConversationsChannel): TokoChatListItemUiModel {
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

    fun mapToTypeCounter(chatListItems: List<TokoChatListItemUiModel>): Map<String, Int> {
        val result = ArrayMap<String, Int>()
        chatListItems.forEach {
            val serviceTypeName = it.getServiceTypeName()
            val lastCounter: Int = result.getOrDefault(it.getServiceTypeName(), Int.ZERO)
            result[serviceTypeName] = lastCounter + it.counter
        }
        return result
    }
}
