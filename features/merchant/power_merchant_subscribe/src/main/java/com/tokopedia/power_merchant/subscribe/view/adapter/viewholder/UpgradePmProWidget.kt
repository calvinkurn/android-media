package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.WidgetUpgradePmProBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.PMProBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.adapter.PmActiveTermAdapter
import com.tokopedia.power_merchant.subscribe.view.model.PMProBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.PmActiveTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetUpgradePmProUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 10/05/21
 */

class UpgradePmProWidget(
    itemView: View?,
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetUpgradePmProUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_upgrade_pm_pro
    }

    private val binding: WidgetUpgradePmProBinding? by viewBinding()
    private val termAdapter by lazy { PmActiveTermAdapter() }

    override fun bind(element: WidgetUpgradePmProUiModel) {
        setupView()
        showTermsList(element.registrationTerms)
        showGeneralBenefits(element.generalBenefits)
        setupUpgradeCta(element)
        setupNewSeller(element)
    }

    private fun setupNewSeller(element: WidgetUpgradePmProUiModel) {
        val shopInfo = element.shopInfo
        val shopLevel = shopInfo.shopLevel
        val scoreShowTickerEligble = 70
        binding?.run {
            tvPmUpgradePmProDesc.text = getString(
                R.string.pm_desc_next_update,
                element.nextMonthlyRefreshDate
            )

            if (shopInfo.shopScore > scoreShowTickerEligble) {
                when (shopLevel) {
                    PMConstant.ShopLevel.TWO -> {
                        tickerPmWidget.setHtmlDescription(
                            root.context.getString(
                                R.string.pm_ticker_eligble_upgrade,
                                PMConstant.EligbleShopGrade.PRO_ADVANCE,
                                element.nextMonthlyRefreshDate
                            )
                        )
                    }
                    PMConstant.ShopLevel.THREE -> {
                        tickerPmWidget.setHtmlDescription(
                            root.context.getString(
                                R.string.pm_ticker_eligble_upgrade,
                                PMConstant.EligbleShopGrade.PRO_EXPERT,
                                element.nextMonthlyRefreshDate
                            )
                        )
                    }
                    PMConstant.ShopLevel.FOUR -> {
                        tickerPmWidget.setHtmlDescription(
                            root.context.getString(
                                R.string.pm_ticker_eligble_upgrade,
                                PMConstant.EligbleShopGrade.PRO_ULTIMATE,
                                element.nextMonthlyRefreshDate
                            )
                        )
                    }
                }
                tickerPmWidget.show()
            } else {
                tickerPmWidget.hide()
            }
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

    private fun showTermsList(terms: List<PmActiveTermUiModel>) {
        binding?.rvPmUpgradeTerms?.run {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = termAdapter
        }
        termAdapter.setItems(terms)
        termAdapter.notifyDataSetChanged()
    }

    private fun setupView() = binding?.run {
        imgPmUpgradeBackdrop.loadImage(Constant.Image.PM_BG_UPSALE_PM_PRO)
        icPmProBadge.loadImage(PMConstant.Images.PM_SHOP_ICON)
        icTargetHeader.loadImage(PMConstant.Images.PM_BADGE)
    }

}