package com.tokopedia.kotlin.extensions.view

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Created By @ilhamsuaib on 17/04/20
 */

fun Fragment.getBooleanArgs(key: String, defValue: Boolean = false): Boolean {
    return arguments?.getBoolean(key, defValue) ?: defValue
}

fun Fragment.getIntArgs(key: String, defValue: Int = 0): Int {
    return arguments?.getInt(key, defValue) ?: defValue
}

fun Fragment.applyUnifyBackgroundColor() {
    activity?.run {
        val backgroundColor = ContextCompat.getColor(context ?: return, com.tokopedia.unifyprinciples.R.color.Unify_Background)
        window.decorView.setBackgroundColor(backgroundColor)
    }
}

fun Fragment.getStringArg(key: String, defValue: String = ""): Lazy<String> = lazy {
    arguments?.getString(key, defValue) ?: defValue
}

fun Fragment.getIntArg(key: String, defValue: Int = 0): Lazy<Int> = lazy {
    arguments?.getInt(key, defValue) ?: defValue
}

fun Fragment.getStringArrayListArg(
    key: String,
    defValue: List<String> = emptyList()
): Lazy<List<String>> = lazy {
    arguments?.getStringArrayList(key) ?: defValue
}

fun Fragment.getBooleanArg(key: String, defValue: Boolean = false): Lazy<Boolean> = lazy {
    arguments?.getBoolean(key) ?: defValue
}
