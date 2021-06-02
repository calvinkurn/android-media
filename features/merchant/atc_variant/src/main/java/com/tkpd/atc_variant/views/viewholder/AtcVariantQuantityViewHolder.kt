package com.tkpd.atc_variant.views.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.data.uidata.VariantQuantityDataModel
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 11/05/21
 */
class AtcVariantQuantityViewHolder(private val view: View,
                                   private val listener: AtcVariantListener) : AbstractViewHolder<VariantQuantityDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_quantity_viewholder
    }

    private val quantityEditor = view.findViewById<QuantityEditorUnify>(R.id.qty_variant_stock)
    private val txtMinOrder = view.findViewById<Typography>(R.id.txt_desc_quantity)
    private val container = view.findViewById<ConstraintLayout>(R.id.container_atc_variant_qty_editor)

    private fun showContainer() {
        container?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private fun hideContainer() {
        container?.layoutParams?.height = 0
    }

    override fun bind(element: VariantQuantityDataModel) {
        if (element.shouldShowView) {
            quantityEditor.minValue = element.minOrder
            quantityEditor.setValue(element.quantity)
            quantityEditor.setAddClickListener {
                element.quantity = quantityEditor.getValue()
                listener.onQuantityUpdate(quantityEditor.getValue(), element.productId)
            }

            txtMinOrder.text = view.context.getString(R.string.atc_variant_min_order_builder, element.minOrder)

            quantityEditor.setSubstractListener {
                element.quantity = quantityEditor.getValue()
                listener.onQuantityUpdate(quantityEditor.getValue(), element.productId)
            }
            showContainer()
        } else {
            hideContainer()
        }
    }
}