package com.tokopedia.pdpsimulation.common.presentation.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.android.synthetic.main.paylater_partner_card_item.view.*

class PayLaterDetailViewHolder(itemView: View, private val interaction: PayLaterOptionInteraction) :
    AbstractViewHolder<Detail>(itemView) {

    companion object {
        val LAYOUT = R.layout.paylater_partner_card_item
    }

    override fun bind(element: Detail) {
        setPayLaterHeader(element)
        setUpRecommendation(element)
        setPayLaterImage(element)
        setPayLaterBenefits(element)
        setUpCta(element)
    }

    private fun setUpRecommendation(element: Detail) {
        if (element.recommendationDetail?.flag == true) {
            itemView.recomrecommendationTitlemendationTitle.visible()
            itemView.recomrecommendationTitlemendationTitle.text = element.recommendationDetail.text
        } else {
            itemView.recomrecommendationTitlemendationTitle.gone()
        }
    }

    private fun setUpCta(element: Detail) {
        itemView.payLaterActionCta.text = element.cta.name
        itemView.payLaterActionCta.setOnClickListener {
            interaction.onCtaClicked(element.cta)
        }
    }

    private fun setPayLaterBenefits(element: Detail) {
        element.benefits?.forEach {
            val typography = Typography(itemView.context)
            typography.text = it
            typography.fontType = Typography.BODY_3
            typography.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
            itemView.llBenefits.addView(typography)
        }

    }

    private fun setPayLaterImage(element: Detail) {
        val imageUrl = if (itemView.context.isDarkMode()) {
            element.gatewayDetail?.img_dark_url
        } else {
            element.gatewayDetail?.img_light_url
        }
        if(!imageUrl.isNullOrEmpty())
            itemView.ivPaylaterPartner.setImageUrl(imageUrl)
    }

    private fun setPayLaterHeader(element: Detail) {
        itemView.apply {
            tvTitlePaymentPartner.text = element.gatewayDetail?.name
            tvInstallmentAmount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.installment_per_month?: 0.0, false)
            tvTenureMultiplier.text = "x${element.tenure}"
            tvInstallmentDescription.text = element.subheader?.parseAsHtml()
            partnerTenureInfo.setOnClickListener {
                if (element.installementDetails != null)
                interaction.installementDetails(element.installementDetails)
            }
        }
    }
}
