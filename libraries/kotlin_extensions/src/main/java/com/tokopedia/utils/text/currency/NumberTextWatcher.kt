package com.tokopedia.utils.text.currency

import android.text.Editable
import android.text.TextUtils
import android.widget.EditText

/**
 * Created by Nathaniel on 3/3/2017.
 */

open class NumberTextWatcher : AfterTextWatcher {

    protected var editText: EditText

    private var defaultValue: String? = null

    constructor(editText: EditText) {
        this.editText = editText
        this.defaultValue = DEFAULT_VALUE
    }

    constructor(editText: EditText, defaultValue: String) {
        this.editText = editText
        this.defaultValue = defaultValue
    }

    private fun applyFormatter() {
        CurrencyFormatHelper.setToRupiahCheckPrefix(editText)
    }

    override fun afterTextChanged(s: Editable) {
        applyFormatter()
        var valueString = CurrencyFormatHelper.removeCurrencyPrefix(s.toString())
        valueString = valueString?.let { StringUtils.removePeriod(it) }
        if (TextUtils.isEmpty(valueString)) {
            editText.setText(defaultValue)
            editText.setSelection(editText.text.length)
            return
        }
        val value = try {
            valueString?.toDouble() ?: 0.0
        } catch (e: NumberFormatException) {
            0.0
        }
        onNumberChanged(value)
    }

    open fun onNumberChanged(number: Double) {

    }

    companion object {
        private const val DEFAULT_VALUE = "0"
    }
}