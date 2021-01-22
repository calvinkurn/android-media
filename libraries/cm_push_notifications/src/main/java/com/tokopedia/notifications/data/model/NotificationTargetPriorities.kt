package com.tokopedia.notifications.data.model

import com.tokopedia.notifications.utils.NotificationValidationManager.NotificationPriorityType

data class NotificationTargetPriorities(
        var priorityType: NotificationPriorityType,
        var isAdvanceTarget: Boolean
)