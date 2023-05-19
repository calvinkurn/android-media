package com.tokopedia.shopadmin.common.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.config.GlobalConfig

object ShopAdminErrorLogger {

    const val ADMIN_TYPE_ERROR = "Admin type error"
    const val ARTICLE_DETAIL_ERROR = "Article detail error"
    const val PERMISSION_LIST_ERROR = "Shop Penalty error"
    const val ADMIN_CONFIRMATION_REG_ERROR = "Admin Confirmation Reg error"
    const val SHOP_ADMIN_INFO_ERROR = "Shop Admin Info error"
    const val UPDATE_USER_PROFILE_ERROR = "Update User Profile error"
    const val VALIDATE_ADMIN_EMAIL_ERROR = "Update User Profile error"
    const val ERROR_GET_ADMIN_ACCESS_ROLE = "Error when get admin access role."

    fun logToCrashlytic(message: String, cause: Throwable?) {
        if (!GlobalConfig.isAllowDebuggingTools()) {
            val errorMessage = "$message - ${cause?.localizedMessage.orEmpty()}"
            FirebaseCrashlytics.getInstance().recordException(ShopAdminException(
                message = errorMessage,
                cause = cause
            ))
        }
    }
}

class ShopAdminException(message: String?, cause: Throwable?) : RuntimeException(message, cause)
