package com.tokopedia.content.common.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

/**
 * Created By : Jonathan Darwin on August 25, 2022
 */

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun EditText.hideKeyboard() {
    showKeyboard(false)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.showKeyboard(isShow: Boolean) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (isShow) imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    else imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Map<String, Any>.forEachIndexed(callback: (idx: Int, entry: Map.Entry<String, Any>) -> Unit) {
    var counter = 0

    forEach {
        callback(counter++, it)
    }
}