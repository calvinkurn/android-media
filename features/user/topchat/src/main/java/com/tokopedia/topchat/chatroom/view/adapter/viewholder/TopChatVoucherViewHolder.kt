package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherViewModel

/**
 * Created by Steven on 18/03/19.
 */
class TopChatVoucherViewHolder(itemView: View, var voucherListener: TopChatVoucherListener)
    : BaseChatViewHolder<TopChatVoucherViewModel>(itemView), MerchantVoucherView.OnMerchantVoucherViewListener{

    private var chatStatus: ImageView = itemView.findViewById<ImageView>(R.id.chat_status)
    private var isOwner: Boolean = false

    override fun bind(viewModel: TopChatVoucherViewModel) {
        super.bind(viewModel)
        val element = viewModel.voucherModel
        val data = MerchantVoucherViewModel(element)
        isOwner = viewModel.isSender
        itemView.findViewById<MerchantVoucherView>(R.id.merchantVoucherView).onMerchantVoucherViewListener = this
        itemView.findViewById<MerchantVoucherView>(R.id.merchantVoucherView).setData(data)

        setupChatBubbleAlignment(isOwner, viewModel)
    }

    private fun setupChatBubbleAlignment(isSender: Boolean, element: TopChatVoucherViewModel){
        if(isSender){
            setChatRight(element)
        }else{
            setChatLeft()
        }
    }

    private fun setChatLeft() {
        itemView.findViewById<LinearLayout>(R.id.topchat_voucher_container).gravity = Gravity.START
        chatStatus.visibility = View.GONE
    }

    private fun setChatRight(element: TopChatVoucherViewModel) {
        itemView.findViewById<LinearLayout>(R.id.topchat_voucher_container).gravity = Gravity.END
        chatStatus.visibility = View.VISIBLE
        setReadStatus(element)
    }

    private fun setReadStatus(element: TopChatVoucherViewModel) {
        var imageResource: Int
        if (element.isShowTime) {
            chatStatus.visibility = View.VISIBLE
            when {
                element.isRead -> imageResource = com.tokopedia.chat_common.R.drawable.ic_chat_read
                else -> imageResource = com.tokopedia.chat_common.R.drawable.ic_chat_unread
            }

            if (element.isDummy) {
                imageResource = com.tokopedia.chat_common.R.drawable.ic_chat_pending
            }
            chatStatus.setImageResource(imageResource)
        } else {
            chatStatus.visibility = View.GONE
        }
    }

    override fun isOwner(): Boolean {
        return isOwner
    }
    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        voucherListener.onVoucherClicked(merchantVoucherViewModel.voucherCode)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_topchat
    }
}
