package com.tokopedia.product.addedit.variant.presentation.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import kotlinx.android.synthetic.main.add_edit_product_variant_custom_input_layout.view.*

class CustomVariantUnitValueForm(context: Context?) : LinearLayout(context) {

    private var layoutPosition: Int? = null

    private var onCustomVariantUnitAddListener: OnCustomVariantUnitAddListener? = null

    interface OnCustomVariantUnitAddListener {
        fun onCustomVariantUnitAdded(customVariantUnitValue: UnitValue, layoutPosition: Int, variantId: Int)
    }

    init {
        context?.run {
            inflateVariantCustomInputLayout(this)
        }
    }

    fun setLayoutPosition(layoutPosition: Int) {
        this.layoutPosition = layoutPosition
    }

    fun setOnButtonSaveClickListener(onCustomVariantUnitAddListener: OnCustomVariantUnitAddListener) {
        this.onCustomVariantUnitAddListener = onCustomVariantUnitAddListener
    }

    private fun inflateVariantCustomInputLayout(context: Context) {
        View.inflate(context, R.layout.add_edit_product_variant_custom_input_layout, this)
    }

    fun setupVariantCustomInputLayout(layoutPosition: Int?, variantId: Int) {
        buttonSave.setOnClickListener {
            layoutPosition?.let {
                val customVariantUnitName = textFieldUnifyCustomValue.getText()
                val customVariantUnitValue = UnitValue(value = customVariantUnitName)
                onCustomVariantUnitAddListener?.onCustomVariantUnitAdded(customVariantUnitValue, layoutPosition, variantId)
            }
        }
    }
}