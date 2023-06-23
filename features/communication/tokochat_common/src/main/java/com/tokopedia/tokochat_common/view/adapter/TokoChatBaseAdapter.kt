package com.tokopedia.tokochat_common.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatHeaderDateDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatImageBubbleDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatMessageBubbleDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatMessageCensorDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatReminderTickerDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatShimmerDelegate
import com.tokopedia.tokochat_common.view.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat_common.view.listener.TokoChatMessageBubbleListener
import com.tokopedia.tokochat_common.view.listener.TokoChatMessageCensorListener
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatReminderTickerUiModel

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
