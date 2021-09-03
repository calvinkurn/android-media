package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil.setTextMakeHyperlink
import com.tokopedia.power_merchant.subscribe.view.adapter.PMProBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.adapter.RegistrationTermAdapter
import com.tokopedia.power_merchant.subscribe.view.model.PMProBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetUpgradePmProUiModel
import kotlinx.android.synthetic.main.widget_upgrade_pm_pro.view.*
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
        with(itemView) {
            if (shopInfo.isNewSeller) {
                if (shopInfo.is30DaysFirstMonday) {
                    viewPmUpgradeTermSection?.tvTermStatus?.show()
                    if (shopInfo.isEligiblePmPro) {
                        tvPmUpgradePmProTitle?.text =
                            getString(R.string.pm_title_new_seller_eligible_after_30_days)
                        tvPmUpgradePmProDesc?.show()
                        tvPmUpgradeBenefitDescription?.text =
                            getString(R.string.pm_desc_new_seller_eligible_benefit_package)
                        tvSetMembershipStatus?.setTextMakeHyperlink(
                            getString(R.string.pm_set_membership_status)
                        ) {
                            listener.onMembershipStatusClickListener()
                        }
                    } else {
                        tvPmUpgradePmProTitle?.text =
                            getString(R.string.pm_title_new_seller_not_eligible_after_30_days)
                        tvPmUpgradePmProDesc?.show()
                        tvPmUpgradeBenefitDescription?.text =
                            getString(R.string.pm_desc_new_seller_not_eligible_benefit_package)
                        hidePmProUpgradeSection()
                        tvSetMembershipStatus?.setTextMakeHyperlink(
                            getString(R.string.pm_deactivate_pm_pro_not_eligible)
                        ) {
                            listener.onDeactivatePMClickListener()
                        }
                    }
                } else {
                    tvPmUpgradePmProTitle?.text =
                        getString(R.string.pm_title_new_seller_before_30_days)
                    tvPmUpgradePmProDesc?.show()
                    tvPmUpgradePmProDesc?.text = getString(
                        R.string.pm_desc_new_seller_before_30_days,
                        getDaysDate(shopInfo.shopAge)
                    )
                    tvSetMembershipStatus?.setTextMakeHyperlink(
                        getString(R.string.pm_set_membership_status)
                    ) {
                        listener.onMembershipStatusClickListener()
                    }
                    viewPmUpgradeTermSection?.tvTermStatus?.hide()
                    hidePmProUpgradeSection()
                }
            } else {
                viewPmUpgradeTermSection?.tvTermStatus?.show()
                tvPmUpgradePmProDesc?.hide()
            }
        }
    }

    private fun getDaysDate(shopAge: Long): String {
        return try {
            val date = Calendar.getInstance(GoldMerchantUtil.getLocale())
            val diffDays = (THIRTY_DAYS - shopAge)
            val targetDays = GoldMerchantUtil.getNNextDaysBasedOnFirstMonday(diffDays.toInt())
            date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) + targetDays)
            GoldMerchantUtil.format(date.timeInMillis, PATTERN_DATE_TEXT)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }


    private fun hidePmProUpgradeSection() {
        with(itemView) {
            tvPmProTncDescription?.hide()
            btnPmProUpgrade?.hide()
        }
    }

    private fun setupUpgradeCta(element: WidgetUpgradePmProUiModel) = with(itemView) {
        val shopScore = element.shopInfo.shopScore.toString()
        powerMerchantTracking.sendEventImpressCTAPmUpgradeLearnMore(shopScore)
        ctaPmUpgradeLearnMore.setOnClickListener {
            powerMerchantTracking.sendEventClickCTAPmUpgradeLearnMore(shopScore)
            RouteManager.route(context, Constant.Url.POWER_MERCHANT_PRO_EDU)
        }
    }

    private fun showGeneralBenefits(benefits: List<PMProBenefitUiModel>) =
        with(itemView.rvPmUpgradeBenefits) {
            val benefitAdapter = PMProBenefitAdapter(benefits)
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
        imgPmUpgradeBackdrop.loadImage(Constant.Image.PM_BG_UPSALE_PM_PRO)
        icPmProBadge.loadImage(PMConstant.Images.PM_PRO_BADGE)
        viewPmUpgradeTermSection.setEligibility(element.shopInfo.isEligiblePmPro)

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
            tvPmUpgradeBenefitDescription.text = context.resources.getString(
                R.string.pm_pro_upgrade_not_eligible_description,
                threshold,
                Constant.POWER_MERCHANT_PRO_CHARGING
            ).parseAsHtml()
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

        tvPmUpgradePmProTitle.text = if (element.shopInfo.isEligiblePmPro) {
            context.getString(R.string.pm_get_exclusive_benefit_of_being_pm_pro_eligible)
        } else {
            context.getString(R.string.pm_get_exclusive_benefit_of_being_pm_pro)
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
            listener.onUpgradePmProTnCClickListener()
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
        fun onUpgradePmProTnCClickListener()
        fun onMembershipStatusClickListener()
        fun onDeactivatePMClickListener()
    }
}