package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.annotation.SuppressLint
import android.view.MotionEvent
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

    override fun bind(element: NotificationUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            PAYLOAD_BUMP_REMINDER -> bindPayloadReminder(element)
        }
    }

    private fun bindPayloadReminder(element: NotificationUiModel) {
        val product = element.product ?: return
        productContainer?.bindReminderState(product)
    }

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindProductData(element)
        bindItemTouch(element)
    }

    private fun bindProductData(element: NotificationUiModel) {
        val product = element.product
        productContainer?.bindProductData(element, product, listener, adapterPosition)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindItemTouch(element: NotificationUiModel) {
        productContainer?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    markAsReadIfUnread(element)
                }
            }
            false
        }
    }

    companion object {
        const val PAYLOAD_BUMP_REMINDER = "payload_bump_reminder"
        val LAYOUT = R.layout.item_notifcenter_single_product_notification
    }
}