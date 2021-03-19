package com.tokopedia.utils.view.binding.internal

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment

@Suppress("NOTHING_TO_INLINE")
inline fun <V : View> View.requireViewByIdCompat(@IdRes id: Int): V {
    return ViewCompat.requireViewById(this, id)
}

@Suppress("NOTHING_TO_INLINE")
inline fun <V : View> Activity.requireViewByIdCompat(@IdRes id: Int): V {
    return ActivityCompat.requireViewById(this, id)
}

fun findRootView(activity: Activity): View {
    val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
    checkNotNull(contentView)
    return when (contentView.childCount) {
        1 -> contentView.getChildAt(0)
        0 -> error("TkpdViewBinding: Please provide the root view explicitly")
        else -> error("TkpdViewBinding: More than one child view found in the content view")
    }
}

fun DialogFragment.getRootView(viewBindingRootId: Int): View {
    val dialog = checkNotNull(dialog)
    val window = checkNotNull(dialog.window)
    return with(window.decorView) {
        if (viewBindingRootId != 0) {
            requireViewByIdCompat(viewBindingRootId)
        } else {
            this
        }
    }
}