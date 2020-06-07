package com.tokopedia.product.addedit.common.util

import android.os.Handler
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

// for now, TextFieldUnify cannot edit character in the middle of string
// to enable that, you can use this function
fun TextFieldUnify?.enableSubstringEditing() {
    val textFieldInput = this?.textFieldInput
    textFieldInput?.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable) {}

        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            Handler().postDelayed({
                if (start == 0 && count == 1) {
                    // handle adding first char of sentences unify setSelection
                    textFieldInput.setSelection(start + 1)
                } else if (before > 0 && count == 0) {
                    // handle removing char unify setSelection
                    textFieldInput.setSelection(start)
                } else if (count == 1) {
                    // handle adding " " char unify setSelection
                    val addedChar = charSequence?.getOrNull(start).toString()
                    val addedCharBefore = charSequence?.getOrNull(start - 1).toString()

                    if (addedChar == " " || addedCharBefore == " ") {
                        textFieldInput.setSelection(start + 1)
                    }
                }

            }, 1)
        }
    })
}

