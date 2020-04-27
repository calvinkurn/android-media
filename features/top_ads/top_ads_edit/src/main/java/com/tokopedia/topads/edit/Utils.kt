package com.tokopedia.topads.edit

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.NumberFormat
import java.util.*

object Utils {
    var locale = Locale("in", "ID")

    fun convertToCurrencyString(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value) + " kali")
    }


    fun dismissKeyboard(context: Context?, view: View?) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager != null && inputMethodManager.isAcceptingText)
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
