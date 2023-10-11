package com.tokopedia.statistic.view.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.analytics.performance.StatisticIdlingResourceListener
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoring
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringInterface
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringListener
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.common.StatisticPageHelper
import com.tokopedia.statistic.common.utils.StatisticAppLinkHandler
import com.tokopedia.statistic.common.utils.logger.StatisticLogger
import com.tokopedia.statistic.databinding.ActivityStcStatisticBinding
import com.tokopedia.statistic.di.DaggerStatisticComponent
import com.tokopedia.statistic.di.StatisticComponent
import com.tokopedia.statistic.view.fragment.StatisticFragment
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import com.tokopedia.statistic.view.viewhelper.FragmentListener
import com.tokopedia.statistic.view.viewhelper.StatisticCoachMarkHelper
import com.tokopedia.statistic.view.viewhelper.StatisticViewPagerAdapter
import com.tokopedia.statistic.view.viewhelper.setOnTabSelectedListener
import com.tokopedia.statistic.view.viewmodel.StatisticActivityViewModel
import com.tokopedia.unifycomponents.setNew
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.resources.isDarkMode
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
        private const val TOAST_DURATION = 1000L
        private const val TOAST_COUNT_DOWN_INTERVAL = 500L
        private const val MANAGE_SHOP_STATS_ROLE = "MANAGE_SHOPSTATS"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var pageHelper: StatisticPageHelper

    @Inject
    lateinit var coachMarkHelper: StatisticCoachMarkHelper

    private val viewModel: StatisticActivityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(StatisticActivityViewModel::class.java)
    }
    private var pages: List<StatisticPageUiModel> = emptyList()
    private var viewPagerAdapter: StatisticViewPagerAdapter? = null
    val performanceMonitoring: StatisticPerformanceMonitoringInterface by lazy {
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

        if (savedInstanceState == null) {
            checkWhiteListStatus()
        }

        binding = ActivityStcStatisticBinding.inflate(layoutInflater).apply {
            root.setBackgroundColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_Background))
            setContentView(root)
        }

        setupView()
        initVar()
        handleAppLink(intent)
        setWhiteStatusBar()

        observeWhiteListStatus()
        observeUserRole()
        fetchPMStatus()
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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
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
        return listOf(
            pageHelper.getShopStatistic(this),
            pageHelper.getProductStatistic(this),
            pageHelper.getTrafficStatistic(this),
            pageHelper.getOperationalStatistic(this),
            pageHelper.getBuyerStatistic(this)
        )
    }

    private fun getNonWhiteListedPages(): List<StatisticPageUiModel> {
        return listOf(
            pageHelper.getShopStatistic(this),
            pageHelper.getProductStatistic(this),
            pageHelper.getTrafficStatistic(this),
            pageHelper.getOperationalStatistic(this),
            pageHelper.getBuyerStatistic(this)
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
        viewPagerAdapter?.clear()
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
            it.notifyDataSetChanged()
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
        viewModel.userRole.observe(this) {
            when (it) {
                is Success -> checkUserRole(it.data)
                is Fail -> StatisticLogger.logToCrashlytics(
                    it.throwable,
                    StatisticLogger.ERROR_SELLER_ROLE
                )
            }
        }
        viewModel.getUserRole()
    }

    private fun fetchPMStatus() {
        viewModel.fetchPMStatus()
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

                val page = pages.firstOrNull { it.pageTitle == title }
                page?.let {
                    tabStatistic.getUnifyTabLayout().getTabAt(index)?.setNew(it.shouldShowTag)
                }

                coachMarkHelper.getTrafficInsightCoachMark(title, tab.view)?.let {
                    coachMarkItems.add(it)
                }
                coachMarkHelper.getOperationalInsightCoachMark(title, tab.view)?.let {
                    coachMarkItems.add(it)
                }
                coachMarkHelper.getProductInsightCoachMark(title, tab.view)?.let {
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
                    coachMarkHelper.saveCoachMarkHasShownByTitle(this@StatisticActivity, coachMarkItem.title.toString())
                    sendCoachMarkImpressionTracker(coachMarkItem.title.toString())
                    sendCoachMarkClickTracker(currentIndex)
                }
            })
            val title = coachMarkItems.firstOrNull()?.title?.toString().orEmpty()
            coachMarkHelper.saveCoachMarkHasShownByTitle(this, title)
        }
    }

    private fun sendCoachMarkClickTracker(currentIndex: Int) {
        val item = coachMark.coachMarkItem.getOrNull(currentIndex.minus(Int.ONE))
        item?.let {
            val title = it.title.toString()
            if (coachMarkHelper.getIsTrafficInsightTab(this, title)) {
                StatisticTracker.sendTrafficInsightCoachMarkCtaClickEvent(
                    Const.PageSource.TRAFFIC_INSIGHT,
                    title
                )
            }
        }
    }

    private fun sendCoachMarkImpressionTracker(title: String) {
        if (coachMarkHelper.getIsTrafficInsightTab(this, title)) {
            StatisticTracker.sendTrafficInsightImpressionCoachMarkEvent(
                Const.PageSource.TRAFFIC_INSIGHT,
                title
            )
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
            setStatusBarColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_Background))
            setLightStatusBar(!isDarkMode())
        }
    }

    private fun checkWhiteListStatus() {
        viewModel.checkWhiteListStatus()
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoring.initPerformanceMonitoring()
    }

    private fun dismissCoachMarkOnTabSelected() {
        if (coachMark.isDismissed) return
        coachMark.dismissCoachMark()
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
