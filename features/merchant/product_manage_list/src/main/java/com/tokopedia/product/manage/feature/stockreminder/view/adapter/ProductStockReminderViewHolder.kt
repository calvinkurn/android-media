package com.tokopedia.product.manage.feature.stockreminder.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.adapter.viewholder.ProductVariantStockViewHolder
import com.tokopedia.product.manage.databinding.ItemStockReminderBinding
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductStockReminderViewHolder(
    itemView: View,
    private val listener: ProductStockReminder
) : AbstractViewHolder<ProductStockReminderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_stock_reminder

        private const val REMINDER_ACTIVE = 2
        private const val REMINDER_INACTIVE = 1

        private const val MINIMUM_STOCK = 5
        private const val MAXIMUM_STOCK = 999999
        private const val EMPTY_INPUT_STOCK = 0
    }

    private var textChangeListener: TextWatcher? = null

    private val binding by viewBinding<ItemStockReminderBinding>()

    override fun bind(product: ProductStockReminderUiModel) {
        binding?.tvProductName?.text = product.productName
        setupStatusSwitch(product)
        setupStockEditorText(product)
        setAddButtonClickListener()
        setSubtractButtonClickListener()
    }

    private fun setupStatusSwitch(product: ProductStockReminderUiModel) {
        binding?.swStockReminder?.isChecked = product.stockAlertStatus == REMINDER_ACTIVE
        binding?.swStockReminder?.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                val stockLimit = binding?.qeStock?.getValue().orZero()
                listener.onChangeStockReminder(product.id, stockLimit, REMINDER_ACTIVE)
            } else {
                listener.onChangeStockReminder(product.id, Int.ZERO, REMINDER_INACTIVE)

            }
        }
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
                if (binding?.swStockReminder?.isChecked.orFalse()) {
                    listener.onChangeStockReminder(product.id, stock, REMINDER_ACTIVE)
                } else {
                    listener.onChangeStockReminder(product.id, stock, REMINDER_INACTIVE)

                }
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
            }
            stock > MAXIMUM_STOCK -> {
                binding?.qeStock?.errorMessageText = itemView.resources.getString(
                    R.string.product_stock_reminder_max_stock_error,
                    MAXIMUM_STOCK.getNumberFormatted()
                )

            }
            else -> {
                binding?.qeStock?.errorMessageText = String.EMPTY
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
        val enableSubtractBtn = stock > MINIMUM_STOCK

        binding?.qeStock?.run {
            addButton.isEnabled = enableAddBtn
            subtractButton.isEnabled = enableSubtractBtn
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }

    interface ProductStockReminder {
        fun onChangeStockReminder(productId: String, stock: Int, status: Int)
    }
}