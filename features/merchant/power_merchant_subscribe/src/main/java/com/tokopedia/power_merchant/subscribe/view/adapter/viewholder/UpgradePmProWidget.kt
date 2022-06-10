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
                        tvPmUpgradePmProDesc.hide()
                    } else {
                        tvPmUpgradePmProDesc.hide()
                    }
                } else {
                    tvPmUpgradePmProDesc.show()
                    tvPmUpgradePmProDesc.text = getString(
                        R.string.pm_desc_new_seller_before_30_days,
                        getDaysDate(shopInfo.shopCreatedDate)
                    )
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
                    showTncMessage()
                    setExpandedChanged(false)
                } else {
                    setExpandedChanged(true)
                }
            } else {
                setExpandedChanged(true)
            }
        } else {
            if (element.shopInfo.isEligiblePmPro) {
                showTncMessage()
                setExpandedChanged(false)
            } else {
                setExpandedChanged(true)
            }
        }

        viewPmUpgradeTermSection.setOnSectionHeaderClickListener {
            setExpandedChanged(it)
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
        fun onUpgradePmProTnCClickListener()
        fun onDeactivatePMClickListener()
    }
}