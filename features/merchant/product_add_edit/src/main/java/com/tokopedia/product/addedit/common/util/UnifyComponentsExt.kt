package com.tokopedia.product.addedit.common.util

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import java.math.BigInteger
import java.text.NumberFormat
import java.util.*

fun TextFieldUnify?.setText(text: String) = this?.textFieldInput?.setText(text)

fun TextAreaUnify?.setText(text: String) = this?.textAreaInput?.setText(text)

fun TextFieldUnify?.getText(): String = this?.textFieldInput?.text.toString()

fun TextFieldUnify?.getTextIntOrZero(): Int = this?.textFieldInput?.text.toString().replace(".", "").toIntOrZero()

fun TextFieldUnify?.getTextBigIntegerOrZero(): BigInteger = this?.textFieldInput?.text.toString().replace(".", "").toBigIntegerOrNull() ?: 0.toBigInteger()

fun TextFieldUnify?.setModeToNumberInput() {
    val textFieldInput = this?.textFieldInput
    val maxLength = Int.MAX_VALUE.toString().length
    textFieldInput?.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    textFieldInput?.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            // clean any kind of number formatting here
            val productPriceInput = charSequence?.toString()?.replace(".", "")
            productPriceInput?.let {
                // format the number
                it.toLongOrNull()?.let { parsedLong ->
                    textFieldInput.removeTextChangedListener(this)
                    val formattedText: String = NumberFormat.getNumberInstance(Locale.US)
                            .format(parsedLong)
                            .toString()
                            .replace(",", ".")
                    textFieldInput.setText(formattedText)
                    textFieldInput.setSelection(formattedText.length)
                    textFieldInput.addTextChangedListener(this)
                }
            }
        }
    })
}

fun TextAreaUnify?.replaceTextAndRestoreCursorPosition(text: String) = this?.textAreaInput?.run {
    val cursorPosition = selectionEnd.orZero()
    setText(text)
    setSelection(cursorPosition.coerceAtMost(text.length))
}