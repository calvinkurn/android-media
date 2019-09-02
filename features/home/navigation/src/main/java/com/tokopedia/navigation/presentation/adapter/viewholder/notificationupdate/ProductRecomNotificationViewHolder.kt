package com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate

import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
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
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_product_recommendation
    }

}