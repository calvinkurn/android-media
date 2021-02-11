package com.tokopedia.statistic.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.performance.StatisticIdlingResourceListener
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoring
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringInterface
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringListener
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.common.utils.StatisticAppLinkHandler
import com.tokopedia.statistic.view.fragment.StatisticFragment
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import com.tokopedia.statistic.view.viewhelper.FragmentListener
import com.tokopedia.statistic.view.viewhelper.StatisticViewPagerAdapter
import kotlinx.android.synthetic.main.activity_stc_statistic.*

/**
 * Created By @ilhamsuaib on 08/06/20
 */

// Internal applink : ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD

class StatisticActivity : BaseActivity(), FragmentListener, StatisticPerformanceMonitoringListener {

    private var pages: List<StatisticPageUiModel> = emptyList()
    private var viewPagerAdapter: StatisticViewPagerAdapter? = null

    val performanceMonitoring: StatisticPerformanceMonitoringInterface by lazy {
        StatisticPerformanceMonitoring()
    }
    var pltListener: StatisticIdlingResourceListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stc_statistic)

        initVar()
        setupView()
        setupViewPager()
        handleAppLink(intent)

        setWhiteStatusBar()
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

    private fun initVar() {
        pages = getStatisticPages()
        viewPagerAdapter = StatisticViewPagerAdapter(supportFragmentManager)
        pages.forEach { page ->
            viewPagerAdapter?.addFragment(StatisticFragment.newInstance(page), page.pageTitle)
        }
    }

    private fun getStatisticPages(): List<StatisticPageUiModel> {
        return listOf(Const.StatisticPage.getShopStatistic(this), Const.StatisticPage.getBuyerStatistic(this))
    }

    private fun setupView() {
        setSupportActionBar(headerStcStatistic)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.stc_shop_statistic)
    }

    private fun setupViewPager() {
        viewPagerAdapter?.let {
            setupTabs()
            viewPagerStatistic.adapter = it
            tabStatistic.setupWithViewPager(viewPagerStatistic)
            viewPagerStatistic.offscreenPageLimit = it.titles.size
        }
    }

    private fun setupTabs() {
        viewPagerAdapter?.let { adapter ->
            adapter.titles.forEach { title ->
                tabStatistic.addNewTab(title)
            }
        }
    }

    private fun handleAppLink(intent: Intent?) {
        StatisticAppLinkHandler.handleAppLink(intent) { page ->
            val tabIndex = pages.indexOfFirst { it.pageSource == page }
            val tab = tabStatistic.tabLayout.getTabAt(tabIndex)
            tab?.select()
        }
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoring.initPerformanceMonitoring()
    }
}