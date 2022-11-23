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

enum class PrescriptionSourceType(val type : String){
    MINI_CONSULT("MINI_CONSULT"),
    UPLOAD("UPLOAD"),
    MULTI("MULTI")
}
