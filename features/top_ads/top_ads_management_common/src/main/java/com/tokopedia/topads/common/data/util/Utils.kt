package com.tokopedia.topads.common.data.util

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.tokopedia.unifycomponents.SearchBarUnify
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

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

}