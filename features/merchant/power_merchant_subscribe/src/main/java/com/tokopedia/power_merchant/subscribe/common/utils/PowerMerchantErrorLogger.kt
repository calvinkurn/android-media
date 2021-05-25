package com.tokopedia.power_merchant.subscribe.common.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 27/03/21
 */

object PowerMerchantErrorLogger {

    const val PM_STATUS_AND_SHOP_INFO_ERROR = "PM status and shop info error"
    const val REGISTRATION_PAGE_ERROR = "PM registration page error"
    const val PM_ACTIVE_IDLE_PAGE_ERROR = "PM active-idle page error"
    const val POWER_MERCHANT_STATUS_ERROR = "Power Merchant status error"
    const val SETTING_AND_SHOP_INFO_ERROR = "Setting and Shop Info error"

    fun logToCrashlytic(message: String, cause: Throwable?) {
        if (!GlobalConfig.isAllowDebuggingTools()) {
            val errorMessage = "$message - ${cause?.localizedMessage.orEmpty()}"
            FirebaseCrashlytics.getInstance().recordException(PowerMerchantException(
                    message = errorMessage,
                    cause = cause
            ))
        }
    }
}

class PowerMerchantException(message: String?, cause: Throwable?) : RuntimeException(message, cause)