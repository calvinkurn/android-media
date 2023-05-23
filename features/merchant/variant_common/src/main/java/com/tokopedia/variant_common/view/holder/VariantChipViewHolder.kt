package com.tokopedia.variant_common.view.holder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.variant_common.R
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.variant_common.databinding.ItemVariantChipViewHolderBinding

/**
 * Created by Yehezkiel on 08/03/20
 */
class VariantChipViewHolder(val view: View,
                            val listener: AtcVariantListener) : BaseVariantViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_chip_view_holder
    }

    private val binding = ItemVariantChipViewHolderBinding.bind(view)
    private val context = binding.root.context

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(binding) {
        txtChipVariant.contentDescription = context.getString(R.string.content_desc_txtChipVariant, element.variantName)
        txtChipVariant.text = element.variantName
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(binding) {
        view.setOnClickListener {
            listener.onVariantClicked(element)
        }
        val chipMargin = txtChipVariant.layoutParams as ViewGroup.MarginLayoutParams

        if (element.flashSale) {
            txtChipVariant.setMargin(chipMargin.leftMargin, chipMargin.topMargin, 6.toPx(), chipMargin.bottomMargin)
            promoChipVariant.show()
        } else {
            txtChipVariant.setMargin(chipMargin.leftMargin, chipMargin.topMargin, 12.toPx(), chipMargin.bottomMargin)
            promoChipVariant.hide()
        }

        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_disabled)
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
                view.isEnabled = false
                view.setOnClickListener(null)
            }
            VariantConstant.STATE_SELECTED -> {
                if (context.isDarkMode()) {
                    containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_selected_dark)
                } else {
                    containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_selected_light)
                }
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                view.isEnabled = true
                view.setOnClickListener(null)
            }
            VariantConstant.STATE_UNSELECTED -> {
                containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_unselected)
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
                view.isEnabled = true
            }
        }
    }
} 
