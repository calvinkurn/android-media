package com.tokopedia.product.manage.common.feature.variant.adapter.viewholder

import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.android.synthetic.main.item_product_variant_stock.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class ProductVariantStockViewHolder(
    itemView: View,
    private val listener: ProductVariantStockListener
): AbstractViewHolder<ProductVariant>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_variant_stock

        private const val MAXIMUM_STOCK = 999999
        private const val MINIMUM_STOCK = 0
        private const val MAXIMUM_LENGTH = 7
        private const val STATUS_SWITCH_DELAY = 500L
    }

    private var tempStock: Int? = null
    private var onClickStatusSwitch: Job? = null
    private var textChangeListener: TextWatcher? = null

    override fun bind(variant: ProductVariant) {
        setProductName(variant)
        setupStockQuantityEditor(variant)
        setupStatusSwitch(variant)
        setupStatusLabel(variant)
        setupStockHint(variant)
        setupCampaignLabel(variant)
    }

    private fun setProductName(variant: ProductVariant) {
        itemView.textProductName.text = variant.name
    }

    private fun setupStockQuantityEditor(variant: ProductVariant) {
        removeStockEditorTextChangedListener()
        setStockMinMaxValue()
        setStockEditorValue(variant.stock)
        setAddButtonClickListener(variant)
        setSubtractButtonClickListener(variant)
        addStockEditorTextChangedListener(variant)
        setupStockEditor(variant)
    }

    private fun setupStockEditor(variant: ProductVariant) {
        val canEditStock = variant.access.editStock

        if(canEditStock) {
            itemView.quantityEditorStock.show()
            itemView.textStock.hide()
        } else {
            itemView.quantityEditorStock.hide()
            itemView.textStock.show()
            itemView.textStock.text = variant.stock.toString()
        }
    }

    private fun setupStatusSwitch(variant: ProductVariant) {
        val canEditProduct = variant.access.editProduct
        itemView.switchStatus.setOnCheckedChangeListener(null)
        itemView.switchStatus.isChecked = variant.isActive()
        itemView.switchStatus.setOnCheckedChangeListener { _, isChecked ->
            onClickStatusSwitch?.cancel()
            onClickStatusSwitch = runWithDelay({
                val status = if (isChecked) {
                    ProductStatus.ACTIVE
                } else {
                    ProductStatus.INACTIVE
                }
                listener.onStatusChanged(variant.id, status)
            }, STATUS_SWITCH_DELAY)
        }
        itemView.switchStatus.isEnabled = canEditProduct
    }

    private fun setupStatusLabel(variant: ProductVariant) {
        val shouldShow = variant.isInactive() || variant.isEmpty()
        itemView.labelInactive.showWithCondition(shouldShow)
    }

    private fun setupStockHint(variant: ProductVariant) {
        val stock = getCurrentStockInput()
        val shouldShow = stock == 0 && !variant.isAllStockEmpty
        itemView.textTotalStockHint.showWithCondition(shouldShow)
    }

    private fun setupCampaignLabel(variant: ProductVariant) {
        itemView.labelCampaign.showWithCondition(variant.isCampaign)
    }

    private fun setStockMinMaxValue() {
        itemView.quantityEditorStock.apply {
            val maxLength = LengthFilter(MAXIMUM_LENGTH)
            editText.filters = arrayOf(maxLength)
            minValue = MINIMUM_STOCK
            maxValue = MAXIMUM_STOCK
        }
    }

    private fun setStockEditorValue(stock: Int) {
        itemView.quantityEditorStock.setValue(stock)
    }

    private fun addStockEditorTextChangedListener(variant: ProductVariant) {
        val quantityEditor = itemView.quantityEditorStock
        tempStock = quantityEditor.getValue()

        quantityEditor.editText.apply {
            textChangeListener = createTextChangeListener(variant)
            addTextChangedListener(textChangeListener)

            setOnFocusChangeListener { _, isFocus ->
                if(!isFocus) {
                    val currentStock = quantityEditor.getValue()
                    tempStock?.let { previousStock ->
                        // if previous stock is not the same as current stock, hit the tracker
                        if(previousStock != currentStock) {
                            ProductManageTracking.eventClickChangeAmountVariant()
                            tempStock = currentStock
                        }
                    }
                }
            }
        }
    }

    private fun removeStockEditorTextChangedListener() {
       textChangeListener?.let {
           itemView.quantityEditorStock.editText.apply {
               removeTextChangedListener(it)
           }
       }
    }

    private fun createTextChangeListener(variant: ProductVariant): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s?.toString().orEmpty()
                val stock = if (input.isNotEmpty()) {
                    itemView.quantityEditorStock.getValue()
                } else {
                    MINIMUM_STOCK
                }
                toggleQuantityEditorBtn(stock)
                listener.onStockChanged(variant.id, stock)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
    }

    private fun setAddButtonClickListener(variant: ProductVariant) {
        itemView.quantityEditorStock.apply {
            addButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock++

                if(stock <= MAXIMUM_STOCK) {
                    tempStock = stock
                    editText.setText(stock.getNumberFormatted())
                    listener.onStockBtnClicked()
                    ProductManageTracking.eventClickChangeAmountVariant()
                }

                setupStockHint(variant)
                setupStatusLabel(variant)
            }
        }
    }

    private fun setSubtractButtonClickListener(variant: ProductVariant) {
        itemView.quantityEditorStock.apply {
            subtractButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock--

                if(stock >= MINIMUM_STOCK) {
                    tempStock = stock
                    editText.setText(stock.getNumberFormatted())
                    listener.onStockBtnClicked()
                    ProductManageTracking.eventClickChangeAmountVariant()
                }

                setupStockHint(variant)
                setupStatusLabel(variant)
            }
        }
    }

    private fun toggleQuantityEditorBtn(stock: Int) {
        val enableAddBtn = stock < MAXIMUM_STOCK
        val enableSubtractBtn = stock > MINIMUM_STOCK

        itemView.quantityEditorStock.apply {
            addButton.isEnabled = enableAddBtn
            subtractButton.isEnabled = enableSubtractBtn
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }

    private fun runWithDelay(block: () -> Unit, delayMs: Long): Job {
        return CoroutineScope(Dispatchers.Main)
            .launchCatchError(block = {
                delay(delayMs)
                block()
            }, onError = {})
    }

    private fun getCurrentStockInput(): Int {
        val quantityEditorStock = itemView.quantityEditorStock
        val input = quantityEditorStock.editText.text.toString()
        return if(input.isNotEmpty()) {
            quantityEditorStock.getValue()
        } else {
            MINIMUM_STOCK
        }
    }

    interface ProductVariantStockListener {
        fun onStockBtnClicked()
        fun onStockChanged(variantId: String, stock: Int)
        fun onStatusChanged(variantId: String, status: ProductStatus)
    }
}
