package com.tokopedia.loginregister.utils

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.OngoingStubbing

internal fun OngoingStubbing.respondWithOk() {
    respondWith(
        Instrumentation.ActivityResult(
            Activity.RESULT_OK,
            Intent()
        )
    )
}