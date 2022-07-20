package com.tokopedia.shop.flashsale.common.extension

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Fragment.setFragmentToUnifyBgColor() {
    if (activity != null && context != null) {
        activity!!.window.decorView.setBackgroundColor(
            ContextCompat.getColor(
                context!!, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }
}

fun Fragment.doOnDelayFinished(delay: Long, operation: () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        delay(delay)

        if (isAdded && context != null) {
            operation()
        }

    }
}