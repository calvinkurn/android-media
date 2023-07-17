package com.tokopedia.tokochat.common.view.chatroom.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.chat_history.TokoChatHeaderDateViewHolder
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatHeaderDateUiModel

class TokoChatHeaderDateDelegate :
    TypedAdapterDelegate<TokoChatHeaderDateUiModel, Any, TokoChatHeaderDateViewHolder>(
        TokoChatHeaderDateViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: TokoChatHeaderDateUiModel,
        holder: TokoChatHeaderDateViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TokoChatHeaderDateViewHolder {
        return TokoChatHeaderDateViewHolder(basicView)
    }
}
