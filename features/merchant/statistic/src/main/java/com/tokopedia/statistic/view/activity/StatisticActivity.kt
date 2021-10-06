package com.tokopedia.statistic.view.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.analytics.performance.StatisticIdlingResourceListener
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoring
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringInterface
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringListener
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.common.StatisticPageHelper
import com.tokopedia.statistic.common.utils.StatisticAppLinkHandler
import com.tokopedia.statistic.common.utils.StatisticRemoteConfig
import com.tokopedia.statistic.common.utils.logger.StatisticLogger
import com.tokopedia.statistic.databinding.ActivityStcStatisticBinding
import com.tokopedia.statistic.di.DaggerStatisticComponent
import com.tokopedia.statistic.di.StatisticComponent
import com.tokopedia.statistic.view.fragment.StatisticFragment
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import com.tokopedia.statistic.view.viewhelper.FragmentListener
import com.tokopedia.statistic.view.viewhelper.StatisticViewPagerAdapter
import com.tokopedia.statistic.view.viewhelper.setOnTabSelectedListener
import com.tokopedia.statistic.view.viewmodel.StatisticActivityViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

// Internal applink : ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD

class StatisticActivity : BaseActivity(), HasComponent<StatisticComponent>,
    FragmentListener, StatisticPerformanceMonitoringListener {

    companion object {
        private const val FIRST_TAB_INDEX = 0
        private const val TAB_LIMIT = 3
        private const val TOAST_DURATION = 2000L
        private const val TOAST_COUNT_DOWN_INTERVAL = 1000L
        private const val MANAGE_SHOP_STATS_ROLE = "MANAGE_SHOPSTATS"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: StatisticActivityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(StatisticActivityViewModel::class.java)
    }
    private var pages: List<StatisticPageUiModel> = emptyList()
    private var viewPagerAdapter: StatisticViewPagerAdapter? = null
    private val performanceMonitoring: StatisticPerformanceMonitoringInterface by lazy {
        StatisticPerformanceMonitoring()
    }
    var pltListener: StatisticIdlingResourceListener? = null

    private val coachMark by lazy { CoachMark2(this) }

    private var selectedPageSource = ""
    private var selectedWidget = ""
    private var binding: ActivityStcStatisticBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        initInjector()

        checkWhiteListStatus()
        binding = ActivityStcStatisticBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        setupView()
        initVar()
        handleAppLink(intent)
        setWhiteStatusBar()

        observeWhiteListStatus()
        observeUserRole()
    }

    override fun getComponent(): StatisticComponent {
        return DaggerStatisticComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleAppLink(intent)
    }

    override fun startNetworkPerformanceMonitoring() {
        performanceMonitoring.startNetworkPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        performanceMonitoring.startRenderPerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        performanceMonitoring.stopPerformanceMonitoring()
        pltListener?.stopMonitoring()
    }

    override fun setHeaderSubTitle(subTitle: String) {
        binding?.headerStcStatistic?.headerSubTitle = subTitle
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initVar() {
        viewPagerAdapter = StatisticViewPagerAdapter(supportFragmentManager)
    }

    private fun getStatisticPages(isWhiteListed: Boolean): List<StatisticPageUiModel> {
        return if (isWhiteListed) {
            getWhiteListedPages()
        } else {
            getNonWhiteListedPages()
        }
    }

    private fun getWhiteListedPages(): List<StatisticPageUiModel> {
        val remoteConfig = StatisticRemoteConfig(FirebaseRemoteConfigImpl(applicationContext))
        return listOf(
            StatisticPageHelper.getShopStatistic(this, userSession, remoteConfig),
            StatisticPageHelper.getProductStatistic(this, userSession, remoteConfig),
            StatisticPageHelper.getOperationalStatistic(this, userSession),
            StatisticPageHelper.getBuyerStatistic(this, userSession)
        )
    }

    private fun getNonWhiteListedPages(): List<StatisticPageUiModel> {
        val remoteConfig = StatisticRemoteConfig(FirebaseRemoteConfigImpl(applicationContext))
        return listOf(
            StatisticPageHelper.getShopStatistic(this, userSession, remoteConfig),
            StatisticPageHelper.getProductStatistic(this, userSession, remoteConfig),
            StatisticPageHelper.getBuyerStatistic(this, userSession)
        )
    }

    private fun setupView() = binding?.run {
        setSupportActionBar(headerStcStatistic)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.stc_statistic)

        tabStatistic.tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        tabStatistic.tabLayout.setOnTabSelectedListener {
            val tabIndex = tabStatistic.tabLayout.selectedTabPosition
            val title = viewPagerAdapter?.titles?.getOrNull(tabIndex).orEmpty()
            dismissCoachMarkOnTabSelected()
            StatisticTracker.sendPageTabClickEvent(userSession.userId, title)
        }
    }

    private fun setupViewPager(isWhiteListed: Boolean) {
        pages = getStatisticPages(isWhiteListed)
        pages.forEachIndexed { index, page ->
            val shouldLoadDataOnCreate = if (selectedPageSource.isNotBlank()) {
                page.pageSource == selectedPageSource
            } else {
                index == FIRST_TAB_INDEX
            }
            viewPagerAdapter?.addFragment(
                StatisticFragment.newInstance(
                    page,
                    shouldLoadDataOnCreate
                ), page.pageTitle
            )
        }

        viewPagerAdapter?.let {
            setupTabs()
            binding?.run {
                viewPagerStatistic.adapter = it
                tabStatistic.setupWithViewPager(viewPagerStatistic)
                viewPagerStatistic.offscreenPageLimit = it.titles.size
            }
        }

        selectTabByPageSource()
    }

    private fun observeWhiteListStatus() {
        binding?.progressBarStcActivity?.visible()
        viewModel.whitelistedStatus.observe(this) {
            binding?.progressBarStcActivity?.gone()
            when (it) {
                is Success -> setupViewPager(it.data)
                else -> setupViewPager(false)
            }
        }
    }

    private fun observeUserRole() {
        viewModel.userRole.observe(this, {
            when (it) {
                is Success -> checkUserRole(it.data)
                is Fail -> StatisticLogger.logToCrashlytics(
                    it.throwable,
                    StatisticLogger.ERROR_SELLER_ROLE
                )
            }
        })
        viewModel.getUserRole()
    }

    private fun setupTabs() = binding?.run {
        val coachMarkItems = mutableListOf<CoachMark2Item>()

        viewPagerAdapter?.let { adapter ->
            if (adapter.titles.isNotEmpty()) {
                tabStatistic.visible()
                tabStatistic.tabLayout.removeAllTabs()
            }
            setTabMode(adapter.titles.size)
            tabStatistic.tabLayout.removeAllTabs()
            adapter.titles.forEachIndexed { index, title ->
                val isFirstIndex = index == FIRST_TAB_INDEX
                val tab = tabStatistic.addNewTab(title, isFirstIndex)
                sendTabImpressionEvent(tab.view, title)

                getOperationalInsightCoachMark(title, tab.view)?.let {
                    coachMarkItems.add(it)
                }
                getProductInsightCoachMark(title, tab.view)?.let {
                    coachMarkItems.add(it)
                }
            }
        }

        showCoachMark(coachMarkItems)
    }

    private fun setTabMode(numberOfTabs: Int) {
        if (numberOfTabs <= TAB_LIMIT) {
            binding?.tabStatistic?.customTabMode = TabLayout.MODE_FIXED
        } else {
            binding?.tabStatistic?.customTabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun showCoachMark(coachMarkItems: List<CoachMark2Item>) {
        if (coachMarkItems.isNotEmpty()) {
            coachMark.showCoachMark(ArrayList(coachMarkItems))
            coachMark.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    saveCoachMarkHasShownByTitle(coachMarkItem.title.toString())
                }
            })
            saveCoachMarkHasShownByTitle(coachMarkItems.first().title.toString())
        }
    }

    private fun sendTabImpressionEvent(view: TabLayout.TabView, title: String) {
        view.addOneTimeGlobalLayoutListener {
            StatisticTracker.sendPageTabImpressionEvent(userSession.userId, title)
        }
    }

    private fun handleAppLink(intent: Intent?) {
        StatisticAppLinkHandler.handleAppLink(intent) { page, widget ->
            selectedPageSource = page
            selectedWidget = widget
            selectTabByPageSource()
        }
    }

    private fun selectTabByPageSource() = binding?.run {
        val tabIndex = pages.indexOfFirst { it.pageSource == selectedPageSource }
        val tab = tabStatistic.tabLayout.getTabAt(tabIndex)
        tab?.let {
            it.select()
            showSelectedWidgetByDataKey(tabIndex)
        }
    }

    private fun showSelectedWidgetByDataKey(tabIndex: Int) {
        val fragment = viewPagerAdapter?.fragments?.getOrNull(tabIndex) as? StatisticFragment
        fragment?.setSelectedWidget(selectedWidget)
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    private fun checkWhiteListStatus() {
        viewModel.checkWhiteListStatus()
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoring.initPerformanceMonitoring()
    }

    private fun getIsProductInsightTab(title: String): Boolean {
        return title == getString(R.string.stc_product) ||
                title == getString(R.string.stc_product_coachmark_title)
    }

    private fun getIsOperationalInsightTab(title: String): Boolean {
        return title == getString(R.string.stc_operational) ||
                title == getString(R.string.stc_operational_coachmark_title)
    }

    private fun getProductInsightCoachMark(title: String, itemView: View): CoachMark2Item? {
        if (getIsProductInsightTab(title)) {
            if (!CoachMarkPreference.hasShown(this, Const.SHOW_PRODUCT_INSIGHT_COACH_MARK_KEY)) {
                return CoachMark2Item(
                    itemView,
                    getString(R.string.stc_product_coachmark_title),
                    getString(R.string.stc_product_coachmark_desc)
                )
            }
        }
        return null
    }

    private fun getOperationalInsightCoachMark(title: String, view: View): CoachMark2Item? {
        if (getIsOperationalInsightTab(title)) {
            if (!CoachMarkPreference.hasShown(
                    this,
                    Const.HAS_SHOWN_OPERATIONAL_INSIGHT_COACH_MARK_KEY
                )
            ) {
                return CoachMark2Item(
                    view,
                    getString(R.string.stc_operational_coachmark_title),
                    getString(R.string.stc_operational_coachmark_desc)
                )
            }
        }
        return null
    }

    private fun dismissCoachMarkOnTabSelected() {
        if (coachMark.isDismissed) return
        coachMark.dismissCoachMark()
    }

    private fun saveCoachMarkHasShownByTitle(title: String) {
        when {
            getIsProductInsightTab(title) -> {
                setCoachMarkHasShown(Const.SHOW_PRODUCT_INSIGHT_COACH_MARK_KEY)
            }
            getIsOperationalInsightTab(title) -> {
                setCoachMarkHasShown(Const.HAS_SHOWN_OPERATIONAL_INSIGHT_COACH_MARK_KEY)
            }
        }
    }

    private fun setCoachMarkHasShown(tag: String) {
        CoachMarkPreference.setShown(this, tag, true)
    }

    private fun checkUserRole(roles: List<String>) {
        if (!roles.contains(MANAGE_SHOP_STATS_ROLE)) {
            showToaster()
        }
    }

    private fun showToaster() {
        val toastCountDown = object : CountDownTimer(TOAST_DURATION, TOAST_COUNT_DOWN_INTERVAL) {
            override fun onTick(p0: Long) {
                Toast.makeText(
                    this@StatisticActivity,
                    getString(R.string.stc_you_havent_access_this_page),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFinish() {
                finish()
            }
        }
        toastCountDown.start()
    }
}
