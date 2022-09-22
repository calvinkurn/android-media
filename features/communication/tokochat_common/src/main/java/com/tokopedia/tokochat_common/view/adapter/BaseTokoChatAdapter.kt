package com.tokopedia.tokochat_common.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat_common.view.adapter.delegate.TokoChatMessageBubbleDelegate

class BaseTokoChatAdapter: BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatMessageBubbleDelegate())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}
