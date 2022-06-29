package com.tokopedia.createpost.producttag.util.extension

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}