package com.tokopedia.notifcenter.data.viewbean

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.MENUNGGU_KONFIRMASI
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PESANAN_DIPROSES
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SAMPAI_TUJUAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SEDANG_DIKIRIM
import com.tokopedia.notifcenter.data.model.DrawerNotification
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactory

class BuyerNotificationViewBean: DrawerNotification(), Visitable<NotificationTransactionFactory> {

    override fun type(typeFactory: NotificationTransactionFactory): Int {
        return typeFactory.type(this)
    }

    fun buyerItem(): DrawerNotification {
        val parentNotification = DrawerNotification()
        val childNotification = mutableListOf<ChildDrawerNotification>()

        this.childs.forEach {
            when (it.id) {
                MENUNGGU_KONFIRMASI -> childNotification.add(it)
                PESANAN_DIPROSES -> childNotification.add(it)
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