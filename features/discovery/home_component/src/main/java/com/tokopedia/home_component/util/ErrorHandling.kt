package com.tokopedia.home_component.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Exception.recordCrashlytics() {
    FirebaseCrashlytics.getInstance().recordException(this)
}
