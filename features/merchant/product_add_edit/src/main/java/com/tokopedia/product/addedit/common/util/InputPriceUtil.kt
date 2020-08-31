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
        textField.removeTextChangedListener(textWatcher)
        val formattedText = formatProductPriceInput(price)
        val lengthDiff = formattedText.length - stringLength
        val cursorPosition = cursorStart + charAddedLength + lengthDiff
        textField.setText(formattedText)
        textField.setSelection(cursorPosition.coerceIn(0, formattedText.length))
        textField.addTextChangedListener(textWatcher)
    }

    fun formatProductPriceInput(productPriceInput: String): String {
        return try {
            val priceWithoutScientificNotation = productPriceInput.format("%f")
            if (priceWithoutScientificNotation.isNotBlank()) {
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