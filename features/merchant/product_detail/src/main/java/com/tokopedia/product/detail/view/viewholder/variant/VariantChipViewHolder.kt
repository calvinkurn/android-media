package com.tokopedia.product.detail.view.viewholder.variant

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.variant.VariantOptionWithAttribute
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.ProductVariantListener
import kotlinx.android.synthetic.main.item_variant_chip_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-27
 */
class VariantChipViewHolder(val view: View,
                            val listener: ProductVariantListener) : BaseVariantViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_chip_view_holder
    }

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(view) {
        txtChipVariant.text = element.variantName
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(view) {
        when (element.currentState) {
            ProductDetailConstant.STATE_EMPTY -> {
                txtChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_disabled)
                view.isEnabled = false
            }
            ProductDetailConstant.STATE_SELECTED -> {
                txtChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_selected)
                view.isEnabled = true
            }
            ProductDetailConstant.STATE_UNSELECTED -> {
                txtChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_unselected)
                view.isEnabled = true
            }
        }

        view.setOnClickListener {
            listener.onVariantClicked(element)
        }
    }
}