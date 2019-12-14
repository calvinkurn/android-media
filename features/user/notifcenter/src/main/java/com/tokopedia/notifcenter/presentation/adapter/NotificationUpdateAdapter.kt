package com.tokopedia.notifcenter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.notifcenter.domain.model.EmptyUpdateState
import com.tokopedia.notifcenter.presentation.adapter.typefactory.NotificationUpdateTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateItemViewModel

/**
 * @author : Steven 11/04/19
 */

open class NotificationUpdateAdapter(notificationUpdateTypeFactory: NotificationUpdateTypeFactoryImpl)
    :BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(notificationUpdateTypeFactory){

    override fun addElement(visitables: List<Visitable<*>>) {
        addMoreData(visitables)
    }

    fun markAllAsRead() {
        for (datum in visitables) {
            if(datum is NotificationUpdateItemViewModel) {
                datum.isRead = true
            }
            notifyItemChanged(visitables.indexOf(datum))
        }
    }

    fun removeEmptyState() {
        visitables.removeAll { it is EmptyUpdateState }
        notifyDataSetChanged()
    }

}