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