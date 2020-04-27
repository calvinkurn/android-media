package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.NotifCenterProductRecomAdapter
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseNotificationItemViewHolder
import com.tokopedia.notifcenter.widget.ProductRecomNotificationItemDecoration

class ProductRecomNotificationViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseNotificationItemViewHolder(itemView, listener) {

    private val rvRecommendation = itemView.findViewById<RecyclerView>(R.id.rv_recommendation)
    private val decoration = ProductRecomNotificationItemDecoration()
    private val layoutAdapter = NotifCenterProductRecomAdapter(listener)

    init {
        with(rvRecommendation) {
            layoutManager = GridLayoutManager(
                    itemView.context,
                    SPAN_COUNT,
                    GridLayoutManager.VERTICAL,
                    false)
            adapter = layoutAdapter
            addItemDecoration(decoration)
        }
    }

    override fun bindNotificationPayload(element: NotificationItemViewBean) {
        layoutAdapter.updateProductRecommendation(element.products)
        layoutAdapter.updateTotalProductCount(element.totalProduct)
        layoutAdapter.updateDataNotification(element.dataNotification)

        val seenProducts = layoutAdapter.getSeenProductRecommendation()
        listener.getAnalytic().trackImpressionProductRecommendation(seenProducts)
    }

    override fun bindOnNotificationClick(element: NotificationItemViewBean) {
        itemView.setOnClickListener {
            val context = it.context
            listener.itemClicked(element, adapterPosition)
            element.isRead = true
            RouteManager.route(context, element.dataNotification.appLink)
        }
    }

    companion object {
        private const val SPAN_COUNT = 4
        @LayoutRes val LAYOUT = R.layout.item_notification_update_product_recommendation
    }

}