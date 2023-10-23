package com.tokopedia.shop.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ShopTimer {
    
    private var job: Job? = null
    
    fun startTimer(intervalMillis: Long, action: () -> Unit) {
        // Cancel the previous job if it exists
        job?.cancel()

        job = CoroutineScope(Dispatchers.Main).launch {
            try {
                while (isActive) {
                    delay(intervalMillis)
                    action()
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
    
    fun stopTimer() {
        job?.cancel()
    }
}
