package com.tokopedia.talk.common.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object FirebaseLogger {

    fun logError(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}