package com.tokopedia.variant_common.view.holder

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.variant_common.R
import com.tokopedia.variant_common.constant.VariantConstant
import com.tokopedia.variant_common.model.VariantOptionWithAttribute
import com.tokopedia.variant_common.view.ProductVariantListener
import kotlinx.android.synthetic.main.item_variant_chip_view_holder.view.*

/**
 * Created by Yehezkiel on 08/03/20
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
        view.setOnClickListener {
            listener.onVariantClicked(element)
        }

        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                txtChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_disabled)
                txtChipVariant.setTextColor(MethodChecker.getColor(context,R.color.Neutral_N700_32))
                view.isEnabled = false
                view.setOnClickListener(null)
            }
            VariantConstant.STATE_SELECTED -> {
                txtChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_selected)
                txtChipVariant.setTextColor(MethodChecker.getColor(context,R.color.g_500))
                view.isEnabled = true
                view.setOnClickListener(null)
            }
            VariantConstant.STATE_UNSELECTED -> {
                txtChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_unselected)
                txtChipVariant.setTextColor(MethodChecker.getColor(context,R.color.g_500))
                view.isEnabled = true
            }
        }
    }
} 