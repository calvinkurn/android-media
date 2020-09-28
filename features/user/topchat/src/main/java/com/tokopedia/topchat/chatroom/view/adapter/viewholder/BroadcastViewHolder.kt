package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ImageAnnouncementViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopChatVoucherViewHolderBinder
import com.tokopedia.topchat.chatroom.view.customview.TopchatMerchantVoucherView
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel

class BroadcastViewHolder constructor(
        itemView: View?,
        private var voucherListener: TopChatVoucherListener
) : AbstractViewHolder<BroadCastUiModel>(itemView) {

    private val bannerView: ImageView? = itemView?.findViewById(R.id.iv_banner)
    private val voucherView: TopchatMerchantVoucherView? = itemView?.findViewById(R.id.broadcast_merchant_voucher)

    override fun bind(element: BroadCastUiModel) {
        bindBanner(element)
        bindVoucher(element)
    }

    private fun bindBanner(element: BroadCastUiModel) {
        val banner = element.banner ?: return
        ImageAnnouncementViewHolderBinder.bindBannerImage(banner, bannerView)
    }

    private fun bindVoucher(element: BroadCastUiModel) {
        val voucher = element.voucherUiModel
        if (voucher != null) {
            voucherView?.visible()
            TopChatVoucherViewHolderBinder.bindVoucherView(voucher, voucherView, voucherListener)
        } else {
            voucherView?.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_message_bubble
    }
}