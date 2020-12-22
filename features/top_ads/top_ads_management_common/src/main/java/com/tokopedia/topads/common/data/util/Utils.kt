package com.tokopedia.topads.common.data.util

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.numberFormatted
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.constant.Constants
import com.tokopedia.unifycomponents.SearchBarUnify
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.jvm.Throws

/**
 * Author errysuprayogi on 26,March,2019
 */
object Utils {

    var locale = Locale("in", "ID")
    const val KALI = " kali"

    @JvmStatic
    @Throws(JSONException::class)
    fun jsonToMap(json: Any?): Map<String, Any>? {
        if (json is JSONObject) return _jsonToMap_(json) else if (json is String) {
            val jsonObject = JSONObject(json as String?)
            return _jsonToMap_(jsonObject)
        }
        return null
    }

    @Throws(JSONException::class)
    private fun _jsonToMap_(json: JSONObject): Map<String, Any> {
        var retMap: Map<String, Any> = HashMap()
        if (json !== JSONObject.NULL) {
            retMap = toMap(json)
        }
        return retMap
    }

    @Throws(JSONException::class)
    private fun toMap(`object`: JSONObject): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val keysItr = `object`.keys()
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            var value = `object`[key]
            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            map[key] = value
        }
        return map
    }

    @Throws(JSONException::class)
    fun toList(array: JSONArray): List<Any> {
        val list: MutableList<Any> = ArrayList()
        for (i in 0 until array.length()) {
            var value = array[i]
            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            list.add(value)
        }
        return list
    }

    private val suffixes = TreeMap<Long, String>()


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
    val decimalFormat = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale("in", "id")))

    fun Number.numberFormatted(): String {
        decimalFormat.maximumFractionDigits = 2
        return decimalFormat.format(this)
    }
    fun Number.thousandFormatted(): String {
        if (toDouble() < 1000) return numberFormatted()

        val exp = (Math.log(this.toDouble())/Math.log(1000.00)).toInt()
        val number = this.toDouble()/Math.pow(1000.00, exp.toDouble())
        return "${number.numberFormatted()}${listOf("rb", "jt", "mil", "T")[exp-1]}"
    }

    fun setSearchListener(searchbar: SearchBarUnify, context: Context?, view: View, onSuccess: () -> Unit) {
        val searchTextField = searchbar.searchBarTextField
        val searchClearButton = searchbar.searchBarIcon
        searchTextField.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(textView: TextView?, actionId: Int, even: KeyEvent?): Boolean {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSuccess.invoke()
                    dismissKeyboard(context, view)
                    return true
                }
                return false
            }
        })
        searchClearButton.setOnClickListener {
            searchTextField.text?.clear()
            onSuccess.invoke()
            dismissKeyboard(context, view)
        }
    }

    fun dismissKeyboard(context: Context?, view: View?) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager?.isAcceptingText == true)
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun convertToCurrencyString(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value) + KALI)
    }

    fun convertToCurrency(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value))
    }

    fun String.removeCommaRawString() = toString().replace(",", "").replace(".", "").replace("Rp", "").trim()

    fun validateKeyword(context: Context?, text: CharSequence?): CharSequence? {
        return if (!text.isNullOrBlank() && text.split(" ").size > Constants.KEYWORD_WORD_COUNT) {
            context?.getString(R.string.error_max_length_keyword)
        } else if (!text.isNullOrBlank() && !text.matches(Constants.KEYWORD_REGEX.toRegex())) {
            context?.getString(R.string.error_keyword)
        } else if (text?.length ?: 0 > 70) {
            context?.getString(R.string.error_max_length)
        } else {
            null
        }
    }

    fun validateKeywordCountAndChars(context: Context?, text: CharSequence?): CharSequence? {
        return if (text?.length ?: 0 > Constants.KEYWORD_CHARACTER_COUNT) {
            context?.getString(R.string.error_max_length)
        } else if (!text.isNullOrBlank() && !text.matches(Constants.KEYWORD_REGEX_WITH_SPECIAL_CHARS.toRegex())) {
            context?.getString(R.string.error_keyword)
        } else {
            null
        }
    }

}