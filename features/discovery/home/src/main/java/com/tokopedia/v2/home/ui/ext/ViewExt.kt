package com.tokopedia.v2.home.ui.ext

import android.view.View
import com.tokopedia.v2.home.util.SafeClickListener

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}