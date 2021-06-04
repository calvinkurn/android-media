package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

/**
 * Created By @ilhamsuaib on 04/06/21
 */

class PMRegistrationFragment : PowerMerchantSubscriptionFragment() {

    companion object {
        private const val KEY_PM_TIER_TYPE = "key_pm_tier_type"

        fun createInstance(pmTireType: Int): PMRegistrationFragment {
            return PMRegistrationFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_PM_TIER_TYPE, pmTireType)
                }
            }
        }
    }

    private var pmGradeBenefitList: List<PMGradeWithBenefitsUiModel>? = null
    private var currentPmRegistrationTireType = PMConstant.PMTierType.POWER_MERCHANT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPmTire()
        observePmRegistrationPage()
    }

    fun setOnFooterCtaClickedListener(term: RegistrationTermUiModel?, isEligiblePm: Boolean, tncAgreed: Boolean, nextShopTireType: Int) {
        val shopInfo = pmBasicInfo?.shopInfo ?: return
        val isPmPro = currentPmRegistrationTireType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        when {
            isModeratedShop -> showModeratedShopBottomSheet()
            shopInfo.isNewSeller && isPmPro -> showNewSellerPmProBottomSheet()
            isEligiblePm -> submitPmRegistrationOnEligible(tncAgreed, nextShopTireType)
            term is RegistrationTermUiModel.ShopScore -> showShopScoreTermBottomSheet(shopInfo)
            term is RegistrationTermUiModel.ActiveProduct -> showActiveProductTermBottomSheet()
            term is RegistrationTermUiModel.Order -> showOrderTermBottomSheet(shopInfo.itemSoldPmProThreshold)
            term is RegistrationTermUiModel.NetItemValue -> showNivTermBottomSheet(shopInfo.netItemValuePmProThreshold)
            term is RegistrationTermUiModel.Kyc -> submitKYC(tncAgreed)
        }
    }

    private fun renderPmRegistrationWidgets() {
        pmBasicInfo?.shopInfo?.let { shopInfo ->
            val registrationHeaderWidget = getRegistrationHeaderWidgetData(shopInfo)

            val isPmPro = currentPmRegistrationTireType == PMConstant.PMTierType.POWER_MERCHANT_PRO
            if (isPmPro) {
                renderPmProRegistrationWidgets(registrationHeaderWidget)
            } else {
                renderRegularPmRegistrationWidget(registrationHeaderWidget)
            }

            recyclerView?.post {
                recyclerView?.smoothScrollToPosition(RecyclerView.SCROLLBAR_POSITION_DEFAULT)
            }
        }
    }

    private fun initPmTire() {
        val defaultTire = PMConstant.PMTierType.POWER_MERCHANT
        currentPmRegistrationTireType = arguments?.getInt(KEY_PM_TIER_TYPE, defaultTire)
                ?: defaultTire
    }

    private fun renderPmProRegistrationWidgets(headerWidget: WidgetRegistrationHeaderUiModel) {
        val widgets = listOf(
                headerWidget,
                WidgetDividerUiModel,
                getPmGradeBenefitWidget(Constant.Url.POWER_MERCHANT_PRO_EDU)
        )
        recyclerView?.visible()
        adapter.clearAllElements()
        renderList(widgets, false)
    }

    private fun renderRegularPmRegistrationWidget(headerWidget: WidgetRegistrationHeaderUiModel) {
        val widgets = listOf(
                headerWidget,
                WidgetDividerUiModel,
                WidgetPotentialUiModel,
                WidgetDividerUiModel,
                getPmGradeBenefitWidget(Constant.Url.POWER_MERCHANT_EDU)
        )
        recyclerView?.visible()
        adapter.clearAllElements()
        renderList(widgets, false)
    }

    private fun observePmRegistrationPage() {
        mViewModel.pmGradeBenefitInfo.observe(viewLifecycleOwner, Observer {
            hideSwipeRefreshLoading()
            when (it) {
                is Success -> {
                    this.pmGradeBenefitList = it.data
                    renderPmRegistrationWidgets()
                }
                is Fail -> {
                    showErrorState(it.throwable)
                    logToCrashlytic(PowerMerchantErrorLogger.PM_REGISTRATION_PAGE_ERROR, it.throwable)
                }
            }
        })
    }

    private fun getPmGradeBenefitWidget(ctaApplink: String): WidgetGradeBenefitUiModel {
        val gradeBenefitList = pmGradeBenefitList.orEmpty()
        return WidgetGradeBenefitUiModel(
                selectedPmTireType = currentPmRegistrationTireType,
                benefitPages = gradeBenefitList.filter { it.pmTier == currentPmRegistrationTireType },
                ctaApplink = ctaApplink
        )
    }

    private fun showNewSellerPmProBottomSheet() {
        val pmShopScoreProThreshold = pmBasicInfo?.shopInfo?.shopScorePmProThreshold.orZero()
        val title: String = getString(R.string.pm_new_seller_upgrade_pm_pro_bottom_sheet_title)
        val description = getString(R.string.pm_new_seller_upgrade_pm_pro_bottom_sheet_description, pmShopScoreProThreshold)
        val ctaText = getString(R.string.pm_content_slider_last_slide_button)
        val illustrationUrl = PMConstant.Images.PM_NEW_REQUIREMENT

        showNotificationBottomSheet(title, description, ctaText, illustrationUrl)
    }

    private fun showShopScoreTermBottomSheet(shopInfo: PMShopInfoUiModel) {
        val isPmPro = currentPmRegistrationTireType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val shopScoreThreshold = if (isPmPro) shopInfo.shopScorePmProThreshold else shopInfo.shopScoreThreshold
        val pmLabel = if (isPmPro) getString(R.string.pm_power_merchant_pro) else getString(R.string.pm_power_merchant)

        val title: String = getString(R.string.pm_bottom_sheet_shop_score_title)
        val description = getString(R.string.pm_bottom_sheet_shop_score_description, shopScoreThreshold, pmLabel)
        val ctaText = getString(R.string.pm_learn_shop_performance)
        val illustrationUrl = PMConstant.Images.PM_SHOP_SCORE_NOT_ELIGIBLE_BOTTOM_SHEET

        showNotificationBottomSheet(title, description, ctaText, illustrationUrl, onPrimaryCtaClicked = {
            RouteManager.route(context, ApplinkConst.SHOP_SCORE_DETAIL)
            powerMerchantTracking.sendEventClickLearnMoreShopPerformancePopUp()
        })

        powerMerchantTracking.sendEventShowPopupImproveShopPerformance()
        powerMerchantTracking.sendEventClickInterestedToRegister()
    }

    private fun showActiveProductTermBottomSheet() {
        val title: String = getString(R.string.pm_bottom_sheet_active_product_title)
        val description: String = getString(R.string.pm_bottom_sheet_active_product_description)
        val ctaText: String = getString(R.string.pm_add_product)
        val illustrationUrl: String = PMConstant.Images.PM_ADD_PRODUCT_BOTTOM_SHEET

        showNotificationBottomSheet(title, description, ctaText, illustrationUrl, onPrimaryCtaClicked = {
            RouteManager.route(context, ApplinkConst.SellerApp.PRODUCT_ADD)
            powerMerchantTracking.sendEventClickAddOneProductPopUp()
        })

        powerMerchantTracking.sendEventShowPopupAddNewProduct()
        powerMerchantTracking.sendEventClickInterestedToRegister()
    }

    private fun submitPmRegistrationOnEligible(isTncChecked: Boolean, nextShopTireType: Int) {
        if (!isTncChecked) {
            val message = getString(R.string.pm_tnc_agreement_error_message)
            val actionText = getString(R.string.power_merchant_ok_label)
            showErrorToaster(message, actionText)
            return
        }

        powerMerchantTracking.sendEventClickUpgradePowerMerchant()
        submitPmRegistration(nextShopTireType)
    }

    private fun showOrderTermBottomSheet(itemSoldThreshold: Long) {
        val title = getString(R.string.pm_not_eligible_order_term_title)
        val description = getString(R.string.pm_not_eligible_order_term_description, itemSoldThreshold.toString())
        val primaryCtaText = getString(R.string.pm_learn_ad_and_promotion)
        val secondaryCtaText = getString(R.string.pm_content_slider_last_slide_button)
        val imageUrl = PMConstant.Images.PM_TOTAL_ORDER_TERM

        showNotificationBottomSheet(title, description, primaryCtaText, imageUrl, secondaryCtaText,
                onPrimaryCtaClicked = {
                    RouteManager.route(requireContext(), ApplinkConst.SellerApp.CENTRALIZED_PROMO)
                }
        )

        powerMerchantTracking.sendEventClickInterestedToRegister()
    }

    private fun showNivTermBottomSheet(nivThreshold: Long) {
        val nivFormatted = CurrencyFormatHelper.convertToRupiah(nivThreshold.toString())
        val title = getString(R.string.pm_not_eligible_niv_term_title)
        val description = getString(R.string.pm_not_eligible_niv_term_description, nivFormatted)
        val primaryCtaText = getString(R.string.pm_learn_ad_and_promotion)
        val secondaryCtaText = getString(R.string.pm_content_slider_last_slide_button)
        val imageUrl = PMConstant.Images.PM_TOTAL_ORDER_TERM

        showNotificationBottomSheet(title, description, primaryCtaText, imageUrl, secondaryCtaText,
                onPrimaryCtaClicked = {
                    RouteManager.route(requireContext(), ApplinkConst.SellerApp.CENTRALIZED_PROMO)
                }
        )

        powerMerchantTracking.sendEventClickInterestedToRegister()
    }

    private fun submitKYC(isTncChecked: Boolean) {
        if (!isTncChecked) {
            val message = getString(R.string.pm_tnc_agreement_error_message)
            val actionText = getString(R.string.power_merchant_ok_label)
            showErrorToaster(message, actionText)
            return
        }

        val isPmPro = currentPmRegistrationTireType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val appLink = if (isPmPro) {
            PMConstant.AppLink.KYC_POWER_MERCHANT_PRO
        } else {
            PMConstant.AppLink.KYC_POWER_MERCHANT
        }
        RouteManager.route(context, appLink)
    }

    private fun getRegistrationHeaderWidgetData(shopInfo: PMShopInfoUiModel): WidgetRegistrationHeaderUiModel {
        return WidgetRegistrationHeaderUiModel(
                shopInfo = shopInfo,
                registrationTerms = if (currentPmRegistrationTireType == PMConstant.PMTierType.POWER_MERCHANT) {
                    PMRegistrationTermHelper.getPmRegistrationTerms(requireContext(), shopInfo)
                } else {
                    PMRegistrationTermHelper.getPmProRegistrationTerms(requireContext(), shopInfo)
                },
                selectedPmType = currentPmRegistrationTireType
        )
    }
}