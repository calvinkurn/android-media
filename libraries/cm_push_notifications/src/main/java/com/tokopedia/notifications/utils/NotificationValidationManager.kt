package com.tokopedia.notifications.utils

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.data.SELLER_APP
import com.tokopedia.appaidl.data.UserKey
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession

class NotificationValidationManager(
        private val context: Context,
        private val mockValidationByMessage: String
) {

    private val userSession by lazy { UserSession(context) }

    //TODO: please remove this after notification_priority has merged from BE
    private fun validationByMessage(): NotificationPriorityType {
        return when (mockValidationByMessage.toLowerCase().trim()) {
            "seller" -> NotificationPriorityType.SellerApp
            "customer" -> NotificationPriorityType.MainApp
            else -> NotificationPriorityType.Both
        }
    }

    fun validate(bundle: Bundle?, notify: () -> Unit) {
        when (validationByMessage()) {
            is NotificationPriorityType.SellerApp -> { // sellerAppPriority > mainAppPriority
                if (GlobalConfig.isSellerApp()) {
                    notify()
                }
            }
            is NotificationPriorityType.MainApp -> { // sellerAppPriority < mainAppPriority
                val isSellerAppInstalled = context.isInstalled(SELLER_APP)
                val isSellerAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false
                val sellerAppUserId = bundle?.getString(UserKey.USER_ID)?: ""

                if (GlobalConfig.isSellerApp()) return
                if (!isSellerAppInstalled && !isSellerAppLogged && userSession.userId == sellerAppUserId) {
                    notify()
                }
            }
            is NotificationPriorityType.Both -> notify()
        }
    }

    sealed class NotificationPriorityType {
        object SellerApp: NotificationPriorityType()
        object MainApp: NotificationPriorityType()
        object Both: NotificationPriorityType()
    }

}