package com.tokopedia.notifcenter.view.adapter.common

import androidx.recyclerview.widget.RecyclerView

/**
 * used to interact ViewHolder to [androidx.recyclerview.widget.RecyclerView.Adapter]
 */
interface NotificationAdapterListener {
    fun getProductCarouselViewPool(): RecyclerView.RecycledViewPool
    fun isPreviousItemNotification(adapterPosition: Int): Boolean
    fun getWidgetOrderTimelineViewPool(): RecyclerView.RecycledViewPool
    fun getWidgetFeedTimelineViewPool(): RecyclerView.RecycledViewPool
    fun getNotificationOrderFilterViewPool(): RecyclerView.RecycledViewPool?
}
