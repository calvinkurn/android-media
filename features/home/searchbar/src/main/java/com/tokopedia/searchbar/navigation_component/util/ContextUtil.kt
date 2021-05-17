package com.tokopedia.searchbar.navigation_component.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.getActivityFromContext(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.getActivityFromContext()
        else -> null
    }
}