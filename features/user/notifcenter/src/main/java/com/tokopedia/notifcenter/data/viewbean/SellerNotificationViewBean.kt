package com.tokopedia.notifcenter.data.viewbean

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PESANAN_BARU
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SAMPAI_TUJUAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SEDANG_DIKIRIM
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SIAP_DIKIRIM
import com.tokopedia.notifcenter.data.model.DrawerNotification
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactory

class SellerNotificationViewBean: DrawerNotification(), Visitable<NotificationTransactionFactory> {

    override fun type(typeFactory: NotificationTransactionFactory): Int {
        return typeFactory.type(this)
    }

    fun sellerItem(): DrawerNotification {
        val parentNotification = DrawerNotification()
        val childNotification = mutableListOf<ChildDrawerNotification>()

        this.childs.forEach {
            when (it.id) {
                PESANAN_BARU -> childNotification.add(it)
                SIAP_DIKIRIM -> childNotification.add(it)
                SEDANG_DIKIRIM -> childNotification.add(it)
                SAMPAI_TUJUAN -> childNotification.add(it)
            }
        }

        parentNotification.id = this.id
        parentNotification.title = this.title
        parentNotification.childs = childNotification

        return parentNotification
    }

}