package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastVoucherBinding

class TopChatRoomBroadcastVoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: TopchatChatroomBroadcastVoucherBinding

    private var voucherListener: TopChatRoomVoucherListener? = null

    init {
        binding = TopchatChatroomBroadcastVoucherBinding.inflate(
            LayoutInflater.from(context), this, true)
    }

    fun setListener(listener: TopChatRoomVoucherListener) {
        this.voucherListener = listener
    }

    fun bind(uiModel: TopChatRoomVoucherUiModel) {
        // Set visibility
        binding.topchatBroadcastSingleVoucher.visible()
        binding.topchatBroadcastRvVoucher.gone()

        // Set data
        binding.topchatBroadcastTvVoucherHeader.text = uiModel.header
        binding.topchatBroadcastTvVoucherHeader.showWithCondition(uiModel.header.isNotBlank())
        binding.topchatBroadcastTvDesc.text = uiModel.description
        binding.topchatBroadcastTvDesc.showWithCondition(uiModel.description.isNotBlank())
        binding.topchatBroadcastSingleVoucher.bind(
            promo = uiModel.voucherUi,
            isFullWidth = true
        )
    }

    fun bind(uiModel: TopChatRoomVoucherCarouselUiModel) {
        // Set visibility
        binding.topchatBroadcastSingleVoucher.gone()
        binding.topchatBroadcastRvVoucher.showWithCondition(uiModel.vouchers.isNotEmpty())

        // Set data
        if (uiModel.vouchers.isNotEmpty()) {
            binding.topchatBroadcastTvVoucherHeader.text = uiModel.vouchers.first().header
            binding.topchatBroadcastTvVoucherHeader.showWithCondition(
                uiModel.vouchers.first().header.isNotBlank()
            )
            binding.topchatBroadcastTvDesc.text = uiModel.vouchers.first().description
            binding.topchatBroadcastTvDesc.showWithCondition(
                uiModel.vouchers.first().description.isNotBlank()
            )
            voucherListener?.let {
                binding.topchatBroadcastRvVoucher.setVoucherListener(it)
            }
            binding.topchatBroadcastRvVoucher.initData(uiModel.vouchers)
        }
    }

    fun cleanUp() {
        binding.topchatBroadcastSingleVoucher.cleanUp()
    }
}
