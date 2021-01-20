package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopChatVoucherViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getStrokeWidthSenderDimenRes
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel
import com.tokopedia.topchat.common.util.ViewUtil

/**
 * Created by Steven on 18/03/19.
 */
class TopChatVoucherViewHolder constructor(
        itemView: View,
        private var voucherListener: TopChatVoucherListener
) : BaseChatViewHolder<TopChatVoucherUiModel>(itemView) {

    private var merchantVoucherView: MerchantVoucherView? = itemView.findViewById(R.id.merchantVoucherView)
    private var voucherContainer: LinearLayout? = itemView.findViewById(R.id.topchat_voucher_container)

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
            merchantVoucherView,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            getStrokeWidthSenderDimenRes()
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            merchantVoucherView,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            getStrokeWidthSenderDimenRes()
    )

    override fun bind(element: TopChatVoucherUiModel) {
        super.bind(element)
        TopChatVoucherViewHolderBinder.bindVoucherView(element, merchantVoucherView, voucherListener)
        TopChatVoucherViewHolderBinder.bindClick(element, merchantVoucherView, voucherListener)
        bindChatBubbleAlignment(element)
        bindBackground(element)
    }

    private fun bindBackground(element: TopChatVoucherUiModel) {
        if (element.isSender) {
            merchantVoucherView?.background = bgSender
        } else {
            merchantVoucherView?.background = bgOpposite
        }
    }

    private fun bindChatBubbleAlignment(element: TopChatVoucherUiModel) {
        if (element.isSender) {
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

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_topchat
    }
}
