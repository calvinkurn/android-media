package com.tokopedia.product.manage.feature.campaignstock.ui.textwatcher

import android.text.Editable
import android.text.TextWatcher
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant
import com.tokopedia.unifycomponents.QuantityEditorUnify

class StockEditorTextWatcher(
    private val editor: QuantityEditorUnify?,
    private val onTotalStockChanged: (Int) -> Unit
): TextWatcher {
    override fun afterTextChanged(s: Editable) {
        val input = s.toString()
        val stock = if (input.isNotEmpty()) {
            input.toInt()
        } else {
            EditProductConstant.MINIMUM_STOCK
        }
        toggleQuantityEditorBtn(stock)
        onTotalStockChanged(stock)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun toggleQuantityEditorBtn(stock: Int) {
        editor?.apply {
            val enableAddBtn = stock < EditProductConstant.MAXIMUM_STOCK
            val enableSubtractBtn = stock > EditProductConstant.MINIMUM_STOCK
            addButton.isEnabled = enableAddBtn
            subtractButton.isEnabled = enableSubtractBtn
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }
}