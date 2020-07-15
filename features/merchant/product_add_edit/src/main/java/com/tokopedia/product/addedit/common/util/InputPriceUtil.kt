package com.tokopedia.product.addedit.common.util

import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

object InputPriceUtil {
    fun applyPriceFormatToInputField(textField: EditText, price: String, textWatcher: TextWatcher) {
        textField.removeTextChangedListener(textWatcher)
        val formattedText = formatProductPriceInput(price)
        textField.setText(formattedText)
        textField.setSelection(formattedText.length)
        textField.addTextChangedListener(textWatcher)
    }

    fun formatProductPriceInput(productPriceInput: String): String {
        val priceWithoutScientificNotation = productPriceInput.format("%f")
        try {
            return if (priceWithoutScientificNotation.isNotBlank()) NumberFormat.getNumberInstance(Locale.US).format(productPriceInput.toBigDecimal()).replace(",", ".")
            else productPriceInput
        } catch (e: NumberFormatException) {
            return productPriceInput
        }
    }
}