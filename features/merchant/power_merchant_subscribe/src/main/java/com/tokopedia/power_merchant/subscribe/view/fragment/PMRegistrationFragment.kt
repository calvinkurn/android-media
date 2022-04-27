package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.config.GlobalConfig
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationBenefitHelper
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.ItemPMProNewSellerRequirement
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetBannerPMRegistration
import com.tokopedia.power_merchant.subscribe.view.model.WidgetDividerUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetGradeBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPMProNewSellerHeaderUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPmProNewSellerBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPotentialUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetRegistrationHeaderUiModel
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

/**
 * Created By @ilhamsuaib on 04/06/21
 */

class PMRegistrationFragment : PowerMerchantSubscriptionFragment() {

    companion object {

        fun createInstance(): PMRegistrationFragment {
            return PMRegistrationFragment()
        }
    }

    private var currentPmRegistrationTireType = PMConstant.PMTierType.POWER_MERCHANT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnSwipeToRefresh()
    }

    override fun observePowerMerchantBasicInfo() {
        observe(sharedViewModel.powerMerchantBasicInfo) {
            when (it) {
                is Success -> {
                    initBasicInfo(it.data)
                    renderPmRegistrationWidgets()
                }
                is Fail -> logToCrashlytic(
                    PowerMerchantErrorLogger.PM_BASIC_INFO_ERROR,
                    it.throwable
                )
            }
            dismissRefreshIndicator()
        }
    }

    override fun onMoreDetailPMEligibilityClicked() {

    }

    fun setOnFooterCtaClickedListener(
        term: RegistrationTermUiModel?,
        isEligiblePm: Boolean,
        tncAgreed: Boolean,
        nextShopTireType: Int
    ) {
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
            val isRegularMerchant = pmBasicInfo?.pmStatus?.isRegularMerchant() == true
            val registrationHeaderWidget =
                getRegistrationHeaderWidgetData(shopInfo, isRegularMerchant)

            renderPmRegistrationWidget(registrationHeaderWidget)

            recyclerView?.post {
                recyclerView?.smoothScrollToPosition(RecyclerView.SCROLLBAR_POSITION_DEFAULT)
            }
        }
    }

    private fun dismissRefreshIndicator() {
        binding?.swipeRefreshPm?.isRefreshing = false
    }

    private fun setOnSwipeToRefresh() {
        binding?.swipeRefreshPm?.setOnRefreshListener {
            fetchPowerMerchantBasicInfo()
        }
    }

    private fun getPMProNewSellerHeaderWidget(): WidgetPMProNewSellerHeaderUiModel {
        return WidgetPMProNewSellerHeaderUiModel(
            imageUrl = Constant.Image.IC_HERO_PM_NEW_SELLER,
            itemRequiredPMProNewSeller = listOf(
                ItemPMProNewSellerRequirement(
                    title = getString(R.string.title_requirement_pm_pro_new_seller_1),
                    imageUrl = Constant.Image.IC_PM_PRO_NEW_SELLER_VERIFIED
                ),
                ItemPMProNewSellerRequirement(
                    title = getString(R.string.title_requirement_pm_pro_new_seller_2),
                    imageUrl = Constant.Image.IC_PM_PRO_NEW_SELLER_SCORE
                ),
                ItemPMProNewSellerRequirement(
                    title = getString(R.string.title_requirement_pm_pro_new_seller_3),
                    imageUrl = Constant.Image.IC_PM_PRO_NEW_SELLER_TRANSACTION
                )
            )
        )
    }

    private fun getPMProNewSellerBenefitWidget(): WidgetPmProNewSellerBenefitUiModel {
        return WidgetPmProNewSellerBenefitUiModel(
            items = context?.let {
                PMRegistrationTermHelper.getBenefitListPmProNewSeller(it)
            } ?: emptyList()
        )
    }

    private fun renderPmRegistrationWidget(headerWidget: WidgetRegistrationHeaderUiModel) {
        val widgets = listOf(
            WidgetBannerPMRegistration,
            headerWidget,
            WidgetDividerUiModel,
            WidgetPotentialUiModel(headerWidget.shopInfo.isNewSeller),
            getPmGradeBenefitWidget()
        )
        recyclerView?.visible()
        adapter.clearAllElements()
        renderList(widgets, false)
    }

    private fun getPmGradeBenefitWidget(): WidgetGradeBenefitUiModel {
        val shopInfo = pmBasicInfo?.shopInfo
        return WidgetGradeBenefitUiModel(
            currentShopLevel = shopInfo?.shopLevel.orZero(),
            currentCompletedOrder = shopInfo?.itemSoldOneMonth.orZero(),
            currentIncome = shopInfo?.netItemValueOneMonth.orZero(),
            benefitPages = PMRegistrationBenefitHelper.getPMBenefitList(requireContext(), shopInfo),
            ctaAppLink = Constant.Url.POWER_MERCHANT_EDU,
            isEligiblePm = shopInfo?.isEligiblePm.orFalse(),
            isEligiblePmPro = shopInfo?.isEligiblePmPro.orFalse()
        )
    }

    private fun showNewSellerPmProBottomSheet() {
        val pmShopScoreProThreshold = pmBasicInfo?.shopInfo?.shopScorePmProThreshold.orZero()
        val title = getString(R.string.pm_new_seller_upgrade_pm_pro_bottom_sheet_title)
        val description = getString(
            R.string.pm_new_seller_upgrade_pm_pro_bottom_sheet_description,
            pmShopScoreProThreshold
        )
        val ctaText = getString(R.string.pm_content_slider_last_slide_button)
        val illustrationUrl = PMConstant.Images.PM_NEW_REQUIREMENT

        showNotificationBottomSheet(title, description, ctaText, illustrationUrl)
    }

    private fun showShopScoreTermBottomSheet(shopInfo: PMShopInfoUiModel) {
        val isPmPro = currentPmRegistrationTireType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val shopScoreThreshold = if (isPmPro) {
            shopInfo.shopScorePmProThreshold
        } else {
            shopInfo.shopScoreThreshold
        }
        val pmLabel = if (isPmPro) {
            getString(R.string.pm_power_merchant_pro)
        } else {
            getString(R.string.pm_power_merchant)
        }

        val title = getString(R.string.pm_bottom_sheet_shop_score_title)
        val description =
            getString(R.string.pm_bottom_sheet_shop_score_description, shopScoreThreshold, pmLabel)
        val ctaText = getString(R.string.pm_learn_shop_performance)
        val illustrationUrl = PMConstant.Images.PM_SHOP_SCORE_NOT_ELIGIBLE_BOTTOM_SHEET

        showNotificationBottomSheet(
            title,
            description,
            ctaText,
            illustrationUrl,
            onPrimaryCtaClicked = {
                RouteManager.route(context, ApplinkConst.SHOP_SCORE_DETAIL)
                powerMerchantTracking.sendEventClickLearnMoreShopPerformancePopUp()
            })

        powerMerchantTracking.sendEventShowPopupImproveShopPerformance()
        powerMerchantTracking.sendEventClickInterestedToRegister()
    }

    private fun showActiveProductTermBottomSheet() {
        val title = getString(R.string.pm_bottom_sheet_active_product_title)
        val description: String = getString(R.string.pm_bottom_sheet_active_product_description)
        val ctaText: String = getString(R.string.pm_add_product)
        val illustrationUrl: String = PMConstant.Images.PM_ADD_PRODUCT_BOTTOM_SHEET

        showNotificationBottomSheet(
            title,
            description,
            ctaText,
            illustrationUrl,
            onPrimaryCtaClicked = {
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

        submitPmRegistration(nextShopTireType)
    }

    private fun showOrderTermBottomSheet(itemSoldThreshold: Long) {
        val title = getString(R.string.pm_not_eligible_order_term_title)
        val description =
            getString(R.string.pm_not_eligible_order_term_description, itemSoldThreshold.toString())
        val primaryCtaText = getString(R.string.pm_learn_ad_and_promotion)
        val secondaryCtaText = getString(R.string.pm_content_slider_last_slide_button)
        val imageUrl = PMConstant.Images.PM_TOTAL_ORDER_TERM

        showNotificationBottomSheet(title, description, primaryCtaText, imageUrl, secondaryCtaText,
            onPrimaryCtaClicked = {
                openCentralizedPromoPage()
            }
        )

        powerMerchantTracking.sendEventClickLearnPopUpImproveNumberOfOrder()
        powerMerchantTracking.sendEventClickInterestedToRegister()
    }

    private fun openCentralizedPromoPage() {
        context?.run {
            if (GlobalConfig.isSellerApp()) {
                RouteManager.route(
                    requireContext(),
                    ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
                )
            } else {
                val appLinks = arrayListOf(
                    ApplinkConstInternalSellerapp.SELLER_HOME,
                    ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
                )
                val intent = SellerMigrationActivity.createIntent(
                    this,
                    SellerMigrationFeatureName.FEATURE_CENTRALIZED_PROMO,
                    GMParamTracker.ScreenName.PM_SUBSCRIBE,
                    appLinks
                )
                startActivity(intent)
            }
        }
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
                openCentralizedPromoPage()
            }
        )

        powerMerchantTracking.sendEventClickLearnPopUpImproveNiv()
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

    private fun getRegistrationHeaderWidgetData(
        shopInfo: PMShopInfoUiModel,
        isRegularMerchant: Boolean
    ): WidgetRegistrationHeaderUiModel {
        val currentPMProRegistrationSelected =
            currentPmRegistrationTireType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        return WidgetRegistrationHeaderUiModel(
            shopInfo = shopInfo,
            registrationTerms = if (currentPmRegistrationTireType == PMConstant.PMTierType.POWER_MERCHANT) {
                PMRegistrationTermHelper.getPmRegistrationTerms(
                    requireContext(), shopInfo,
                    currentPMProRegistrationSelected, isRegularMerchant
                )
            } else {
                PMRegistrationTermHelper.getPmProRegistrationTerms(
                    requireContext(),
                    shopInfo,
                    currentPMProRegistrationSelected
                )
            },
            selectedPmType = currentPmRegistrationTireType
        )
    }
}