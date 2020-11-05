package com.tokopedia.notifcenter.listener.v3

import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel

/**
 * used to interact between [androidx.recyclerview.widget.RecyclerView.ViewHolder] and activity
 */
interface NotificationItemListener {
    fun showLongerContent(element: NotificationUiModel)
    fun showProductBottomSheet(element: NotificationUiModel)
}