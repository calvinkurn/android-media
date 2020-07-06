package com.tokopedia.kotlin.extensions.view

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