package com.tokopedia.tokochat_common.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatHeaderDateDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatImageBubbleDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatMessageBubbleDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatReminderTickerDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatShimmerDelegate
import com.tokopedia.tokochat_common.view.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat_common.view.listener.TokoChatMessageBubbleListener
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener

open class TokoChatBaseAdapter(
    reminderTickerListener: TokochatReminderTickerListener,
    imageAttachmentListener: TokoChatImageAttachmentListener,
    bubbleMessageBubbleListener: TokoChatMessageBubbleListener
): BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatShimmerDelegate())
        delegatesManager.addDelegate(TokoChatReminderTickerDelegate(reminderTickerListener))
        delegatesManager.addDelegate(TokoChatMessageBubbleDelegate(bubbleMessageBubbleListener))
        delegatesManager.addDelegate(TokoChatHeaderDateDelegate())
        delegatesManager.addDelegate(TokoChatImageBubbleDelegate(imageAttachmentListener))
    }
}
