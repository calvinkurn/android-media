package com.tokopedia.pushnotif.util

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.data.MAIN_APP
import com.tokopedia.appaidl.data.SELLER_APP
import com.tokopedia.appaidl.data.UserKey
import com.tokopedia.appaidl.utils.isInstalled
import com.tokopedia.config.GlobalConfig
import com.tokopedia.pushnotif.data.model.NotificationTargetPriorities
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
    fun validate(bundle: Bundle?, notify: ValidationCallback) {
        if (data.priorityType is NotificationPriorityType.Both) {
            notify.isRenderable()
            return
        }

        if (data.isAdvanceTarget) {
            notify.isRenderable()
        } else {
            if (!GlobalConfig.isSellerApp()) {
                val isSellerAppInstalled = context.isInstalled(SELLER_APP)
                val isSellerAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false
                val sellerAppUserId = bundle?.getString(UserKey.USER_ID)?: ""

                if (!isSellerAppInstalled || !isSellerAppLogged
                        || userSession.userId != sellerAppUserId
                        || data.priorityType is NotificationPriorityType.MainApp) {
                    notify.isRenderable()
                }
            } else {
                val isMainAppInstalled = context.isInstalled(MAIN_APP)
                val isMainAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false
                val mainAppUserId = bundle?.getString(UserKey.USER_ID)?: ""

                if (!isMainAppInstalled || !isMainAppLogged
                        || userSession.userId != mainAppUserId
                        || data.priorityType is NotificationPriorityType.SellerApp) {
                    notify.isRenderable()
                }
            }
        }
    }

    interface ValidationCallback {
        fun isRenderable()
    }

    sealed class NotificationPriorityType {
        object SellerApp: NotificationPriorityType()
        object MainApp: NotificationPriorityType()
        object Both: NotificationPriorityType()
    }

}