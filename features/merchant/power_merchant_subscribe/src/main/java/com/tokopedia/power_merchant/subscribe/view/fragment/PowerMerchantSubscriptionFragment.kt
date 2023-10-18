package com.tokopedia.power_merchant.subscribe.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.gm.common.constant.*
import com.tokopedia.gm.common.data.source.local.model.*
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.performance.PerformanceMonitoringConst
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.databinding.FragmentPmPowerMerchantSubscriptionBinding
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.activity.FallbackActivity
import com.tokopedia.power_merchant.subscribe.view.activity.SubscriptionActivityInterface
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.PMWidgetListener
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.*
import com.tokopedia.power_merchant.subscribe.view.helper.PMActiveTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.*
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSharedViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSubscriptionViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/03/21
 */

open class PowerMerchantSubscriptionFragment :
    BaseListFragment<BaseWidgetUiModel, WidgetAdapterFactoryImpl>(),
    PMWidgetListener,
    OptInConfirmationBottomSheet.OptInConfirmationListener {

    companion object {
        fun createInstance(): PowerMerchantSubscriptionFragment {
            return PowerMerchantSubscriptionFragment()
        }
    }

    protected val binding: FragmentPmPowerMerchantSubscriptionBinding? by viewBinding()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    protected val mViewModel: PowerMerchantSubscriptionViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(PowerMerchantSubscriptionViewModel::class.java)
    }
    protected val recyclerView: RecyclerView?
        get() = super.getRecyclerView(view)

    protected var isModeratedShop = false
    protected var pmBasicInfo: PowerMerchantBasicInfoUiModel? = null

    protected val sharedViewModel: PowerMerchantSharedViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(PowerMerchantSharedViewModel::class.java)
    }

    private val indexOfUpgradePmProWidget: Int
        get() = adapter.data.indexOfFirst { it is WidgetUpgradePmProUiModel }

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_SUBSCRIBE

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvPmRegistration

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl =
        WidgetAdapterFactoryImpl(this, powerMerchantTracking)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pm_power_merchant_subscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        observePowerMerchantBasicInfo()
        observePmActiveState()
        observeShopModerationStatus()
    }

    override fun onItemClicked(t: BaseWidgetUiModel?) {}

    override fun loadData(page: Int) {}

    override fun setOnDeactivatePMClickListener() {
        showRegularPmDeactivationBottomSheet()
        powerMerchantTracking.sendEventClickStopPmBecomeRm()
        powerMerchantTracking.sendEventClickStopPowerMerchant()
    }

    override fun cancelPmDeactivationSubmission(position: Int) {
        observePmCancelDeactivationSubmission()
        mViewModel.cancelPmDeactivationSubmission()

        val isPmPro = pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT_PRO
        powerMerchantTracking.sendEventClickCancelOptOutPowerMerchant(isPmPro)
    }

    override fun setOnTickerWidgetRemoved(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            adapter.data.removeAt(position)
            recyclerView?.post {
                adapter.notifyItemRemoved(position)
            }
        }
    }

    override fun showPmProStatusInfo(model: PMProStatusInfoUiModel) {
        val bottomSheet = PMProStatusInfoBottomSheet.createInstance(model)
        if (childFragmentManager.isStateSaved || bottomSheet.isAdded) {
            return
        }

        bottomSheet.show(childFragmentManager)
    }

    override fun showHelpPmNotActive() {
        val bottomSheet = InfoPmIfNotActiveBottomSheet.createInstance()
        if (childFragmentManager.isStateSaved || bottomSheet.isAdded) {
            return
        }

        bottomSheet.show(childFragmentManager)
    }

    override fun goToMembershipDetail() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE)
    }

    override fun onPMProNewSellerLearnMore() {
        context?.let {
            RouteManager.route(
                it,
                ApplinkConstInternalGlobal.WEBVIEW,
                Constant.Url.POWER_MERCHANT_PRO_EDU
            )
        }
    }

    override fun showServiceFeeByCategory() {
        context?.let {
            RouteManager.route(
                it,
                ApplinkConstInternalGlobal.WEBVIEW,
                Constant.Url.PM_SERVICE_FEE
            )
        }
    }

    override fun setOptInConfirmationSuccess(pmActivationStatusUiModel: PMActivationStatusUiModel, isPmPro: Boolean) {
        val successMessage = if (isPmPro) {
            getString(com.tokopedia.power_merchant.subscribe.R.string.opt_in_pm_pro_activation_success_message)
        } else {
            getString(com.tokopedia.power_merchant.subscribe.R.string.opt_in_pm_activation_success_message)
        }
        setOnPmActivationSuccess(pmActivationStatusUiModel, successMessage)
    }

    protected open fun observePowerMerchantBasicInfo() {
        observe(sharedViewModel.powerMerchantBasicInfo) {
            if (it is Success) {
                initBasicInfo(it.data)
                fetchPageContent()
                showPmOptOutConfirmation(it.data)
            }
        }
    }

    private fun showPmOptOutConfirmation(data: PowerMerchantBasicInfoUiModel) {
        if (!data.pmStatus.autoExtendEnabled) {
            activity?.intent?.data?.let {
                val params = UriUtil.uriQueryParamsToMap(it)
                val showPopupValue = params[PowerMerchantDeepLinkMapper.QUERY_PARAM_SHOW_POPUP]
                showPopupValue?.let { showPopup ->
                    val isPmProActive = data.pmStatus.isPowerMerchantPro()
                    val bottomSheet = OptInConfirmationBottomSheet.newInstance(isPmProActive)
                    bottomSheet.setOptInListener(this)
                    bottomSheet.show(childFragmentManager)
                }
            }
        }
    }

    protected fun initBasicInfo(data: PowerMerchantBasicInfoUiModel) {
        this.pmBasicInfo = data
    }

    protected fun showErrorToaster(message: String, actionText: String) {
        view?.run {
            Toaster.toasterCustomBottomHeight =
                context.resources.getDimensionPixelSize(R.dimen.pm_spacing_100dp)
            view?.rootView?.let {
                Toaster.build(
                    it,
                    message,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    actionText
                )
                    .show()
            }
        }
    }

    protected fun showModeratedShopBottomSheet() {
        val title: String = context?.getString(R.string.pm_bottom_sheet_moderated_shop_title).orEmpty()
        val description: String = context?.getString(R.string.pm_bottom_sheet_moderated_shop_description).orEmpty()
        val ctaText: String = context?.getString(R.string.pm_content_slider_last_slide_button).orEmpty()
        val illustrationUrl: String = PMConstant.Images.PM_MODERATED_SHOP

        showNotificationBottomSheet(
            title,
            description,
            ctaText,
            illustrationUrl,
            onPrimaryCtaClicked = {
                powerMerchantTracking.sendEventClickAcknowledgeShopModeration()
            }
        )

        powerMerchantTracking.sendEventPopupUnableToRegisterShopModeration()
    }

    protected fun showNotificationBottomSheet(
        title: String,
        description: String,
        primaryCtaText: String,
        imgUrl: String,
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
                Handler(Looper.getMainLooper()).post {
                    show(this@PowerMerchantSubscriptionFragment.childFragmentManager)
                }
            }
        }
    }

    protected fun fetchPowerMerchantBasicInfo() {
        sharedViewModel.getPowerMerchantBasicInfo(false)
    }

    protected fun showErrorState(throwable: Throwable) {
        (activity as? SubscriptionActivityInterface)?.showErrorState(throwable)
    }

    protected fun showRegistrationFragment() {
        (activity as? SubscriptionActivityInterface)?.setViewForRegistrationPage()
    }

    protected fun reSetupFooterView() {
        (activity as? SubscriptionActivityInterface)?.renderFooterView()
    }

    protected fun hideFooterView() {
        (activity as? SubscriptionActivityInterface)?.hideFooterView()
    }

    protected fun startCustomMetricPerformanceMonitoring(tag: String) {
        (activity as? SubscriptionActivityInterface)?.startCustomMetricPerformanceMonitoring(tag)
    }

    protected fun stopCustomMetricPerformanceMonitoring(tag: String) {
        (activity as? SubscriptionActivityInterface)?.stopCustomMetricPerformanceMonitoring(tag)
    }

    protected fun stopRenderPerformanceMonitoring() {
        (activity as? SubscriptionActivityInterface)?.stopRenderPerformanceMonitoring()
    }

    protected fun submitPmRegistration() {
        if (isModeratedShop) {
            showModeratedShopBottomSheet()
            return
        }

        showActivationProgress()
        observePmActivationStatus()
        mViewModel.submitPMActivation()

        powerMerchantTracking.sendEventClickUpgradePowerMerchantPro()
    }

    protected fun logToCrashlytic(message: String, throwable: Throwable) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    protected fun hideSwipeRefreshLoading() {
        binding?.swipeRefreshPm?.isRefreshing = false
    }

    private fun showRegularPmDeactivationBottomSheet() {
        val isPmPro = pmBasicInfo?.pmStatus?.isPowerMerchantPro() == true
        val bottomSheet = PowerMerchantDeactivationBottomSheet.newInstance(isPmPro)
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return
        bottomSheet.setListener(object :
                PowerMerchantDeactivationBottomSheet.BottomSheetCancelListener {
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

    private fun setupView() = binding?.run {
        swipeRefreshPm.setOnRefreshListener {
            reloadPageContent()
        }

        setupRecyclerView()
        setupPmProUpgradeView()
    }

    private fun setupPmProUpgradeView() = binding?.run {
        viewPmUpgradePmPro.setOnClickListener {
            val layoutManager = recyclerView?.layoutManager as? LinearLayoutManager
            layoutManager?.scrollToPositionWithOffset(indexOfUpgradePmProWidget, 0)
            hideUpgradePmProStickyView()
        }
    }

    private fun setupRecyclerView() = binding?.run {
        val layManager = object : LinearLayoutManager(context) {
            override fun scrollVerticallyBy(
                dy: Int,
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ): Int {
                setOnVerticalScrolled()
                return super.scrollVerticallyBy(dy, recycler, state)
            }
        }
        recyclerView?.layoutManager = layManager
        recyclerView?.gone()

        setupRecyclerViewAnimation()
    }

    private fun setupRecyclerViewAnimation() {
        context?.let {
            val animation: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(it, R.anim.layout_animation_pm_recycler_view)
            recyclerView?.layoutAnimation = animation
        }
    }

    private fun setOnVerticalScrolled() {
        val layoutManager = recyclerView?.layoutManager as? LinearLayoutManager
        val lastVisible = layoutManager?.findLastVisibleItemPosition() ?: RecyclerView.NO_POSITION
        val indexOfUpgradePmProWidget =
            adapter.data.indexOfFirst { it is WidgetUpgradePmProUiModel }
        if (lastVisible != RecyclerView.NO_POSITION && lastVisible < indexOfUpgradePmProWidget) {
            showUpgradePmProStickyView()
        } else {
            hideUpgradePmProStickyView()
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
            context?.getString(R.string.pm_expired_time_format, dateStr, hourStr).orEmpty()
        } catch (e: Exception) {
            expiredTime
        }
    }

    private fun observePmActivationStatus() {
        mViewModel.pmActivationStatus.observeOnce(this.viewLifecycleOwner) {
            hideActivationProgress()
            when (it) {
                is Success -> setOnPmActivationSuccess(it.data)
                is Fail -> {
                    setOnPmActivationFailed(it.throwable)
                    logToCrashlytic(PowerMerchantErrorLogger.PM_ACTIVATION_ERROR, it.throwable)
                }
            }
        }
    }

    private fun observePmCancelDeactivationSubmission() {
        mViewModel.pmCancelDeactivationStatus.observeOnce(viewLifecycleOwner) {
            when (it) {
                is Success -> setOnCancelDeactivationSuccess(it.data)
                is Fail -> {
                    setOnCancelDeactivationFailed(it.throwable)
                    logToCrashlytic(
                        PowerMerchantErrorLogger.PM_CANCEL_DEACTIVATION_ERROR,
                        it.throwable
                    )
                }
            }
        }
    }

    private fun setOnCancelDeactivationFailed(throwable: Throwable) {
        notifyCancelPmDeactivationWidget()
        val actionText = context?.getString(R.string.pm_try_again).orEmpty()
        showErrorToaster(getErrorMessage(throwable), actionText)
    }

    private fun setOnCancelDeactivationSuccess(data: PMActivationStatusUiModel) {
        notifyCancelPmDeactivationWidget()
        if (!data.isSuccess) {
            val actionText = context?.getString(R.string.power_merchant_ok_label).orEmpty()
            showErrorToaster(data.message, actionText)
            return
        }

        view?.run {
            Toaster.toasterCustomBottomHeight =
                context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl5)
                    .orZero()
            val message =
                if (pmBasicInfo?.pmStatus?.isPowerMerchantPro() == true) {
                    context?.getString(R.string.pm_cancel_pm_pro_deactivation_message).orEmpty()
                } else {
                    context?.getString(R.string.pm_cancel_pm_deactivation_message).orEmpty()
                }
            Toaster.build(
                rootView,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL
            ).show()
        }

        fetchPowerMerchantBasicInfo()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyCancelPmDeactivationWidget() {
        val cancelPmDeactivationWidgetPosition =
            adapter.data.indexOfFirst { it is WidgetCancelDeactivationSubmissionUiModel }
        recyclerView?.post {
            if (cancelPmDeactivationWidgetPosition != RecyclerView.NO_POSITION) {
                adapter.notifyItemChanged(cancelPmDeactivationWidgetPosition)
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getErrorMessage(t: Throwable): String {
        return when (t) {
            is UnknownHostException -> context?.getString(R.string.pm_network_error_message).orEmpty()
            else -> context?.getString(R.string.pm_system_error_message).orEmpty()
        }
    }

    private fun reloadPageContent() {
        if (pmBasicInfo == null) {
            fetchPowerMerchantBasicInfo()
        } else {
            binding?.swipeRefreshPm?.isRefreshing = true
            fetchPageContent()
        }
    }

    private fun fetchPageContent() {
        if (pmBasicInfo == null) {
            showErrorState(RuntimeException())
            return
        }

        when (pmBasicInfo?.pmStatus?.status) {
            PMStatusConst.ACTIVE, PMStatusConst.IDLE -> {
                val pmTire = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.POWER_MERCHANT
                fetchPmActiveState(pmTire)
            }
        }
    }

    private fun setOnPmActivationSuccess(data: PMActivationStatusUiModel, successMessage: String? = null) {
        notifyUpgradePmProWidget()

        if (data.isSuccess) {
            renderUiOnActivationSuccess(successMessage)
        } else {
            if (data.shouldUpdateApp()) {
                openFallbackPage()
            } else {
                val actionText = context?.getString(R.string.power_merchant_ok_label).orEmpty()
                showErrorToaster(data.message, actionText)
            }
        }
    }

    private fun openFallbackPage() {
        context?.let {
            FallbackActivity.startActivity(it)
        }
    }

    private fun renderUiOnActivationSuccess(successMessage: String?) {
        view?.rootView?.let {
            it.post {
                val message = if (successMessage.isNullOrBlank()) getString(R.string.pm_submit_activation_success) else successMessage
                Toaster.toasterCustomBottomHeight = it.context.resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl5
                )
                Toaster.build(
                    it,
                    message,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL
                ).show()
            }

            fetchPowerMerchantBasicInfo()

            powerMerchantTracking.sendEventShowPopupSuccessRegister()
        }
    }

    private fun setOnPmActivationFailed(throwable: Throwable) {
        notifyUpgradePmProWidget()
        val actionText = context?.getString(R.string.pm_try_again).orEmpty()
        showErrorToaster(getErrorMessage(throwable), actionText)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyUpgradePmProWidget() {
        val upgradePmProWidgetPosition = adapter.data.indexOfFirst {
            it is WidgetUpgradePmProUiModel
        }
        recyclerView?.post {
            if (upgradePmProWidgetPosition != RecyclerView.NO_POSITION) {
                adapter.notifyItemChanged(upgradePmProWidgetPosition)
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun showActivationProgress() {
        (activity as? SubscriptionActivityInterface)?.showActivationProgress()
    }

    private fun hideActivationProgress() {
        (activity as? SubscriptionActivityInterface)?.hideActivationProgress()
    }

    private fun observePmActiveState() {
        mViewModel.pmPmActiveData.observe(viewLifecycleOwner, {
            stopCustomMetricPerformanceMonitoring(PerformanceMonitoringConst.PM_ACTIVE_DATA_METRICS)
            hideSwipeRefreshLoading()
            when (it) {
                is Success -> {
                    renderPmActiveState(it.data)
                }
                is Fail -> {
                    showErrorState(it.throwable)
                    logToCrashlytic(PowerMerchantErrorLogger.PM_ACTIVE_PAGE_ERROR, it.throwable)
                }
            }
            stopRenderPerformanceMonitoring()
        })
    }

    private fun fetchPmActiveState(pmTire: Int) {
        startCustomMetricPerformanceMonitoring(PerformanceMonitoringConst.PM_ACTIVE_DATA_METRICS)
        mViewModel.getPmActiveStateData(pmTire)
    }

    private fun renderPmActiveState(data: PMGradeBenefitInfoUiModel) {
        showUpgradePmProStickyView()
        val isAutoExtendEnabled = getAutoExtendEnabled()
        val isActive = pmBasicInfo?.pmStatus?.status == PMStatusConst.ACTIVE
        val isPmPro = pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val isPm = pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT
        val widgets = mutableListOf<BaseWidgetUiModel>()
        val tickerList = pmBasicInfo?.tickers
        val isRegularMerchant =
            pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT &&
                pmBasicInfo?.pmStatus?.status == PMStatusConst.INACTIVE
        val isPmActive = isPm && isActive
        val deactivatedStatusName = if (pmBasicInfo?.pmStatus?.subscriptionType.isZero()) {
            context?.getString(R.string.pm_regular_merchant)
        } else {
            context?.getString(R.string.pm_power_merchant)
        }

        if (!tickerList.isNullOrEmpty() && !isModeratedShop) {
            widgets.add(WidgetTickerUiModel(tickerList))
        }

        if (!isAutoExtendEnabled) {
            widgets.add(
                WidgetCancelDeactivationSubmissionUiModel(
                    getExpiredTimeFmt(),
                    deactivatedStatusName.orEmpty()
                )
            )
        }
        widgets.add(getShopGradeWidgetData(data))
        widgets.add(WidgetDividerUiModel)
        widgets.add(getCurrentShopGradeBenefit(data))
        val shouldShowUpgradePmProWidget = isAutoExtendEnabled && !isPmPro &&
            isPmActive
        if (shouldShowUpgradePmProWidget) {
            widgets.add(WidgetDividerUiModel)
            getUpgradePmProWidget(getShopGradeWidgetData(data), data)?.let {
                widgets.add(it)
            }
        }
        if (isRegularMerchant) {
            widgets.add(WidgetDividerUiModel)
            widgets.add(
                WidgetSingleCtaUiModel(
                    context?.getString(R.string.pm_pm_transition_period_learnmore).orEmpty(),
                    Constant.Url.POWER_MERCHANT_FEATURES
                )
            )
        }
        widgets.add(WidgetDividerUiModel)
        widgets.add(WidgetFeeServiceUiModel(pmBasicInfo?.shopInfo?.shopScore.orZero()))
        if (isAutoExtendEnabled) {
            widgets.add(WidgetDividerUiModel)
            widgets.add(WidgetPMDeactivateUiModel)
        }
        recyclerView?.visible()
        adapter.clearAllElements()
        renderList(widgets, false)
        recyclerView?.post {
            recyclerView?.smoothScrollToPosition(RecyclerView.SCROLLBAR_POSITION_DEFAULT)
        }
    }

    private fun showUpgradePmProStickyView() {
        val isAutoExtendEnabled = getAutoExtendEnabled()
        val isNewSeller30FirstMonday = pmBasicInfo?.shopInfo?.is30DaysFirstMonday.orFalse()
        val shouldShowView = pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT &&
            pmBasicInfo?.pmStatus?.status == PMStatusConst.ACTIVE &&
            isNewSeller30FirstMonday && isAutoExtendEnabled
        binding?.viewPmUpgradePmPro?.isVisible = shouldShowView
    }

    private fun hideUpgradePmProStickyView() {
        binding?.viewPmUpgradePmPro?.gone()
    }

    private fun observeShopModerationStatus() {
        observe(sharedViewModel.shopModerationStatus) {
            if (it is Success) {
                isModeratedShop = it.data.isModeratedShop
            }
        }
    }

    private fun getAutoExtendEnabled(): Boolean {
        val expiredTime = pmBasicInfo?.pmStatus?.expiredTime
        val autoExtendEnabled = pmBasicInfo?.pmStatus?.autoExtendEnabled.orTrue()
        return !expiredTime.isNullOrBlank() && autoExtendEnabled
    }

    private fun getCurrentShopGradeBenefit(data: PMGradeBenefitInfoUiModel): WidgetExpandableUiModel {
        val grade = data.currentPMGrade
        val benefits = mutableListOf<BaseExpandableItemUiModel>()
        data.currentPMBenefits?.forEachIndexed { index, benefit ->
            if (!benefits.any { it.text == benefit.categoryName }) {
                val shouldShowTopSeparator = index != 0
                benefits.add(ExpandableSectionUiModel(benefit.categoryName, shouldShowTopSeparator))
            }
            if (benefit.benefitName.isNotBlank()) {
                benefits.add(ExpandableItemUiModel(benefit.benefitName, benefit.appLink.orEmpty()))
            }
        }
        return WidgetExpandableUiModel(
            grade = grade,
            nextMonthlyRefreshDate = data.nextMonthlyRefreshDate,
            nextQuarterlyCalibrationRefreshDate = data.nextQuarterlyCalibrationRefreshDate,
            nextShopLevel = data.nextPMGrade?.shopLevel?.toString().orEmpty(),
            nextGradeName = data.nextPMGrade?.gradeName.orEmpty(),
            pmStatus = pmBasicInfo?.pmStatus?.status ?: PMStatusConst.INACTIVE,
            pmTierType = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.POWER_MERCHANT,
            isAutoExtendEnabled = pmBasicInfo?.pmStatus?.autoExtendEnabled.orFalse(),
            items = benefits
        )
    }

    private fun getShopGradeWidgetData(data: PMGradeBenefitInfoUiModel): WidgetShopGradeUiModel {
        val shopGrade = data.currentPMGrade
        val shopInfo = pmBasicInfo?.shopInfo

        return WidgetShopGradeUiModel(
            isNewSeller = shopInfo?.isNewSeller.orTrue(),
            is30FirstMonday = shopInfo?.is30DaysFirstMonday.orFalse(),
            pmTierType = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.POWER_MERCHANT,
            shopScore = shopInfo?.shopScore.orZero(),
            shopScoreThreshold = shopInfo?.shopScoreThreshold.orZero(),
            pmProShopScoreThreshold = shopInfo?.shopScorePmProThreshold.orZero(),
            itemSoldThreshold = shopInfo?.itemSoldPmProThreshold.orZero(),
            netItemValueThreshold = shopInfo?.netItemValuePmProThreshold.orZero(),
            nextMonthlyRefreshDate = pmBasicInfo?.shopInfo?.nextMonthlyRefreshDate.orEmpty(),
            shopAge = shopInfo?.shopAge.orZero(),
            gradeBackgroundUrl = shopGrade?.backgroundUrl.orEmpty(),
            pmStatus = pmBasicInfo?.pmStatus?.status ?: PMStatusConst.INACTIVE,
            shopGrade = shopGrade?.gradeName ?: PMConstant.ShopGrade.PM
        )
    }

    private fun showDeactivationQuestionnaire() {
        val pmExpirationDate = pmBasicInfo?.pmStatus?.expiredTime.orEmpty()
        val currentPmTireType = pmBasicInfo?.pmStatus?.pmTier
            ?: PMConstant.PMTierType.POWER_MERCHANT
        val bottomSheet = DeactivationQuestionnaireBottomSheet.createInstance(
            pmExpirationDate,
            currentPmTireType,
            PMConstant.PMTierType.POWER_MERCHANT
        )
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return

        bottomSheet.setOnDeactivationSuccess {
            fetchPowerMerchantBasicInfo()
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun getUpgradePmProWidget(
        shopGradeWidgetData: WidgetShopGradeUiModel,
        data: PMGradeBenefitInfoUiModel
    ): WidgetUpgradePmProUiModel? {
        context?.let { context ->
            pmBasicInfo?.shopInfo?.let {
                return WidgetUpgradePmProUiModel(
                    shopInfo = it,
                    registrationTerms = PMActiveTermHelper.getPmProRegistrationTerms(
                        requireContext(),
                        it,
                        true
                    ),
                    generalBenefits = PMActiveTermHelper.getBenefitList(context),
                    shopGrade = shopGradeWidgetData.shopGrade,
                    nextMonthlyRefreshDate = data.nextMonthlyRefreshDate
                )
            }
        }
        return null
    }
}
