package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.widget.CardProductNotificationCardUnify

class SingleProductNotificationViewHolder constructor(
        itemView: View?,
        private val listener: NotificationItemListener?
) : BaseNotificationViewHolder(itemView, listener) {

    private val productContainer: CardProductNotificationCardUnify? = itemView?.findViewById(
            R.id.pc_single
    )

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindProductData(element)
        bindClickItem(element)
    }

    private fun bindProductData(element: NotificationUiModel) {
        val product = element.product
        productContainer?.bindProductData(product)
    }

    private fun bindClickItem(element: NotificationUiModel) {
        container?.setOnClickListener {
            listener?.showProductBottomSheet(element)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_single_product_notification
    }
}