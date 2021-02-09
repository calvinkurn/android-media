package com.tokopedia.notification.common.utils

import android.content.Context
import android.os.Bundle
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.isAppInstalled
import com.tokopedia.notification.common.data.UserKey
import com.tokopedia.user.session.UserSession

data class NotificationTargetPriorities(
        var priorityType: NotificationValidationManager.NotificationPriorityType,
        var isAdvanceTarget: Boolean
)

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
    * @param(cancel): a callback for set the cancel tracking
    * */
    fun validate(bundle: Bundle?, notify: () -> Unit, cancel: () -> Unit) {
        if (data.priorityType is NotificationPriorityType.Both) {
            notify()
            return
        }

        if (data.isAdvanceTarget) {
            notify()
        } else {
            if (!GlobalConfig.isSellerApp()) {
                val isSellerAppInstalled = context.isAppInstalled(SELLER_APP)
                val isSellerAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false
                val sellerAppUserId = bundle?.getString(UserKey.USER_ID)?: ""

                if (!isSellerAppInstalled || !isSellerAppLogged
                        || userSession.userId != sellerAppUserId
                        || data.priorityType is NotificationPriorityType.MainApp) {
                    notify()
                } else {
                    cancel()
                }
            } else {
                val isMainAppInstalled = context.isAppInstalled(CUSTOMER_APP)
                val isMainAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false
                val mainAppUserId = bundle?.getString(UserKey.USER_ID)?: ""

                if (!isMainAppInstalled || !isMainAppLogged
                        || userSession.userId != mainAppUserId
                        || data.priorityType is NotificationPriorityType.SellerApp) {
                    notify()
                } else {
                    cancel()
                }
            }
        }
    }

    fun validate(bundle: Bundle?, notify: ValidationCallback) {
        validate(bundle, {
            notify.isRenderable()
        }, {} /* no-op, notifier haven't cancel tracker */)
    }

    interface ValidationCallback {
        fun isRenderable()
    }

    sealed class NotificationPriorityType {
        object SellerApp: NotificationPriorityType()
        object MainApp: NotificationPriorityType()
        object Both: NotificationPriorityType()
    }

    companion object {
        const val CUSTOMER_APP = "com.tokopedia.tkpd"
        const val SELLER_APP = "com.tokopedia.sellerapp"
    }

}