package com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction

import com.tokopedia.notifcenter.data.viewbean.NotificationFilterSectionViewBean
import com.tokopedia.notifcenter.data.viewbean.BuyerNotificationViewBean
import com.tokopedia.notifcenter.data.viewbean.SellerNotificationViewBean
import com.tokopedia.notifcenter.presentation.adapter.typefactory.base.BaseNotificationTypeFactory

interface NotificationTransactionFactory : BaseNotificationTypeFactory {
    fun type(buyerNotification: BuyerNotificationViewBean): Int
    fun type(sellerNotification: SellerNotificationViewBean): Int
    fun type(filter: NotificationFilterSectionViewBean): Int
}