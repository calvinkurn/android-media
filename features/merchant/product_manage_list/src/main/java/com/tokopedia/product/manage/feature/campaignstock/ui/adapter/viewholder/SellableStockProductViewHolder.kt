package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.QuantityEditorUnify
import kotlinx.android.synthetic.main.item_campaign_stock_variant_editor.view.*

class SellableStockProductViewHolder(itemView: View?,
                                     private val onVariantStockChanged: (productId: String, stock: Int) -> Unit,
                                     private val onVariantStatusChanged: (productId: String, status: ProductStatus) -> Unit): AbstractViewHolder<SellableStockProductUIModel>(itemView) {

    private var stockEditTextWatcher: TextWatcher? = null

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_variant_editor

        private const val MAXIMUM_LENGTH = 7
        private const val MINIMUM_INPUT = 0
    }

    override fun onViewRecycled() {
        removeListeners()
        super.onViewRecycled()
    }

    override fun bind(element: SellableStockProductUIModel) {
        with(itemView) {
            tv_campaign_stock_variant_editor_name?.text = element.productName
            qte_campaign_stock_variant_editor?.setElement(element)
            label_campaign_stock_inactive.showWithCondition(!element.isActive)
            label_campaign_stock.showWithCondition(element.isCampaign)
            switch_campaign_stock_variant_editor?.run {
                isChecked = element.isActive
                setOnCheckedChangeListener { _, isChecked ->
                    element.isActive = isChecked
                    val status = if (isChecked) {
                        ProductStatus.ACTIVE
                    } else {
                        ProductStatus.INACTIVE
                    }
                    this@with.label_campaign_stock_inactive.showWithCondition(!isChecked)
                    onVariantStatusChanged(element.productId, status)
                    ProductManageTracking.eventClickAllocationProductStatus(isVariant = true, isOn = isChecked)
                }
            }
            switch_campaign_stock_variant_editor.isEnabled = element.access.editProduct
        }
        showHideStockInfo(element)
    }

    private fun QuantityEditorUnify.setElement(element: SellableStockProductUIModel) {
        val maxLength = InputFilter.LengthFilter(MAXIMUM_LENGTH)
        editText.filters = arrayOf(maxLength)
        minValue = EditProductConstant.MINIMUM_STOCK
        maxValue = EditProductConstant.MAXIMUM_STOCK

        setValue(element.stock.toIntOrZero())

        stockEditTextWatcher = getStockTextChangeListener {
            val stock = if(it.isNotEmpty()) {
                getValue()
            } else {
                EditProductConstant.MINIMUM_STOCK
            }
            showHideStockInfo(element)
            toggleQuantityEditorBtn(stock)
            element.stock = stock.toString()
            onVariantStockChanged(element.productId, stock)
        }
        editText.addTextChangedListener(stockEditTextWatcher)

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                ProductManageTracking.eventClickAllocationInputStock(isVariant = true)
            }
        }

        setAddClickListener {
            ProductManageTracking.eventClickAllocationIncreaseStock(isVariant = true)
        }
        setSubstractListener {
            ProductManageTracking.eventClickAllocationDecreaseStock(isVariant = true)
        }

        setupStockEditor(element)
    }

    private fun showHideStockInfo(element: SellableStockProductUIModel) {
        val stock = getCurrentStockInput()
        val shouldShow = stock == 0 && !element.isAllStockEmpty
        itemView.emptyStockInfo.showWithCondition(shouldShow)
    }

    private fun setupStockEditor(element: SellableStockProductUIModel) {
        val canEditStock = element.access.editStock

        if(canEditStock) {
            itemView.qte_campaign_stock_variant_editor.show()
            itemView.textStock.hide()
        } else {

            itemView.qte_campaign_stock_variant_editor.hide()
            itemView.textStock.show()
            itemView.textStock.text = element.stock
        }
    }

    private fun QuantityEditorUnify.toggleQuantityEditorBtn(stock: Int) {
        val enableAddBtn = stock < EditProductConstant.MAXIMUM_STOCK
        val enableSubtractBtn = stock > EditProductConstant.MINIMUM_STOCK

        addButton.isEnabled = enableAddBtn
        subtractButton.isEnabled = enableSubtractBtn
    }

    private fun getStockTextChangeListener(afterTextChanged: (String) -> Unit): TextWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged(editable?.toString().orEmpty())
                }
            }

    private fun removeListeners() {
        itemView.run {
            stockEditTextWatcher?.let {
                qte_campaign_stock_variant_editor?.editText?.removeTextChangedListener(it)
            }
            switch_campaign_stock_variant_editor?.setOnCheckedChangeListener(null)
        }
    }

    private fun getCurrentStockInput(): Int {
        val stockEditor = itemView.qte_campaign_stock_variant_editor
        val input = stockEditor?.editText?.text.toString()

        return if(input.isNotEmpty()) {
            stockEditor.getValue()
        } else {
            MINIMUM_INPUT
        }
    }
}