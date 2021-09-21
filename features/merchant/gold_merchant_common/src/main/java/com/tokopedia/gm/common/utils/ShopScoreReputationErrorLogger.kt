package com.tokopedia.gm.common.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.config.GlobalConfig

object ShopScoreReputationErrorLogger {

    const val SHOP_INFO_PM_SETTING_INFO_ERROR = "Shop Info and PM Setting Info error in %s"
    const val SHOP_INFO_SETTING_ERROR = "Shop info setting error"
    const val SHOP_PERFORMANCE_ERROR = "Shop Performance error"
    const val SHOP_PENALTY_ERROR = "Shop Penalty error"
    const val SHOP_PENALTY_DETAIL_NEXT_ERROR = "Shop penalty detail lazy load error"

    const val OLD_SHOP_SCORE_ERROR = "Old Shop Score error"
    const val SHOP_ACCOUNT = "Akun Toko"
    const val SHOP_PERFORMANCE = "Shop Performance"

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