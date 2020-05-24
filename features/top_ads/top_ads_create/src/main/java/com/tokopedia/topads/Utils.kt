package com.tokopedia.topads

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.NumberFormat
import java.util.*

/**
 * Author errysuprayogi on 15,November,2019
 */
object Utils {
    private val suffixes = TreeMap<Long, String>()
    var locale = Locale("in", "ID")


    init {
        suffixes[1_0L] = " puluh"
        suffixes[1_000L] = " ratus"
        suffixes[1_000_000L] = " juta"
    }

    fun format(value: Long): String {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == java.lang.Long.MIN_VALUE) return format(java.lang.Long.MIN_VALUE + 1)
        if (value < 0) return "-" + format(-value)
        if (value < 1000) return java.lang.Long.toString(value) //deal with easy case

        val e = suffixes.floorEntry(value)
        val divideBy = e.key
        val suffix = e.value

        val truncated = value / (divideBy!! / 10) //the number part of the output times 10
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
    }

        fun convertToCurrencyString(value: Long): String {
            return (NumberFormat.getNumberInstance(locale).format(value)+" kali")
        }

    fun dismissKeyboard(context: Context?, view: View?) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager?.isAcceptingText!!) {
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }
}
