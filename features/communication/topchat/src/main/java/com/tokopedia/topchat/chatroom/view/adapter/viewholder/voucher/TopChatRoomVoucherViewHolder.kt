package com.tokopedia.topchat.chatroom.view.adapter.viewholder.voucher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomVoucherItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class TopChatRoomVoucherItemViewHolder(
    itemView: View,
    private val listener: TopChatRoomVoucherListener
) : RecyclerView.ViewHolder(itemView) {

    private val binding: TopchatChatroomVoucherItemBinding? by viewBinding()
    private var uiModel: TopChatRoomVoucherUiModel? = null
    private var broadcastUiModel: TopChatRoomBroadcastUiModel? = null

    init {
        setListener()
    }

    fun bind(
        uiModel: TopChatRoomVoucherUiModel,
        broadcastUiModel: TopChatRoomBroadcastUiModel
    ) {
        this.uiModel = uiModel
        this.broadcastUiModel = broadcastUiModel
        uiModel.voucherUi?.let {
            binding?.topchatVoucher?.bind(
                promo = it,
                isFullWidth = false
            )
        }
        impressTracker(uiModel)
    }

    private fun impressTracker(uiModel: TopChatRoomVoucherUiModel) {
        val broadcastUiModel = this.broadcastUiModel
        if (broadcastUiModel != null) {
            val bannerUiModel = broadcastUiModel.banner
            if (bannerUiModel != null && !bannerUiModel.isLoading) {
                binding?.root?.addOnImpressionListener(uiModel.impressHolder) {
                    listener.onImpressionBroadcastVoucher(
                        broadcastUiModel,
                        uiModel,
                        layoutPosition,
                        getTotalItems()
                    )
                }
            }
        }
    }

    private fun setListener() {
        binding?.root?.setOnClickListener {
            if (broadcastUiModel != null && uiModel != null) {
                listener.onClickBroadcastVoucher(
                    broadcastUiModel!!,
                    uiModel!!,
                    layoutPosition,
                    getTotalItems()
                )
            }
        }
    }

    private fun getTotalItems(): Int {
        return (itemView.parent as? RecyclerView)?.adapter?.itemCount.orZero()
    }

    fun onViewRecycled() {
        binding?.topchatVoucher?.cleanUp()
    }
}
