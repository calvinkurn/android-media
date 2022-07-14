package com.tokopedia.campaign.utils.extension

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Fragment.doOnDelayFinished(delay : Long, block: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(delay)
        block()
    }
}