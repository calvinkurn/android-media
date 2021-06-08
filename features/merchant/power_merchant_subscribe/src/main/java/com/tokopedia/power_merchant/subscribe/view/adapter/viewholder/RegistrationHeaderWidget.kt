package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.adapter.RegistrationTermAdapter
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetRegistrationHeaderUiModel
import kotlinx.android.synthetic.main.widget_pm_registration_header.view.*

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

    override fun bind(element: WidgetRegistrationHeaderUiModel) {
        setupView(element)
        setupTermsList(element)
    }

    private fun setupView(element: WidgetRegistrationHeaderUiModel) = with(itemView) {
        tvPmHeaderTerms.setOnSectionHeaderClickListener { isExpanded ->
            setOnExpandChanged(isExpanded, element)
        }
        tvPmHeaderTerms.setEligibility(getPmEligibilityStatus(element))
    }

    private fun getPmEligibilityStatus(element: WidgetRegistrationHeaderUiModel): Boolean {
        return if (element.selectedPmType == PMConstant.PMTierType.POWER_MERCHANT_PRO) {
            element.shopInfo.isEligiblePmPro
        } else {
            element.shopInfo.isEligiblePm
        }
    }

    private fun setOnExpandChanged(isExpanded: Boolean, element: WidgetRegistrationHeaderUiModel) = with(itemView) {
        rvPmRegistrationTerm.isVisible = isExpanded
        tvPmHeaderTerms.setExpanded(isExpanded)
        if (isExpanded) {
            setTickerVisibility(element.shopInfo)
        } else {
            tickerPmHeader.gone()
        }
    }

    private fun showTermList(terms: List<RegistrationTermUiModel>) {
        termAdapter.setItems(terms)
        termAdapter.notifyDataSetChanged()
    }

    private fun setTickerVisibility(shopInfo: PMShopInfoUiModel) {
        val isEligibleShopScore = !shopInfo.isNewSeller && (shopInfo.isEligibleShopScore() || shopInfo.isEligibleShopScorePmPro())
        val hasActiveProduct = shopInfo.isNewSeller && shopInfo.hasActiveProduct
        val isTickerVisible = shopInfo.kycStatusId == KYCStatusId.PENDING && (isEligibleShopScore || hasActiveProduct)
        itemView.tickerPmHeader.isVisible = isTickerVisible
    }

    private fun setupTermsList(element: WidgetRegistrationHeaderUiModel) {
        with(itemView.rvPmRegistrationTerm) {
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
        }
    }

    interface Listener {

    }
}