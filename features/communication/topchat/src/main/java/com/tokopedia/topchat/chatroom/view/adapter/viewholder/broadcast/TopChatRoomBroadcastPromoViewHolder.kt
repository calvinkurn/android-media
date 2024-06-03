package com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastBannerListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastPromoItemBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class TopChatRoomBroadcastPromoViewHolder(
    itemView: View,
    deferredAttachment: DeferredViewHolderAttachment,
    bannerListener: TopChatRoomBroadcastBannerListener,
    productListener: TopChatRoomBroadcastProductListener,
    voucherListener: TopChatRoomVoucherListener
) : AbstractViewHolder<TopChatRoomBroadcastUiModel>(itemView) {
    private val binding: TopchatChatroomBroadcastPromoItemBinding? by viewBinding()

    private val paddingOpposite: Int by lazy(LazyThreadSafetyMode.NONE) { 1.toPx() }
    private val paddingSender: Int by lazy(LazyThreadSafetyMode.NONE) { 3.toPx() }

    init {
        binding?.topchatChatroomBroadcastPromoBase?.setListener(bannerListener, deferredAttachment)
        binding?.topchatChatroomBroadcastPromoProduct?.setListener(productListener, deferredAttachment)
        binding?.topchatChatroomBroadcastPromoVoucher?.setListener(voucherListener)
        binding?.topchatChatroomBroadcastPromoDim?.alpha = 0.5f
    }

    override fun bind(uiModel: TopChatRoomBroadcastUiModel) {
        bindBackground(uiModel)
        bindBase(uiModel)
        bindPromoProduct(uiModel)
        bindVoucher(uiModel)
        bindStatus(uiModel)
    }

    override fun bind(uiModel: TopChatRoomBroadcastUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        if (payloads.first() == DeferredAttachment.PAYLOAD_DEFERRED) {
            bindBase(uiModel)
            bindPromoProduct(uiModel)
            bindVoucher(uiModel)
        }
    }

    private fun bindBackground(uiModel: TopChatRoomBroadcastUiModel) {
        if (uiModel.isOpposite) {
            binding?.root?.setPadding(
                paddingOpposite,
                paddingOpposite,
                paddingOpposite,
                paddingOpposite
            )
            binding?.root?.setBackgroundResource(
                R.drawable.topchat_chatroom_broadcast_background_receiver
            )
        } else {
            binding?.root?.setPadding(
                paddingSender,
                paddingSender,
                paddingSender,
                paddingSender
            )
            binding?.root?.setBackgroundResource(
                R.drawable.topchat_chatroom_broadcast_background_sender
            )
        }
    }

    private fun bindBase(uiModel: TopChatRoomBroadcastUiModel) {
        binding?.topchatChatroomBroadcastPromoBase?.bind(uiModel)
    }

    private fun bindPromoProduct(uiModel: TopChatRoomBroadcastUiModel) {
        binding?.topchatChatroomBroadcastPromoProduct?.bind(uiModel)
    }

    private fun bindVoucher(uiModel: TopChatRoomBroadcastUiModel) {
        val voucherAttachment = uiModel.singleVoucher
        val voucherCarousel = uiModel.voucherCarousel
        if (voucherAttachment != null || voucherCarousel != null) {
            binding?.topchatChatroomBroadcastPromoVoucher?.show()
            binding?.topchatChatroomBroadcastPromoVoucher?.bind(uiModel)
        } else {
            binding?.topchatChatroomBroadcastPromoVoucher?.hide()
        }
    }

    private fun bindStatus(uiModel: TopChatRoomBroadcastUiModel) {
        binding?.topchatChatroomBroadcastPromoStatus?.bindStatus(uiModel)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding?.topchatChatroomBroadcastPromoBase?.cleanUp()
        binding?.topchatChatroomBroadcastPromoProduct?.cleanUp()
        binding?.topchatChatroomBroadcastPromoVoucher?.cleanUp()
        binding?.topchatChatroomBroadcastPromoStatus?.cleanUp()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topchat_chatroom_broadcast_promo_item
    }
}
