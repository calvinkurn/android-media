package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
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
    }

    override fun onViewRecycled() {
        removeListeners()
        super.onViewRecycled()
    }

    override fun bind(element: SellableStockProductUIModel) {
        with(itemView) {
            tv_campaign_stock_variant_editor_name?.text = element.productName
            qte_campaign_stock_variant_editor?.setElement(element)
            if (element.isActive) {
                label_campaign_stock_inactive?.visibility = View.INVISIBLE
            } else {
                label_campaign_stock_inactive?.visible()
            }
            switch_campaign_stock_variant_editor?.run {
                isChecked = element.isActive
                setOnCheckedChangeListener { _, isChecked ->
                    element.isActive = isChecked
                    val status =
                            if (isChecked) {
                                this@with.label_campaign_stock_inactive?.visibility = View.INVISIBLE
                                ProductStatus.ACTIVE
                            } else {
                                this@with.label_campaign_stock_inactive?.visible()
                                ProductStatus.INACTIVE
                            }
                    onVariantStatusChanged(element.productId, status)
                    ProductManageTracking.eventClickAllocationProductStatus(isVariant = true, isOn = isChecked)
                }
            }
            switch_campaign_stock_variant_editor.isEnabled = element.access.editProduct
        }
    }

    private fun QuantityEditorUnify.setElement(element: SellableStockProductUIModel) {
        val maxLength = InputFilter.LengthFilter(MAXIMUM_LENGTH)
        editText.filters = arrayOf(maxLength)
        minValue = EditProductConstant.MINIMUM_STOCK
        maxValue = EditProductConstant.MAXIMUM_STOCK

        setValue(element.stock.toIntOrZero())

        stockEditTextWatcher = getStockTextChangeListener {
            val input = it
            val stock = if(input.isNotEmpty()) {
                input.toInt()
            } else {
                EditProductConstant.MINIMUM_STOCK
            }
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

        setEditStockAccess(element)
    }

    private fun QuantityEditorUnify.setEditStockAccess(element: SellableStockProductUIModel) {
        val canEditStock = element.access.editStock
        editText.isEnabled = canEditStock
        addButton.isEnabled = canEditStock
        subtractButton.isEnabled = canEditStock
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

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }
}