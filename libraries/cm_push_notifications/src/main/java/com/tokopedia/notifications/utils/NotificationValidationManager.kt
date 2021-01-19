package com.tokopedia.notifications.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
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
        val data = mockValidationByMessage.toUpperCase().trim()
        return when {
            data.contains("seller") -> NotificationPriorityType.SellerApp
            data.contains("customer") -> NotificationPriorityType.MainApp
            else -> NotificationPriorityType.Both
        }
    }

    fun validate(bundle: Bundle?, notify: () -> Unit) {
        val appName = if (GlobalConfig.isSellerApp()) "sellerapp" else "mainapp"
        when (validationByMessage()) {
            is NotificationPriorityType.SellerApp -> { // sellerAppPriority > mainAppPriority
                if (GlobalConfig.isSellerApp()) {
                    Log.d("AIDL_App ($appName)", "validate: seller notify")
                    notify()
                }
            }
            is NotificationPriorityType.MainApp -> { // sellerAppPriority < mainAppPriority
                val isSellerAppInstalled = context.isInstalled(SELLER_APP)
                val isSellerAppLogged = bundle?.getBoolean(UserKey.IS_LOGIN)?: false
                val sellerAppUserId = bundle?.getString(UserKey.USER_ID)?: ""
                Log.d("AIDL_App ($appName)", """
                        PRE_VALIDATION:
                        validate: customer notify\n
                        isSellerAppInstalled: $isSellerAppInstalled\n
                        isSellerAppLogged: $isSellerAppLogged\n
                        sellerAppUserId: $sellerAppUserId\n
                    """.trimIndent())

                if (GlobalConfig.isSellerApp()) return
                if (!isSellerAppInstalled && !isSellerAppLogged && userSession.userId == sellerAppUserId) {
                    Log.d("AIDL_App ($appName)", """
                        PASSED:
                        validate: customer notify\n
                        isSellerAppInstalled: $isSellerAppInstalled\n
                        isSellerAppLogged: $isSellerAppLogged\n
                        sellerAppUserId: $sellerAppUserId\n
                    """.trimIndent())
                    notify()
                }
            }
            is NotificationPriorityType.Both -> {
                Log.d("AIDL_App ($appName)", "validate: notify both apps")
                notify()
            }
        }
    }

    sealed class NotificationPriorityType {
        object SellerApp: NotificationPriorityType()
        object MainApp: NotificationPriorityType()
        object Both: NotificationPriorityType()
    }

}