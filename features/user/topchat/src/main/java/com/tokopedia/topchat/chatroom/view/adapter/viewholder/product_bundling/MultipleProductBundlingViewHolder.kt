package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.view.View
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.databinding.ItemTopchatMultipleProductBundlingBinding
import com.tokopedia.utils.view.binding.viewBinding

class MultipleProductBundlingViewHolder (
    itemView: View?,
    private val listener: ProductBundlingListener,
    private val adapterListener: AdapterListener
) : BaseChatViewHolder<MultipleProductBundlingUiModel>(itemView) {

    private val binding: ItemTopchatMultipleProductBundlingBinding? by viewBinding()

    override fun bind(element: MultipleProductBundlingUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            Payload.REBIND -> bind(element)
            DeferredAttachment.PAYLOAD_DEFERRED -> bind(element)
        }
    }

    override fun bind(element: MultipleProductBundlingUiModel) {
        binding?.containerBundlingAttachment?.bindData(
            element, adapterPosition, listener, adapterListener)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_multiple_product_bundling
    }
}