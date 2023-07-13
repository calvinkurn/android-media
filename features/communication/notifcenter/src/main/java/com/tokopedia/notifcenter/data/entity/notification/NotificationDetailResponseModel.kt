package com.tokopedia.notifcenter.data.entity.notification

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory

class NotificationDetailResponseModel constructor(
    val items: List<Visitable<NotificationTypeFactory>>,
    val hasNext: Boolean
)

class NotificationDetailResponseWrapper(
    val result: com.tokopedia.usecase.coroutines.Result<NotificationDetailResponseModel>,
    val loadType: NotifcenterDetailUseCase.NotificationDetailLoadType,
    val lastKnownPair: Pair<Int, Visitable<NotificationTypeFactory>>?
)
