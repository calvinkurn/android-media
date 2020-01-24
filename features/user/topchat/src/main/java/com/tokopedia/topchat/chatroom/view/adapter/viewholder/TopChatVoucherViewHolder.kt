package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherViewModel
import kotlinx.android.synthetic.main.voucher_item_topchat.view.*

/**
 * Created by Steven on 18/03/19.
 */
class TopChatVoucherViewHolder(itemView: View, private var voucherListener: TopChatVoucherListener)
    : BaseChatViewHolder<TopChatVoucherViewModel>(itemView), MerchantVoucherView.OnMerchantVoucherViewListener {

    private var chatStatus: ImageView = itemView.findViewById(R.id.chat_status)
    private var isOwner: Boolean = false
    private lateinit var model: TopChatVoucherViewModel
    private var merchantVoucherView: MerchantVoucherView? = itemView.findViewById(R.id.merchantVoucherView)

    override fun bind(viewModel: TopChatVoucherViewModel) {
        super.bind(viewModel)
        model = viewModel
        val element = viewModel.voucherModel
        val data = MerchantVoucherViewModel(element)
        isOwner = viewModel.isSender

        bindVoucherView(viewModel, data)
        setupChatBubbleAlignment(isOwner, viewModel)

        itemView.setOnClickListener {
            voucherListener.onVoucherClicked(data)
        }
    }

    private fun bindVoucherView(viewModel: TopChatVoucherViewModel, data: MerchantVoucherViewModel) {
        merchantVoucherView?.onMerchantVoucherViewListener = this
        merchantVoucherView?.setData(data)
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    override fun getDateId(): Int {
        return R.id.tvDate
    }

    private fun setupChatBubbleAlignment(isSender: Boolean, element: TopChatVoucherViewModel) {
        if (isSender) {
            setChatRight(element)
            bindChatReadStatus(element)
        } else {
            setChatLeft()
        }
    }

    private fun setChatLeft() {
        itemView.findViewById<LinearLayout>(R.id.topchat_voucher_container).gravity = Gravity.START
        chatStatus.visibility = View.GONE
        itemView.llReadStatusContainer?.setBackgroundResource(R.drawable.bg_topchat_chat_left_voucher)
    }

    private fun setChatRight(element: TopChatVoucherViewModel) {
        itemView.findViewById<LinearLayout>(R.id.topchat_voucher_container).gravity = Gravity.END
        chatStatus.visibility = View.VISIBLE
        bindChatReadStatus(element)
        itemView.llReadStatusContainer?.setBackgroundResource(R.drawable.bg_topchat_chat_right_voucher)
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
