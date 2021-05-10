package com.tkpd.atc_variant.views.viewholder.item

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.views.AtcVariantListener
import com.tkpd.atc_variant.views.viewholder.BaseAtcVariantItemViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 06/05/21
 */
class ItemVariantChipViewHolder(val view: View,
                                val listener: AtcVariantListener) : BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_chip_viewholder
    }

    val txtChipVariant = view.findViewById<Typography>(R.id.txt_chip_variant)
    val promoChipVariant = view.findViewById<NotificationUnify>(R.id.promo_chip_variant)
    val containerChipVariant = view.findViewById<LinearLayout>(R.id.container_chip_variant)


    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(view) {
        txtChipVariant.contentDescription = context.getString(R.string.atc_content_desc_txtChipVariant, element.variantName)
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
                containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_chip_disabled)
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
                view.isEnabled = false
                view.setOnClickListener(null)
            }
            VariantConstant.STATE_SELECTED -> {
                containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_chip_selected)
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                view.isEnabled = true
                view.setOnClickListener(null)
            }
            VariantConstant.STATE_UNSELECTED -> {
                containerChipVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_chip_unselected)
                txtChipVariant.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                view.isEnabled = true
            }
        }
    }
}