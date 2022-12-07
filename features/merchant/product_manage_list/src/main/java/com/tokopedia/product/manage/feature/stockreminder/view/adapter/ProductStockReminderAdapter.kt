package com.tokopedia.product.manage.feature.stockreminder.view.adapter

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemProductStockReminderBinding
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.MAXIMUM_STOCK_REMINDER
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.MINIMUM_STOCK_REMINDER
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_ACTIVE
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_INACTIVE
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel

class ProductStockReminderAdapter(
    private val listener: ProductStockReminderListener
) : RecyclerView.Adapter<ProductStockReminderAdapter.ProductStockReminderViewHolder>() {

    private var dataProducts: List<ProductStockReminderUiModel> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductStockReminderViewHolder {
        val binding = ItemProductStockReminderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductStockReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductStockReminderViewHolder, position: Int) {
        holder.bind(dataProducts[position])
    }

    override fun getItemCount() = dataProducts.size

    fun setItems(data: List<ProductStockReminderUiModel>) {
        this.dataProducts = data
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.dataProducts = emptyList()
        notifyDataSetChanged()
    }

    inner class ProductStockReminderViewHolder(private val binding: ItemProductStockReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var textChangeListener: TextWatcher? = null

        private var firstStateChecked = false

        fun bind(product: ProductStockReminderUiModel) {
            binding.tvProductName.text = product.productName
            setupStatusSwitch(product)
            setupStockEditorText(product)
            setAddButtonClickListener()
            setSubtractButtonClickListener()
            firstStateChecked = true
        }

        private fun setupStatusSwitch(product: ProductStockReminderUiModel) {
            binding.swStockReminder.setOnCheckedChangeListener { _, _ ->
                val stockLimit = binding.qeStock.getValue().orZero()
                validateMinMaxStock(stockLimit, product.maxStock)
                notifyChange(product.id, stockLimit)
            }
            binding.swStockReminder.isChecked =
                product.stockAlertStatus == REMINDER_ACTIVE
        }

        private fun setupStockEditorText(product: ProductStockReminderUiModel) {
            binding.qeStock.editText.run {
                val maxLength = InputFilter.LengthFilter(StockReminderConst.MAXIMUM_LENGTH)
                filters = arrayOf(maxLength)
                textChangeListener = createTextChangeListener(product)
                addTextChangedListener(textChangeListener)
            }

            binding.qeStock.run {
                maxValue = product.maxStock ?: MAXIMUM_STOCK_REMINDER
                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        clearFocus()
                        KeyboardHandler.DropKeyboard(itemView.context, this)
                    }
                    true
                }
            }
            binding.qeStock.setValue(product.stockAlertCount)
        }

        private fun createTextChangeListener(product: ProductStockReminderUiModel): TextWatcher {
            return object : TextWatcher {
                override fun afterTextChanged(editor: Editable?) {
                    val input = editor.toString()
                    val stock = if (input.isNotEmpty()) {
                        input.toInt()
                    } else {
                        StockReminderConst.EMPTY_INPUT_STOCK
                    }
                    validateMinMaxStock(stock, product.maxStock)
                    notifyChange(product.id, stock)
                    toggleQuantityEditorBtn(stock)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }
        }

        private fun validateMinMaxStock(stock: Int, maxStock: Int?) {
            val maxStockReminder = maxStock ?: MAXIMUM_STOCK_REMINDER
            when {
                stock < MINIMUM_STOCK_REMINDER -> {
                    binding.qeStock.errorMessageText = itemView.resources.getString(
                        R.string.product_stock_reminder_min_stock_error,
                        MINIMUM_STOCK_REMINDER
                    )
                }
                stock > maxStockReminder -> {
                    binding.qeStock.errorMessageText = itemView.resources.getString(
                        R.string.product_stock_reminder_max_stock_error,
                        maxStockReminder.getNumberFormatted()
                    )
                }
                else -> {
                    binding.qeStock.errorMessageText = String.EMPTY
                }
            }
        }

        private fun setAddButtonClickListener() {
            binding.qeStock.run {
                addButton.setOnClickListener {
                    val input = editText.text.toString()

                    var stock = if (input.isNotEmpty()) {
                        input.toInt()
                    } else {
                        StockReminderConst.EMPTY_INPUT_STOCK
                    }

                    stock++

                    if (stock <= binding.qeStock.maxValue.orZero()) {
                        editText.setText(stock.getNumberFormatted())
                    }
                }
            }
        }

        private fun setSubtractButtonClickListener() {
            binding.qeStock.run {
                subtractButton.setOnClickListener {
                    val input = editText.text.toString()

                    var stock = if (input.isNotEmpty()) {
                        input.toInt()
                    } else {
                        StockReminderConst.EMPTY_INPUT_STOCK
                    }

                    stock--

                    if (stock > StockReminderConst.EMPTY_INPUT_STOCK) {
                        editText.setText(stock.getNumberFormatted())
                    }
                }
            }
        }

        private fun toggleQuantityEditorBtn(stock: Int) {
            val enableAddBtn = stock < binding.qeStock.maxValue.orZero()
            val enableSubstractBtn = stock > Int.ZERO
            binding.qeStock.run {
                addButton.isEnabled = enableAddBtn
                subtractButton.isEnabled = enableSubstractBtn
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
                val isActive = binding.swStockReminder.isChecked.orFalse()
                if (isActive) {
                    listener.onChangeStockReminder(
                        productId,
                        stock,
                        REMINDER_ACTIVE
                    )
                } else {
                    listener.onChangeStockReminder(
                        productId,
                        stock,
                        REMINDER_INACTIVE
                    )
                }
            }
        }
    }

    interface ProductStockReminderListener {
        fun onChangeStockReminder(
            productId: String,
            stock: Int,
            status: Int
        )
    }
}
