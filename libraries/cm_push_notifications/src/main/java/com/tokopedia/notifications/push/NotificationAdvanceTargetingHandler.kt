package com.tokopedia.notifications.push

import android.content.Context
import android.os.Bundle
import com.tokopedia.notification.common.utils.NotificationTargetPriorities
import com.tokopedia.notification.common.utils.NotificationValidationManager
import com.tokopedia.notifications.model.AdvanceTargetingData
import com.tokopedia.notifications.model.BaseNotificationModel

class NotificationAdvanceTargetingHandler {

    fun checkForValidityAndAdvanceTargeting(
        applicationContext: Context,
        aidlApiBundle : Bundle?,
        baseNotificationModel: BaseNotificationModel,
        advanceTargetingData: AdvanceTargetingData,
        onValid : (BaseNotificationModel) -> Unit,
        onCancel : (BaseNotificationModel) -> Unit
    ) {
        aidlApiBundle?.let { aidlBundle ->

            /*
            * getting the smart push notification data from payload such as:
            * mainAppPriority
            * sellerAppPriority
            * advanceTarget
            * */
            val targeting = advanceTargetNotification(advanceTargetingData)

            // the smart push notification validators
            NotificationValidationManager(applicationContext, targeting).validate(aidlBundle, {
                onValid(baseNotificationModel)
            }, {
                // set cancelled notification if isn't notified
                onCancel(baseNotificationModel)
                //PushController(applicationContext).cancelPushNotification(baseNotificationModel)
            })

        } ?: onValid(baseNotificationModel)
    }

    private fun advanceTargetNotification(data: AdvanceTargetingData): NotificationTargetPriorities {
        val appPriorities = when {
            data.getMainAppPriority() < data.getSellerAppPriority() ->
                NotificationValidationManager.NotificationPriorityType.MainApp
            data.getMainAppPriority() > data.getSellerAppPriority() ->
                NotificationValidationManager.NotificationPriorityType.SellerApp
            else -> NotificationValidationManager.NotificationPriorityType.Both
        }
        return NotificationTargetPriorities(appPriorities, data.isAdvanceTargeting)
    }
}