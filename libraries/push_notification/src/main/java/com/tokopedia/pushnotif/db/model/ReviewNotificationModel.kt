package com.tokopedia.pushnotif.db.model

import com.tokopedia.pushnotif.model.ApplinkNotificationModel

data class ReviewNotificationModel(
        val applinkNotificationModel: ApplinkNotificationModel = ApplinkNotificationModel(),
        val notificationType: Int = 0,
        var notificationId: Int = 0
)