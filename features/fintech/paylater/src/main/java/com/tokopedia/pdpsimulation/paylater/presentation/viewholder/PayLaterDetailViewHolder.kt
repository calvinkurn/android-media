package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PayLaterCtaClick
import com.tokopedia.pdpsimulation.common.analytics.PayLaterTickerCtaClick
import com.tokopedia.pdpsimulation.common.analytics.PayLaterTickerImpression
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.common.utils.Util
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.pdpsimulation.paylater.helper.PayLaterHelper
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.android.synthetic.main.paylater_partner_card_item.view.*

class PayLaterDetailViewHolder(itemView: View, private val interaction: PayLaterOptionInteraction) :
    AbstractViewHolder<Detail>(itemView) {

    companion object {
        val LAYOUT = R.layout.paylater_partner_card_item
        const val TYPE_FILLED = "filled"
        private const val TICKER_TYPE_GENERAL = "general"
        private const val TICKER_TYPE_WARNING = "warning"
        private const val TICKER_TYPE_DANGER = "danger"
        private const val TICKER_TYPE_INFO = "info"
    }

    private val context: Context = itemView.context

    override fun bind(element: Detail) {
        setPayLaterHeader(element)
        setUpRecommendation(element)
        setPayLaterImage(element)
        setUpTicker(element)
        setUpFooter(element)
        setUpCta(element)
    }

    private fun setUpRecommendation(element: Detail) {
        if (element.recommendationDetail?.flag == true) {
            itemView.clDetailParent.background = if (itemView.context.isDarkMode()) {
                MethodChecker.getDrawable(context, R.drawable.bg_paylater_recommended_dark_gradient)
            } else {
                MethodChecker.getDrawable(context, R.drawable.bg_paylater_recommended_light_gradient)
            }
            itemView.clPartnerCard.background = MethodChecker.getDrawable(
                context,
                R.drawable.bg_paylater_card_border_recommendation
            )
            itemView.tvRecommendationTitle.visible()
            itemView.tvRecommendationTitle.text = element.recommendationDetail.text
            itemView.payLaterPartnerCard.cardType = CardUnify.TYPE_SHADOW
        } else {
            itemView.clDetailParent.background = null
            itemView.clPartnerCard.background = null
            itemView.payLaterPartnerCard.cardType = CardUnify.TYPE_BORDER

            itemView.tvRecommendationTitle.gone()
        }
    }

    private fun setUpCta(element: Detail) {
        itemView.payLaterActionCta.text = element.cta.name
        itemView.payLaterActionCta.buttonVariant =
            if (element.cta.button_color == TYPE_FILLED) UnifyButton.Variant.FILLED else UnifyButton.Variant.GHOST
        itemView.payLaterActionCta.setOnClickListener {
            interaction.invokeAnalytics(
                getInstallmentInfoEvent(
                    element,
                    element.cta.android_url
                        ?: ""
                )
            )
            interaction.onCtaClicked(element)
        }
    }

    private fun setUpFooter(element: Detail) {
        if (element.paylaterDisableDetail?.status == true || element.ticker.isShown) {
            itemView.payLaterActionCta.gone()
            itemView.llBenefits.gone()
        } else {
            itemView.payLaterActionCta.visible()
            setPayLaterBenefits(element)
        }

        if (element.paylaterDisableDetail?.status == true) {
            itemView.disableTitleDetail()
        } else {
            itemView.enableTitleDetail()
        }
    }

    private fun setUpTicker(element: Detail) {
        itemView.payLaterStatusTicker.shouldShowWithAction(element.ticker.isShown) {
            val urlList = HtmlLinkHelper(context, element.ticker.content).urlList

            itemView.payLaterStatusTicker.setHtmlDescription(element.ticker.content)
            itemView.payLaterStatusTicker.tickerType = when (element.ticker.type) {
                TICKER_TYPE_GENERAL -> Ticker.TYPE_INFORMATION
                TICKER_TYPE_DANGER -> Ticker.TYPE_ERROR
                TICKER_TYPE_INFO -> Ticker.TYPE_ANNOUNCEMENT
                TICKER_TYPE_WARNING -> Ticker.TYPE_WARNING
                else -> Ticker.TYPE_INFORMATION
            }
            itemView.payLaterStatusTicker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    val urlText = urlList.firstOrNull { it.linkUrl == linkUrl }?.linkText ?: ""

                    interaction.invokeAnalytics(
                        getGPLTickerCtaCLickEvent(element, urlText)
                    )
                    RouteManager.route(
                        itemView.context,
                        linkUrl.toString()
                    )
                }

                override fun onDismiss() {}
            })

            interaction.invokeAnalytics(
                getGPLTickerImpressionEvent(element)
            )
        }
    }

    private fun setPayLaterBenefits(element: Detail) {
        itemView.llBenefits.removeAllViews()
        itemView.llBenefits.visible()
        element.benefits?.forEach {
            val typography = Typography(itemView.context)
            typography.text = it
            typography.setType(Typography.DISPLAY_3)
            typography.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
            )
            itemView.llBenefits.addView(typography)
        }
    }

    private fun setPayLaterImage(element: Detail) {
        val imageUrl = if (itemView.context.isDarkMode()) {
            element.gatewayDetail?.img_dark_url
        } else {
            element.gatewayDetail?.img_light_url
        }
        if (!imageUrl.isNullOrEmpty()) {
            itemView.ivPaylaterPartner.setImageUrl(imageUrl)
        }
    }

    private fun setPayLaterHeader(element: Detail) {
        itemView.apply {
            tvTitlePaymentPartner.text = element.gatewayDetail?.name
            tvInstallmentAmount.text = Util.getTextRBPRemoteConfig(
                context,
                PayLaterHelper.convertPriceValueToIdrFormat(
                    element.installment_per_month_ceil
                        ?: 0,
                    false
                ),
                element.priceSection.installmentPerMonth
            )

            if (Util.isRBPOn(context)) {
                tvTenureMultiplier.shouldShowWithAction(element.priceSection.tenure != Int.ONE) {
                    tvTenureMultiplier.text = context.getString(R.string.paylater_x_tenure, element.priceSection.tenure)
                }

                tvPrefixInstallment.shouldShowWithAction(element.priceSection.prefix.isNotEmpty()) {
                    tvPrefixInstallment.text = element.priceSection.prefix
                }

                tvOriginalInstallment.shouldShowWithAction(element.priceSection.originalPerMonth.isNotEmpty()) {
                    tvOriginalInstallment.paintFlags = tvOriginalInstallment.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvOriginalInstallment.text = element.priceSection.originalPerMonth
                }
            } else {
                if (element.tenure != Int.ONE) {
                    tvTenureMultiplier.visible()
                    tvTenureMultiplier.text =
                        context.getString(R.string.paylater_x_tenure, element.tenure)
                } else {
                    tvTenureMultiplier.gone()
                    tvInstallmentAmount.text = element.optionalTenureHeader
                }
            }

            if (element.subheader.isNullOrEmpty()) {
                tvInstallmentDescription.gone()
            } else {
                tvInstallmentDescription.visible()
                tvInstallmentDescription.text = element.subheader.parseAsHtml()
            }
            partnerTenureInfo.setOnClickListener {
                if (element.installementDetails != null) {
                    interaction.installementDetails(element)
                }
            }
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun getInstallmentInfoEvent(detail: Detail, link: String = "") =
        PayLaterCtaClick().apply {
            tenureOption = detail.tenure ?: 0
            userStatus = detail.userState ?: ""
            payLaterPartnerName = detail.gatewayDetail?.gatewayCode ?: ""
            emiAmount = detail.installment_per_month_ceil.toString()
            limit = detail.limit ?: ""
            redirectLink = link
            ctaWording = detail.cta.name ?: ""
            linkingStatus = detail.linkingStatus ?: ""
            action = PdpSimulationAnalytics.CLICK_CTA_PARTNER_CARD
            promoName = detail.promoName.orEmpty()
            previousRate = detail.previousRate
            newRate = detail.newRate
        }

    @SuppressLint("PII Data Exposure")
    private fun getGPLTickerCtaCLickEvent(detail: Detail, cta: String) =
        PayLaterTickerCtaClick().apply {
            tenureOption = detail.tenure ?: 0
            userStatus = detail.userState ?: ""
            payLaterPartnerName = detail.gatewayDetail?.gatewayCode ?: ""
            linkingStatus = detail.linkingStatus ?: ""
            action = PdpSimulationAnalytics.CLICK_PAYLATER_GPL_TICKER
            promoName = detail.promoName.orEmpty()
            tickerType = detail.ticker.type
            tickerCta = cta
        }

    @SuppressLint("PII Data Exposure")
    private fun getGPLTickerImpressionEvent(detail: Detail) =
        PayLaterTickerImpression().apply {
            tenureOption = detail.tenure ?: 0
            userStatus = detail.userState ?: ""
            payLaterPartnerName = detail.gatewayDetail?.gatewayCode ?: ""
            linkingStatus = detail.linkingStatus ?: ""
            action = PdpSimulationAnalytics.IMPRESSION_PAYLATER_GPL_TICKER
            promoName = detail.promoName.orEmpty()
            tickerType = detail.ticker.type
        }
}

private fun View.disableTitleDetail() {
    this.tvTitlePaymentPartner.setTextColor(
        ContextCompat.getColor(
            this.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN400
        )
    )
    this.tvInstallmentAmount.setTextColor(
        ContextCompat.getColor(
            this.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN400
        )
    )
    this.tvTenureMultiplier.setTextColor(
        ContextCompat.getColor(
            this.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN400
        )
    )
    this.partnerTenureInfo.isEnabled = false
}
private fun View.enableTitleDetail() {
    this.tvTitlePaymentPartner.setTextColor(
        ContextCompat.getColor(
            this.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        )
    )
    this.tvInstallmentAmount.setTextColor(
        ContextCompat.getColor(
            this.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        )
    )
    this.tvTenureMultiplier.setTextColor(
        ContextCompat.getColor(
            this.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN600
        )
    )
    this.partnerTenureInfo.isEnabled = true
}
