package com.tokopedia.tokochat.common.view.chatlist.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.viewholder.TokoChatListItemShimmerViewHolder
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListShimmerUiModel

class TokoChatListShimmerDelegate:
    TypedAdapterDelegate<TokoChatListShimmerUiModel, Any, TokoChatListItemShimmerViewHolder>(
        TokoChatListItemShimmerViewHolder.LAYOUT
    )
{

    override fun onBindViewHolder(
        item: TokoChatListShimmerUiModel,
        holder: TokoChatListItemShimmerViewHolder
    ) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TokoChatListItemShimmerViewHolder {
        return TokoChatListItemShimmerViewHolder(basicView)
    }
}
