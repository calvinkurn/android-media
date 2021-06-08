package com.tokopedia.power_merchant.subscribe.view.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantBasicInfoUiModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.PMTermAndConditionBottomSheet
import com.tokopedia.power_merchant.subscribe.view.fragment.PMRegistrationFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscriptionFragment
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.helper.PMViewPagerAdapter
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
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_pm_subsription.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/02/21
 */

class SubscriptionActivity : BaseActivity(), HasComponent<PowerMerchantSubscribeComponent>, SubscriptionActivityInterface {

    companion object {
        private const val PM_TAB_INDEX = 0
        private const val PM_PRO_TAB_INDEX = 1
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    private val sharedViewModel: PowerMerchantSharedViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PowerMerchantSharedViewModel::class.java)
    }

    private var pmActiveStatePage: PowerMerchantSubscriptionFragment? = null
    private val pmRegistrationPage: Pair<String, PMRegistrationFragment> by lazy {
        val title = getString(R.string.pm_power_merchant)
        val rpmTire = PMConstant.PMTierType.POWER_MERCHANT
        Pair(title, PMRegistrationFragment.createInstance(rpmTire))
    }
    private val pmProRegistrationPage: Pair<String, PMRegistrationFragment> by lazy {
        val title = getString(R.string.pm_power_merchant_pro)
        val pmProTire = PMConstant.PMTierType.POWER_MERCHANT_PRO
        Pair(title, PMRegistrationFragment.createInstance(pmProTire))
    }

    private val viewPagerAdapter by lazy {
        PMViewPagerAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        switchPMToWebView()
        setContentView(R.layout.activity_pm_subsription)
        window.decorView.setBackgroundColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))

        fetchPmBasicInfo(true)
        setupView()
        observePmBasicInfo()
        observeShopModerationStatus()
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
        loaderPmSubscription.gone()
        imgPmHeaderBackdrop.gone()
        imgPmHeaderImage.gone()
        tvPmHeaderDesc.gone()
        tabPmSubscription.gone()
        viewPagerPmSubscription.visible()
        globalErrorPmSubscription.gone()
    }

    override fun setViewForRegistrationPage() {
        loaderPmSubscription.gone()
        imgPmHeaderBackdrop.visible()
        imgPmHeaderImage.visible()
        tvPmHeaderDesc.visible()
        tabPmSubscription.visible()
        viewPagerPmSubscription.visible()
        globalErrorPmSubscription.gone()
    }

    override fun showLoadingState() {
        loaderPmSubscription.visible()
        imgPmHeaderBackdrop.gone()
        imgPmHeaderImage.gone()
        tvPmHeaderDesc.gone()
        tabPmSubscription.gone()
        viewPagerPmSubscription.gone()
        globalErrorPmSubscription.gone()
    }

    override fun showErrorState(throwable: Throwable) {
        loaderPmSubscription.gone()
        imgPmHeaderBackdrop.gone()
        imgPmHeaderImage.gone()
        tvPmHeaderDesc.gone()
        tabPmSubscription.gone()
        viewPagerPmSubscription.gone()
        globalErrorPmSubscription.visible()
        globalErrorPmSubscription.setActionClickListener {
            fetchPmBasicInfo(false)
        }
    }

    override fun showActivationProgress() {
        pmRegistrationFooterView.showLoadingState()
    }

    override fun hideActivationProgress() {
        pmRegistrationFooterView.hideLoadingState()
    }

    private fun observeShopModerationStatus() {
        sharedViewModel.getShopModerationStatus(userSession.shopId.toLongOrZero())
        observe(sharedViewModel.shopModerationStatus) {
            when (it) {
                is Success -> showModerationShopTicker(it.data)
                is Fail -> logToCrashlytics(it.throwable, PowerMerchantErrorLogger.PM_SHOP_MODERATION_STATUS_ERROR)
            }
        }
    }

    private fun observePmBasicInfo() {
        observe(sharedViewModel.powerMerchantBasicInfo) {
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
        if (!data.isModeratedShop) {
            tickerPmContainer.gone()
            return
        }

        tickerPmContainer.visible()
        val tickerTitle = getString(R.string.pm_moderated_shop_ticker_title)
        val tickerDescription = getString(R.string.pm_moderated_shop_ticker_description)
        val tickersData = listOf(TickerData(tickerTitle, tickerDescription, Ticker.TYPE_WARNING, false))
        tickerPmView.run {
            val adapter = TickerPagerAdapter(context, tickersData)
            addPagerView(adapter, tickersData)
        }
    }

    private fun setOnSuccessGetBasicInfo(data: PowerMerchantBasicInfoUiModel) {
        setupViewPager(data)
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setupView() {
        setSupportActionBar(toolbarPmSubscription)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setWhiteStatusBar()
    }

    private fun fetchPmBasicInfo(isFirstLoad: Boolean) {
        showLoadingState()
        sharedViewModel.getPowerMerchantBasicInfo(isFirstLoad)
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    private fun setupViewPager(data: PowerMerchantBasicInfoUiModel) {
        when (data.pmStatus.status) {
            PMStatusConst.INACTIVE -> setupRegistrationPage(data)
            else -> setupActiveState()
        }

        viewPagerPmSubscription.adapter = viewPagerAdapter
        tabPmSubscription.tabLayout.removeAllTabs()
        viewPagerAdapter.getTitles().forEach {
            tabPmSubscription.addNewTab(it)
        }

        tabPmSubscription.tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        tabPmSubscription.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabIndex = tabPmSubscription.tabLayout.selectedTabPosition
                setOnTabIndexSelected(data, tabIndex)
                sendTrackerOnPMTabClicked(tabIndex)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        tabPmSubscription.setupWithViewPager(viewPagerPmSubscription)

        val isChargingPeriodPmPro = data.periodTypePmPro == PeriodType.CHARGING_PERIOD_PM_PRO
        val defaultTabIndex = if (data.shopInfo.isEligiblePmPro && isChargingPeriodPmPro) {
            PM_PRO_TAB_INDEX
        } else {
            PM_TAB_INDEX
        }
        setOnTabIndexSelected(data, defaultTabIndex)
        tabPmSubscription.tabLayout.getTabAt(defaultTabIndex)?.select()

        val isSingleOrEmptyTab = viewPagerAdapter.getTitles().size <= 1
        tabPmSubscription.isVisible = !isSingleOrEmptyTab
    }

    private fun sendTrackerOnPMTabClicked(tabIndex: Int) {
        if (tabIndex == PM_PRO_TAB_INDEX) {
            powerMerchantTracking.sendEventClickTabPowerMerchantPro()
        }
    }

    private fun setupActiveState() {
        pmRegistrationFooterView.gone()
        setViewForPmSuccessState()
        viewPagerAdapter.clearFragment()
        if (pmActiveStatePage == null) {
            pmActiveStatePage = PowerMerchantSubscriptionFragment.createInstance()
        }
        pmActiveStatePage?.let {
            viewPagerAdapter.addFragment(it, getString(R.string.pm_power_merchant))
        }
    }

    private fun setupRegistrationPage(data: PowerMerchantBasicInfoUiModel) {
        setViewForRegistrationPage()
        viewPagerAdapter.clearFragment()
        viewPagerAdapter.addFragment(pmRegistrationPage.second, pmRegistrationPage.first)
        if (data.periodTypePmPro == PeriodType.CHARGING_PERIOD_PM_PRO) {
            viewPagerAdapter.addFragment(pmProRegistrationPage.second, pmProRegistrationPage.first)
        }
    }

    private fun setOnTabIndexSelected(data: PowerMerchantBasicInfoUiModel, tabIndex: Int) {
        val isPmProSelected = tabIndex == 1

        if (isPmProSelected) {
            imgPmHeaderBackdrop.loadImage(Constant.Image.PM_BG_REGISTRATION_PM_PRO)
            imgPmHeaderImage.loadImage(PMConstant.Images.PM_PRO_BADGE)
            tvPmHeaderDesc.setText(R.string.pm_registration_header_pm_pro)
        } else {
            imgPmHeaderBackdrop.loadImage(R.drawable.bg_pm_registration_header)
            imgPmHeaderImage.loadImage(PMConstant.Images.PM_BADGE)
            tvPmHeaderDesc.setText(R.string.pm_registration_header_pm)
        }

        setupFooterView(data, isPmProSelected)
    }

    private fun setupFooterView(data: PowerMerchantBasicInfoUiModel, isPmProSelected: Boolean) {
        if (data.pmStatus.status != PMStatusConst.INACTIVE) {
            pmRegistrationFooterView.gone()
            return
        }

        val shopInfo = data.shopInfo

        val registrationTerms = if (isPmProSelected) {
            PMRegistrationTermHelper.getPmProRegistrationTerms(this, shopInfo)
        } else {
            PMRegistrationTermHelper.getPmRegistrationTerms(this, shopInfo)
        }

        val isEligiblePm = (if (isPmProSelected) shopInfo.isEligiblePmPro else shopInfo.isEligiblePm)
                && !registrationTerms.any { !it.isChecked }

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

        with(pmRegistrationFooterView) {
            if (shopInfo.kycStatusId == KYCStatusId.PENDING) gone() else visible()
            setCtaText(ctaText)
            setTnCVisibility(needTnC)
            setOnTickboxCheckedListener {
                powerMerchantTracking.sendEventClickTickBox()
            }
            setOnCtaClickListener { tncAgreed ->
                if (isPmProSelected) {
                    pmProRegistrationPage.second.setOnFooterCtaClickedListener(firstPriorityTerm, isEligiblePm, tncAgreed, PMConstant.ShopTierType.POWER_MERCHANT_PRO)
                } else {
                    pmRegistrationPage.second.setOnFooterCtaClickedListener(firstPriorityTerm, isEligiblePm, tncAgreed, PMConstant.ShopTierType.POWER_MERCHANT)
                }
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
        val isSwitchPMToWebView = remoteConfig.getBoolean(RemoteConfigKey.PM_SWITCH_TO_WEB_VIEW, false)

        if (isSwitchPMToWebView) {
            RouteManager.route(this, ApplinkConstInternalGlobal.WEBVIEW, PowerMerchantDeepLinkMapper.PM_WEBVIEW_URL)
            finish()
        }
    }

    private fun logToCrashlytics(throwable: Throwable, message: String) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }
}