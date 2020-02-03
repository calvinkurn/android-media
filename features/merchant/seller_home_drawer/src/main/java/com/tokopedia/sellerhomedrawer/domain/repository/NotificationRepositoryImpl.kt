package com.tokopedia.sellerhomedrawer.domain.repository

import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel
import com.tokopedia.sellerhomedrawer.domain.factory.NotificationSourceFactory
import rx.Observable
import java.util.*
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(val notificationSourceFactory: NotificationSourceFactory): NotificationRepository {

    override fun getNotification(params: HashMap<String, Any>?): Observable<NotificationModel> {
        return notificationSourceFactory.cloudNotificationSource.getNotification(params as TKPDMapParam<String, Any>)
    }

}