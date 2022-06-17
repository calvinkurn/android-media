package com.tokopedia.shop.flashsale.common.extension

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Fragment.setFragmentToUnifyBgColor() {
    if (activity != null && context != null) {
        activity!!.window.decorView.setBackgroundColor(
            ContextCompat.getColor(
                context!!, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }
}

fun Fragment.doOnDelayFinished(delay : Long, block: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(delay)
        block()
    }
}