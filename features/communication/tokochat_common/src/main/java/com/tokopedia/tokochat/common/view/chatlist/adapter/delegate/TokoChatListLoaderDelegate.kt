package com.tokopedia.tokochat.common.view.chatlist.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.viewholder.TokoChatListLoaderViewHolder
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListLoaderUiModel

class TokoChatListLoaderDelegate:
    TypedAdapterDelegate<TokoChatListLoaderUiModel, Any, TokoChatListLoaderViewHolder>(
        TokoChatListLoaderViewHolder.LAYOUT
    )
{
    override fun onBindViewHolder(
        item: TokoChatListLoaderUiModel,
        holder: TokoChatListLoaderViewHolder
    ) {
        // No-op
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TokoChatListLoaderViewHolder {
        return TokoChatListLoaderViewHolder(basicView)
    }
}
