package com.tokopedia.topchat.chatroom.view.adapter.viewholder.voucher

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopChatVoucherViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getStrokeWidthSenderDimenRes
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by Steven on 18/03/19.
 */
class TopChatOldVoucherViewHolder constructor(
        itemView: View,
        private var voucherListener: TopChatRoomVoucherListener
) : BaseChatViewHolder<TopChatRoomVoucherUiModel>(itemView) {

    private var merchantVoucherView: MerchantVoucherView? = itemView.findViewById(R.id.merchantVoucherView)
    private var voucherContainer: LinearLayout? = itemView.findViewById(R.id.topchat_voucher_container)

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
            merchantVoucherView,
            unifyprinciplesR.color.Unify_NN0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            ViewUtil.getShadowColorViewHolder(itemView.context),
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            unifyprinciplesR.color.Unify_NN0,
            getStrokeWidthSenderDimenRes()
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            merchantVoucherView,
            unifyprinciplesR.color.Unify_GN50,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_0,
            ViewUtil.getShadowColorViewHolder(itemView.context),
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            unifyprinciplesR.color.Unify_GN50,
            getStrokeWidthSenderDimenRes()
    )

    override fun bind(element: TopChatRoomVoucherUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            Payload.REBIND -> bind(element)
        }
    }

    override fun bind(element: TopChatRoomVoucherUiModel) {
        super.bind(element)
        TopChatVoucherViewHolderBinder.bindVoucherView(element, merchantVoucherView)
        TopChatVoucherViewHolderBinder.bindClick(
            element,
            merchantVoucherView,
            voucherListener,
            TopChatVoucherViewHolderBinder.SOURCE_MANUAL_ATTACHMENT
        )
        bindChatBubbleAlignment(element)
        bindBackground(element)
        voucherListener.onImpressionVoucher(element, TopChatVoucherViewHolderBinder.SOURCE_MANUAL_ATTACHMENT)
    }

    private fun bindBackground(element: TopChatRoomVoucherUiModel) {
        if (element.isSender) {
            merchantVoucherView?.background = bgSender
        } else {
            merchantVoucherView?.background = bgOpposite
        }
    }

    private fun bindChatBubbleAlignment(element: TopChatRoomVoucherUiModel) {
        if (element.isSender) {
            setChatRight(element)
        } else {
            setChatLeft()
        }
    }

    private fun setChatLeft() {
        voucherContainer?.gravity = Gravity.START
    }

    private fun setChatRight(element: TopChatRoomVoucherUiModel) {
        voucherContainer?.gravity = Gravity.END
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topchat_chatroom_old_voucher_item
    }
}
