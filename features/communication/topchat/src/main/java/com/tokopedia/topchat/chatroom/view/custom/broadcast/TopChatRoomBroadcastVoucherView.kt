package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastVoucherBinding

class TopChatRoomBroadcastVoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: TopchatChatroomBroadcastVoucherBinding

    private var listener: TopChatRoomVoucherListener? = null
    private var uiModel: TopChatRoomVoucherUiModel? = null
    private var broadcastUiModel: TopChatRoomBroadcastUiModel? = null

    init {
        binding = TopchatChatroomBroadcastVoucherBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun setListener(listener: TopChatRoomVoucherListener) {
        this.listener = listener
        setSingleVoucherListener()
    }

    fun bind(broadcastUiModel: TopChatRoomBroadcastUiModel) {
        this.broadcastUiModel = broadcastUiModel
        val voucherAttachment = broadcastUiModel.singleVoucher
        val voucherCarousel = broadcastUiModel.voucherCarousel
        if (voucherAttachment != null) {
            bindSingleVoucher(voucherAttachment)
        } else if (voucherCarousel != null) {
            bindVoucherCarousel(voucherCarousel)
        } else {
            binding.topchatBroadcastSingleVoucher.gone()
            binding.topchatBroadcastRvVoucher.gone()
        }
    }

    private fun bindSingleVoucher(uiModel: TopChatRoomVoucherUiModel) {
        this.uiModel = uiModel
        val promoSimpleItem = uiModel.voucherUi
        if (promoSimpleItem != null) {
            binding.topchatBroadcastSingleVoucher.visible()
            binding.topchatBroadcastSingleVoucher.bind(
                promo = uiModel.voucherUi,
                isFullWidth = true
            )
            binding.topchatBroadcastTvVoucherHeader.text = uiModel.header
            binding.topchatBroadcastTvVoucherHeader.showWithCondition(uiModel.header.isNotBlank())
            binding.topchatBroadcastTvDesc.text = uiModel.description
            binding.topchatBroadcastTvDesc.showWithCondition(uiModel.description.isNotBlank())
            impressSingleVoucher()
        } else {
            binding.topchatBroadcastSingleVoucher.gone()
        }
        binding.topchatBroadcastRvVoucher.gone()
    }

    private fun bindVoucherCarousel(
        uiModel: TopChatRoomVoucherCarouselUiModel
    ) {
        if (uiModel.vouchers.isNotEmpty()) {
            binding.topchatBroadcastTvVoucherHeader.text = uiModel.vouchers.first().header
            binding.topchatBroadcastTvVoucherHeader.showWithCondition(
                uiModel.vouchers.first().header.isNotBlank()
            )
            binding.topchatBroadcastTvDesc.text = uiModel.vouchers.first().description
            binding.topchatBroadcastTvDesc.showWithCondition(
                uiModel.vouchers.first().description.isNotBlank()
            )
            setCarouselVoucherListener()
            binding.topchatBroadcastRvVoucher.initData(uiModel.vouchers)
            binding.topchatBroadcastRvVoucher.show()
        } else {
            binding.topchatBroadcastRvVoucher.gone()
        }
        binding.topchatBroadcastSingleVoucher.gone()
    }

    private fun impressSingleVoucher() {
        val uiModel = this.uiModel
        val broadcastUiModel = this.broadcastUiModel
        if (uiModel != null && broadcastUiModel != null) {
            val bannerUiModel = broadcastUiModel.banner
            if (bannerUiModel != null && !bannerUiModel.isLoading) {
                binding.topchatBroadcastSingleVoucher.addOnImpressionListener(uiModel.impressHolder) {
                    listener?.onImpressionBroadcastVoucher(
                        broadcast = broadcastUiModel,
                        voucher = uiModel
                    )
                }
            }
        }
    }

    private fun setSingleVoucherListener() {
        binding.topchatBroadcastSingleVoucher.setOnClickListener {
            val uiModel = this.uiModel
            val broadcastUiModel = this.broadcastUiModel
            if (uiModel != null && broadcastUiModel != null) {
                listener?.onClickBroadcastVoucher(
                    broadcast = broadcastUiModel,
                    voucher = uiModel
                )
            }
        }
    }

    private fun setCarouselVoucherListener() {
        val broadcastUiModel = this.broadcastUiModel
        if (broadcastUiModel != null) {
            listener?.let {
                binding.topchatBroadcastRvVoucher.setVoucherListener(
                    it,
                    broadcastUiModel
                )
            }
        }
    }

    fun cleanUp() {
        binding.topchatBroadcastSingleVoucher.cleanUp()
    }
}
