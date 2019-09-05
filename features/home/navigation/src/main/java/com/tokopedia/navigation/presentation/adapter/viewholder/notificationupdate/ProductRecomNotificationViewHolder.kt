package com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate

import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.adapter.NotifcenterProductRecommendationAdapter
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.widget.ProductRecomNotificationItemDecoration

class ProductRecomNotificationViewHolder(itemView: View, listener: NotificationUpdateItemListener) : NotificationUpdateItemViewHolder(itemView, listener) {

    private val rvRecommendation = itemView.findViewById<RecyclerView>(R.id.rv_recommendation)
    private val decoration = ProductRecomNotificationItemDecoration()
    private val layoutAdapter = NotifcenterProductRecommendationAdapter()
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
    }

    override fun bindOnNotificationClick(element: NotificationUpdateItemViewModel) {
        itemView.setOnClickListener {
            RouteManager.route(
                    itemView.context,
                    ApplinkConstInternalMarketplace.HOME_RECOMMENDATION,
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
        } else { "0" }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_product_recommendation
    }

}