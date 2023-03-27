package com.tokopedia.tokochat_common.view.adapter.delegate

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat_common.view.adapter.viewholder.chat_history.TokoChatImageBubbleViewHolder
import com.tokopedia.tokochat_common.view.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel

class TokoChatImageBubbleDelegate(
    private val imageAttachmentListener: TokoChatImageAttachmentListener
) : TypedAdapterDelegate<TokoChatImageBubbleUiModel, Any, TokoChatImageBubbleViewHolder>(
    TokoChatImageBubbleViewHolder.LAYOUT
) {

    override fun onBindViewHolderWithPayloads(
        item: TokoChatImageBubbleUiModel,
        holder: TokoChatImageBubbleViewHolder,
        payloads: Bundle
    ) {
        when {
            (payloads.getBoolean(TokoChatImageBubbleViewHolder.PAYLOAD.PAYLOAD_SUCCESS.key)) -> {
                holder.bindWithSuccessPayload(item)
            }
            (payloads.getBoolean(TokoChatImageBubbleViewHolder.PAYLOAD.PAYLOAD_ERROR_LOAD.key)) -> {
                holder.bindWithErrorLoadPayload(item)
            }
            (payloads.getBoolean(TokoChatImageBubbleViewHolder.PAYLOAD.PAYLOAD_ERROR_UPLOAD.key)) -> {
                holder.bindWithErrorUploadPayload(item)
            }
        }
    }

    override fun onBindViewHolder(item: TokoChatImageBubbleUiModel, holder: TokoChatImageBubbleViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TokoChatImageBubbleViewHolder {
        return TokoChatImageBubbleViewHolder(basicView, imageAttachmentListener)
    }
}
