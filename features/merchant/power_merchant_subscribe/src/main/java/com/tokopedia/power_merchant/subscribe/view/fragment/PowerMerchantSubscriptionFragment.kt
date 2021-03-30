package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.*
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusAndShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.PMWidgetListener
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.ContentSliderBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.DeactivationBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.PMNotificationBottomSheet
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.*
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSubscriptionViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_pm_power_merchant_subscription.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PowerMerchantSubscriptionFragment : BaseListFragment<BaseWidgetUiModel, WidgetAdapterFactoryImpl>(),
        PMWidgetListener {

    companion object {
        fun createInstance(): PowerMerchantSubscriptionFragment {
            return PowerMerchantSubscriptionFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mViewModel: PowerMerchantSubscriptionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PowerMerchantSubscriptionViewModel::class.java)
    }

    private var pmStatusAndShopInfo: PMStatusAndShopInfoUiModel? = null
    private var pmSettingInfo: PowerMerchantSettingInfoUiModel? = null

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvPmRegistration

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl = WidgetAdapterFactoryImpl(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pm_power_merchant_subscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observePmStatusAndShopInfo()
        observePmActivationStatus()
        showSuccessRegistrationPopupEndGamePeriod()
    }

    override fun onItemClicked(t: BaseWidgetUiModel?) {}

    override fun loadData(page: Int) {}

    override fun setOnDeactivatePMClickListener() {
        DeactivationBottomSheet
    }

    override fun setOnReloadClickListener() {
        reloadPageContent()
    }

    override fun setOnTickerWidgetRemoved(position: Int) {
        adapter.data.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    fun setPmSettingInfo(pmSettingInfo: PowerMerchantSettingInfoUiModel) {
        this.pmSettingInfo = pmSettingInfo
    }

    private fun setupView() = view?.run {
        swipeRefreshPm.setOnRefreshListener {
            reloadPageContent()
        }
    }

    private fun observePmStatusAndShopInfo() {
        fetchPmStatusAndShopInfo()
        mViewModel.pmStatusAndShopInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    this.pmStatusAndShopInfo = it.data
                    fetchPageContent()
                }
                is Fail -> {
                    showErrorState()
                    hideSwipeRefreshLoading()
                    logToCrashlytic(PowerMerchantErrorLogger.PM_STATUS_AND_SHOP_INFO_ERROR, it.throwable)
                }
            }
        })
    }

    private fun observePmActivationStatus() {
        mViewModel.pmActivationStatus.observe(viewLifecycleOwner, Observer {
            hideActivationProgress()
            when (it) {
                is Success -> setOnPmActivationSuccess()
                is Fail -> setOnPmActivationFail()
            }
        })
    }

    private fun reloadPageContent() {
        if (pmStatusAndShopInfo == null) {
            mViewModel.getPmStatusAndShopInfo()
        } else {
            view?.swipeRefreshPm?.isRefreshing = true
            fetchPageContent()
        }
    }

    private fun fetchPmStatusAndShopInfo() {
        showLoadingState()
        mViewModel.getPmStatusAndShopInfo()
    }

    private fun fetchPageContent() {
        if (pmStatusAndShopInfo == null) {
            showErrorState()
            return
        }

        when (pmStatusAndShopInfo?.pmStatus?.status) {
            PMStatusConst.INACTIVE -> observePmRegistrationPage()
            else -> observePmActiveState()
        }
    }

    private fun observePmRegistrationPage() {
        mViewModel.shopInfoAndPMGradeBenefits.observe(viewLifecycleOwner, Observer {
            hideSwipeRefreshLoading()
            when (it) {
                is Success -> renderRegistrationPM(it.data)
                is Fail -> {
                    showErrorState()
                    logToCrashlytic(PowerMerchantErrorLogger.REGISTRATION_PAGE_ERROR, it.throwable)
                }
            }
        })

        mViewModel.getPmRegistrationData()
    }

    private fun showLoadingState() {
        adapter.data.clear()
        renderList(listOf(WidgetLoadingStateUiModel))
        view?.pmRegistrationFooterView?.gone()
    }

    private fun showErrorState() {
        adapter.data.clear()
        renderList(listOf(WidgetErrorStateUiModel))
        view?.pmRegistrationFooterView?.gone()
    }

    private fun renderRegistrationPM(data: PMGradeBenefitAndShopInfoUiModel) {
        val widgets = listOf(
                getHeaderWidgetData(data.shopInfo),
                getPotentialBenefitWidgetData(),
                WidgetDividerUiModel,
                WidgetGradeBenefitUiModel(benefitPages = data.gradeBenefitList)
        )
        adapter.data.clear()
        renderList(widgets)

        setupFooterCta(data.shopInfo)
    }

    private fun setOnPmActivationSuccess() {
        if (pmSettingInfo?.periodeType == PeriodType.TRANSITION_PERIOD) {
            showSuccessRegistrationPopupTransitionPeriod()
        } else {
            showSuccessRegistrationPopupEndGamePeriod()
        }
    }

    private fun setOnPmActivationFail() {
        val errorMessage = "Aktivasi Power Merchant gagal, coba lagi!"
        showErrorToaster(errorMessage)
    }

    private fun showActivationProgress() {
        view?.pmRegistrationFooterView?.showRegistrationProgress()
    }

    private fun hideActivationProgress() {
        view?.pmRegistrationFooterView?.hideRegistrationProgress()
    }

    private fun showSuccessRegistrationPopupTransitionPeriod() {
        val title = getString(R.string.pm_registration_success_title)
        val description = getString(R.string.pm_registration_success_description)
        val ctaText = getString(R.string.pm_learn_new_pm)
        val illustrationUrl = PMConstant.Images.PM_REGISTRATION_SUCCESS
        var reloadPageOnDismiss = true
        showNotificationBottomSheet(title, description, ctaText, illustrationUrl, callback = {
            reloadPageOnDismiss = false
            if (pmStatusAndShopInfo?.shopInfo?.isNewSeller == true) {
                showNewPowerMerchantInfoPopup()
            } else {
                showPerubahanPowerMerchantPopup()
            }
        }, onDismiss = {
            if (reloadPageOnDismiss) {
                fetchPmStatusAndShopInfo()
            }
        })
    }

    private fun showSuccessRegistrationPopupEndGamePeriod() {
        val title = getString(R.string.pm_registration_success_title)
        val description = getString(R.string.pm_registration_success_description_end_game)
        val ctaText = getString(R.string.pm_see_the_next)
        val illustrationUrl = PMConstant.Images.PM_REGISTRATION_SUCCESS
        showNotificationBottomSheet(title, description, ctaText, illustrationUrl, onDismiss = {
            fetchPmStatusAndShopInfo()
        })
    }

    private fun showNotificationBottomSheet(title: String, description: String, ctaText: String, imgUrl: String,
                                            callback: (() -> Unit)? = null, onDismiss: (() -> Unit)? = null) {
        val notifBottomSheet = PMNotificationBottomSheet.createInstance(title, description, imgUrl)
        if (!notifBottomSheet.isAdded && !childFragmentManager.isStateSaved) {
            notifBottomSheet.setPrimaryButtonClickListener(ctaText) {
                callback?.invoke()
                notifBottomSheet.dismiss()
            }
            notifBottomSheet.setOnDismissListener {
                onDismiss?.invoke()
            }
            Handler().post {
                notifBottomSheet.show(childFragmentManager)
            }
        }
    }

    private fun showNewPowerMerchantInfoPopup() {
        val bottomSheet = ContentSliderBottomSheet.createInstance(childFragmentManager)
        if (bottomSheet.isAdded) return
        val slideItems = listOf(ContentSliderUiModel(
                title = getString(R.string.pm_new_pm_inf_title),
                description = getString(R.string.pm_new_pm_inf_description),
                imgUrl = PMConstant.Images.PM_NEW_REQUIREMENT
        ))
        with(bottomSheet) {
            val emptyTitle = ""
            setContent(emptyTitle, slideItems)
            setOnPrimaryCtaClickListener {
                dismiss()
            }
            setOnSecondaryCtaClickListener {
                //todo : goto ss interrupt page
                dismiss()
            }
            setOnDismissListener {
                fetchPmStatusAndShopInfo()
            }
            show(this@PowerMerchantSubscriptionFragment.childFragmentManager)
        }
    }

    private fun showPerubahanPowerMerchantPopup() {
        val bottomSheet = ContentSliderBottomSheet.createInstance(childFragmentManager)
        if (bottomSheet.isAdded && childFragmentManager.isStateSaved) return

        val title = getString(R.string.pm_power_merchant_slider_title)
        val slideItems = listOf(
                ContentSliderUiModel(
                        title = getString(R.string.pm_power_merchant_new_term_title),
                        description = getString(R.string.pm_power_merchant_new_term_description),
                        imgUrl = PMConstant.Images.PM_NEW_REQUIREMENT
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_integrated_with_reputation_title),
                        description = getString(R.string.pm_integrated_with_reputation_description),
                        imgUrl = PMConstant.Images.PM_INTEGRATED_WITH_REPUTATION
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_new_benefits_title),
                        description = getString(R.string.pm_new_benefits_description),
                        imgUrl = PMConstant.Images.PM_NEW_BENEFITS
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_new_benefits_title),
                        description = getString(R.string.pm_new_schema_description),
                        imgUrl = PMConstant.Images.PM_NEW_SCHEMA
                )
        )

        with(bottomSheet) {
            setContent(title, slideItems)
            setOnPrimaryCtaClickListener {
                dismiss()
            }
            setOnSecondaryCtaClickListener {
                //todo : goto ss interrupt page
                bottomSheet.dismiss()
            }
            setOnDismissListener {
                fetchPmStatusAndShopInfo()
            }
            show(this@PowerMerchantSubscriptionFragment.childFragmentManager)
        }
    }

    private fun setupFooterCta(shopInfo: PMShopInfoUiModel) = view?.run {
        val isEligibleShopScore = !shopInfo.isNewSeller && shopInfo.isEligibleShopScore
        val hasActiveProduct = shopInfo.isNewSeller && shopInfo.hasActiveProduct
        val needTnC = (!shopInfo.isKyc && (isEligibleShopScore || hasActiveProduct))
                || (shopInfo.isKyc && (isEligibleShopScore || hasActiveProduct))
        if (shopInfo.kycStatusId == KYCStatusId.PENDING) {
            pmRegistrationFooterView.gone()
        } else {
            pmRegistrationFooterView.visible()
        }
        pmRegistrationFooterView.setTnCVisibility(needTnC)
        val ctaText = if (needTnC || shopInfo.isNewSeller) {
            getString(R.string.power_merchant_register_now)
        } else {
            getString(R.string.pm_interested_to_register)
        }
        pmRegistrationFooterView.setCtaText(ctaText)

        pmRegistrationFooterView.setOnCtaClickListener {
            when {
                shopInfo.isEligiblePm -> submitPMRegistration(it)
                !shopInfo.isKyc && ((!shopInfo.isNewSeller && shopInfo.isEligibleShopScore)
                        || (shopInfo.isNewSeller && shopInfo.hasActiveProduct)) -> submitKYC(it)
                else -> showRegistrationTermBottomSheet(shopInfo)
            }
        }
    }

    private fun submitKYC(isTncChecked: Boolean) {
        if (!isTncChecked) {
            val message = getString(R.string.pm_tnc_agreement_error_message)
            showErrorToaster(message)
            return
        }

        RouteManager.route(context, ApplinkConst.KYC_SELLER_DASHBOARD)
    }

    private fun showErrorToaster(message: String) {
        view?.run {
            val actionText = getString(R.string.power_merchant_ok_label)
            Toaster.toasterCustomBottomHeight = context.resources.getDimensionPixelSize(R.dimen.pm_spacing_100dp)
            Toaster.build(rvPmRegistration, message, Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR, actionText)
                    .show()
        }
    }

    private fun showRegistrationTermBottomSheet(shopInfo: PMShopInfoUiModel) {
        val isNewSeller = shopInfo.isNewSeller
        val title: String
        val description: String
        val ctaText: String
        val illustrationUrl: String
        val appLink: String

        if (isNewSeller) {
            title = getString(R.string.pm_bottom_sheet_active_product_title)
            description = getString(R.string.pm_bottom_sheet_active_product_description)
            ctaText = getString(R.string.pm_add_product)
            illustrationUrl = Constant.ImageUrl.ADD_PRODUCT_BOTTOM_SHEET
            appLink = ApplinkConst.SellerApp.PRODUCT_ADD
        } else {
            title = getString(R.string.pm_bottom_sheet_shop_score_title)
            description = getString(R.string.pm_bottom_sheet_shop_score_description)
            ctaText = getString(R.string.pm_learn_shop_performance)
            illustrationUrl = Constant.ImageUrl.SHOP_SCORE_BOTTOM_SHEET
            appLink = ApplinkConst.SHOP_SCORE_DETAIL
        }

        val shopScoreBottomSheet = PMNotificationBottomSheet.createInstance(title, description, illustrationUrl)
        shopScoreBottomSheet.setPrimaryButtonClickListener(ctaText) {
            shopScoreBottomSheet.dismiss()
            RouteManager.route(context, appLink)
        }

        //to prevent IllegalStateException: Fragment already added
        if (shopScoreBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        Handler().post {
            shopScoreBottomSheet.show(childFragmentManager)
        }
    }

    private fun submitPMRegistration(isTncChecked: Boolean) {
        if (!isTncChecked) {
            val message = getString(R.string.pm_tnc_agreement_error_message)
            showErrorToaster(message)
            return
        }

        showActivationProgress()
        mViewModel.submitPMActivation()
    }

    private fun observePmActiveState() {
        view?.pmRegistrationFooterView?.gone()
        mViewModel.pmPmActiveData.observe(viewLifecycleOwner, Observer {
            hideSwipeRefreshLoading()
            when (it) {
                is Success -> renderPMActiveState(it.data)
                is Fail -> {
                    showErrorState()
                    logToCrashlytic(PowerMerchantErrorLogger.PM_ACTIVE_IDLE_PAGE_ERROR, it.throwable)
                }
            }
        })

        mViewModel.getPmActiveData()
    }

    private fun renderPMActiveState(data: PMActiveDataUiModel) {
        val widgets = mutableListOf<BaseWidgetUiModel>()
        val tickers = pmSettingInfo?.tickers
        if (!tickers.isNullOrEmpty()) {
            widgets.add(WidgetTickerUiModel(tickers))
        }
        widgets.add(WidgetQuitSubmissionUiModel(data.expiredTime))
        widgets.add(getShopGradeWidgetData(data))
        widgets.add(WidgetDividerUiModel)
        widgets.add(getCurrentShopGradeBenefit(data))
        widgets.add(WidgetDividerUiModel)
        if (data.nextPMGrade != null) {
            widgets.add(getNextShopGradeWidgetData(data))
            widgets.add(WidgetDividerUiModel)
        }
        widgets.add(WidgetNextUpdateUiModel(data.nextQuarterlyCalibrationRefreshDate))
        widgets.add(WidgetSingleCtaUiModel(getString(R.string.pm_pm_transition_period_learnmore), Constant.Url.POWER_MERCHANT_EDU))
        widgets.add(WidgetPMDeactivateUiModel)
        adapter.data.clear()
        renderList(widgets)
    }

    private fun getNextShopGradeWidgetData(data: PMActiveDataUiModel): WidgetNextShopGradeUiModel {
        val nextGrade = data.nextPMGrade
        return WidgetNextShopGradeUiModel(
                shopLevel = nextGrade?.shopLevel.orZero(),
                shopScoreMin = nextGrade?.shopScoreMin.orZero(),
                gradeName = nextGrade?.gradeName ?: PMShopGrade.NO_GRADE,
                gradeBadgeUrl = nextGrade?.imgBadgeUrl.orEmpty(),
                benefitList = data.nextPMBenefits?.map { it.benefitName }.orEmpty()
        )
    }

    private fun getCurrentShopGradeBenefit(data: PMActiveDataUiModel): WidgetExpandableUiModel {
        val grade = data.currentPMGrade
        val benefits = mutableListOf<BaseExpandableItemUiModel>()
        data.currentPMBenefits?.forEach { benefit ->
            if (!benefits.any { it.text == benefit.categoryName }) {
                benefits.add(ExpandableSectionUiModel(benefit.categoryName))
            }
            if (benefit.benefitName.isNotBlank()) {
                benefits.add(ExpandableItemUiModel(benefit.benefitName, benefit.appLink.orEmpty()))
            }
        }
        return WidgetExpandableUiModel(
                title = getString(R.string.pm_benefit_shop_grade, grade?.gradeName.orEmpty()),
                items = benefits
        )
    }

    private fun getShopGradeWidgetData(data: PMActiveDataUiModel): WidgetShopGradeUiModel {
        val shopGrade = data.currentPMGrade
        val shopScoreThreshold = pmStatusAndShopInfo?.shopInfo?.shopScoreThreshold.orZero()
        return WidgetShopGradeUiModel(
                shopGrade = shopGrade?.gradeName.orEmpty(),
                shopScore = shopGrade?.shopScore.orZero(),
                threshold = shopScoreThreshold,
                shopLevel = shopGrade?.shopLevel ?: ShopLevel.NO_LEVEL,
                gradeBadgeImgUrl = shopGrade?.imgBadgeUrl.orEmpty(),
                gradeBackgroundUrl = shopGrade?.backgroundUrl.orEmpty(),
                nextPmCalculationDate = data.nextMonthlyRefreshDate,
                pmStatus = data.pmStatus
        )
    }

    private fun getHeaderWidgetData(shopInfo: PMShopInfoUiModel): WidgetRegistrationHeaderUiModel {
        return WidgetRegistrationHeaderUiModel(
                shopInfo = shopInfo,
                terms = PMRegistrationTermHelper.getPMRegistrationTerms(requireContext(), shopInfo)
        )
    }

    private fun getPotentialBenefitWidgetData(): WidgetPotentialUiModel {
        return WidgetPotentialUiModel(listOf(
                PotentialItemUiModel(
                        resDrawableIcon = R.drawable.ic_pm_benefit_01,
                        description = getString(R.string.pm_potential_benefit_01)
                ),
                PotentialItemUiModel(
                        resDrawableIcon = R.drawable.ic_pm_benefit_02,
                        description = getString(R.string.pm_potential_benefit_02)
                ),
                PotentialItemUiModel(
                        resDrawableIcon = R.drawable.ic_pm_benefit_03,
                        description = getString(R.string.pm_potential_benefit_03)
                )
        ))
    }

    private fun logToCrashlytic(message: String, throwable: Throwable) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    private fun hideSwipeRefreshLoading() {
        view?.swipeRefreshPm?.isRefreshing = false
    }
}