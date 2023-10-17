package com.tokopedia.topads.common.data.util

import android.content.Context
import android.content.res.Resources
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.constant.Constants
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BUDGET_MULTIPLE_FACTOR
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_0
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.text.currency.NumberTextWatcher
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import kotlin.jvm.Throws

/**
 * Author errysuprayogi on 26,March,2019
 */
object Utils {

    var locale = Locale("in", "ID")
    private const val KALI = " kali"

    fun Typography.attributedText(@StringRes id: Int) {
        text = Html.fromHtml(this.context.resources.getString(id))
    }

    fun <T> fastLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

    /**
     * This method helps to validate and update ui for edit bid textfield
     * @param[block] is false ,if validation passed or is true is bid is error
     */
    fun TextFieldUnify.addBidValidationListener(
        minBid: String, maxBid: String, suggestedBid: String,
        block: ((error: Boolean) -> Unit)? = null,
    ) {
        val textField = this
        val resources = textField.context.resources
        textField.setMessage(String.format(
            resources.getString(R.string.topads_common_keyword_recommended_budget), suggestedBid
        ))
        textFieldInput.addTextChangedListener(
            object : NumberTextWatcher(textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    val result = number.toInt()
                    textField.setError(true)
                    when {
                        result < minBid.toDoubleOrZero() -> {
                            textField.setMessage(String.format(
                                resources.getString(R.string.min_bid_error_new), minBid
                            ))
                            block?.invoke(true)
                        }
                        result > maxBid.toDoubleOrZero() -> {
                            textField.setMessage(String.format(
                                resources.getString(R.string.max_bid_error_new), maxBid
                            ))
                            block?.invoke(true)
                        }
                        result % BUDGET_MULTIPLE_FACTOR != CONST_0 -> {
                            textField.setMessage(String.format(
                                resources.getString(R.string.topads_common_error_multiple_50), "50"
                            ))
                            block?.invoke(true)
                        }
                        else -> {
                            textField.setError(false)
                            textField.setMessage(String.format(
                                resources.getString(R.string.topads_common_keyword_recommended_budget),
                                suggestedBid
                            ))
                            block?.invoke(false)
                        }
                    }
                }
            }
        )
    }

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
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, CONST_0)
    }

    fun convertToCurrencyString(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value) + KALI)
    }

    fun convertToCurrency(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value))
    }

    fun String.removeCommaRawString() = toString().replace(",", "").replace(".", "").replace("Rp", "").trim()

    private const val VALIDATE_KEYWORD_MAX_RANGE = 70
    fun validateKeyword(context: Context?, text: CharSequence?): CharSequence? {
        return if (!text.isNullOrBlank() && text.split(" ").size > Constants.KEYWORD_WORD_COUNT) {
            context?.getString(R.string.error_max_length_keyword)
        } else if (!text.isNullOrBlank() && !text.matches(Constants.KEYWORD_REGEX.toRegex())) {
            context?.getString(R.string.error_keyword)
        } else if (text?.length ?: CONST_0 > VALIDATE_KEYWORD_MAX_RANGE) {
            context?.getString(R.string.error_max_length)
        } else {
            null
        }
    }

    fun validateKeywordCountAndChars(context: Context?, text: CharSequence?): CharSequence? {
        return if (text?.length ?: CONST_0 > Constants.KEYWORD_CHARACTER_COUNT) {
            context?.getString(R.string.error_max_length)
        } else if (!text.isNullOrBlank() && !text.matches(Constants.KEYWORD_REGEX_WITH_SPECIAL_CHARS.toRegex())) {
            context?.getString(R.string.error_keyword)
        } else {
            null
        }
    }

    fun getErrorMessage(context: Context?, message: String): String {
        context?.let {
            return when (message) {
                TopAdsCommonConstant.ERROR_TOO_MANY_REQUEST -> {
                    it.getString(R.string.topads_common_error_too_many_request)
                }
                TopAdsCommonConstant.ERROR_INVALID_ITEM_ID -> {
                    it.getString(R.string.topads_common_error_invalid_item)
                }
                TopAdsCommonConstant.ERROR_INVALID_KEYWORD -> {
                    it.getString(R.string.topads_common_error_invalid_keyword)
                }
                TopAdsCommonConstant.EROOR_GROUP_NAME_EXIST ->{
                    it.getString(R.string.topads_common_error_invalid_keyword)
                }
                else -> message
            }
        }
        return message
    }

}
