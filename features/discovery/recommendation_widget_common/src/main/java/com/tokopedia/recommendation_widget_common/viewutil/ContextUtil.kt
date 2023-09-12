package com.tokopedia.recommendation_widget_common.viewutil

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.LifecycleOwner

fun Context.getActivityFromContext(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.getActivityFromContext()
        else -> null
    }
}

fun Context.asLifecycleOwner(): LifecycleOwner? {
    return when (this) {
        is LifecycleOwner -> this
        is ContextWrapper -> this.baseContext.asLifecycleOwner()
        else -> null
    }
}
