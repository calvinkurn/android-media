package com.tokopedia.product.addedit.common.util

import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

object InputPriceUtil {

    fun applyPriceFormatToInputField(
        textField: EditText,
        price: String,
        cursorStart: Int,
        stringLength: Int,
        charAddedLength: Int,
        textWatcher: TextWatcher
    ) {
        try {
            textField.removeTextChangedListener(textWatcher)
            val formattedText = formatProductPriceInput(price)
            val lengthDiff = formattedText.length - stringLength
            val cursorPosition = cursorStart + charAddedLength + lengthDiff
            textField.setText(formattedText)
            textField.setSelection(cursorPosition.coerceIn(0, formattedText.length))
            textField.addTextChangedListener(textWatcher)
        } catch (e: Exception) {
            AddEditProductErrorHandler.logMessage("applyPriceFormatToInputField: $price")
            AddEditProductErrorHandler.logExceptionToCrashlytics(e)
        }
    }

    fun formatProductPriceInput(productPriceInput: String): String {
        return try {
            if (!productPriceInput.matches(Regex("-?(\\d+([.,]\\d+)+)+(E\\+\\d+)?"))) {
                NumberFormat.getNumberInstance(Locale.US)
                    .format(productPriceInput.toBigDecimal())
                    .replace(",", ".")
            } else {
                productPriceInput
            }
        } catch (e: Exception) {
            AddEditProductErrorHandler.logMessage("productPriceInput: $productPriceInput")
            AddEditProductErrorHandler.logExceptionToCrashlytics(e)
            productPriceInput
        }
    }
}
