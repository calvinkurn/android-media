package com.tokopedia.notifications.utils

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.data.SELLER_APP
import com.tokopedia.appaidl.data.UserKey
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession

class NotificationValidationManager(
        private val context: Context,
        private val priorityStatus: NotificationPrioMock
) {

    private val userSession by lazy { UserSession(context) }

    fun validate(bundle: Bundle?, notify: () -> Unit) {
        when (priorityStatus) {
            is NotificationPrioMock.SellerApp -> { // sellerAppPriority > mainAppPriority
                if (GlobalConfig.isSellerApp()) {
                    notify()
                }
            }
            is NotificationPrioMock.MainApp -> { // sellerAppPriority < mainAppPriority
                val isSellerAppInstalled = context.isInstalled(SELLER_APP)
                val isSellerAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false
                val sellerAppUserId = bundle?.getString(UserKey.USER_ID)?: ""

                if (GlobalConfig.isSellerApp()) return
                if (!isSellerAppInstalled && !isSellerAppLogged && userSession.userId == sellerAppUserId) {
                    notify()
                }
            }
            is NotificationPrioMock.Both -> notify()
        }
    }

    sealed class NotificationPrioMock {
        object SellerApp: NotificationPrioMock()
        object MainApp: NotificationPrioMock()
        object Both: NotificationPrioMock()
    }

}