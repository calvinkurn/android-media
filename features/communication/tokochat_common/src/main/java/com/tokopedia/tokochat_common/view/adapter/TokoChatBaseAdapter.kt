package com.tokopedia.tokochat_common.view.adapter

import android.os.Bundle
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
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

    fun getImageAttachmentPositionWithId(id: String): Int? {
        var result: Int? = null
        for (i in Int.ZERO until itemList.size) {
            val item = itemList[i]
            if (item is TokoChatImageBubbleUiModel && item.imageId == id) {
                result = i
                break
            }
        }
        return result
    }

    fun createPayloads(payloadName: String, value: Boolean): Bundle {
        return Bundle().apply {
            putBoolean(payloadName, value)
        }
    }
}
