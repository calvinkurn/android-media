package com.tokopedia.navigation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateTypeFactoryImpl
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

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
}