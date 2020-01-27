package com.tokopedia.notifcenter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.presentation.adapter.typefactory.update.NotificationUpdateTypeFactoryImpl

/**
 * @author : Steven 11/04/19
 */

open class NotificationUpdateAdapter(
        notificationUpdateTypeFactory: NotificationUpdateTypeFactoryImpl
) : BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(notificationUpdateTypeFactory){

    override fun addElement(visitables: List<Visitable<*>>) {
        addMoreData(visitables)
    }

    fun markAllAsRead() {
        for (data in visitables) {
            if(data is NotificationItemViewBean) {
                data.isRead = true
            }
            notifyItemChanged(visitables.indexOf(data))
        }
    }

    fun removeEmptyState() {
        visitables.removeAll { it is NotificationEmptyStateViewBean }
        notifyDataSetChanged()
    }

}