package com.tokopedia.statistic.view.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.analytics.performance.StatisticIdlingResourceListener
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoring
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringInterface
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringListener
import com.tokopedia.statistic.common.StatisticPageHelper
import com.tokopedia.statistic.common.utils.StatisticAppLinkHandler
import com.tokopedia.statistic.di.DaggerStatisticComponent
import com.tokopedia.statistic.di.StatisticComponent
import com.tokopedia.statistic.view.fragment.StatisticFragment
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import com.tokopedia.statistic.view.viewhelper.FragmentListener
import com.tokopedia.statistic.view.viewhelper.StatisticViewPagerAdapter
import com.tokopedia.statistic.view.viewhelper.setOnTabSelectedListener
import com.tokopedia.statistic.view.viewmodel.StatisticActivityViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_stc_statistic.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

// Internal applink : ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD

class StatisticActivity : BaseActivity(), HasComponent<StatisticComponent>,
        FragmentListener, StatisticPerformanceMonitoringListener {

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: StatisticActivityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(StatisticActivityViewModel::class.java)
    }
    private var pages: List<StatisticPageUiModel> = emptyList()
    private var viewPagerAdapter: StatisticViewPagerAdapter? = null
    val performanceMonitoring: StatisticPerformanceMonitoringInterface by lazy {
        StatisticPerformanceMonitoring()
    }
    var pltListener: StatisticIdlingResourceListener? = null

    private var selectedPageSource = ""
    private var selectedWidget = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        initInjector()

        checkWhiteListStatus()
        setContentView(R.layout.activity_stc_statistic)

        setupView()
        initVar()
        handleAppLink(intent)
        setWhiteStatusBar()

        observeWhiteListStatus()
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
        headerStcStatistic.headerSubTitle = subTitle
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initVar() {
        viewPagerAdapter = StatisticViewPagerAdapter(supportFragmentManager)
    }

    private fun getStatisticPages(isWhiteListed: Boolean): List<StatisticPageUiModel> {
        adjustHeaderConfig(isWhiteListed)
        return if (isWhiteListed) {
            supportActionBar?.title = getString(R.string.stc_statistic)
            tabStatistic.visible()
            listOf(StatisticPageHelper.getShopStatistic(this, userSession),
                    StatisticPageHelper.getProductStatistic(this, userSession),
                    StatisticPageHelper.getBuyerStatistic(this, userSession))
        } else {
            supportActionBar?.title = getString(R.string.stc_shop_statistic)
            tabStatistic.gone()
            listOf(StatisticPageHelper.getShopStatistic(this, userSession))
        }
    }

    private fun adjustHeaderConfig(isWhiteListed: Boolean) {
        val lParams = headerStcStatistic.layoutParams as? LinearLayout.LayoutParams
        if (isWhiteListed) {
            supportActionBar?.title = getString(R.string.stc_statistic)
            tabStatistic.visible()

            val marginBottom = resources.getDimension(R.dimen.dimen_stc_minus2dp)
            lParams?.setMargins(0, 0, 0, marginBottom.toInt())
        } else {
            supportActionBar?.title = getString(R.string.stc_shop_statistic)
            tabStatistic.gone()

            lParams?.setMargins(0, 0, 0, 0)
        }

        headerStcStatistic.requestLayout()
    }

    private fun setupView() {
        setSupportActionBar(headerStcStatistic)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tabStatistic.tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        tabStatistic.tabLayout.setOnTabSelectedListener {
            val tabIndex = tabStatistic.tabLayout.selectedTabPosition
            val title = viewPagerAdapter?.titles?.getOrNull(tabIndex).orEmpty()
            StatisticTracker.sendPageTabClickEvent(userSession.userId, title)
        }
    }

    private fun setupViewPager(isWhiteListed: Boolean) {
        pages = getStatisticPages(isWhiteListed)
        pages.forEach { page ->
            viewPagerAdapter?.addFragment(StatisticFragment.newInstance(page), page.pageTitle)
        }

        viewPagerAdapter?.let {
            setupTabs()
            viewPagerStatistic.adapter = it
            tabStatistic.setupWithViewPager(viewPagerStatistic)
            viewPagerStatistic.offscreenPageLimit = it.titles.size
        }

        selectTabByPageSource()
    }

    private fun observeWhiteListStatus() {
        progressBarStcActivity.visible()
        viewModel.whitelistedStatus.observe(this) {
            progressBarStcActivity.gone()
            when (it) {
                is Success -> setupViewPager(it.data)
                else -> setupViewPager(false)
            }
        }
    }

    private fun setupTabs() {
        viewPagerAdapter?.let { adapter ->
            if (adapter.titles.isNotEmpty()) {
                tabStatistic.tabLayout.removeAllTabs()
            }
            adapter.titles.forEach { title ->
                val tab = tabStatistic.addNewTab(title)
                sendTabImpressionEvent(tab.view, title)
            }
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

    private fun selectTabByPageSource() {
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
}