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
import com.tokopedia.topchat.common.util.ViewUtil

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
    private var voucherContainer: LinearLayout? = itemView.findViewById(R.id.topchat_voucher_container)

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
            itemView,
            com.tokopedia.unifyprinciples.R.color.Neutral_N0,
            R.dimen.dp_0,
            R.dimen.dp_0,
            R.dimen.dp_0,
            R.dimen.dp_0,
            R.color.topchat_message_shadow,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            com.tokopedia.unifyprinciples.R.color.Neutral_N0,
            R.dimen.dp_topchat_1point5
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            itemView,
            R.color.bg_topchat_right_message,
            R.dimen.dp_0,
            R.dimen.dp_0,
            R.dimen.dp_0,
            R.dimen.dp_0,
            R.color.topchat_message_shadow,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            R.color.bg_topchat_right_message,
            R.dimen.dp_topchat_1point5
    )

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

        bindBackground(viewModel)
    }

    private fun bindBackground(viewModel: TopChatVoucherUiModel) {
        if (viewModel.isSender) {
            merchantVoucherView?.background = bgSender
        } else {
            merchantVoucherView?.background = bgOpposite
        }
    }

    private fun bindVoucherView(viewModel: TopChatVoucherUiModel, data: MerchantVoucherViewModel) {
        merchantVoucherView?.onMerchantVoucherViewListener = this
        merchantVoucherView?.setData(data, false)
    }

    private fun setupChatBubbleAlignment(isSender: Boolean, element: TopChatVoucherUiModel) {
        if (isSender) {
            setChatRight(element)
        } else {
            setChatLeft()
        }
    }

    private fun setChatLeft() {
        voucherContainer?.gravity = Gravity.START
    }

    private fun setChatRight(element: TopChatVoucherUiModel) {
        voucherContainer?.gravity = Gravity.END
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
