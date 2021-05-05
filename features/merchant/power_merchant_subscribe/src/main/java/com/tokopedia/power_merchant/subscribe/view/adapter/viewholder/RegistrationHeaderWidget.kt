package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.RegistrationTermAdapter
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetRegistrationHeaderUiModel
import kotlinx.android.synthetic.main.widget_pm_registration_header.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationHeaderWidget(
        itemView: View,
        private val listener: Listener
) : AbstractViewHolder<WidgetRegistrationHeaderUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_registration_header

        private const val PM_REGULAR_TAB_POSITION = 0
        private const val PM_PRO_TAB_POSITION = 1
    }

    private val termAdapter by lazy { RegistrationTermAdapter() }
    private val pmRegularLabel by lazy { getString(R.string.pm_power_merchant).asCamelCase() }
    private val pmProLabel by lazy { getString(R.string.pm_power_merchant_pro).asCamelCase() }

    override fun bind(element: WidgetRegistrationHeaderUiModel) {
        setupView(element)
        setupTermsList()
    }

    private fun setupView(element: WidgetRegistrationHeaderUiModel) = with(itemView) {
        setupPmSection(element)

        tvPmHeaderTerms.setOnSectionHeaderClickListener { isExpanded ->
            setOnExpandChanged(isExpanded, element)
        }
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

    private fun setupPmSection(element: WidgetRegistrationHeaderUiModel) = with(itemView) {
        tabPmTypeSection.tabLayout.removeAllTabs()
        val isPmPro = element.selectedPmType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        tabPmTypeSection.addNewTab(pmRegularLabel)
        tabPmTypeSection.addNewTab(pmProLabel, isPmPro)

        tabPmTypeSection.tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        tabPmTypeSection.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabIndex = tabPmTypeSection.tabLayout.selectedTabPosition
                setOnPmTypeTabIndexSelected(tabIndex, element)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        if (isPmPro) {
            setupPmProState(element)
        } else {
            setupRegularPmState(element)
        }
    }

    private fun setOnPmTypeTabIndexSelected(tabIndex: Int, element: WidgetRegistrationHeaderUiModel) {
        when (tabIndex) {
            PM_REGULAR_TAB_POSITION -> {
                element.selectedPmType = PMConstant.PMTierType.POWER_MERCHANT
                setupRegularPmState(element)
                listener.onPowerMerchantSectionClickListener(element)
            }
            PM_PRO_TAB_POSITION -> {
                element.selectedPmType = PMConstant.PMTierType.POWER_MERCHANT_PRO
                setupPmProState(element)
                listener.onPowerMerchantProSectionClickListener(element)
            }
        }
    }

    private fun setupRegularPmState(element: WidgetRegistrationHeaderUiModel) = with(itemView) {
        val isEligible = getPmEligibilityStatus(element)
        showTermList(element.pmTerms)
        setOnExpandChanged(!isEligible, element)

        imgPmHeaderBackdrop.loadImageDrawable(R.drawable.bg_pm_registration_header)
        imgPmHeaderImage.loadImageDrawable(R.drawable.ic_pm_badge_pm_filled)
        tvPmHeaderDesc.setText(R.string.pm_registration_header_pm)
        tvPmHeaderTerms.setEligibility(isEligible)
    }

    private fun setupPmProState(element: WidgetRegistrationHeaderUiModel) = with(itemView) {
        val isEligible = getPmEligibilityStatus(element)
        showTermList(element.pmProTerms)
        setOnExpandChanged(!isEligible, element)

        imgPmHeaderBackdrop.loadImageDrawable(R.drawable.bg_pm_pro_registration_header)
        imgPmHeaderImage.loadImageDrawable(R.drawable.ic_pm_badge_pm_pro_filled)
        tvPmHeaderDesc.setText(R.string.pm_registration_header_pm_pro)
        tvPmHeaderTerms.setEligibility(isEligible)
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

    private fun setupTermsList() {
        with(itemView.rvPmRegistrationTerm) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = termAdapter
        }
    }

    interface Listener {
        fun onPowerMerchantSectionClickListener(headerWidget: WidgetRegistrationHeaderUiModel)
        fun onPowerMerchantProSectionClickListener(headerWidget: WidgetRegistrationHeaderUiModel)
    }
}