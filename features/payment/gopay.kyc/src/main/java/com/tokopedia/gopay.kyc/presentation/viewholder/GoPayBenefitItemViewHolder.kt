package com.tokopedia.gopay.kyc.presentation.viewholder

import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.domain.data.GoPayPlusBenefit
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class GoPayBenefitItemViewHolder(itemView: View) : AbstractViewHolder<GoPayPlusBenefit>(itemView) {

    companion object {
        val LAYOUT_ID = R.layout.gopay_kyc_general_item_layout
    }

    private val goPayItemBenefitTitleTextView =
        itemView.findViewById<Typography>(R.id.goPayBenefitTitle)
    private val goPayItemBenefitTextView = itemView.findViewById<Typography>(R.id.goPayBenefit)
    private val goPayItemBenefitPlusTextView =
        itemView.findViewById<Typography>(R.id.goPayPlusBenefit)

    private val goPayPlusItemBenefitBlueTickImageView =
        itemView.findViewById<ImageUnify>(R.id.goPayPlusBenefitImage)
    private val goPayItemBenefitCancelImageView =
        itemView.findViewById<ImageUnify>(R.id.goPayBenefitCancelImage)

    private val goPayBenefitParentView =
        itemView.findViewById<ConstraintLayout>(R.id.goKycBenefitParentView)
    private val goPayPlusItemBenefitLL =
        itemView.findViewById<LinearLayoutCompat>(R.id.goPayPlusBenefitLL)

    private val divider = itemView.findViewById<View>(R.id.divider)
    private val verticalLineView = itemView.findViewById<View>(R.id.verticalLineView)


    override fun bind(model: GoPayPlusBenefit) {

        if (model.isLastItem) bindLastRow()

        if (model.benefitTitle.isNotEmpty())
            goPayItemBenefitTitleTextView.text = model.benefitTitle

        setupGopayColumn(model)
        setupGopayPlusColumn(model)
    }

    private fun setupGopayColumn(model: GoPayPlusBenefit) {
        if (model.benefitGopay.isNotEmpty())
            goPayItemBenefitTextView.text = model.benefitGopay
        else {
            goPayItemBenefitCancelImageView.visibility = View.VISIBLE
            goPayItemBenefitTextView.visibility = View.GONE
        }
    }

    private fun setupGopayPlusColumn(model: GoPayPlusBenefit) {
        if (model.benefitGopayPlus.isNotEmpty())
            goPayItemBenefitPlusTextView.text =
                model.benefitGopayPlus
        else {
            goPayPlusItemBenefitBlueTickImageView.visibility = View.VISIBLE
            goPayItemBenefitPlusTextView.visibility = View.GONE
        }
    }

    private fun bindLastRow() {
        goPayPlusItemBenefitLL.setBackgroundResource(R.drawable.bg_gopay_benefit_blue_table_footer)
        goPayBenefitParentView.setBackgroundResource(R.drawable.bg_gopay_benefit_table_footer)
        divider.hide()
        verticalLineView.hide()
    }
}