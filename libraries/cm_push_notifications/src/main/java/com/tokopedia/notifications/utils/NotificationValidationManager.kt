package com.tokopedia.notifications.utils

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.data.SELLER_APP
import com.tokopedia.appaidl.data.UserKey
import com.tokopedia.appaidl.utils.isInstalled
import com.tokopedia.config.GlobalConfig
import com.tokopedia.notifications.data.model.NotificationTargetPriorities
import com.tokopedia.user.session.UserSession

class NotificationValidationManager(
        private val context: Context,
        private val data: NotificationTargetPriorities
) {

    private val userSession by lazy { UserSession(context) }

    /*
    * validate;
    * to showing the CM push notification based on app priorities
    * @param(bundle): comes from AIDL services, which is the data gathering from another apps
    * @param(notify): a callback for a valid type to rendering the notification
    * */
    fun validate(bundle: Bundle?, notify: () -> Unit) {
        if (data.isAdvanceTarget) {
            notify()
        } else {
            when (data.priorityType) {
                is NotificationPriorityType.SellerApp -> {
                    if (GlobalConfig.isSellerApp()) {
                        notify()
                    }
                }
                is NotificationPriorityType.MainApp -> {
                    if (GlobalConfig.isSellerApp()) return

                    val isSellerAppInstalled = context.isInstalled(SELLER_APP)
                    val isSellerAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false
                    val sellerAppUserId = bundle?.getString(UserKey.USER_ID)?: ""

                    if (isSellerAppInstalled && isSellerAppLogged && userSession.userId == sellerAppUserId) {
                        notify()
                    }
                }
                is NotificationPriorityType.Both -> notify()
            }
        }
    }

    sealed class NotificationPriorityType {
        object SellerApp: NotificationPriorityType()
        object MainApp: NotificationPriorityType()
        object Both: NotificationPriorityType()
    }

}