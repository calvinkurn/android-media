package com.tokopedia.appaidl.manager

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.data.SELLER_APP
import com.tokopedia.appaidl.data.UserKey
import com.tokopedia.appaidl.utils.isInstalled
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession

class NotificationValidationManager(
        private val context: Context,
        private val priorities: Map<String, String>
) {

    private val userSession by lazy { UserSession(context) }

    // a mapper to convert prioritiesMap into a sealed class of PriorityType
    private fun validationByPriorities(): NotificationPriorityType {
        val mainAppPriority = priorities[KEY_MAIN_PRIORITY]?.toInt()?: 0
        val sellerAppPriority = priorities[KEY_SELLER_PRIORITY]?.toInt()?: 0

        // render the CM push notification
        return when {
            // for mainApp
            mainAppPriority > sellerAppPriority -> NotificationPriorityType.MainApp

            // for sellerApp
            mainAppPriority < sellerAppPriority -> NotificationPriorityType.SellerApp

            // both of apps
            else -> NotificationPriorityType.Both
        }
    }

    /*
    * validate;
    * to showing the CM push notification based on app priorities
    * @param(bundle): comes from AIDL services, which is the data gathering from another apps
    * @param(notify): a callback for a valid type to rendering the notification
    * */
    fun validate(bundle: Bundle?, notify: () -> Unit) {
        when (validationByPriorities()) {
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

    sealed class NotificationPriorityType {
        object SellerApp: NotificationPriorityType()
        object MainApp: NotificationPriorityType()
        object Both: NotificationPriorityType()
    }

    companion object {
        private const val KEY_MAIN_PRIORITY = "seller_priority"
        private const val KEY_SELLER_PRIORITY = "seller_priority"

        fun setAppPriorities(
                mainPriority: String,
                sellerPriority: String
        ): Map<String, String> {
            return mutableMapOf<String, String>().also {
                it[KEY_MAIN_PRIORITY] = mainPriority
                it[KEY_SELLER_PRIORITY] = sellerPriority
            }
        }

        /*
        * converting from { "message": any() } to appPriorities based on contains character
        * TODO: please remove this after notification_priority has merged from BE
        * */
        fun mockPriorities(message: String): Map<String, String> {
            val mockPrioritiesMap = mutableMapOf<String, String>()
            val dataMessage = message.toLowerCase().trim()

            when {
                dataMessage.contains("seller") -> {
                    mockPrioritiesMap[KEY_SELLER_PRIORITY] = "2"
                    mockPrioritiesMap[KEY_MAIN_PRIORITY] = "1"
                }
                dataMessage.contains("customer") -> {
                    mockPrioritiesMap[KEY_SELLER_PRIORITY] = "1"
                    mockPrioritiesMap[KEY_MAIN_PRIORITY] = "2"
                }
                else -> {
                    mockPrioritiesMap[KEY_SELLER_PRIORITY] = "1"
                    mockPrioritiesMap[KEY_MAIN_PRIORITY] = "1"
                }
            }

            return mockPrioritiesMap
        }
    }

}