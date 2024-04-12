package com.tokopedia.topchat.chatroom.view.adapter.viewholder.voucher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomVoucherItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class TopChatRoomVoucherItemViewHolder(
    itemView: View,
    private val listener: TopChatRoomVoucherListener
) : RecyclerView.ViewHolder(itemView) {

    private val binding: TopchatChatroomVoucherItemBinding? by viewBinding()
    private var uiModel: TopChatRoomVoucherUiModel? = null

    init {
        binding?.root?.setOnClickListener {
            uiModel?.let {
                listener.onClickVoucher(it, "") //TODO: Source
            }
        }
    }

    fun bind(uiModel: TopChatRoomVoucherUiModel) {
        this.uiModel = uiModel
        uiModel.voucherUi.let {
            binding?.topchatVoucher?.bind(
                promo = it,
                isFullWidth = false
            )
        }
        impressTracker(uiModel)
    }

    private fun impressTracker(uiModel: TopChatRoomVoucherUiModel) {
        binding?.root?.addOnImpressionListener(uiModel.impressHolder) {
            listener.onImpressionVoucher(uiModel, "") //TODO: Source
        }
    }

    fun onViewRecycled() {
        binding?.topchatVoucher?.cleanUp()
    }
}
