package com.tokopedia.statistic.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.statistic.R
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
import com.tokopedia.statistic.view.viewmodel.StatisticActivityViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_stc_statistic.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

// Internal applink : ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD

class StatisticActivity : BaseActivity(), HasComponent<StatisticComponent>,
        FragmentListener, StatisticPerformanceMonitoringListener {

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
        return if (isWhiteListed) {
            listOf(StatisticPageHelper.getShopStatistic(this), StatisticPageHelper.getBuyerStatistic(this))
        } else {
            listOf(StatisticPageHelper.getShopStatistic(this))
        }
    }

    private fun setupView() {
        setSupportActionBar(headerStcStatistic)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.stc_shop_statistic)
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
            adapter.titles.forEach { title ->
                tabStatistic.addNewTab(title)
            }
        }
    }

    private fun handleAppLink(intent: Intent?) {
        StatisticAppLinkHandler.handleAppLink(intent) { page ->
            selectedPageSource = page
            selectTabByPageSource()
        }
    }

    private fun selectTabByPageSource() {
        val tabIndex = pages.indexOfFirst { it.pageSource == selectedPageSource }
        val tab = tabStatistic.tabLayout.getTabAt(tabIndex)
        tab?.select()
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