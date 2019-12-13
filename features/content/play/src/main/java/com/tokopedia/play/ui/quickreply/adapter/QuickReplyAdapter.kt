package com.tokopedia.play.ui.quickreply.adapter

import com.tokopedia.adapter_delegate.BaseDiffUtilAdapter
import com.tokopedia.play.data.QuickReply
import com.tokopedia.play.ui.quickreply.adapter.delegate.QuickReplyAdapterDelegate

/**
 * Created by jegul on 13/12/19
 */
class QuickReplyAdapter(
        onQuickReplyClicked: (String) -> Unit
) : BaseDiffUtilAdapter<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    init {
        delegatesManager
                .addDelegate(QuickReplyAdapterDelegate(onQuickReplyClicked))
    }

    fun setQuickReply(quickReply: QuickReply) {
        setItemsAndAnimateChanges(quickReply.data)
    }
}