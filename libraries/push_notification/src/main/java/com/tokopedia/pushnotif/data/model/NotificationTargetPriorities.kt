package com.tokopedia.pushnotif.data.model

import com.tokopedia.pushnotif.util.NotificationValidationManager

data class NotificationTargetPriorities(
        var priorityType: NotificationValidationManager.NotificationPriorityType,
        var isAdvanceTarget: Boolean
)