package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.*
import com.tokopedia.gm.common.data.source.local.model.*
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
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
import kotlinx.android.synthetic.main.fragment_pm_power_merchant_subscription.view.*
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PowerMerchantSubscriptionFragment : BaseListFragment<BaseWidgetUiModel, WidgetAdapterFactoryImpl>(),
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

    private val mViewModel: PowerMerchantSubscriptionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PowerMerchantSubscriptionViewModel::class.java)
    }
    private val sharedViewModel: PowerMerchantSharedViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PowerMerchantSharedViewModel::class.java)
    }

    private val recyclerView: RecyclerView?
        get() = super.getRecyclerView(view)

    private var isModeratedShop = false
    private var upgradePmProWidgetPosition: Int = RecyclerView.NO_POSITION
    private var cancelPmDeactivationWidgetPosition: Int = RecyclerView.NO_POSITION
    private var pmBasicInfo: PowerMerchantBasicInfoUiModel? = null
    private var pmGradeBenefitList: List<PMGradeWithBenefitsUiModel>? = null
    private var currentPmTireType = PMConstant.PMTierType.POWER_MERCHANT

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
    }

    override fun cancelPmDeactivationSubmission(position: Int) {
        cancelPmDeactivationWidgetPosition = position
        observePmCancelDeactivationSubmission()
        val currentPmTireType = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.NA
        val shopTier = getShopTireByPmTire(currentPmTireType)
        mViewModel.cancelPmDeactivationSubmission(shopTier)
    }

    override fun setOnReloadClickListener() {
        reloadPageContent()
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
        this.upgradePmProWidgetPosition = adapterPosition
        submitPmRegistration(PMConstant.ShopTierType.POWER_MERCHANT_PRO)
    }

    fun setOnFooterCtaClickedListener(term: RegistrationTermUiModel?, isEligiblePm: Boolean, tncAgreed: Boolean, nextShopTireType: Int) {
        val shopInfo = pmBasicInfo?.shopInfo ?: return
        when {
            isModeratedShop -> showModeratedShopBottomSheet()
            isEligiblePm -> submitPmRegistrationOnEligible(tncAgreed, nextShopTireType)
            term is RegistrationTermUiModel.ShopScore -> showShopScoreTermBottomSheet(shopInfo)
            term is RegistrationTermUiModel.ActiveProduct -> showActiveProductTermBottomSheet()
            term is RegistrationTermUiModel.Order -> showOrderTermBottomSheet(shopInfo.itemSoldPmProThreshold)
            term is RegistrationTermUiModel.Niv -> showNivTermBottomSheet(shopInfo.nivPmProThreshold)
            term is RegistrationTermUiModel.Kyc -> submitKYC(tncAgreed)
        }
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

    private fun showModeratedShopBottomSheet() {
        val title: String = getString(R.string.pm_bottom_sheet_moderated_shop_title)
        val description: String = getString(R.string.pm_bottom_sheet_moderated_shop_description)
        val ctaText: String = getString(R.string.pm_content_slider_last_slide_button)
        val illustrationUrl: String = PMConstant.Images.PM_INACTIVE

        showNotificationBottomSheet(title, description, ctaText, illustrationUrl)
    }

    private fun setupView() = view?.run {
        swipeRefreshPm.setOnRefreshListener {
            reloadPageContent()
        }
        recyclerView?.gone()
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
        mViewModel.pmActivationStatus.observe(viewLifecycleOwner, Observer {
            hideActivationProgress()
            when (it) {
                is Success -> setOnPmActivationSuccess(it.data)
                is Fail -> setOnPmActivationFailed(it.throwable)
            }
        })
    }

    private fun observePmCancelDeactivationSubmission() {
        mViewModel.pmCancelDeactivationStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> setOnCancelDeactivationSuccess(it.data)
                is Fail -> setOnCancelDeactivationFailed(it.throwable)
            }
        })
    }

    private fun setOnCancelDeactivationFailed(throwable: Throwable) {
        notifyCancelPmDeactivationWidget()
        val actionText = getString(R.string.pm_try_again)
        showErrorToaster(getErrorMessage(throwable), actionText)
    }

    private fun setOnCancelDeactivationSuccess(data: PMActivationStatusUiModel) {
        if (!data.isSuccess) {
            setOnCancelDeactivationFailed(MessageErrorException(data.errorMessage))
            return
        }

        notifyCancelPmDeactivationWidget()

        view?.run {
            val message = getString(R.string.pm_cancel_pm_deactivation_success)
            val actionText = getString(R.string.power_merchant_ok_label)
            Toaster.toasterCustomBottomHeight = context.resources.getDimensionPixelSize(R.dimen.layout_lvl5)
            Toaster.build(rootView, message, Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL, actionText)
                    .show()
        }

        fetchPowerMerchantBasicInfo()
    }

    private fun notifyCancelPmDeactivationWidget() {
        if (cancelPmDeactivationWidgetPosition != RecyclerView.NO_POSITION) {
            recyclerView?.post {
                adapter.notifyItemChanged(cancelPmDeactivationWidgetPosition)
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

    private fun showErrorState() {
        (activity as? SubscriptionActivityInterface)?.showErrorState()
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

    private fun initBasicInfo(data: PowerMerchantBasicInfoUiModel) {
        this.pmBasicInfo = data

        val defaultTire = PMConstant.PMTierType.POWER_MERCHANT
        currentPmTireType = arguments?.getInt(KEY_PM_TIER_TYPE, defaultTire) ?: defaultTire
    }

    private fun renderPmRegistrationWidgets() {
        pmBasicInfo?.shopInfo?.let { shopInfo ->
            val registrationHeaderWidget = getRegistrationHeaderWidgetData(shopInfo)

            val isPmPro = currentPmTireType == PMConstant.PMTierType.POWER_MERCHANT_PRO
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

    private fun renderPmProRegistrationWidgets(headerWidget: WidgetRegistrationHeaderUiModel) {
        val widgets = listOf(
                headerWidget,
                WidgetDividerUiModel,
                getPmGradeBenefitWidget()
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
                getPmGradeBenefitWidget()
        )
        recyclerView?.visible()
        adapter.clearAllElements()
        renderList(widgets, false)
    }

    private fun getPmGradeBenefitWidget(): WidgetGradeBenefitUiModel {
        val gradeBenefitList = pmGradeBenefitList.orEmpty()
        return WidgetGradeBenefitUiModel(
                selectedPmTireType = currentPmTireType,
                benefitPages = gradeBenefitList.filter { it.pmTier == currentPmTireType }
        )
    }

    private fun setOnPmActivationSuccess(data: PMActivationStatusUiModel) {
        if (!data.isSuccess) {
            setOnPmActivationFailed(MessageErrorException(data.errorMessage))
            return
        }

        notifyUpgradePmProWidget()

        view?.rootView?.let {
            val isPmPro = data.currentShopTier == PMConstant.ShopTierType.POWER_MERCHANT_PRO
            val message = if (isPmPro) {
                getString(R.string.pm_pro_registration_success_message)
            } else {
                getString(R.string.pm_registration_success_message)
            }
            val actionText = getString(R.string.oke)

            it.post {
                Toaster.toasterCustomBottomHeight = it.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl5)
                Toaster.build(it, message, Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL, actionText)
                        .show()
            }

            fetchPowerMerchantBasicInfo()
        }
    }

    private fun setOnPmActivationFailed(throwable: Throwable) {
        notifyUpgradePmProWidget()
        val actionText = getString(R.string.pm_try_again)
        showErrorToaster(getErrorMessage(throwable), actionText)
    }

    private fun notifyUpgradePmProWidget() {
        if (upgradePmProWidgetPosition != RecyclerView.NO_POSITION) {
            recyclerView?.post {
                adapter.notifyItemChanged(upgradePmProWidgetPosition)
            }
        }
    }

    private fun showActivationProgress() {
        (activity as? SubscriptionActivityInterface)?.showActivationProgress()
    }

    private fun hideActivationProgress() {
        (activity as? SubscriptionActivityInterface)?.hideActivationProgress()
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
        val isPmPro = currentPmTireType == PMConstant.PMTierType.POWER_MERCHANT_PRO
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

    private fun submitPmRegistrationOnEligible(isTncChecked: Boolean, nextShopTireType: Int) {
        if (!isTncChecked) {
            val message = getString(R.string.pm_tnc_agreement_error_message)
            val actionText = getString(R.string.power_merchant_ok_label)
            showErrorToaster(message, actionText)
            return
        }

        showActivationProgress()
        submitPmRegistration(nextShopTireType)
    }

    private fun submitPmRegistration(nextShopTireType: Int) {
        if (isModeratedShop) {
            showModeratedShopBottomSheet()
            return
        }

        val currentPmTire = pmBasicInfo?.pmStatus?.pmTier ?: PMConstant.PMTierType.NA
        val currentShopTireType = getShopTireByPmTire(currentPmTire)

        observePmActivationStatus()
        mViewModel.submitPMActivation(currentShopTireType, nextShopTireType)
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
        val shouldShowUpgradePmProWidget = isAutoExtendEnabled && !isPmPro && !pmBasicInfo?.shopInfo?.isNewSeller.orTrue()
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
            val pmProThreshold = pmBasicInfo?.shopInfo?.itemSoldPmProThreshold
                    ?: PMShopInfoUiModel.DEFAULT_PM_PRO_SHOP_SCORE_THRESHOLD
            widgets.add(WidgetDividerUiModel)
            widgets.add(getNextShopGradeWidgetData(data))
            widgets.add(WidgetDividerUiModel)
            widgets.add(WidgetNextUpdateUiModel(pmProThreshold, data.nextQuarterlyCalibrationRefreshDate))
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

    private fun observePmRegistrationPage() {
        mViewModel.pmGradeBenefitInfo.observe(viewLifecycleOwner, Observer {
            hideSwipeRefreshLoading()
            when (it) {
                is Success -> {
                    this.pmGradeBenefitList = it.data
                    renderPmRegistrationWidgets()
                }
                is Fail -> {
                    showErrorState()
                    logToCrashlytic(PowerMerchantErrorLogger.REGISTRATION_PAGE_ERROR, it.throwable)
                }
            }
        })
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
                grade = grade,
                nextMonthlyRefreshDate = data.nextMonthlyRefreshDate,
                nextQuarterlyCalibrationRefreshDate = data.nextQuarterlyCalibrationRefreshDate,
                pmStatus = data.pmStatus,
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
                shopScore = pmBasicInfo?.shopInfo?.shopScore.orZero(),
                threshold = shopScoreThreshold,
                gradeBadgeImgUrl = shopGrade?.imgBadgeUrl.orEmpty(),
                gradeBackgroundUrl = shopGrade?.backgroundUrl.orEmpty(),
                pmStatus = data.pmStatus
        )
    }

    private fun getRegistrationHeaderWidgetData(shopInfo: PMShopInfoUiModel): WidgetRegistrationHeaderUiModel {
        return WidgetRegistrationHeaderUiModel(
                shopInfo = shopInfo,
                registrationTerms = if (currentPmTireType == PMConstant.PMTierType.POWER_MERCHANT) {
                    PMRegistrationTermHelper.getPmRegistrationTerms(requireContext(), shopInfo)
                } else {
                    PMRegistrationTermHelper.getPmProRegistrationTerms(requireContext(), shopInfo)
                },
                selectedPmType = currentPmTireType
        )
    }

    private fun showDeactivationQuestionnaire(pmTireType: Int) {
        val pmExpirationDate = pmBasicInfo?.pmStatus?.expiredTime.orEmpty()
        val bottomSheet = DeactivationQuestionnaireBottomSheet.createInstance(pmExpirationDate, pmTireType)
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return

        bottomSheet.setOnDeactivationSuccess {
            fetchPowerMerchantBasicInfo()
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun logToCrashlytic(message: String, throwable: Throwable) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    private fun hideSwipeRefreshLoading() {
        view?.swipeRefreshPm?.isRefreshing = false
    }

    private fun getUpgradePmProWidget(): WidgetUpgradePmProUiModel? {
        pmBasicInfo?.shopInfo?.let {
            val benefitList = listOf(
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_pro_general_benefit_1, Constant.POWER_MERCHANT_PRO_CHARGING),
                            drawableResIcon = R.drawable.ic_pm_free_shipping_rounded
                    ),
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_pro_general_benefit_2),
                            drawableResIcon = R.drawable.ic_pm_search_rounded
                    ),
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_pro_general_benefit_3),
                            drawableResIcon = R.drawable.ic_pm_gosend_rounded
                    ),
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_pro_general_benefit_4),
                            drawableResIcon = R.drawable.ic_pm_pro_bange_rounded
                    ),
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_pro_general_benefit_5),
                            drawableResIcon = R.drawable.ic_pm_tokopedia_care_rounded
                    ),
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_pro_general_benefit_6),
                            drawableResIcon = R.drawable.ic_pm_star_rounded
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