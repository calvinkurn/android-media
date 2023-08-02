package com.tokopedia.power_merchant.subscribe.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantBasicInfoUiModel
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.performance.PMPerformanceMonitoring
import com.tokopedia.power_merchant.subscribe.analytics.performance.PerformanceMonitoringConst
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.databinding.ActivityPmSubsriptionBinding
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.PMTermAndConditionBottomSheet
import com.tokopedia.power_merchant.subscribe.view.fragment.PMRegistrationFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscriptionFragment
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.ModerationShopStatusUiModel
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSharedViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/02/21
 */

class SubscriptionActivity : BaseSimpleActivity(), HasComponent<PowerMerchantSubscribeComponent>,
    SubscriptionActivityInterface {

    companion object {
        private const val REQUEST_LOGIN = 1313
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    private var binding: ActivityPmSubsriptionBinding? = null

    private var performanceMonitoring: PMPerformanceMonitoring? = null
    private val sharedViewModel: PowerMerchantSharedViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PowerMerchantSharedViewModel::class.java)
    }

    private val pmActiveStatePage: PowerMerchantSubscriptionFragment by lazy {
        PowerMerchantSubscriptionFragment.createInstance()
    }
    private val pmRegistrationPage: PMRegistrationFragment by lazy {
        PMRegistrationFragment.createInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        initInjector()
        switchPMToWebView()
        binding = ActivityPmSubsriptionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (userSession.isLoggedIn) {
            fetchPmBasicInfo(true)
        } else {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_LOGIN)
        }
        setupView()
        observePmBasicInfo()
        observeShopModerationStatus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOGIN -> handleAfterLogin()
        }
    }

    override fun getNewFragment(): Fragment? = null

    private fun handleAfterLogin() {
        if (userSession.isLoggedIn) {
            fetchPmBasicInfo(true)
        } else {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun getComponent(): PowerMerchantSubscribeComponent {
        val appComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerPowerMerchantSubscribeComponent.builder()
            .baseAppComponent(appComponent)
            .build()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setViewForPmSuccessState() {
        binding?.run {
            loaderPmSubscription.gone()
            framePmFragment.visible()
            globalErrorPmSubscription.gone()
        }
    }

    override fun setViewForRegistrationPage() {
        binding?.run {
            loaderPmSubscription.gone()
            framePmFragment.visible()
            globalErrorPmSubscription.gone()
        }
    }

    override fun renderFooterView() {
        (sharedViewModel.powerMerchantBasicInfo.value as? Success)?.data?.let {
            setupFooterView(it)
        }
    }

    override fun hideFooterView() {
        binding?.pmRegistrationFooterView?.gone()
    }

    override fun showLoadingState() {
        binding?.run {
            loaderPmSubscription.visible()
            framePmFragment.gone()
            globalErrorPmSubscription.gone()
        }
    }

    override fun showErrorState(throwable: Throwable) {
        binding?.run {
            loaderPmSubscription.gone()
            framePmFragment.gone()
            globalErrorPmSubscription.visible()
            globalErrorPmSubscription.setActionClickListener {
                fetchPmBasicInfo(false)
            }
        }
    }

    override fun showActivationProgress() {
        binding?.pmRegistrationFooterView?.showLoadingState()
    }

    override fun hideActivationProgress() {
        binding?.pmRegistrationFooterView?.hideLoadingState()
    }

    override fun stopRenderPerformanceMonitoring() {
        stopPerformanceMonitoring()
    }

    override fun startCustomMetricPerformanceMonitoring(tag: String) {
        startCustomMetricMonitoring(tag)
    }

    override fun stopCustomMetricPerformanceMonitoring(tag: String) {
        stopCustomMetricMonitoring(tag)
    }

    private fun observeShopModerationStatus() {
        startCustomMetricMonitoring(PerformanceMonitoringConst.PM_SHOP_MODERATION_STATUS_METRICS)
        sharedViewModel.getShopModerationStatus(userSession.shopId.toLongOrZero())

        observe(sharedViewModel.shopModerationStatus) {
            stopCustomMetricMonitoring(PerformanceMonitoringConst.PM_SHOP_MODERATION_STATUS_METRICS)
            when (it) {
                is Success -> showModerationShopTicker(it.data)
                is Fail -> logToCrashlytics(
                    it.throwable,
                    PowerMerchantErrorLogger.PM_SHOP_MODERATION_STATUS_ERROR
                )
            }
        }
    }

    private fun observePmBasicInfo() {
        observe(sharedViewModel.powerMerchantBasicInfo) {
            startRenderPerformanceMonitoring()
            stopCustomMetricPerformanceMonitoring(PerformanceMonitoringConst.PM_BASIC_INFO_METRICS)
            when (it) {
                is Success -> setOnSuccessGetBasicInfo(it.data)
                is Fail -> {
                    showErrorState(it.throwable)
                    logToCrashlytics(it.throwable, PowerMerchantErrorLogger.PM_BASIC_INFO_ERROR)
                }
            }
        }
    }

    private fun showModerationShopTicker(data: ModerationShopStatusUiModel) {
        binding?.run {
            if (!data.isModeratedShop) {
                tickerPmContainer.gone()
                return
            }

            tickerPmContainer.visible()
            val tickerTitle = getString(R.string.pm_moderated_shop_ticker_title)
            val tickerDescription = getString(R.string.pm_moderated_shop_ticker_description)
            val tickersData =
                listOf(TickerData(tickerTitle, tickerDescription, Ticker.TYPE_WARNING, false))
            tickerPmView.run {
                val adapter = TickerPagerAdapter(context, tickersData)
                addPagerView(adapter, tickersData)
            }
        }
    }

    private fun setOnSuccessGetBasicInfo(data: PowerMerchantBasicInfoUiModel) {
        setupViewPager(data)
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setupView() {
        window.decorView.setBackgroundColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_Background))
        setSupportActionBar(binding?.toolbarPmSubscription)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun fetchPmBasicInfo(isFirstLoad: Boolean) {
        showLoadingState()
        startNetworkPerformanceMonitoring()
        startCustomMetricPerformanceMonitoring(PerformanceMonitoringConst.PM_BASIC_INFO_METRICS)
        sharedViewModel.getPowerMerchantBasicInfo(isFirstLoad)
    }

    private fun setupViewPager(data: PowerMerchantBasicInfoUiModel) {
        when (data.pmStatus.status) {
            PMStatusConst.INACTIVE -> setupRegistrationPage()
            else -> setupActiveState()
        }

        setupFooterView(data)
    }

    private fun setupActiveState() {
        binding?.pmRegistrationFooterView?.gone()
        setViewForPmSuccessState()
        supportFragmentManager.beginTransaction()
            .replace(R.id.framePmFragment, pmActiveStatePage)
            .commit()
    }

    private fun setupRegistrationPage() {
        setViewForRegistrationPage()
        supportFragmentManager.beginTransaction()
            .replace(R.id.framePmFragment, pmRegistrationPage)
            .commit()
    }

    private fun setupFooterView(data: PowerMerchantBasicInfoUiModel) {
        if (data.pmStatus.status != PMStatusConst.INACTIVE) {
            binding?.pmRegistrationFooterView?.gone()
            return
        }

        val shopInfo = data.shopInfo
        val isRegularMerchant = data.pmStatus.pmTier == PMConstant.PMTierType.POWER_MERCHANT &&
                data.pmStatus.status == PMStatusConst.INACTIVE

        val registrationTerms = PMRegistrationTermHelper.getPmRegistrationTerms(
            this,
            shopInfo,
            isRegularMerchant
        )

        val isEligiblePm = shopInfo.isEligiblePm && !registrationTerms.any { !it.isChecked }

        val firstPriorityTerm = registrationTerms.filter {
            if (!shopInfo.isNewSeller) {
                !it.isChecked && it !is RegistrationTermUiModel.ActiveProduct
            } else {
                !it.isChecked
            }
        }.sortedBy { it.periority }.firstOrNull()

        //show tnc check box only if kyc not eligible or pm/pm pro eligible
        val needTnC = firstPriorityTerm is RegistrationTermUiModel.Kyc || isEligiblePm

        val ctaText = if (needTnC) {
            getString(R.string.power_merchant_register_now)
        } else {
            getString(R.string.pm_interested_to_register)
        }

        binding?.pmRegistrationFooterView?.run {
            if (shopInfo.kycStatusId == KYCStatusId.PENDING) gone() else visible()
            setCtaText(ctaText)
            setTnCVisibility(needTnC)
            setOnTickboxCheckedListener {
                powerMerchantTracking.sendEventClickTickBox()
            }
            setOnCtaClickListener { tncAgreed ->
                pmRegistrationPage.setOnFooterCtaClickedListener(
                    firstPriorityTerm,
                    isEligiblePm,
                    tncAgreed
                )
            }
            setOnTncClickListener {
                showPmTermAndCondition()
            }
        }
    }

    private fun showPmTermAndCondition() {
        val bottomSheet = PMTermAndConditionBottomSheet.newInstance()
        if (supportFragmentManager.isStateSaved || bottomSheet.isAdded) {
            return
        }

        bottomSheet.show(supportFragmentManager)
    }

    private fun switchPMToWebView() {
        val remoteConfig = FirebaseRemoteConfigImpl(this)
        val isSwitchPMToWebView =
            remoteConfig.getBoolean(RemoteConfigKey.PM_SWITCH_TO_WEB_VIEW, false)

        if (isSwitchPMToWebView) {
            RouteManager.route(
                this,
                ApplinkConstInternalGlobal.WEBVIEW,
                PowerMerchantDeepLinkMapper.PM_WEBVIEW_URL
            )
            finish()
        }
    }

    private fun logToCrashlytics(throwable: Throwable, message: String) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    private fun initPerformanceMonitoring() {
        if (UserSession(this).isLoggedIn) {
            performanceMonitoring = PMPerformanceMonitoring()
            performanceMonitoring?.initPerformanceMonitoring()
        }
    }

    private fun startNetworkPerformanceMonitoring() {
        performanceMonitoring?.startNetworkPerformanceMonitoring()
    }

    private fun startRenderPerformanceMonitoring() {
        performanceMonitoring?.startRenderPerformanceMonitoring()
    }

    private fun startCustomMetricMonitoring(tag: String) {
        performanceMonitoring?.startCustomMetric(tag)
    }

    private fun stopCustomMetricMonitoring(tag: String) {
        performanceMonitoring?.stopCustomMetric(tag)
    }

    private fun stopPerformanceMonitoring() {
        performanceMonitoring?.stopRenderPerformanceMonitoring()
    }
}
