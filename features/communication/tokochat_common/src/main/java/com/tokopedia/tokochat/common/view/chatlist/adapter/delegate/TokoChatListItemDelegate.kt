package com.tokopedia.tokochat.common.view.chatlist.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.viewholder.TokoChatListItemViewHolder
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel

class TokoChatListItemDelegate(
    private val listener: TokoChatListItemListener
):
    TypedAdapterDelegate<TokoChatListItemUiModel, Any, TokoChatListItemViewHolder>(
        TokoChatListItemViewHolder.LAYOUT
) {
    override fun onBindViewHolder(item: TokoChatListItemUiModel, holder: TokoChatListItemViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TokoChatListItemViewHolder {
        return TokoChatListItemViewHolder(basicView, listener)
    }
}
