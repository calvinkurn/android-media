package com.tokopedia.product.manage.feature.stockreminder.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemStockReminderBinding
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.EMPTY_INPUT_STOCK
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.MINIMUM_STOCK
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.MAXIMUM_STOCK
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_ACTIVE
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_INACTIVE
import com.tokopedia.utils.view.binding.viewBinding

class ProductStockReminderViewHolder(
    itemView: View,
    private val listener: ProductStockReminderListener
) : AbstractViewHolder<ProductStockReminderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_stock_reminder
    }

    private var textChangeListener: TextWatcher? = null

    private var isValid = false

    private var firstStateChecked = false

    private val binding by viewBinding<ItemStockReminderBinding>()

    override fun bind(product: ProductStockReminderUiModel) {
        binding?.tvProductName?.text = product.productName
        setupStatusSwitch(product)
        setupStockEditorText(product)
        setAddButtonClickListener()
        setSubtractButtonClickListener()
        firstStateChecked = true

    }

    private fun setupStatusSwitch(product: ProductStockReminderUiModel) {
        binding?.swStockReminder?.setOnCheckedChangeListener { _, _ ->
            val stockLimit = binding?.qeStock?.getValue().orZero()

            notifyChange(product.id,stockLimit)
        }
        binding?.swStockReminder?.isChecked = product.stockAlertStatus == REMINDER_ACTIVE
    }

    private fun setupStockEditorText(product: ProductStockReminderUiModel) {
        binding?.qeStock?.editText?.run {
            textChangeListener = createTextChangeListener(product)
            addTextChangedListener(textChangeListener)
        }

        binding?.qeStock?.setValue(product.stockAlertCount)

        binding?.qeStock?.run {
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus()
                    KeyboardHandler.DropKeyboard(itemView.context, this)
                }
                true
            }
        }
    }

    private fun createTextChangeListener(product: ProductStockReminderUiModel): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(editor: Editable?) {
                val input = editor.toString()
                val stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    EMPTY_INPUT_STOCK
                }
                validateMinMaxStock(stock)
                toggleQuantityEditorBtn(stock)
                notifyChange(product.id,stock)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
    }

    fun validateMinMaxStock(stock: Int) {
        when {
            stock < MINIMUM_STOCK -> {
                binding?.qeStock?.errorMessageText = itemView.resources.getString(
                    R.string.product_stock_reminder_min_stock_error,
                    MINIMUM_STOCK
                )
                isValid = false
            }
            stock > MAXIMUM_STOCK -> {
                binding?.qeStock?.errorMessageText = itemView.resources.getString(
                    R.string.product_stock_reminder_max_stock_error,
                    MAXIMUM_STOCK.getNumberFormatted()
                )
                isValid = false
            }
            else -> {
                binding?.qeStock?.errorMessageText = String.EMPTY
                isValid = true
            }
        }
    }

    private fun setAddButtonClickListener() {
        binding?.qeStock?.run {
            addButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    EMPTY_INPUT_STOCK
                }

                stock++

                if (stock <= binding?.qeStock?.maxValue.orZero()) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun setSubtractButtonClickListener() {
        binding?.qeStock?.run {
            subtractButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    EMPTY_INPUT_STOCK
                }

                stock--

                if (stock > EMPTY_INPUT_STOCK) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun toggleQuantityEditorBtn(stock: Int) {
        val enableAddBtn = stock < binding?.qeStock?.maxValue.orZero()

        binding?.qeStock?.run {
            addButton.isEnabled = enableAddBtn
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }

    private fun notifyChange(
        productId: String,
        stock: Int
    ) {
        if (firstStateChecked) {
            if (binding?.swStockReminder?.isChecked.orFalse()) {
                listener.onChangeStockReminder(productId, stock, REMINDER_ACTIVE, isValid)
            } else {
                isValid = true
                listener.onChangeStockReminder(productId, Int.ZERO, REMINDER_INACTIVE, isValid)
            }
        }
    }

    interface ProductStockReminderListener {
        fun onChangeStockReminder(productId: String, stock: Int, status: Int, isValid: Boolean)
    }
}