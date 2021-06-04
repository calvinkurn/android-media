package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.*
import com.tokopedia.gm.common.data.source.local.model.*
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.activity.SubscriptionActivityInterface
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.PMWidgetListener
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.*
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.*
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSharedViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSubscriptionViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.fragment_pm_power_merchant_subscription.*
import kotlinx.android.synthetic.main.fragment_pm_power_merchant_subscription.view.*
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/03/21
 */

open class PowerMerchantSubscriptionFragment : BaseListFragment<BaseWidgetUiModel, WidgetAdapterFactoryImpl>(),
        PMWidgetListener {

    companion object {
        private const val KEY_PM_TIER_TYPE = "key_pm_tier_type"

        fun createInstance(pmTireType: Int): PowerMerchantSubscriptionFragment {
            return PowerMerchantSubscriptionFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_PM_TIER_TYPE, pmTireType)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    protected val mViewModel: PowerMerchantSubscriptionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PowerMerchantSubscriptionViewModel::class.java)
    }
    protected val recyclerView: RecyclerView?
        get() = super.getRecyclerView(view)

    protected var isModeratedShop = false
    protected var pmBasicInfo: PowerMerchantBasicInfoUiModel? = null

    private val sharedViewModel: PowerMerchantSharedViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PowerMerchantSharedViewModel::class.java)
    }

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvPmRegistration

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl = WidgetAdapterFactoryImpl(this, powerMerchantTracking)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        val isPmPro = pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT_PRO
        if (isPmPro) {
            showPmProDeactivationBottomSheet()
        } else {
            showRegularPmDeactivationBottomSheet()
        }

        powerMerchantTracking.sendEventClickStopPowerMerchant()
    }

    override fun cancelPmDeactivationSubmission(position: Int) {
        observePmCancelDeactivationSubmission()
        val currentPmTireType = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.NA
        val shopTier = getShopTireByPmTire(currentPmTireType)
        mViewModel.cancelPmDeactivationSubmission(shopTier)
    }

    override fun setOnTickerWidgetRemoved(position: Int) {
        adapter.data.removeAt(position)
        recyclerView?.post {
            adapter.notifyItemRemoved(position)
        }
    }

    override fun showUpdateInfoBottomSheet() {
        val fragment = UpdateInfoBottomSheet.createInstance()
        if (childFragmentManager.isStateSaved || fragment.isAdded) return
        fragment.show(childFragmentManager)
    }

    override fun onUpgradePmProClickListener(adapterPosition: Int) {
        submitPmRegistration(PMConstant.ShopTierType.POWER_MERCHANT_PRO)
    }

    override fun onUpgradePmProTnCClickListener() {
        val bottomSheet = PMTermAndConditionBottomSheet.newInstance()
        if (childFragmentManager.isStateSaved || bottomSheet.isAdded) {
            return
        }

        bottomSheet.show(childFragmentManager)
    }

    protected fun showErrorToaster(message: String, actionText: String) {
        view?.run {
            Toaster.toasterCustomBottomHeight = context.resources.getDimensionPixelSize(R.dimen.pm_spacing_100dp)
            view?.rootView?.let {
                Toaster.build(it, message, Toaster.LENGTH_LONG,
                        Toaster.TYPE_ERROR, actionText)
                        .show()
            }
        }
    }

    protected fun showModeratedShopBottomSheet() {
        val title: String = getString(R.string.pm_bottom_sheet_moderated_shop_title)
        val description: String = getString(R.string.pm_bottom_sheet_moderated_shop_description)
        val ctaText: String = getString(R.string.pm_content_slider_last_slide_button)
        val illustrationUrl: String = PMConstant.Images.PM_MODERATED_SHOP

        showNotificationBottomSheet(title, description, ctaText, illustrationUrl, onPrimaryCtaClicked = {
            powerMerchantTracking.sendEventClickAcknowledgeShopModeration()
        })

        powerMerchantTracking.sendEventPopupUnableToRegisterShopModeration()
    }

    protected fun showNotificationBottomSheet(
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

    protected fun showErrorState(throwable: Throwable) {
        (activity as? SubscriptionActivityInterface)?.showErrorState(throwable)
    }

    protected fun submitPmRegistration(nextShopTireType: Int) {
        if (isModeratedShop) {
            showModeratedShopBottomSheet()
            return
        }

        val currentPmTire = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.NA
        val currentShopTireType = getShopTireByPmTire(currentPmTire)

        showActivationProgress()
        observePmActivationStatus()
        mViewModel.submitPMActivation(currentShopTireType, nextShopTireType)
    }

    private fun showPmProDeactivationBottomSheet() {
        val bottomSheet = PowerMerchantProDeactivationBottomSheet.createInstance()
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return
        bottomSheet.setOnNextClickListener { shopTire ->
            val pmTireType = if (shopTire == PMConstant.ShopTierType.POWER_MERCHANT) {
                PMConstant.PMTierType.POWER_MERCHANT_PRO
            } else {
                PMConstant.PMTierType.POWER_MERCHANT
            }

            showDeactivationQuestionnaire(pmTireType)
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun showRegularPmDeactivationBottomSheet() {
        val bottomSheet = PowerMerchantDeactivationBottomSheet.newInstance(getExpiredTimeFmt(), pmBasicInfo?.isFreeShippingEnabled.orFalse())
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return

        bottomSheet.setListener(object : PowerMerchantDeactivationBottomSheet.BottomSheetCancelListener {
            override fun onClickCancelButton() {
                showDeactivationQuestionnaire(PMConstant.PMTierType.POWER_MERCHANT)
                bottomSheet.dismiss()
            }

            override fun onClickBackButton() {
                bottomSheet.dismiss()
            }
        })
        bottomSheet.show(childFragmentManager)
    }

    private fun setupView() = view?.run {
        swipeRefreshPm.setOnRefreshListener {
            reloadPageContent()
        }

        setupRecyclerView()
        setupPmProUpgradeView()
    }

    private fun setupPmProUpgradeView() = view?.run {
        viewPmUpgradePmPro.setOnClickListener {
            val layoutManager = recyclerView?.layoutManager as? LinearLayoutManager
            val indexOfUpgradePmProWidget = adapter.data.indexOfFirst { it is WidgetUpgradePmProUiModel }
            layoutManager?.scrollToPositionWithOffset(indexOfUpgradePmProWidget, 0)
            hideUpgradePmProStickyView()
        }
    }

    private fun setupRecyclerView() = view?.run {
        val layManager = object : LinearLayoutManager(context) {
            override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
                setOnVerticalScrolled()
                return super.scrollVerticallyBy(dy, recycler, state)
            }
        }
        recyclerView?.layoutManager = layManager
        recyclerView?.gone()
    }

    private fun setOnVerticalScrolled() {
        val layoutManager = recyclerView?.layoutManager as? LinearLayoutManager
        val lastVisible = layoutManager?.findLastVisibleItemPosition() ?: RecyclerView.NO_POSITION
        val indexOfUpgradePmProWidget = adapter.data.indexOfFirst { it is WidgetUpgradePmProUiModel }
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
            getString(R.string.pm_expired_time_format, dateStr, hourStr)
        } catch (e: Exception) {
            expiredTime
        }
    }

    private fun observePowerMerchantBasicInfo() {
        sharedViewModel.powerMerchantBasicInfo.observe(viewLifecycleOwner, Observer {
            if (it is Success) {
                initBasicInfo(it.data)
                fetchPageContent()
            }
        })
    }

    private fun observePmActivationStatus() {
        mViewModel.pmActivationStatus.observeOnce(viewLifecycleOwner, Observer {
            hideActivationProgress()
            when (it) {
                is Success -> setOnPmActivationSuccess(it.data)
                is Fail -> {
                    setOnPmActivationFailed(it.throwable)
                    logToCrashlytic(PowerMerchantErrorLogger.PM_ACTIVATION_ERROR, it.throwable)
                }
            }
        })
    }

    private fun observePmCancelDeactivationSubmission() {
        mViewModel.pmCancelDeactivationStatus.observeOnce(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> setOnCancelDeactivationSuccess(it.data)
                is Fail -> {
                    setOnCancelDeactivationFailed(it.throwable)
                    logToCrashlytic(PowerMerchantErrorLogger.PM_CANCEL_DEACTIVATION_ERROR, it.throwable)
                }
            }
        })
    }

    private fun setOnCancelDeactivationFailed(throwable: Throwable) {
        notifyCancelPmDeactivationWidget()
        val actionText = getString(R.string.pm_try_again)
        showErrorToaster(getErrorMessage(throwable), actionText)
    }

    private fun setOnCancelDeactivationSuccess(data: PMActivationStatusUiModel) {
        notifyCancelPmDeactivationWidget()
        if (!data.isSuccess) {
            val actionText = getString(R.string.power_merchant_ok_label)
            showErrorToaster(data.message, actionText)
            return
        }

        view?.run {
            val actionText = getString(R.string.power_merchant_ok_label)
            Toaster.toasterCustomBottomHeight = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl5)
            Toaster.build(rootView, data.message, Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL, actionText)
                    .show()
        }

        fetchPowerMerchantBasicInfo()
    }

    private fun notifyCancelPmDeactivationWidget() {
        val cancelPmDeactivationWidgetPosition = adapter.data.indexOfFirst { it is WidgetCancelDeactivationSubmissionUiModel }
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
            is UnknownHostException -> getString(R.string.pm_network_error_message)
            else -> getString(R.string.pm_system_error_message)
        }
    }

    private fun reloadPageContent() {
        if (pmBasicInfo == null) {
            fetchPowerMerchantBasicInfo()
        } else {
            view?.swipeRefreshPm?.isRefreshing = true
            fetchPageContent()
        }
    }

    private fun fetchPowerMerchantBasicInfo() {
        sharedViewModel.getPowerMerchantBasicInfo()
    }

    private fun fetchPageContent() {
        if (pmBasicInfo == null) {
            showErrorState(RuntimeException())
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

    private fun initBasicInfo(data: PowerMerchantBasicInfoUiModel) {
        this.pmBasicInfo = data
    }

    private fun setOnPmActivationSuccess(data: PMActivationStatusUiModel) {
        notifyUpgradePmProWidget()

        if (!data.isSuccess) {
            val actionText = getString(R.string.power_merchant_ok_label)
            showErrorToaster(data.message, actionText)
            return
        }

        view?.rootView?.let {
            val actionText = getString(R.string.pm_oke)

            it.post {
                Toaster.toasterCustomBottomHeight = it.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl5)
                Toaster.build(it, data.message, Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL, actionText)
                        .show()
            }

            fetchPowerMerchantBasicInfo()

            powerMerchantTracking.sendEventShowPopupSuccessRegister()
        }
    }

    private fun setOnPmActivationFailed(throwable: Throwable) {
        notifyUpgradePmProWidget()
        val actionText = getString(R.string.pm_try_again)
        showErrorToaster(getErrorMessage(throwable), actionText)
    }

    private fun notifyUpgradePmProWidget() {
        val upgradePmProWidgetPosition = adapter.data.indexOfFirst { it is WidgetUpgradePmProUiModel }
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

    private fun getShopTireByPmTire(pmTire: Int): Int {
        return when (pmTire) {
            PMConstant.PMTierType.POWER_MERCHANT -> PMConstant.ShopTierType.POWER_MERCHANT
            PMConstant.PMTierType.POWER_MERCHANT_PRO -> PMConstant.ShopTierType.POWER_MERCHANT_PRO
            else -> PMConstant.ShopTierType.REGULAR_MERCHANT
        }
    }

    private fun observePmActiveState() {
        mViewModel.pmPmActiveData.observe(viewLifecycleOwner, Observer {
            hideSwipeRefreshLoading()
            when (it) {
                is Success -> renderPmActiveState(it.data)
                is Fail -> {
                    showErrorState(it.throwable)
                    logToCrashlytic(PowerMerchantErrorLogger.PM_ACTIVE_PAGE_ERROR, it.throwable)
                }
            }
        })
    }

    private fun fetchPmActiveState() {
        mViewModel.getPmActiveStateData()
    }

    private fun renderPmActiveState(data: PMActiveDataUiModel) {
        showUpgradePmProStickyView()
        val isAutoExtendEnabled = getAutoExtendEnabled()
        val isPmActive = pmBasicInfo?.pmStatus?.status == PMStatusConst.ACTIVE
        val isPmPro = pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val widgets = mutableListOf<BaseWidgetUiModel>()
        val tickerList = pmBasicInfo?.tickers
        if (!tickerList.isNullOrEmpty() && !isModeratedShop) {
            widgets.add(WidgetTickerUiModel(tickerList))
        }
        if (!isAutoExtendEnabled) {
            widgets.add(WidgetCancelDeactivationSubmissionUiModel(getExpiredTimeFmt()))
        }
        widgets.add(getShopGradeWidgetData(data))
        widgets.add(WidgetDividerUiModel)
        widgets.add(getCurrentShopGradeBenefit(data))
        val shouldShowUpgradePmProWidget = isAutoExtendEnabled && !isPmPro && !pmBasicInfo?.shopInfo?.isNewSeller.orTrue() && isPmActive
        if (shouldShowUpgradePmProWidget) {
            widgets.add(WidgetDividerUiModel)
            getUpgradePmProWidget()?.let {
                widgets.add(it)
            }
        }
        val shouldShowNextGradeWidget = data.nextPMGrade != null && isAutoExtendEnabled
                && data.currentPMGrade?.gradeName != PMShopGrade.ULTIMATE
                && isPmPro && isPmActive
        if (shouldShowNextGradeWidget) {
            val pmProThreshold = pmBasicInfo?.shopInfo?.shopScorePmProThreshold
                    ?: PMShopInfoUiModel.DEFAULT_PM_PRO_SHOP_SCORE_THRESHOLD
            widgets.add(WidgetDividerUiModel)
            widgets.add(getNextShopGradeWidgetData(data))
            widgets.add(WidgetDividerUiModel)
            widgets.add(WidgetNextUpdateUiModel(pmProThreshold, data.nextMonthlyRefreshDate))
        }
        if (pmBasicInfo?.shopInfo?.isNewSeller.orFalse()) {
            widgets.add(WidgetDividerUiModel)
            widgets.add(WidgetSingleCtaUiModel(
                    getString(R.string.pm_pm_transition_period_learnmore),
                    Constant.Url.POWER_MERCHANT_FEATURES
            ))
        }
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
        val shouldShowView = pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT
                && pmBasicInfo?.pmStatus?.status == PMStatusConst.ACTIVE
                && !pmBasicInfo?.shopInfo?.isNewSeller.orTrue() && isAutoExtendEnabled
        view?.viewPmUpgradePmPro?.isVisible = shouldShowView
    }

    private fun hideUpgradePmProStickyView() {
        view?.viewPmUpgradePmPro?.gone()
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

    private fun getNextShopGradeWidgetData(data: PMActiveDataUiModel): WidgetNextShopGradeUiModel {
        val nextGrade = data.nextPMGrade
        return WidgetNextShopGradeUiModel(
                shopLevel = nextGrade?.shopLevel.orZero(),
                shopScoreMin = nextGrade?.shopScoreMin.orZero(),
                gradeName = nextGrade?.gradeName ?: PMShopGrade.ADVANCED,
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
                grade = grade,
                nextMonthlyRefreshDate = data.nextMonthlyRefreshDate,
                nextQuarterlyCalibrationRefreshDate = data.nextQuarterlyCalibrationRefreshDate,
                pmStatus = pmBasicInfo?.pmStatus?.status ?: PMStatusConst.INACTIVE,
                pmTierType = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.POWER_MERCHANT,
                isAutoExtendEnabled = pmBasicInfo?.pmStatus?.autoExtendEnabled.orFalse(),
                items = benefits
        )
    }

    private fun getShopGradeWidgetData(data: PMActiveDataUiModel): WidgetShopGradeUiModel {
        val shopGrade = data.currentPMGrade
        val shopInfo = pmBasicInfo?.shopInfo
        val isPmPro = pmBasicInfo?.pmStatus?.pmTier == PMConstant.PMTierType.POWER_MERCHANT_PRO
        val shopScoreThreshold = if (isPmPro) {
            shopInfo?.shopScorePmProThreshold.orZero()
        } else {
            shopInfo?.shopScoreThreshold.orZero()
        }
        return WidgetShopGradeUiModel(
                isNewSeller = shopInfo?.isNewSeller.orTrue(),
                pmTierType = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.POWER_MERCHANT,
                shopScore = shopInfo?.shopScore.orZero(),
                threshold = shopScoreThreshold,
                gradeBadgeImgUrl = shopGrade?.imgBadgeUrl.orEmpty(),
                gradeBackgroundUrl = shopGrade?.backgroundUrl.orEmpty(),
                pmStatus = pmBasicInfo?.pmStatus?.status ?: PMStatusConst.INACTIVE
        )
    }

    private fun showDeactivationQuestionnaire(pmTireType: Int) {
        val pmExpirationDate = pmBasicInfo?.pmStatus?.expiredTime.orEmpty()
        val currentPmTireType = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.POWER_MERCHANT
        val bottomSheet = DeactivationQuestionnaireBottomSheet.createInstance(pmExpirationDate, currentPmTireType, pmTireType)
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return

        bottomSheet.setOnDeactivationSuccess {
            fetchPowerMerchantBasicInfo()
        }
        bottomSheet.show(childFragmentManager)
    }

    protected fun logToCrashlytic(message: String, throwable: Throwable) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    protected fun hideSwipeRefreshLoading() {
        view?.swipeRefreshPm?.isRefreshing = false
    }

    private fun getUpgradePmProWidget(): WidgetUpgradePmProUiModel? {
        pmBasicInfo?.shopInfo?.let {
            val benefitList = listOf(
                    PMProBenefitUiModel(
                            description = getString(R.string.pm_pro_general_benefit_1, Constant.POWER_MERCHANT_PRO_CHARGING),
                            icon = IconUnify.COURIER_FAST
                    ),
                    PMProBenefitUiModel(
                            description = getString(R.string.pm_pro_general_benefit_2),
                            icon = IconUnify.SEARCH
                    ),
                    PMProBenefitUiModel(
                            description = getString(R.string.pm_pro_general_benefit_3),
                            icon = IconUnify.COURIER
                    ),
                    PMProBenefitUiModel(
                            description = getString(R.string.pm_pro_general_benefit_4),
                            imgUrl = PMConstant.Images.PM_PRO_BADGE
                    ),
                    PMProBenefitUiModel(
                            description = getString(R.string.pm_pro_general_benefit_5),
                            icon = IconUnify.CALL_CENTER
                    ),
                    PMProBenefitUiModel(
                            description = getString(R.string.pm_pro_general_benefit_6),
                            icon = IconUnify.STAR_CIRCLE
                    )
            )
            return WidgetUpgradePmProUiModel(
                    shopInfo = it,
                    registrationTerms = PMRegistrationTermHelper.getPmProRegistrationTerms(requireContext(), it),
                    generalBenefits = benefitList
            )
        }
        return null
    }
}