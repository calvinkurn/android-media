package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

/**
 * Created by Steven on 18/03/19.
 */
class TopChatVoucherViewHolder(
        itemView: View,
        private var voucherListener: TopChatVoucherListener
) : BaseChatViewHolder<TopChatVoucherUiModel>(itemView), MerchantVoucherView.OnMerchantVoucherViewListener {

    private var isOwner: Boolean = false
    private lateinit var model: TopChatVoucherUiModel
    private var merchantVoucherView: MerchantVoucherView? = itemView.findViewById(R.id.merchantVoucherView)

    override fun bind(viewModel: TopChatVoucherUiModel) {
        super.bind(viewModel)
        model = viewModel
        val element = viewModel.voucherModel
        val data = MerchantVoucherViewModel(element)
        isOwner = viewModel.isSender

        bindVoucherView(viewModel, data)
        setupChatBubbleAlignment(isOwner, viewModel)

        itemView.setOnClickListener {
            data.isPublic = !viewModel.hasCtaCopy()
            voucherListener.onVoucherClicked(data)
        }
    }

    private fun bindVoucherView(viewModel: TopChatVoucherUiModel, data: MerchantVoucherViewModel) {
        merchantVoucherView?.onMerchantVoucherViewListener = this
        merchantVoucherView?.setData(data, false)
    }

    private fun setupChatBubbleAlignment(isSender: Boolean, element: TopChatVoucherUiModel) {
        if (isSender) {
            setChatRight(element)
            bindChatReadStatus(element)
        } else {
            setChatLeft()
        }
    }

    private fun setChatLeft() {
        itemView.findViewById<LinearLayout>(R.id.topchat_voucher_container).gravity = Gravity.START
    }

    private fun setChatRight(element: TopChatVoucherUiModel) {
        itemView.findViewById<LinearLayout>(R.id.topchat_voucher_container).gravity = Gravity.END
    }

    override fun isOwner(): Boolean {
        return isOwner
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        voucherListener.onVoucherCopyClicked(merchantVoucherViewModel.voucherCode
                , model.messageId, model.replyId
                , model.blastId, model.attachmentId, model.replyTime, model.fromUid)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_topchat
    }
}
