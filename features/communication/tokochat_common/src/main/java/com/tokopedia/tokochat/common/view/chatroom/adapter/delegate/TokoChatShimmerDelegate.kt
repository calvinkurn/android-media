package com.tokopedia.tokochat.common.view.chatroom.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.state.TokoChatLoadingStateViewHolder
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatLoadingUiModel

class TokoChatShimmerDelegate: TypedAdapterDelegate<TokoChatLoadingUiModel, Any, TokoChatLoadingStateViewHolder>(
    TokoChatLoadingStateViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: TokoChatLoadingUiModel,
        holder: TokoChatLoadingStateViewHolder
    ) {
        //no op
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TokoChatLoadingStateViewHolder {
        return TokoChatLoadingStateViewHolder(basicView)
    }

}
