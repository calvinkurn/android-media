package com.tokopedia.epharmacy.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.usecase.BuildConfig

object EPharmacyUtils {

    fun logException(e: Exception) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(e)
        } else {
            e.printStackTrace()
        }
    }
}

enum class PrescriptionActionType(val type: String) {
    REDIRECT_PWA("REDIRECT_CONSULTATION_PWA"),
    REDIRECT_OPTION("SHOW_PRESCRIPTION_ATTACHMENT_OPTION"),
    REDIRECT_UPLOAD("REDIRECT_PRESCRIPTION_UPLOAD_PAGE"),
    REDIRECT_PRESCRIPTION("REDIRECT_CONSULTATION_PRESCRIPTION"),
    REDIRECT_NONE("NONE"),
}
