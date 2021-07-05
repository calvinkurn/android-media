package com.tokopedia.pushnotif.util

import com.tokopedia.notification.common.utils.NotificationTargetPriorities
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel
import com.tokopedia.notification.common.utils.NotificationValidationManager.NotificationPriorityType as NotificationPriorityType

object AdvanceTargetUtil {

    @JvmStatic
    fun advanceTargetNotification(notificationModel: ApplinkNotificationModel): NotificationTargetPriorities {
        val mainAppPriority = notificationModel.mainAppPriority
        val sellerAppPriority = notificationModel.sellerAppPriority
        val isAdvanceTarget = notificationModel.isAdvanceTarget

        val appPriorities = when {
            mainAppPriority.toInt() < sellerAppPriority.toInt() -> NotificationPriorityType.MainApp
            mainAppPriority.toInt() > sellerAppPriority.toInt() -> NotificationPriorityType.SellerApp
            else -> NotificationPriorityType.Both
        }

        return NotificationTargetPriorities(appPriorities, isAdvanceTarget)
    }

}