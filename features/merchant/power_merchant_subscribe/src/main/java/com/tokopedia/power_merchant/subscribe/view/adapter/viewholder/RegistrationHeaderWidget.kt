package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmRegistrationHeaderBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.RegistrationTermAdapter
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetRegistrationHeaderUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationHeaderWidget(
    itemView: View,
    private val listener: Listener,
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetRegistrationHeaderUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_registration_header
    }

    private val termAdapter by lazy {
        RegistrationTermAdapter(this::onTermCtaClickedListener)
    }

    private val binding: WidgetPmRegistrationHeaderBinding? by viewBinding()

    override fun bind(element: WidgetRegistrationHeaderUiModel) {
        setupView(element)
        setupTermsList(element)
        setupPmEligibilityView(element)
    }

    private fun setupPmEligibilityView(element: WidgetRegistrationHeaderUiModel) {
        binding?.run {
            val isEligiblePM = getPmEligibilityStatus(element)
            tvPmHeaderEligiblePMDetail.isVisible = isEligiblePM

            if (!isEligiblePM) {
                val isFistTermNotEligible = !element.registrationTerms
                    .firstOrNull()?.isChecked.orFalse()
                when {
                    isFistTermNotEligible -> {
                        horLinePmHeader.visible()
                        tvPmHeaderEligibleFor.visible()
                        tvPmHeaderEligibleFor.text = root.context.getString(
                            R.string.pm_registration_term_header
                        )
                        tvPmHeaderEligibleFor.gravity = Gravity.CENTER
                    }
                    else -> {
                        horLinePmHeader.gone()
                        tvPmHeaderEligibleFor.gone()
                    }
                }
                return
            }
            val text = if (element.shopInfo.isEligiblePmPro) getString(R.string.pm_pm_pro) else getString(R.string.pm)
            tvPmHeaderEligibleFor.text = root.context.getString(R.string.pm_eligible_pm_grade, text)
                .parseAsHtml()
            tvPmHeaderEligibleFor.gravity = Gravity.START
            tvPmHeaderEligiblePMDetail.setOnClickListener {
                powerMerchantTracking.sendEventClickDetailTermPM(element.shopInfo.shopScore.toString())
                listener.onMoreDetailPMEligibilityClicked()
            }
        }
    }

    private fun setupView(element: WidgetRegistrationHeaderUiModel) = binding?.run {
        tvPmHeaderTerms.setOnSectionHeaderClickListener { isExpanded ->
            setOnExpandChanged(isExpanded, element)
        }
    }

    private fun getPmEligibilityStatus(element: WidgetRegistrationHeaderUiModel): Boolean {
        return element.registrationTerms.all { it.isChecked }
    }

    private fun setOnExpandChanged(isExpanded: Boolean, element: WidgetRegistrationHeaderUiModel) =
        binding?.run {
            rvPmRegistrationTerm.isVisible = isExpanded
            tvPmHeaderTerms.setExpanded(isExpanded)
            if (isExpanded) {
                setTickerVisibility(element.shopInfo)
            } else {
                tickerPmHeader.gone()
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTermList(terms: List<RegistrationTermUiModel>) {
        termAdapter.setItems(terms)
        termAdapter.notifyDataSetChanged()
    }

    private fun setTickerVisibility(shopInfo: PMShopInfoUiModel) {
        val isEligibleShopScore =
            !shopInfo.isNewSeller && (shopInfo.isEligibleShopScore() || shopInfo.isEligibleShopScorePmPro())
        val hasActiveProduct = shopInfo.isNewSeller && shopInfo.hasActiveProduct
        val isTickerVisible =
            shopInfo.kycStatusId == KYCStatusId.PENDING && (isEligibleShopScore || hasActiveProduct)
        binding?.tickerPmHeader?.isVisible = isTickerVisible
    }

    private fun setupTermsList(element: WidgetRegistrationHeaderUiModel) {
        binding?.rvPmRegistrationTerm?.run {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = termAdapter
        }
        showTermList(element.registrationTerms)
    }

    private fun onTermCtaClickedListener(term: RegistrationTermUiModel) {
        when (term) {
            is RegistrationTermUiModel.ActiveProduct -> powerMerchantTracking.sendEventClickAddProduct()
            is RegistrationTermUiModel.ShopScore -> powerMerchantTracking.sendEventClickLearnMoreShopPerformance()
            is RegistrationTermUiModel.Kyc -> powerMerchantTracking.sendEventClickKycDataVerification()
            else -> {
                // no op
            }
        }
    }

    interface Listener {
        fun onMoreDetailPMEligibilityClicked() {}
    }
}
