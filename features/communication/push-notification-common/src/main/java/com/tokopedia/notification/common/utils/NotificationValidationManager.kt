package com.tokopedia.notification.common.utils

import android.content.Context
import android.os.Bundle
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.isAppInstalled
import com.tokopedia.notification.common.data.UserKey
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.util.EncoderDecoder

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
            val appUserId = decryptData(bundle?.getString(UserKey.USER_ID)?: "")
            if (!GlobalConfig.isSellerApp()) {
                val isSellerAppInstalled = context.isAppInstalled(SELLER_APP)
                val isSellerAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false

                if (!isSellerAppInstalled || !isSellerAppLogged
                        || userSession.userId != appUserId
                        || data.priorityType is NotificationPriorityType.MainApp) {
                    notify()
                } else {
                    cancel()
                }
            } else {
                val isMainAppInstalled = context.isAppInstalled(CUSTOMER_APP)
                val isMainAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false

                if (!isMainAppInstalled || !isMainAppLogged
                        || userSession.userId != appUserId
                        || data.priorityType is NotificationPriorityType.SellerApp) {
                    notify()
                } else {
                    cancel()
                }
            }
        }
    }

    private fun decryptData(data: String): String {
        return EncoderDecoder.Decrypt(data, UserKey.IV_KEY_PUSH_NOTIF)
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
