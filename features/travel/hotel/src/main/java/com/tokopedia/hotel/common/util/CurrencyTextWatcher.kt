package com.tokopedia.hotel.common.util

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import com.tokopedia.hotel.common.util.HotelStringUtils.Companion.convertToNumeric
import com.tokopedia.hotel.common.util.HotelStringUtils.Companion.getThousandSeparatorString

/**
 * @author by jessica on 20/04/20
 */

class CurrencyTextWatcher @JvmOverloads constructor(editText: EditText, format: String? = null, useCommaForThousand: Boolean = true) : TextWatcher {
    private var defaultValue: String
    private var maxLength = DEFAULT_MAX_LENGTH
    private val editText: EditText
    private val useCommaForThousand: Boolean
    var stringFormat: String? = null
    var prefixLength = 0

    constructor(editText: EditText, currencyEnum: CurrencyEnum) : this(editText, currencyEnum.formatString, currencyEnum.isUseCommaAsThousand()) {}

    private var onNumberChangeListener: OnNumberChangeListener? = null
    fun setDefaultValue(defaultValue: String) {
        this.defaultValue = defaultValue
    }

    fun setMaxLength(maxLength: Int) {
        this.maxLength = maxLength
    }

    interface OnNumberChangeListener {
        fun onNumberChanged(value: Double)
    }

    fun setOnNumberChangeListener(onNumberChangeListener: OnNumberChangeListener?) {
        this.onNumberChangeListener = onNumberChangeListener
    }

    fun setFormat(format: String?) {
        if (TextUtils.isEmpty(format)) {
            this.stringFormat = "%s"
        } else {
            this.stringFormat = format
        }
        prefixLength = this.stringFormat!!.indexOf("%")
        if (prefixLength < 0) {
            prefixLength = 0
        }
    }

    override fun afterTextChanged(s: Editable) {
        var sString = s.toString()
        if (sString.length >= maxLength + prefixLength) {
            sString = sString.substring(0, maxLength + prefixLength)
        }
        val doubleValue = convertToNumeric(sString, useCommaForThousand)!!
        if (onNumberChangeListener != null) {
            onNumberChangeListener!!.onNumberChanged(doubleValue)
        }
        editText.removeTextChangedListener(this)
        if (doubleValue == 0.0) {
            editText.setText(String.format(stringFormat!!, defaultValue))
            editText.setSelection(editText.text.length)
        } else {
            var selectionStart = editText.selectionStart - prefixLength
            if (selectionStart < 0) {
                selectionStart = 0
            }
            val thousandString = getThousandSeparatorString(doubleValue, useCommaForThousand, selectionStart)
            editText.setText(String.format(stringFormat!!, thousandString!!.formattedString))
            editText.setSelection(Math.min(editText.length(), thousandString.selection + prefixLength))
        }
        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    companion object {
        const val DEFAULT_MAX_LENGTH = 13 // 9.999.999.999 is max for default
    }

    init {
        this.editText = editText
        defaultValue = "0"
        this.useCommaForThousand = useCommaForThousand
        setFormat(format)
    }
}
