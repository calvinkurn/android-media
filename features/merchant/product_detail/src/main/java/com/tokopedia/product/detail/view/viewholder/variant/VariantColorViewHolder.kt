package com.tokopedia.product.detail.view.viewholder.variant

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.variant.VariantOptionWithAttribute
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.ProductVariantListener
import kotlinx.android.synthetic.main.item_variant_color_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-27
 */

class VariantColorViewHolder(val view: View,
                             val listener: ProductVariantListener) : BaseVariantViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_color_view_holder
    }

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(view) {
        variantColorItem.setInnerColor(element.variantHex)
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(view) {
        when (element.currentState) {
            ProductDetailConstant.STATE_EMPTY -> {
                variantColorItem.setColorNotAvailable()
                view.isEnabled = false
            }
            ProductDetailConstant.STATE_SELECTED -> {
                variantColorItem.setColorSelected()
                view.isEnabled = false
            }
            ProductDetailConstant.STATE_UNSELECTED -> {
                variantColorItem.setColorAvailable()
                view.isEnabled = true
            }
        }

        view.setOnClickListener {
            listener.onVariantClicked(element)
        }
    }
}