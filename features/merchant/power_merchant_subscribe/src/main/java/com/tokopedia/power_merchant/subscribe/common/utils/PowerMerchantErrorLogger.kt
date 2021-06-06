package com.tokopedia.power_merchant.subscribe.common.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 27/03/21
 */

object PowerMerchantErrorLogger {

    const val PM_REGISTRATION_PAGE_ERROR = "PM registration page error"
    const val PM_ACTIVE_PAGE_ERROR = "PM active page error"
    const val PM_SHOP_MODERATION_STATUS_ERROR = "PM shop moderation status error"
    const val PM_BASIC_INFO_ERROR = "PM basic info error"
    const val PM_ACTIVATION_ERROR = "PM activation error"
    const val PM_CANCEL_DEACTIVATION_ERROR = "PM cancel activation error"
    const val PM_DEACTIVATION_ERROR = "PM deactivation error"
    const val PM_DEACTIVATION_QUESTIONNAIRE_ERROR = "PM deactivation questionnaire error"

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