package com.tokopedia.tokochat.common.view.chatlist.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.viewholder.TokoChatListEmptyItemViewHolder
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListEmptyUiModel

class TokoChatListEmptyItemDelegate :
    TypedAdapterDelegate<TokoChatListEmptyUiModel, Any, TokoChatListEmptyItemViewHolder>(
        TokoChatListEmptyItemViewHolder.LAYOUT
    )
{
    override fun onBindViewHolder(
        item: TokoChatListEmptyUiModel,
        holder: TokoChatListEmptyItemViewHolder
    ) {
        holder.bind()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TokoChatListEmptyItemViewHolder {
        return TokoChatListEmptyItemViewHolder(basicView)
    }
}
