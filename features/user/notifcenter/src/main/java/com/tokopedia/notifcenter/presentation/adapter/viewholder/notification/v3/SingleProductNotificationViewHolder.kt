package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.widget.ProductNotificationCardUnify

class SingleProductNotificationViewHolder constructor(
        itemView: View?,
        private val listener: NotificationItemListener?
) : BaseNotificationViewHolder(itemView, listener) {

    private val productContainer: ProductNotificationCardUnify? = itemView?.findViewById(
            R.id.pc_single
    )

    override fun isLongerContent(element: NotificationUiModel) = false

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindProductData(element)
    }

    private fun bindProductData(element: NotificationUiModel) {
        val product = element.product
        productContainer?.bindProductData(product, listener)
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_single_product_notification
    }
}