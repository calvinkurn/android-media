package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PATTERN_DATE_TEXT
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.utils.GoldMerchantUtil
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.databinding.WidgetUpgradePmProBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.PMProBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.adapter.RegistrationTermAdapter
import com.tokopedia.power_merchant.subscribe.view.model.PMProBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetUpgradePmProUiModel
import com.tokopedia.utils.view.binding.viewBinding
import java.text.ParseException
import java.util.*

/**
 * Created By @ilhamsuaib on 10/05/21
 */

class UpgradePmProWidget(
    itemView: View?,
    private val listener: Listener,
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetUpgradePmProUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_upgrade_pm_pro
        private const val THIRTY_DAYS = 30
    }

    private val binding: WidgetUpgradePmProBinding? by viewBinding()
    private val termAdapter by lazy { RegistrationTermAdapter() }

    override fun bind(element: WidgetUpgradePmProUiModel) {
        setupView(element)
        showTermsList(element.registrationTerms)
        showGeneralBenefits(element.generalBenefits)
        setupUpgradeCta(element)
        setupNewSeller(element)
    }

    private fun setupNewSeller(element: WidgetUpgradePmProUiModel) {
        val shopInfo = element.shopInfo
        binding?.run {
            if (shopInfo.isNewSeller) {
                if (shopInfo.is30DaysFirstMonday) {
                    if (shopInfo.isEligiblePmPro) {
                        tvPmUpgradePmProTitle.text =
                            getString(R.string.pm_title_new_seller_eligible_after_30_days)
                        tvPmUpgradePmProDesc.hide()
                        tvPmUpgradeBenefitDescription.text =
                            MethodChecker.fromHtml(
                                getString(R.string.pm_desc_new_seller_eligible_benefit_package)
                            )
                    } else {
                        tvPmUpgradePmProTitle.text =
                            getString(R.string.pm_title_new_seller_not_eligible_after_30_days)
                        tvPmUpgradePmProDesc.hide()
                        tvPmUpgradeBenefitDescription.text =
                            MethodChecker.fromHtml(
                                getString(R.string.pm_desc_new_seller_not_eligible_benefit_package)
                            )
                        hidePmProUpgradeSection()
                    }
                } else {
                    tvPmUpgradePmProTitle.text =
                        getString(R.string.pm_title_new_seller_before_30_days)
                    tvPmUpgradePmProDesc.show()
                    tvPmUpgradePmProDesc.text = getString(
                        R.string.pm_desc_new_seller_before_30_days,
                        getDaysDate(shopInfo.shopCreatedDate)
                    )
                    hidePmProUpgradeSection()
                }
            } else {
                tvPmUpgradePmProDesc.hide()
            }
        }
    }

    private fun getDaysDate(shopCreatedDate: String): String {
        return try {
            return GoldMerchantUtil.getNNextDaysBasedOnShopScoreCalculation(shopCreatedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }


    private fun hidePmProUpgradeSection() {
        binding?.run {
            tvPmProTncDescription.hide()
            btnPmProUpgrade.hide()
        }
    }

    private fun setupUpgradeCta(element: WidgetUpgradePmProUiModel) = binding?.run {
        val shopScore = element.shopInfo.shopScore.toString()
        powerMerchantTracking.sendEventImpressUpsellPmPro(shopScore)
        ctaPmUpgradeLearnMore.setOnClickListener {
            powerMerchantTracking.sendEventClickCTAPmUpgradeLearnMore(shopScore)
            RouteManager.route(root.context, Constant.Url.POWER_MERCHANT_PRO_EDU)
        }
    }

    private fun showGeneralBenefits(benefits: List<PMProBenefitUiModel>) =
        binding?.rvPmUpgradeBenefits?.run {
            val benefitAdapter = PMProBenefitAdapter(benefits)
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = benefitAdapter
        }

    private fun showTermsList(terms: List<RegistrationTermUiModel>) {
        binding?.rvPmUpgradeTerms?.run {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = termAdapter
        }
        termAdapter.setItems(terms)
        termAdapter.notifyDataSetChanged()
    }

    private fun setupView(element: WidgetUpgradePmProUiModel) = binding?.run {
        imgPmUpgradeBackdrop.loadImage(Constant.Image.PM_BG_UPSALE_PM_PRO)
        icPmProBadge.loadImage(PMConstant.Images.PM_PRO_BADGE)
        val isShowTermStatus = element.registrationTerms.all { it.isChecked }
        viewPmUpgradeTermSection.setTermStatus(isShowTermStatus)

        if (element.shopInfo.isNewSeller) {
            if (element.shopInfo.is30DaysFirstMonday) {
                if (element.shopInfo.isEligiblePmPro) {
                    tvPmUpgradeBenefitDescription.text = getString(
                        R.string.pm_pro_upgrade_eligible_true_description
                    ).parseAsHtml()
                    tvPmProTncDescription.visible()
                    btnPmProUpgrade.visible()
                    showTncMessage()
                    setExpandedChanged(false)
                } else {
                    tvPmUpgradeBenefitDescription.text = getString(
                        R.string.pm_pro_upgrade_eligible_false_description
                    ).parseAsHtml()
                    tvPmProTncDescription.gone()
                    btnPmProUpgrade.gone()
                    setExpandedChanged(true)
                }
            } else {
                tvPmUpgradeBenefitDescription.text = getString(
                    R.string.pm_pro_upgrade_30_first_monday_false_description
                ).parseAsHtml()
                tvPmProTncDescription.gone()
                btnPmProUpgrade.gone()
                setExpandedChanged(true)
            }
        } else {
            if (element.shopInfo.isEligiblePmPro) {
                tvPmUpgradeBenefitDescription.text = getString(
                    R.string.pm_pro_upgrade_eligible_description,
                    Constant.POWER_MERCHANT_PRO_CHARGING
                ).parseAsHtml()
                tvPmProTncDescription.visible()
                btnPmProUpgrade.visible()
                showTncMessage()
                setExpandedChanged(false)
            } else {
                val threshold = element.shopInfo.shopScorePmProThreshold
                tvPmUpgradeBenefitDescription.text = root.context.resources.getString(
                    R.string.pm_pro_upgrade_not_eligible_description,
                    threshold,
                    Constant.POWER_MERCHANT_PRO_CHARGING
                ).parseAsHtml()
                tvPmProTncDescription.gone()
                btnPmProUpgrade.gone()
                setExpandedChanged(true)
            }
        }

        viewPmUpgradeTermSection.setOnSectionHeaderClickListener {
            setExpandedChanged(it)
        }

        btnPmProUpgrade.isLoading = false
        btnPmProUpgrade.setOnClickListener {
            btnPmProUpgrade.isLoading = true
            listener.onUpgradePmProClickListener(adapterPosition)
        }

        tvPmUpgradePmProTitle.text = if (element.shopInfo.isEligiblePmPro) {
            root.context.getString(R.string.pm_get_exclusive_benefit_of_being_pm_pro_eligible)
        } else {
            root.context.getString(R.string.pm_get_exclusive_benefit_of_being_pm_pro)
        }
    }

    private fun showTncMessage() = binding?.run {
        val clickableText = "S&K"
        val ctaTextColor = com.tokopedia.unifycomponents.R.color.Unify_G500
        val termDescription = PowerMerchantSpannableUtil.createSpannableString(
            text = root.context.getString(R.string.pm_pro_upgrade_tnc_description).parseAsHtml(),
            highlightText = clickableText,
            colorId = root.context.getResColor(ctaTextColor),
            isBold = true
        ) {
            listener.onUpgradePmProTnCClickListener()
        }
        tvPmProTncDescription.movementMethod = LinkMovementMethod.getInstance()
        tvPmProTncDescription.text = termDescription
    }

    private fun setExpandedChanged(isExpanded: Boolean) = binding?.run {
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
        fun onUpgradePmProTnCClickListener()
        fun onDeactivatePMClickListener()
    }
}