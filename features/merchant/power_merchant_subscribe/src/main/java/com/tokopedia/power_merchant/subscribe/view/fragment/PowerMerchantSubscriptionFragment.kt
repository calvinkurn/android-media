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
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.*
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusAndShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.gm.common.utils.PMShopScoreInterruptHelper
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
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
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.PowerMerchantCancelBottomSheet
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.*
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSubscriptionViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_pm_power_merchant_subscription.view.*
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeUnit
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

    @Inject
    lateinit var interruptHelper: PMShopScoreInterruptHelper

    private val mViewModel: PowerMerchantSubscriptionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PowerMerchantSubscriptionViewModel::class.java)
    }

    private var pmStatusAndShopInfo: PMStatusAndShopInfoUiModel? = null
    private var pmSettingInfo: PowerMerchantSettingInfoUiModel? = null
    private var cancelPmDeactivationWidgetPosition: Int? = null

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
        observePmActiveState()
        observePmRegistrationPage()
        observePmActivationStatus()
        observePmCancelDeactivationSubmission()
    }

    override fun onItemClicked(t: BaseWidgetUiModel?) {}

    override fun loadData(page: Int) {}

    override fun setOnDeactivatePMClickListener() {
        val bottomSheet = PowerMerchantCancelBottomSheet.newInstance(getExpiredTimeFmt(), pmStatusAndShopInfo?.isFreeShippingEnabled.orFalse())
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return

        bottomSheet.setListener(object : PowerMerchantCancelBottomSheet.BottomSheetCancelListener {
            override fun onClickCancelButton() {
                showDeactivationQuestionnaire()
                bottomSheet.dismiss()
            }

            override fun onClickBackButton() {
                bottomSheet.dismiss()
            }
        })
        bottomSheet.show(childFragmentManager)
    }

    override fun cancelPmDeactivationSubmission(position: Int) {
        cancelPmDeactivationWidgetPosition = position
        mViewModel.cancelPmDeactivationSubmission()
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

    private fun getExpiredTimeFmt(): String {
        val expiredTime = pmStatusAndShopInfo?.pmStatus?.expiredTime.orEmpty()
        return try {
            val currentFormat = "dd MMMM yyyy HH:mm:ss"
            val dateFormat = "dd MMMM yyyy"
            val hourFormat = "HH.mm"
            val dateStr = DateFormatUtils.formatDate(currentFormat, dateFormat, expiredTime)
            val hourStr = DateFormatUtils.formatDate(currentFormat, hourFormat, expiredTime)
            getString(R.string.pm_expired_time_format, dateStr, hourStr)
        } catch (e: Exception) {
            expiredTime
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
                is Fail -> {
                    val actionText = getString(R.string.pm_try_again)
                    showErrorToaster(getErrorMessage(it.throwable), actionText)
                }
            }
        })
    }

    private fun observePmCancelDeactivationSubmission() {
        mViewModel.pmCancelDeactivationStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    showCancelDeactivationToaster()
                    fetchPmStatusAndShopInfo()
                }
                is Fail -> {
                    cancelPmDeactivationWidgetPosition?.let { position ->
                        adapter.notifyItemChanged(position)
                    }
                    val actionText = getString(R.string.pm_try_again)
                    showErrorToaster(getErrorMessage(it.throwable), actionText)
                }
            }
        })
    }

    private fun showCancelDeactivationToaster() {
        view?.run {
            val message = getString(R.string.pm_cancel_pm_deactivation_success)
            val actionText = getString(R.string.power_merchant_ok_label)
            Toaster.toasterCustomBottomHeight = context.resources.getDimensionPixelSize(R.dimen.layout_lvl2)
            Toaster.build(rvPmRegistration, message, Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL, actionText)
                    .show()
        }
    }

    private fun getErrorMessage(t: Throwable): String {
        return when (t) {
            is UnknownHostException -> getString(R.string.pm_system_error_message)
            else -> getString(R.string.pm_network_error_message)
        }
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
            PMStatusConst.INACTIVE -> fetchPmRegistrationData()
            else -> fetchPmActiveState()
        }
    }

    private fun fetchPmRegistrationData() {
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

    private fun renderPmRegistrationPM(data: PMGradeBenefitAndShopInfoUiModel) {
        if (pmStatusAndShopInfo?.pmStatus?.status != PMStatusConst.INACTIVE) return

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

    private fun showActivationProgress() {
        view?.pmRegistrationFooterView?.showRegistrationProgress()
    }

    private fun hideActivationProgress() {
        view?.pmRegistrationFooterView?.hideRegistrationProgress()
    }

    private fun showSuccessRegistrationPopupTransitionPeriod() {
        val title = getString(R.string.pm_registration_success_title)
        val description = getString(R.string.pm_registration_success_description, PMConstant.TRANSITION_PERIOD_START_DATE)
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
                openShopScoreInterruptPage()
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
                        description = getString(R.string.pm_power_merchant_new_term_description, PMConstant.TRANSITION_PERIOD_START_DATE),
                        imgUrl = PMConstant.Images.PM_NEW_REQUIREMENT
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_integrated_with_reputation_title),
                        description = getString(R.string.pm_integrated_with_reputation_description, PMConstant.TRANSITION_PERIOD_START_DATE),
                        imgUrl = PMConstant.Images.PM_INTEGRATED_WITH_REPUTATION
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_new_benefits_title),
                        description = getString(R.string.pm_new_benefits_description),
                        imgUrl = PMConstant.Images.PM_NEW_BENEFITS
                ),
                ContentSliderUiModel(
                        title = getString(R.string.pm_new_benefits_title),
                        description = getString(R.string.pm_new_schema_description, PMConstant.TRANSITION_PERIOD_START_DATE),
                        imgUrl = PMConstant.Images.PM_NEW_SCHEMA
                )
        )

        with(bottomSheet) {
            setContent(title, slideItems)
            setOnPrimaryCtaClickListener {
                dismiss()
            }
            setOnSecondaryCtaClickListener {
                openShopScoreInterruptPage()
                bottomSheet.dismiss()
            }
            setOnDismissListener {
                fetchPmStatusAndShopInfo()
            }
            show(this@PowerMerchantSubscriptionFragment.childFragmentManager)
        }
    }

    private fun openShopScoreInterruptPage() {
        activity?.let {
            interruptHelper.openInterruptPage(it)
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
            val actionText = getString(R.string.power_merchant_ok_label)
            showErrorToaster(message, actionText)
            return
        }

        RouteManager.route(context, ApplinkConst.KYC_SELLER_DASHBOARD)
    }

    private fun showErrorToaster(message: String, actionText: String) {
        view?.run {
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
            illustrationUrl = PMConstant.Images.PM_ADD_PRODUCT_BOTTOM_SHEET
            appLink = ApplinkConst.SellerApp.PRODUCT_ADD
        } else {
            title = getString(R.string.pm_bottom_sheet_shop_score_title)
            description = getString(R.string.pm_bottom_sheet_shop_score_description)
            ctaText = getString(R.string.pm_learn_shop_performance)
            illustrationUrl = PMConstant.Images.PM_SHOP_SCORE_NOT_ELIGIBLE_BOTTOM_SHEET
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
            val actionText = getString(R.string.power_merchant_ok_label)
            showErrorToaster(message, actionText)
            return
        }

        showActivationProgress()
        mViewModel.submitPMActivation()
    }

    private fun observePmActiveState() {
        mViewModel.pmPmActiveData.observe(viewLifecycleOwner, Observer {
            hideSwipeRefreshLoading()
            when (it) {
                is Success -> renderPmActiveState(it.data)
                is Fail -> {
                    showErrorState()
                    logToCrashlytic(PowerMerchantErrorLogger.PM_ACTIVE_IDLE_PAGE_ERROR, it.throwable)
                }
            }
        })
    }

    private fun fetchPmActiveState() {
        mViewModel.getPmActiveData()
    }

    private fun renderPmActiveState(data: PMActiveDataUiModel) {
        if(pmStatusAndShopInfo?.pmStatus?.status == PMStatusConst.INACTIVE) return

        view?.pmRegistrationFooterView?.gone()
        val isAutoExtendEnabled = getAutoExtendEnabled()
        val widgets = mutableListOf<BaseWidgetUiModel>()
        if (!pmSettingInfo?.tickers.isNullOrEmpty() && isAutoExtendEnabled) {
            widgets.add(WidgetTickerUiModel(pmSettingInfo?.tickers.orEmpty()))
        }
        if (!isAutoExtendEnabled) {
            widgets.add(WidgetCancelDeactivationSubmissionUiModel(getExpiredTimeFmt()))
        }
        widgets.add(getShopGradeWidgetData(data))
        widgets.add(WidgetDividerUiModel)
        widgets.add(getCurrentShopGradeBenefit(data))
        widgets.add(WidgetDividerUiModel)
        val shouldShowNextGradeWidget = data.nextPMGrade != null && isAutoExtendEnabled
                && data.currentPMGrade?.gradeName != PMShopGrade.DIAMOND
                && !pmStatusAndShopInfo?.shopInfo?.isNewSeller.orTrue()
        if (shouldShowNextGradeWidget) {
            widgets.add(getNextShopGradeWidgetData(data))
            widgets.add(WidgetDividerUiModel)
        }
        if (isAutoExtendEnabled && !pmStatusAndShopInfo?.shopInfo?.isNewSeller.orTrue()) {
            widgets.add(WidgetNextUpdateUiModel(data.nextQuarterlyCalibrationRefreshDate))
        }
        widgets.add(WidgetSingleCtaUiModel(getString(R.string.pm_pm_transition_period_learnmore), Constant.Url.POWER_MERCHANT_EDU))
        if (isAutoExtendEnabled) {
            widgets.add(WidgetPMDeactivateUiModel)
        }
        adapter.data.clear()
        renderList(widgets)
    }

    private fun observePmRegistrationPage() {
        mViewModel.shopInfoAndPMGradeBenefits.observe(viewLifecycleOwner, Observer {
            hideSwipeRefreshLoading()
            when (it) {
                is Success -> renderPmRegistrationPM(it.data)
                is Fail -> {
                    showErrorState()
                    logToCrashlytic(PowerMerchantErrorLogger.REGISTRATION_PAGE_ERROR, it.throwable)
                }
            }
        })
    }

    private fun getAutoExtendEnabled(): Boolean {
        val expiredTime = pmStatusAndShopInfo?.pmStatus?.expiredTime
        val autoExtendEnabled = pmStatusAndShopInfo?.pmStatus?.autoExtendEnabled.orTrue()
        return !expiredTime.isNullOrBlank() && autoExtendEnabled
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
                isNewSeller = pmStatusAndShopInfo?.shopInfo?.isNewSeller.orTrue(),
                shopGrade = shopGrade?.gradeName.orEmpty(),
                shopScore = shopGrade?.shopScore.orZero(),
                shopAge = pmStatusAndShopInfo?.shopInfo?.shopAge ?: 1,
                threshold = shopScoreThreshold,
                gradeBadgeImgUrl = shopGrade?.imgBadgeUrl.orEmpty(),
                gradeBackgroundUrl = shopGrade?.backgroundUrl.orEmpty(),
                nextPmCalculationDate = getPmNextCalculationDate(),
                newSellerTenure = getNewSellerTenure(),
                pmStatus = data.pmStatus
        )
    }

    private fun getNewSellerTenure(): String {
        val daysBecomeExisting = 90
        val shopAge = pmStatusAndShopInfo?.shopInfo?.shopAge ?: 1
        val remainingDays = daysBecomeExisting.minus(shopAge)
        val remainingDaysMillis = TimeUnit.DAYS.toMillis(remainingDays.toLong())
        val endOfTenureMillis = Date().time.plus(remainingDaysMillis)
        val dateFormat = "dd MMMM yyyy"
        return DateFormatUtils.getFormattedDate(endOfTenureMillis, dateFormat)
    }

    private fun getPmNextCalculationDate(): String {
        val shopInfo = pmStatusAndShopInfo?.shopInfo
        return if (shopInfo?.isNewSeller.orTrue()) {
            val day60ofTenure = 60
            val shopAge = shopInfo?.shopAge ?: 1
            return if (shopAge < day60ofTenure) {
                getNewSellerTenure()
            } else {
                pmSettingInfo?.periodeEndDate.orEmpty()
            }
        } else {
            pmSettingInfo?.periodeEndDate.orEmpty()
        }
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

    private fun showDeactivationQuestionnaire() {
        val bottomSheet = DeactivationBottomSheet.createInstance()
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return
        bottomSheet.show(childFragmentManager)
        bottomSheet.setOnDeactivationSuccess {
            fetchPmStatusAndShopInfo()
        }
    }

    private fun logToCrashlytic(message: String, throwable: Throwable) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    private fun hideSwipeRefreshLoading() {
        view?.swipeRefreshPm?.isRefreshing = false
    }
}