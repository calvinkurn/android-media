package com.tokopedia.troubleshooter.notification.data.mapper

import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.data.entity.NotificationTroubleshoot

fun NotificationTroubleshoot.mapToSendTroubleshoot(): NotificationSendTroubleshoot {
    val result = this.notificationSendTroubleshoot
    return NotificationSendTroubleshoot(
        isSuccess = result.isSuccess,
        errorMessage = result.errorMessage
    )
}