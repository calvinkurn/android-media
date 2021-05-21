package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitUiModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.view.adapter.GradeBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.adapter.RegistrationTermAdapter
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetUpgradePmProUiModel
import kotlinx.android.synthetic.main.widget_upgrade_pm_pro.view.*

/**
 * Created By @ilhamsuaib on 10/05/21
 */

class UpgradePmProWidget(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<WidgetUpgradePmProUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_upgrade_pm_pro
    }

    private val termAdapter by lazy { RegistrationTermAdapter() }

    override fun bind(element: WidgetUpgradePmProUiModel) {
        setupView(element)
        showTermsList(element.registrationTerms)
        showGeneralBenefits(element.generalBenefits)
        setupUpgradeCta()
    }

    private fun setupUpgradeCta() = with(itemView) {
        ctaPmUpgradeLearnMore.setOnClickListener {
            RouteManager.route(context, Constant.Url.POWER_MERCHANT_EDU)
        }
    }

    private fun showGeneralBenefits(benefits: List<PMGradeBenefitUiModel>) = with(itemView.rvPmUpgradeBenefits) {
        val benefitAdapter = GradeBenefitAdapter(benefits)
        layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }
        adapter = benefitAdapter
    }

    private fun showTermsList(terms: List<RegistrationTermUiModel>) {
        with(itemView.rvPmUpgradeTerms) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = termAdapter
        }
        termAdapter.setItems(terms)
        termAdapter.notifyDataSetChanged()
    }

    private fun setupView(element: WidgetUpgradePmProUiModel) = with(itemView) {
        imgPmUpgradeBackdrop.loadImageDrawable(R.drawable.bg_pm_upgrade_pm_pro)
        icPmProBadge.loadImageDrawable(R.drawable.ic_pm_badge_pm_pro_filled)
        viewPmUpgradeTermSection.setEligibility(element.shopInfo.isEligiblePmPro)

        if (element.shopInfo.isEligiblePmPro) {
            tvPmUpgradeBenefitDescription.text = getString(R.string.pm_pro_upgrade_eligible_description, Constant.POWER_MERCHANT_PRO_CHARGING).parseAsHtml()
            tvPmProTncDescription.visible()
            btnPmProUpgrade.visible()
            showTncMessage()
            setExpandedChanged(false)
        } else {
            val threshold = element.shopInfo.shopScorePmProThreshold
            val charging = "1,5%."
            tvPmUpgradeBenefitDescription.text = context.resources.getString(R.string.pm_pro_upgrade_not_eligible_description, threshold, charging).parseAsHtml()
            tvPmProTncDescription.gone()
            btnPmProUpgrade.gone()
            setExpandedChanged(true)
        }

        viewPmUpgradeTermSection.setOnSectionHeaderClickListener {
            setExpandedChanged(it)
        }

        btnPmProUpgrade.isLoading = false
        btnPmProUpgrade.setOnClickListener {
            btnPmProUpgrade.isLoading = true
            listener.onUpgradePmProClickListener(adapterPosition)
        }
    }

    private fun showTncMessage() = with(itemView) {
        val clickableText = "S&K"
        val ctaTextColor = com.tokopedia.unifycomponents.R.color.Unify_G500
        val termDescription = PowerMerchantSpannableUtil.createSpannableString(
                text = context.getString(R.string.pm_pro_upgrade_tnc_description).parseAsHtml(),
                highlightText = clickableText,
                colorId = context.getResColor(ctaTextColor),
                isBold = true
        ) {
            RouteManager.route(itemView.context, Constant.Url.POWER_MERCHANT_TERMS_AND_CONDITION)
        }
        tvPmProTncDescription.movementMethod = LinkMovementMethod.getInstance()
        tvPmProTncDescription.text = termDescription
    }

    private fun setExpandedChanged(isExpanded: Boolean) = with(itemView) {
        if (isExpanded) {
            viewPmUpgradeTermSection.setExpanded(true)
            rvPmUpgradeTerms.visible()
            horLinePmUpgrade1.visible()
        } else {
            viewPmUpgradeTermSection.setExpanded(false)
            rvPmUpgradeTerms.gone()
            horLinePmUpgrade1.gone()
        }
    }

    interface Listener {
        fun onUpgradePmProClickListener(adapterPosition: Int)
    }
}