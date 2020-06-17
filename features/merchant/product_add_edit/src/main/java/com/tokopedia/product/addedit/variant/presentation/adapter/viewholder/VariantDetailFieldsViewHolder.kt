package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailFieldsViewModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify

class VariantDetailFieldsViewHolder(itemView: View?) : AbstractViewHolder<VariantDetailFieldsViewModel>(itemView) {

    private var unitValueLabel: AppCompatTextView? = null
    private var statusSwitch: SwitchUnify? = null
    private var priceField: TextFieldUnify? = null
    private var stockField: TextFieldUnify? = null
    private var skuField: TextFieldUnify? = null

    init {
        unitValueLabel = itemView?.findViewById(R.id.tv_unit_value_label)
        statusSwitch = itemView?.findViewById(R.id.su_variant_status)
        priceField = itemView?.findViewById(R.id.tfu_price_field)
        stockField = itemView?.findViewById(R.id.tfu_stock_field)
        skuField = itemView?.findViewById(R.id.tfu_sku_field)
    }

    override fun bind(element: VariantDetailFieldsViewModel?) {
        element?.run {
            val variantDetailInputLayoutModel = this.variantDetailInputLayoutModel
            unitValueLabel?.text = variantDetailInputLayoutModel.unitValueLabel
            statusSwitch?.isChecked = variantDetailInputLayoutModel.isActive
            priceField?.textFieldInput?.setText(variantDetailInputLayoutModel.price)
            priceField?.setError(variantDetailInputLayoutModel.isPriceError)
            priceField?.setMessage(variantDetailInputLayoutModel.priceFieldErrorMessage)
            stockField?.textFieldInput?.setText(variantDetailInputLayoutModel.stock)
            stockField?.setError(variantDetailInputLayoutModel.isStockError)
            stockField?.setMessage(variantDetailInputLayoutModel.stockFieldErrorMessage)
            skuField?.textFieldInput?.setText(variantDetailInputLayoutModel.sku)
            if (variantDetailInputLayoutModel.isSkuFieldVisible) skuField?.show()
            else skuField?.hide()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.product_variant_detail_fields_layout
    }
}