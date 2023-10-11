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
import com.tokopedia.gm.common.presentation.model.ShopLevelUiModel
import com.tokopedia.gm.common.presentation.view.bottomsheet.BottomSheetShopTooltipLevel
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationBenefitHelper
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.BaseWidgetUiModel
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetBannerPMRegistration
import com.tokopedia.power_merchant.subscribe.view.model.WidgetDividerUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetGradeBenefitUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPotentialUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetRegistrationHeaderUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetTickerUiModel
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

    private var shopLevelInfo: ShopLevelUiModel? = null
    private var currentPmRegistrationTireType = PMConstant.PMTierType.POWER_MERCHANT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getShopLevelInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeShopLevelInfo()
        setOnSwipeToRefresh()
    }

    override fun observePowerMerchantBasicInfo() {
        observe(sharedViewModel.powerMerchantBasicInfo) {
            when (it) {
                is Success -> {
                    showRegistrationFragment()
                    reSetupFooterView()
                    initBasicInfo(it.data)
                    renderPmRegistrationWidgets()
                }
                is Fail -> {
                    hideFooterView()
                    showErrorState(it.throwable)
                    logToCrashlytic(
                        PowerMerchantErrorLogger.PM_BASIC_INFO_ERROR,
                        it.throwable
                    )
                }
            }
            dismissRefreshIndicator()
        }
    }

    override fun onMoreDetailPMEligibilityClicked() {
        val widgetIndex = adapter.data.indexOfFirst { it is WidgetGradeBenefitUiModel }
        if (widgetIndex != RecyclerView.NO_POSITION) {
            recyclerView?.smoothScrollToPosition(widgetIndex)
            recyclerView?.post {
                adapter.notifyItemChanged(widgetIndex)
            }
        }
    }

    override fun showShopLevelInfoBottomSheet() {
        powerMerchantTracking.sendEventClickTooltipShopLevel(
            shopLevelInfo?.shopLevel.orZero().toString()
        )
        shopLevelInfo?.let { shopLevel ->
            if (childFragmentManager.isStateSaved) return

            val bottomSheetShopTooltipLevel = BottomSheetShopTooltipLevel.createInstance(
                shopLevel = shopLevel.shopLevel.toLong(),
                shopIncome = shopLevel.netItemValue.toString(),
                productSold = shopLevel.itemSold.toString(),
                period = shopLevel.period,
                nextUpdate = shopLevel.nextUpdate
            )
            bottomSheetShopTooltipLevel.show(childFragmentManager)
        }
    }

    fun setOnFooterCtaClickedListener(
        term: RegistrationTermUiModel?,
        isEligiblePm: Boolean,
        tncAgreed: Boolean
    ) {
        pmBasicInfo?.shopInfo?.let { shopInfo ->
            when {
                isModeratedShop -> showModeratedShopBottomSheet()
                isEligiblePm -> submitPmRegistrationOnEligible(tncAgreed)
                term is RegistrationTermUiModel.ShopScore -> showShopScoreTermBottomSheet(shopInfo)
                term is RegistrationTermUiModel.ActiveProduct -> showActiveProductTermBottomSheet()
                term is RegistrationTermUiModel.Order -> showOrderTermBottomSheet(shopInfo.itemSoldPmProThreshold)
                term is RegistrationTermUiModel.NetItemValue -> showNivTermBottomSheet(shopInfo.netItemValuePmProThreshold)
                term is RegistrationTermUiModel.Kyc -> submitKYC(tncAgreed)
            }
        }
    }

    private fun observeShopLevelInfo() {
        observe(mViewModel.shopLevelInfo) {
            when (it) {
                is Success -> {
                    this.shopLevelInfo = it.data
                }
                is Fail -> logToCrashlytic(
                    PowerMerchantErrorLogger.PM_SHOP_LEVEL_INFO_ERROR,
                    it.throwable
                )
            }
        }
    }

    private fun getShopLevelInfo() {
        mViewModel.getShopLevelInfo()
    }

    private fun renderPmRegistrationWidgets() {
        pmBasicInfo?.shopInfo?.let { shopInfo ->
            val isRegularMerchant = pmBasicInfo?.pmStatus?.isRegularMerchant() == true
            val registrationHeaderWidget = getRegistrationHeaderWidgetData(
                shopInfo, isRegularMerchant
            )

            renderPmRegistrationWidget(registrationHeaderWidget)
        }
    }

    private fun dismissRefreshIndicator() {
        binding?.swipeRefreshPm?.isRefreshing = false
    }

    private fun setOnSwipeToRefresh() {
        binding?.swipeRefreshPm?.setOnRefreshListener {
            fetchPowerMerchantBasicInfo()
            getShopLevelInfo()
        }
    }

    private fun renderPmRegistrationWidget(headerWidget: WidgetRegistrationHeaderUiModel) {
        val tickerList = pmBasicInfo?.tickers
        val widgets = mutableListOf<BaseWidgetUiModel>()
        if (!tickerList.isNullOrEmpty() && !isModeratedShop) {
            widgets.add(WidgetTickerUiModel(tickerList))
        }
        val mainWidgets = listOf(
            WidgetBannerPMRegistration,
            headerWidget,
            WidgetDividerUiModel,
            WidgetPotentialUiModel(pmBasicInfo?.shopInfo?.shopScore.orZero()),
            getPmGradeBenefitWidget()
        )
        widgets.addAll(mainWidgets)
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
            shopScore = shopInfo?.shopScore.orZero()
        )
    }

    private fun showShopScoreTermBottomSheet(shopInfo: PMShopInfoUiModel) {
        val title = getString(R.string.pm_bottom_sheet_shop_score_title)
        val description =
            getString(R.string.pm_bottom_sheet_shop_score_description, shopInfo.shopScoreThreshold)
        val ctaText = getString(R.string.pm_learn_shop_performance)
        val illustrationUrl = PMConstant.Images.PM_NEW_REQUIREMENT

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

        powerMerchantTracking.sendEventShowPopupAddNewProduct(
            pmBasicInfo?.shopInfo?.shopScore.orZero().toString()
        )
        powerMerchantTracking.sendEventClickInterestedToRegister()
    }

    private fun submitPmRegistrationOnEligible(isTncChecked: Boolean) {
        if (!isTncChecked) {
            val message = getString(R.string.pm_tnc_agreement_error_message)
            val actionText = getString(R.string.power_merchant_ok_label)
            showErrorToaster(message, actionText)
            return
        }

        submitPmRegistration()
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

        val appLink = PMConstant.AppLink.KYC_POWER_MERCHANT
        RouteManager.route(context, appLink)
    }

    private fun getRegistrationHeaderWidgetData(
        shopInfo: PMShopInfoUiModel,
        isRegularMerchant: Boolean
    ): WidgetRegistrationHeaderUiModel {
        return WidgetRegistrationHeaderUiModel(
            shopInfo = shopInfo,
            registrationTerms = PMRegistrationTermHelper.getPmRegistrationTerms(
                requireContext(), shopInfo, isRegularMerchant
            ),
            selectedPmType = currentPmRegistrationTireType
        )
    }
}