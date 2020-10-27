package com.tokopedia.notifcenter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory

class NotificationAdapter(
        baseListAdapterTypeFactory: NotificationTypeFactory?
) : BaseListAdapter<Visitable<*>, NotificationTypeFactory>(
        baseListAdapterTypeFactory
) {

    fun shouldDrawDivider(position: Int): Boolean {
        return isNotificationItem(position) && !isNextItemDivider(position)
    }

    private fun isNotificationItem(position: Int): Boolean {
        val item = visitables.getOrNull(position) ?: return false
        return item is NotificationUiModel
    }

    private fun isNextItemDivider(position: Int): Boolean {
        val item = visitables.getOrNull(position + 1) ?: return false
        return item is BigDividerUiModel
    }

}