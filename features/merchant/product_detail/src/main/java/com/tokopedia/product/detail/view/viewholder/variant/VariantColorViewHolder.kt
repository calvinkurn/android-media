package com.tokopedia.product.detail.view.viewholder.variant

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.variant.VariantOptionWithAttribute
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_variant_color_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-27
 */

class VariantColorViewHolder(val view: View,
                             val listener: DynamicProductDetailListener) : BaseVariantViewHolder<VariantOptionWithAttribute>(view) {

    companion object{
        val LAYOUT = R.layout.item_variant_color_view_holder
    }

    override fun bind(element: VariantOptionWithAttribute) = with(view) {

        variantColorItem.setInnerColor(element.variantHex)

        when (element.currentState) {
            ProductDetailConstant.STATE_EMPTY -> {
                variantColorItem.setColorNotAvailable()
            }
            ProductDetailConstant.STATE_SELECTED -> {
                variantColorItem.setColorSelected()
            }
            ProductDetailConstant.STATE_UNSELECTED -> {
                variantColorItem.setColorAvailable()
            }
        }

        view.setOnClickListener {
            //TO DO
        }
    }
}