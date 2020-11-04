package com.tokopedia.notifcenter.presentation.adapter.common

import androidx.recyclerview.widget.RecyclerView

/**
 * used to interact between [androidx.recyclerview.widget.RecyclerView.Adapter] and activity
 */
interface NotificationAdapterListener {
    fun getProductCarouselViewPool(): RecyclerView.RecycledViewPool
}