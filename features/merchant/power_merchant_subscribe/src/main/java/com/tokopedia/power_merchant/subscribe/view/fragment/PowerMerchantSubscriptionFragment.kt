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
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantBasicInfoUiModel
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

    private var pmBasicInfo: PowerMerchantBasicInfoUiModel? = null
    private var cancelPmDeactivationWidgetPosition: Int? = null
    private var pmGradeBenefitAndShopInfo: PMGradeBenefitAndShopInfoUiModel? = null
    private var selectedPmType = PMConstant.PMType.PM_REGULAR

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

        observePowerMerchantBasicInfo()
        observePmActiveState()
        observePmRegistrationPage()
        observePmActivationStatus()
        observePmCancelDeactivationSubmission()
    }

    override fun onItemClicked(t: BaseWidgetUiModel?) {}

    override fun loadData(page: Int) {}

    override fun setOnDeactivatePMClickListener() {
        val bottomSheet = PowerMerchantCancelBottomSheet.newInstance(getExpiredTimeFmt(), pmBasicInfo?.isFreeShippingEnabled.orFalse())
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

    override fun onPowerMerchantSectionClickListener(headerWidget: WidgetRegistrationHeaderUiModel) {
        selectedPmType = headerWidget.selectedPmType
        renderRegularPmRegistrationWidget(headerWidget)
    }

    override fun onPowerMerchantProSectionClickListener(headerWidget: WidgetRegistrationHeaderUiModel) {
        selectedPmType = headerWidget.selectedPmType
        renderPmProRegistrationWidgets(headerWidget)
    }

    private fun setupView() = view?.run {
        swipeRefreshPm.setOnRefreshListener {
            reloadPageContent()
        }
    }

    private fun getExpiredTimeFmt(): String {
        val expiredTime = pmBasicInfo?.pmStatus?.expiredTime.orEmpty()
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

    private fun observePowerMerchantBasicInfo() {
        fetchPowerMerchantBasicInfo()
        mViewModel.powerMerchantBasicInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    this.pmBasicInfo = it.data
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
                    fetchPowerMerchantBasicInfo()
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
        if (pmBasicInfo == null) {
            mViewModel.getPowerMerchantBasicInfo()
        } else {
            view?.swipeRefreshPm?.isRefreshing = true
            fetchPageContent()
        }
    }

    private fun fetchPowerMerchantBasicInfo() {
        showLoadingState()
        mViewModel.getPowerMerchantBasicInfo()
    }

    private fun fetchPageContent() {
        if (pmBasicInfo == null) {
            showErrorState()
            return
        }

        when (pmBasicInfo?.pmStatus?.status) {
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

    private fun initPmActiveData(data: PMGradeBenefitAndShopInfoUiModel) {
        this.pmGradeBenefitAndShopInfo = data
        selectedPmType = if (data.shopInfo.isEligiblePmPro) {
            PMConstant.PMType.PM_PRO
        } else {
            PMConstant.PMType.PM_REGULAR
        }
    }

    private fun renderPmRegistrationWidgets(data: PMGradeBenefitAndShopInfoUiModel) {
        adapter.data.clear()
        val registrationHeaderWidget = getRegistrationHeaderWidgetData(data.shopInfo)

        if (data.shopInfo.isEligiblePmPro) {
            renderPmProRegistrationWidgets(registrationHeaderWidget)
        } else {
            renderRegularPmRegistrationWidget(registrationHeaderWidget)
        }
    }

    private fun renderPmProRegistrationWidgets(headerWidget: WidgetRegistrationHeaderUiModel) {
        val widgets = listOf(
                headerWidget,
                WidgetPmProStaticBenefit,
                WidgetDividerUiModel,
                WidgetGradeBenefitUiModel(benefitPages = pmGradeBenefitAndShopInfo?.gradeBenefitList.orEmpty())
        )
        adapter.data.clear()
        adapter.data.addAll(widgets)
        adapter.notifyDataSetChanged()

        setupFooterCta()
    }

    private fun renderRegularPmRegistrationWidget(headerWidget: WidgetRegistrationHeaderUiModel) {
        val widgets = listOf(
                headerWidget,
                WidgetPotentialUiModel,
                WidgetDividerUiModel,
                WidgetGradeBenefitUiModel(benefitPages = pmGradeBenefitAndShopInfo?.gradeBenefitList.orEmpty())
        )

        adapter.data.clear()
        adapter.data.addAll(widgets)
        adapter.notifyDataSetChanged()

        setupFooterCta()
    }

    private fun setOnPmActivationSuccess() {
        showSuccessRegistrationPopupEndGamePeriod()
    }

    private fun showActivationProgress() {
        view?.pmRegistrationFooterView?.showRegistrationProgress()
    }

    private fun hideActivationProgress() {
        view?.pmRegistrationFooterView?.hideRegistrationProgress()
    }

    private fun showSuccessRegistrationPopupEndGamePeriod() {
        val title = getString(R.string.pm_registration_success_title)
        val description = getString(R.string.pm_registration_success_description_end_game)
        val ctaText = getString(R.string.pm_see_the_next)
        val illustrationUrl = PMConstant.Images.PM_REGISTRATION_SUCCESS
        showNotificationBottomSheet(title, description, ctaText, illustrationUrl, onDismiss = {
            fetchPowerMerchantBasicInfo()
        })
    }

    private fun showNotificationBottomSheet(
            title: String, description: String, primaryCtaText: String, imgUrl: String,
            secondaryCtaText: String? = null,
            onPrimaryCtaClicked: (() -> Unit)? = null,
            onSecondaryCtaClicked: (() -> Unit)? = null,
            onDismiss: (() -> Unit)? = null
    ) {
        val notifBottomSheet = PMNotificationBottomSheet.createInstance(title, description, imgUrl)

        if (!notifBottomSheet.isAdded && !childFragmentManager.isStateSaved) {
            with(notifBottomSheet) {
                setPrimaryButtonClickListener(primaryCtaText) {
                    onPrimaryCtaClicked?.invoke()
                    dismiss()
                }
                setSecondaryCtaClickListener(secondaryCtaText) {
                    onSecondaryCtaClicked?.invoke()
                    dismiss()
                }
                setOnDismissListener {
                    onDismiss?.invoke()
                }
                Handler().post {
                    show(this@PowerMerchantSubscriptionFragment.childFragmentManager)
                }
            }
        }
    }

    private fun setupFooterCta() = view?.run {
        val shopInfo = pmGradeBenefitAndShopInfo?.shopInfo ?: return@run
        val isPmPro = selectedPmType == PMConstant.PMType.PM_PRO
        val isEligiblePm = if (isPmPro) shopInfo.isEligiblePmPro else shopInfo.isEligiblePm

        val registrationTerms = if (isPmPro) {
            getRegistrationHeaderWidgetData(shopInfo).pmProTerms
        } else {
            getRegistrationHeaderWidgetData(shopInfo).pmTerms
        }

        val firstPriorityTerm = registrationTerms.filter {
            if (!shopInfo.isNewSeller) {
                !it.isChecked && it !is RegistrationTermUiModel.ActiveProduct
            } else {
                !it.isChecked
            }
        }.sortedBy { it.periority }.firstOrNull()

        //show tnc check box only if kyc not eligible or pm pro eligible
        val needTnC = firstPriorityTerm is RegistrationTermUiModel.Kyc || isEligiblePm

        val ctaText = if (needTnC || shopInfo.isNewSeller) {
            getString(R.string.power_merchant_register_now)
        } else {
            getString(R.string.pm_interested_to_register)
        }

        with(pmRegistrationFooterView) {
            if (shopInfo.kycStatusId == KYCStatusId.PENDING) gone() else visible()
            setCtaText(ctaText)
            setTnCVisibility(needTnC)
            setOnCtaClickListener { tncAgreed ->
                when {
                    isEligiblePm -> submitPMRegistration(tncAgreed)
                    firstPriorityTerm is RegistrationTermUiModel.ShopScore -> showShopScoreTermBottomSheet(shopInfo)
                    firstPriorityTerm is RegistrationTermUiModel.ActiveProduct -> showActiveProductTermBottomSheet()
                    firstPriorityTerm is RegistrationTermUiModel.Order -> showOrderTermBottomSheet(shopInfo.itemSoldPmProThreshold)
                    firstPriorityTerm is RegistrationTermUiModel.Niv -> showNivTermBottomSheet(shopInfo.nivPmProThreshold)
                    firstPriorityTerm is RegistrationTermUiModel.Kyc -> submitKYC(tncAgreed)
                }
            }
        }
    }

    private fun showOrderTermBottomSheet(itemSoldThreshold: Int) {
        val title = getString(R.string.pm_not_eligible_order_term_title)
        val description = getString(R.string.pm_not_eligible_order_term_description, itemSoldThreshold)
        val primaryCtaText = getString(R.string.pm_learn_ad_and_promotion)
        val secondaryCtaText = getString(R.string.pm_content_slider_last_slide_button)
        val imageUrl = PMConstant.Images.PM_TOTAL_ORDER_TERM

        showNotificationBottomSheet(title, description, primaryCtaText, imageUrl, secondaryCtaText,
                onPrimaryCtaClicked = {
                    RouteManager.route(requireContext(), ApplinkConst.SellerApp.CENTRALIZED_PROMO)
                }
        )
    }

    private fun showNivTermBottomSheet(nivThreshold: Long) {

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

    private fun showShopScoreTermBottomSheet(shopInfo: PMShopInfoUiModel) {
        val isPmPro = selectedPmType == PMConstant.PMType.PM_PRO
        val shopScoreThreshold = if (isPmPro) shopInfo.shopScorePmProThreshold else shopInfo.shopScoreThreshold
        val pmLabel = if (isPmPro) getString(R.string.pm_power_merchant_pro) else getString(R.string.pm_power_merchant)

        val title: String = getString(R.string.pm_bottom_sheet_shop_score_title)
        val description = getString(R.string.pm_bottom_sheet_shop_score_description, shopScoreThreshold, pmLabel)
        val ctaText = getString(R.string.pm_learn_shop_performance)
        val illustrationUrl = PMConstant.Images.PM_SHOP_SCORE_NOT_ELIGIBLE_BOTTOM_SHEET

        showNotificationBottomSheet(title, description, ctaText, illustrationUrl, onPrimaryCtaClicked = {
            RouteManager.route(context, ApplinkConst.SHOP_SCORE_DETAIL)
        })
    }

    private fun showActiveProductTermBottomSheet() {
        val title: String = getString(R.string.pm_bottom_sheet_active_product_title)
        val description: String = getString(R.string.pm_bottom_sheet_active_product_description)
        val ctaText: String = getString(R.string.pm_add_product)
        val illustrationUrl: String = PMConstant.Images.PM_ADD_PRODUCT_BOTTOM_SHEET

        showNotificationBottomSheet(title, description, ctaText, illustrationUrl, onPrimaryCtaClicked = {
            RouteManager.route(context, ApplinkConst.SellerApp.PRODUCT_ADD)
        })
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
        view?.pmRegistrationFooterView?.gone()
        val isAutoExtendEnabled = getAutoExtendEnabled()
        val widgets = mutableListOf<BaseWidgetUiModel>()
        /*if (!pmSettingInfo?.tickers.isNullOrEmpty() && isAutoExtendEnabled) {
            widgets.add(WidgetTickerUiModel(pmSettingInfo?.tickers.orEmpty()))
        }*/
        if (!isAutoExtendEnabled) {
            widgets.add(WidgetCancelDeactivationSubmissionUiModel(getExpiredTimeFmt()))
        }
        widgets.add(getShopGradeWidgetData(data))
        widgets.add(WidgetDividerUiModel)
        widgets.add(getCurrentShopGradeBenefit(data))
        widgets.add(WidgetDividerUiModel)
        val shouldShowNextGradeWidget = data.nextPMGrade != null && isAutoExtendEnabled
                && data.currentPMGrade?.gradeName != PMShopGrade.DIAMOND
                && !pmBasicInfo?.shopInfo?.isNewSeller.orTrue()
        if (shouldShowNextGradeWidget) {
            widgets.add(getNextShopGradeWidgetData(data))
            widgets.add(WidgetDividerUiModel)
        }
        if (isAutoExtendEnabled && !pmBasicInfo?.shopInfo?.isNewSeller.orTrue()) {
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
                is Success -> {
                    initPmActiveData(it.data)
                    renderPmRegistrationWidgets(it.data)
                }
                is Fail -> {
                    showErrorState()
                    logToCrashlytic(PowerMerchantErrorLogger.REGISTRATION_PAGE_ERROR, it.throwable)
                }
            }
        })
    }

    private fun getAutoExtendEnabled(): Boolean {
        val expiredTime = pmBasicInfo?.pmStatus?.expiredTime
        val autoExtendEnabled = pmBasicInfo?.pmStatus?.autoExtendEnabled.orTrue()
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
        val shopScoreThreshold = pmBasicInfo?.shopInfo?.shopScoreThreshold.orZero()
        return WidgetShopGradeUiModel(
                isNewSeller = pmBasicInfo?.shopInfo?.isNewSeller.orTrue(),
                shopGrade = shopGrade?.gradeName.orEmpty(),
                shopScore = shopGrade?.shopScore.orZero(),
                shopAge = pmBasicInfo?.shopInfo?.shopAge ?: 1,
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
        val shopAge = pmBasicInfo?.shopInfo?.shopAge ?: 1
        val remainingDays = daysBecomeExisting.minus(shopAge)
        val remainingDaysMillis = TimeUnit.DAYS.toMillis(remainingDays.toLong())
        val endOfTenureMillis = Date().time.plus(remainingDaysMillis)
        val dateFormat = "dd MMMM yyyy"
        return DateFormatUtils.getFormattedDate(endOfTenureMillis, dateFormat)
    }

    private fun getPmNextCalculationDate(): String {
        val shopInfo = pmBasicInfo?.shopInfo
        return if (shopInfo?.isNewSeller.orTrue()) {
            val day60ofTenure = 60
            val shopAge = shopInfo?.shopAge ?: 1
            return if (shopAge < day60ofTenure) {
                getNewSellerTenure()
            } else {
                pmBasicInfo?.settingInfo?.periodeEndDate.orEmpty()
            }
        } else {
            pmBasicInfo?.settingInfo?.periodeEndDate.orEmpty()
        }
    }

    private fun getRegistrationHeaderWidgetData(shopInfo: PMShopInfoUiModel): WidgetRegistrationHeaderUiModel {
        return WidgetRegistrationHeaderUiModel(
                shopInfo = shopInfo,
                pmTerms = PMRegistrationTermHelper.getPmRegistrationTerms(requireContext(), shopInfo),
                pmProTerms = PMRegistrationTermHelper.getPmProRegistrationTerms(requireContext(), shopInfo),
                selectedPmType = selectedPmType
        )
    }

    private fun showDeactivationQuestionnaire() {
        val bottomSheet = DeactivationBottomSheet.createInstance()
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return
        bottomSheet.show(childFragmentManager)
        bottomSheet.setOnDeactivationSuccess {
            fetchPowerMerchantBasicInfo()
        }
    }

    private fun logToCrashlytic(message: String, throwable: Throwable) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    private fun hideSwipeRefreshLoading() {
        view?.swipeRefreshPm?.isRefreshing = false
    }
}