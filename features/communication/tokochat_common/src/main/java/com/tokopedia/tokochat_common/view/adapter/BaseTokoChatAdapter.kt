package com.tokopedia.tokochat_common.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatMessageBubbleDelegate

open class BaseTokoChatAdapter: BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatMessageBubbleDelegate())
    }
}
