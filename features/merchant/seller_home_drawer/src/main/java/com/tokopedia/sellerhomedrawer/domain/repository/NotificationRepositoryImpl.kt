package com.tokopedia.sellerhomedrawer.domain.repository

import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel
import com.tokopedia.sellerhomedrawer.domain.factory.NotificationSourceFactory
import com.tokopedia.sellerhomedrawer.domain.usecase.NotificationUseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(val notificationSourceFactory: NotificationSourceFactory): NotificationRepository {

    override fun getNotification(params: HashMap<String, Any>?): Observable<NotificationModel> {
        val tkpdMapParam = TKPDMapParam<String, Any>()
        if (params != null && params.isNotEmpty()) {
            tkpdMapParam.put(NotificationUseCase.PARAM_TYPE, params[NotificationUseCase.PARAM_TYPE])
        }
        return notificationSourceFactory.cloudNotificationSource.getNotification(tkpdMapParam)
    }

}