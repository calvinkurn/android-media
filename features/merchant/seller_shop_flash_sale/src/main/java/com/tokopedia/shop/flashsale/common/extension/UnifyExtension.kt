package com.tokopedia.shop.flashsale.common.extension

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.UnifyButton

fun UnifyButton?.showLoading() {
    this?.run {
        isLoading = true
        loadingText = this.context.getString(R.string.sfs_please_wait)
        isClickable = false
    }
}

fun UnifyButton?.stopLoading() {
    this?.run {
        isLoading = false
        isClickable = true
    }
}

fun TextFieldUnify2?.setModeToNumberDelimitedInput(maxLength: Int) {
    val textFieldInput = this?.editText
    textFieldInput?.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    textFieldInput?.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                val cleanedText = charSequence?.toString()?.filter { it.isDigit() }.orEmpty()
                if (cleanedText.isNotEmpty()) {
                    val parsedLong = cleanedText.toLongOrZero()
                    val formattedText = parsedLong.getNumberFormatted()
                    val lengthDiff = formattedText.length - charSequence?.length.orZero()
                    val cursorPosition = start + count + lengthDiff
                    val fixedPosition = cursorPosition.coerceIn(Int.ZERO, formattedText.length)

                    textFieldInput.removeTextChangedListener(this)
                    textFieldInput.setText(formattedText)
                    textFieldInput.setSelection(fixedPosition)
                    textFieldInput.addTextChangedListener(this)
                }
            } catch (e: Exception) {
                // TODO: Log to crashlytics
            }
        }
    })
}

fun TextFieldUnify2.getTextAsNumber() = editText.text.toString().filterDigit()

fun TextFieldUnify2?.getTextLong() = this?.getTextAsNumber()?.toLongOrNull()

fun TextFieldUnify2?.getTextInt() = this?.getTextAsNumber()?.toIntOrNull()