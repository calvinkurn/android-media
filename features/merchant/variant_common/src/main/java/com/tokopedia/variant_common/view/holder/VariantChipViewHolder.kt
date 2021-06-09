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
import kotlinx.android.synthetic.main.item_variant_chip_view_holder.view.*
import android.view.ViewGroup
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

/**
 * Created by Yehezkiel on 08/03/20
 */
class VariantChipViewHolder(val view: View,
                            val listener: AtcVariantListener) : BaseVariantViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_chip_view_holder
    }

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(view) {
        txtChipVariant.contentDescription = context.getString(R.string.content_desc_txtChipVariant, element.variantName)
        txtChipVariant.text = element.variantName
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(view) {
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
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
                view.isEnabled = false
                view.setOnClickListener(null)
            }
            VariantConstant.STATE_SELECTED -> {
                if (context.isDarkMode()) {
                    containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_selected_dark)
                } else {
                    containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_selected_light)
                }
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                view.isEnabled = true
                view.setOnClickListener(null)
            }
            VariantConstant.STATE_UNSELECTED -> {
                containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_chip_unselected)
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                view.isEnabled = true
            }
        }
    }
} 