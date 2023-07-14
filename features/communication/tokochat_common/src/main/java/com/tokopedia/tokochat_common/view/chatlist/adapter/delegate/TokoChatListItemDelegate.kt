package com.tokopedia.tokochat_common.view.chatlist.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat_common.view.chatlist.adapter.viewholder.TokoChatListItemViewHolder
import com.tokopedia.tokochat_common.view.chatlist.listener.TokoChatListItemListener

class TokoChatListItemDelegate(
    private val listener: TokoChatListItemListener
):
    TypedAdapterDelegate<String, Any, TokoChatListItemViewHolder>(
        TokoChatListItemViewHolder.LAYOUT
) {
    override fun onBindViewHolder(item: String, holder: TokoChatListItemViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TokoChatListItemViewHolder {
        return TokoChatListItemViewHolder(basicView, listener)
    }
}
