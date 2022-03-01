package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PayLaterCtaClick
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.pdpsimulation.paylater.helper.PayLaterHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.android.synthetic.main.paylater_partner_card_item.view.*

class PayLaterDetailViewHolder(itemView: View, private val interaction: PayLaterOptionInteraction) :
        AbstractViewHolder<Detail>(itemView) {

    companion object {
        val LAYOUT = R.layout.paylater_partner_card_item
        const val TYPE_FILLED = "filled"
    }

    private val context: Context = itemView.context

    override fun bind(element: Detail) {
        setPayLaterHeader(element)
        setUpRecommendation(element)
        setPayLaterImage(element)
        setUpFooter(element)
        setUpCta(element)
    }

    private fun setUpRecommendation(element: Detail) {
        if (element.recommendationDetail?.flag == true) {

            itemView.clDetailParent.background = MethodChecker.getDrawable(context, R.drawable.bg_paylater_recommended_gradient)
            itemView.clPartnerCard.background = MethodChecker.getDrawable(context, R.drawable.bg_paylater_card_border_recommendation)
            itemView.tvRecommendationTitle.visible()
            itemView.tvRecommendationTitle.text = element.recommendationDetail.text

        } else {

            itemView.clDetailParent.background = null
            itemView.clPartnerCard.background = null

            itemView.tvRecommendationTitle.gone()
        }
    }

    private fun setUpCta(element: Detail) {
        itemView.payLaterActionCta.text = element.cta.name
        itemView.payLaterActionCta.buttonVariant = if (element.cta.button_color == TYPE_FILLED) UnifyButton.Variant.FILLED else UnifyButton.Variant.GHOST
        itemView.payLaterActionCta.setOnClickListener {
            interaction.invokeAnalytics(getInstallmentInfoEvent(element, element.cta.android_url
                    ?: ""))
            interaction.onCtaClicked(element)
        }
    }

    private fun setUpFooter(element: Detail) {
        if (element.paylaterDisableDetail?.status == true) {
            itemView.payLaterActionCta.gone()
            itemView.llBenefits.gone()
            itemView.payLaterStatusTicker.visible()
            itemView.payLaterStatusTicker.setHtmlDescription(element.paylaterDisableDetail.header.orEmpty())
        } else {
            itemView.payLaterActionCta.visible()
            itemView.payLaterStatusTicker.gone()
            setPayLaterBenefits(element)
        }
    }

    private fun setPayLaterBenefits(element: Detail) {
        itemView.llBenefits.removeAllViews()
        itemView.llBenefits.visible()
        element.benefits?.forEach {
            val typography = Typography(itemView.context)
            typography.text = it
            typography.setType(Typography.BODY_3)
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
        if (!imageUrl.isNullOrEmpty())
            itemView.ivPaylaterPartner.setImageUrl(imageUrl)
    }

    private fun setPayLaterHeader(element: Detail) {
        itemView.apply {
            tvTitlePaymentPartner.text = element.gatewayDetail?.name
            tvInstallmentAmount.text = PayLaterHelper.convertPriceValueToIdrFormat(element.installment_per_month_ceil
                    ?: 0, false)
            if (element.tenure != 1)
                tvTenureMultiplier.text = context.getString(R.string.paylater_x_tenure, element.tenure)
            else {
                tvTenureMultiplier.gone()
                tvInstallmentAmount.text = element.optionalTenureHeader
            }
            if (element.subheader.isNullOrEmpty())
                tvInstallmentDescription.gone()
            else {
                tvInstallmentDescription.visible()
                tvInstallmentDescription.text = element.subheader.parseAsHtml()
            }
            partnerTenureInfo.setOnClickListener {
                if (element.installementDetails != null)
                    interaction.installementDetails(element)
            }
        }
    }

    private fun getInstallmentInfoEvent(detail: Detail, link: String = "") = PayLaterCtaClick().apply {
        tenureOption = detail.tenure ?: 0
        userStatus = detail.userState ?: ""
        payLaterPartnerName = detail.gatewayDetail?.name ?: ""
        emiAmount = detail.installment_per_month_ceil.toString()
        limit = detail.limit ?: ""
        redirectLink = link
        ctaWording = detail.cta.name ?: ""
        linkingStatus = detail.linkingStatus ?: ""
        action = PdpSimulationAnalytics.CLICK_CTA_PARTNER_CARD
    }
}
