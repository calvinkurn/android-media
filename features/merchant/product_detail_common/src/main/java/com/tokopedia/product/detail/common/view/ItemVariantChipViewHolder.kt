package com.tokopedia.product.detail.common.view

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

/**
 * Created by Yehezkiel on 06/05/21
 */
class ItemVariantChipViewHolder(val view: View,
                                val listener: AtcVariantListener) : BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_chip_viewholder
    }

    private val txtChipVariant = view.findViewById<Typography>(R.id.txt_chip_variant)
    private val promoChipVariant = view.findViewById<NotificationUnify>(R.id.promo_chip_variant)
    private val containerChipVariant = view.findViewById<LinearLayout>(R.id.container_chip_variant)

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(view) {
        txtChipVariant.contentDescription = context.getString(R.string.atc_content_desc_txtChipVariant, element.variantName)
        txtChipVariant.text = element.variantName
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(view) {
        setViewListener(element, VariantConstant.IGNORE_STATE)
        val shouldShowFlashSale = element.currentState != VariantConstant.STATE_EMPTY
                && element.currentState != VariantConstant.STATE_SELECTED_EMPTY
        renderFlashSale(shouldShowFlashSale, element.flashSale)

        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_chip_disabled)
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            }
            VariantConstant.STATE_SELECTED, VariantConstant.STATE_SELECTED_EMPTY -> {
                if (context.isDarkMode()) {
                    containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_chip_selected_dark)
                } else {
                    containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_chip_selected_light)
                }
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                setViewListener(element, element.currentState)
            }
            VariantConstant.STATE_UNSELECTED -> {
                containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_chip_unselected)
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            }
        }
    }

    private fun renderFlashSale(shouldRender: Boolean, isCampaignActive: Boolean) {
        val chipMargin = txtChipVariant.layoutParams as ViewGroup.MarginLayoutParams
        if (shouldRender && isCampaignActive) {
            txtChipVariant.setMargin(chipMargin.leftMargin, chipMargin.topMargin, 6.toPx(), chipMargin.bottomMargin)
            promoChipVariant.show()
        } else {
            txtChipVariant.setMargin(chipMargin.leftMargin, chipMargin.topMargin, 12.toPx(), chipMargin.bottomMargin)
            promoChipVariant.hide()
        }
    }

    private fun setViewListener(element: VariantOptionWithAttribute, state: Int) {
        view.setOnClickListener {
            listener.onVariantClicked(element, state)
        }
    }
}