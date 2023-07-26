package com.tokopedia.tokochat.common.view.chatroom.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat.common.view.chatroom.adapter.delegate.TokoChatHeaderDateDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.delegate.TokoChatImageBubbleDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.delegate.TokoChatMessageBubbleDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.delegate.TokoChatMessageCensorDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.delegate.TokoChatReminderTickerDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.delegate.TokoChatShimmerDelegate
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatMessageBubbleListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatMessageCensorListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatReminderTickerUiModel

open class TokoChatBaseAdapter(
    reminderTickerListener: TokochatReminderTickerListener,
    imageAttachmentListener: TokoChatImageAttachmentListener,
    bubbleMessageBubbleListener: TokoChatMessageBubbleListener,
    messageCensorListener: TokoChatMessageCensorListener
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatShimmerDelegate())
        delegatesManager.addDelegate(TokoChatReminderTickerDelegate(reminderTickerListener))
        delegatesManager.addDelegate(TokoChatMessageBubbleDelegate(bubbleMessageBubbleListener))
        delegatesManager.addDelegate(TokoChatHeaderDateDelegate())
        delegatesManager.addDelegate(TokoChatImageBubbleDelegate(imageAttachmentListener))
        delegatesManager.addDelegate(TokoChatMessageCensorDelegate(messageCensorListener))
    }

    fun getImageAttachmentPairWithId(id: String): Pair<Int, TokoChatImageBubbleUiModel>? {
        itemList.forEachIndexed { index, item ->
            if (item is TokoChatImageBubbleUiModel && item.imageId == id) {
                return Pair(index, item)
            }
        }
        return null
    }

    fun getTickerPairWithTag(tag: String): Pair<Int, TokoChatReminderTickerUiModel>? {
        itemList.forEachIndexed { index, item ->
            if (item is TokoChatReminderTickerUiModel && item.tag == tag) {
                return Pair(index, item)
            }
        }
        return null
    }
}
