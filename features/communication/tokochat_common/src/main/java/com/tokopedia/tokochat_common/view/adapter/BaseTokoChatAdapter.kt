package com.tokopedia.tokochat_common.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatMessageBubbleDelegate
import com.tokopedia.tokochat_common.view.adapter.delegate.experiment.StringDelegate

open class BaseTokoChatAdapter: BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatMessageBubbleDelegate())
        delegatesManager.addDelegate(StringDelegate())
    }
}
