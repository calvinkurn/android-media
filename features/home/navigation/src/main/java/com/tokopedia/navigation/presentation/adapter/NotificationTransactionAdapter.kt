package com.tokopedia.navigation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.navigation.GlobalNavConstant.MENUNGGU_KONFIRMASI
import com.tokopedia.navigation.domain.model.DrawerNotification
import com.tokopedia.navigation.domain.model.PurchaseNotification
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactoryImpl

class NotificationTransactionAdapter(
        notificationFactory: NotificationTransactionFactoryImpl
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(notificationFactory) {

    fun updateValue() {
        for (item in data) {
            if (item is PurchaseNotification) {
                val childs = item.childs
                for (child in childs) {
                    if (child.id == MENUNGGU_KONFIRMASI) {
                        child.title = "HAHAHA"
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

}