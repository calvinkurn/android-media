package com.tokopedia.notifcenter.presentation.adapter.viewholder.notificationupdate

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.presentation.adapter.NotifcenterProductRecommendationAdapter
import com.tokopedia.notifcenter.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.notifcenter.widget.ProductRecomNotificationItemDecoration

class ProductRecomNotificationViewHolder(itemView: View, listener: NotificationUpdateItemListener) : NotificationUpdateItemViewHolder(itemView, listener) {

    private val rvRecommendation = itemView.findViewById<RecyclerView>(R.id.rv_recommendation)
    private val decoration = ProductRecomNotificationItemDecoration()
    private val layoutAdapter = NotifcenterProductRecommendationAdapter(listener)
    private val SPAN_COUNT = 4

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

    override fun bindNotificationPayload(element: NotificationUpdateItemViewModel) {
        layoutAdapter.updateProductRecommendation(element.products)
        layoutAdapter.updateTotalProductCount(element.totalProduct)

        val seenProducts = layoutAdapter.getSeenProductRecommendation()
        listener.getAnalytic().trackImpressionProductRecommendation(seenProducts)
    }

    override fun bindOnNotificationClick(element: NotificationUpdateItemViewModel) {
        itemView.setOnClickListener {
            val context = it.context
            listener.itemClicked(element, adapterPosition)
            element.isRead = true
            RouteManager.route(
                    context,
                    ApplinkConst.RECOMMENDATION_PAGE,
                    getLastShowedProductId(element),
                    NotificationUpdateItemViewModel.SOURCE
            )
        }
    }

    private fun getLastShowedProductId(element: NotificationUpdateItemViewModel): String {
        val products = element.products
        val lastItemPosition = products.size - 1
        val lastMaxSeenPosition = NotifcenterProductRecommendationAdapter.MAX_ITEM - 1
        val lastPosition = if (products.size >= NotifcenterProductRecommendationAdapter.MAX_ITEM) {
            lastMaxSeenPosition
        } else {
            lastItemPosition
        }

        return if (lastPosition >= 0) {
            element.products[lastPosition].productId
        } else {
            "0"
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_product_recommendation
    }

}