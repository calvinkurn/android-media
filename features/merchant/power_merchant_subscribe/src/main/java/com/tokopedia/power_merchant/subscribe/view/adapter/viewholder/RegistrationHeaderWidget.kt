package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.parseAsHtml
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

        private const val PM_CHARGING = "1,25%"
        private const val PM_PRO_CHARGING = "1,5%"
    }

    private val termAdapter by lazy { RegistrationTermAdapter() }

    override fun bind(element: WidgetRegistrationHeaderUiModel) {
        setupView(element)
        setupTermsList()
    }

    private fun setupView(element: WidgetRegistrationHeaderUiModel) {
        val shopInfo = element.shopInfo
        setupPmSection(element)
        setTickerVisibility(shopInfo)
    }

    private fun setupPmSection(element: WidgetRegistrationHeaderUiModel) = with(itemView) {
        pmsPmRegular.setTitle(context.getString(R.string.pm_power_merchant))
        pmsPmRegular.setOnClickListener {
            setupRegularPmState(element)
            element.selectedPmType = PMConstant.PMTierType.PM_REGULAR
            listener.onPowerMerchantSectionClickListener(element)
        }

        pmsPmPro.setTitle(context.getString(R.string.pm_power_merchant_pro))
        pmsPmPro.setOnClickListener {
            setupPmProState(element)
            element.selectedPmType = PMConstant.PMTierType.PM_PRO
            listener.onPowerMerchantProSectionClickListener(element)
        }

        if (element.selectedPmType == PMConstant.PMTierType.PM_PRO) {
            setupPmProState(element)
        } else {
            setupRegularPmState(element)
        }
    }

    private fun setupRegularPmState(element: WidgetRegistrationHeaderUiModel) {
        showPmChargingInfo(PMConstant.PMTierType.PM_REGULAR)
        setPmRegularSectionSelected()
        showTermList(element.pmTerms)
    }

    private fun setupPmProState(element: WidgetRegistrationHeaderUiModel) {
        showPmChargingInfo(PMConstant.PMTierType.PM_PRO)
        setPmProSectionSelected()
        showTermList(element.pmProTerms)
    }

    private fun showTermList(terms: List<RegistrationTermUiModel>) {
        termAdapter.setItems(terms)
        termAdapter.notifyDataSetChanged()
    }

    private fun showPmChargingInfo(selectedPmType: Int) = with(itemView) {
        val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        val isPmPro = selectedPmType == PMConstant.PMTierType.PM_PRO
        val pmCharging = if (isPmPro) PM_PRO_CHARGING else PM_CHARGING

        val pmDescription = context.getString(R.string.pm_power_merchant_section_description, textColor, pmCharging)
        tvPmChargingInfo.text = pmDescription.parseAsHtml()
    }

    private fun setPmRegularSectionSelected() = with(itemView) {
        pmsPmRegular.setSelectedStatus(true)
        pmsPmPro.setSelectedStatus(false)
        imgPmHeaderBackdrop.loadImageDrawable(R.drawable.bg_pm_registration_header)
        imgPmHeaderImage.loadImage(PMConstant.Images.PM_REGISTRATION_PM)
        tvPmHeaderDesc.setText(R.string.pm_registration_header_pm)

        val textColor = PMCommonUtils.getHexColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        context.getString(R.string.pm_power_merchant_section_description, textColor, PM_CHARGING)
    }

    private fun setPmProSectionSelected() = with(itemView) {
        pmsPmRegular.setSelectedStatus(false)
        pmsPmPro.setSelectedStatus(true)
        imgPmHeaderBackdrop.loadImageDrawable(R.drawable.bg_pm_pro_registration_header)
        imgPmHeaderImage.loadImage(PMConstant.Images.PM_REGISTRATION_PM_PRO)
        tvPmHeaderDesc.setText(R.string.pm_registration_header_pm_pro)
    }

    private fun setTickerVisibility(shopInfo: PMShopInfoUiModel) {
        val isEligibleShopScore = !shopInfo.isNewSeller && (shopInfo.isEligibleShopScore() || shopInfo.isEligibleShopScorePmPro())
        val hasActiveProduct = shopInfo.isNewSeller && shopInfo.hasActiveProduct
        val isTickerVisible = shopInfo.kycStatusId == KYCStatusId.PENDING && (isEligibleShopScore || hasActiveProduct)
        itemView.tickerPmHeader.visibility = if (isTickerVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
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