package com.tokopedia.homenav.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.fragment.MainNavFragmentArgs
import com.tokopedia.searchbar.navigation_component.NavToolbar
import kotlinx.android.synthetic.main.activity_main_nav.*

class HomeNavActivity: AppCompatActivity(), HomeNavPerformanceInterface {

    private var pageSource: String = ""

    private val navPerformanceMonitoring = PerformanceMonitoring()
    private val navPerformanceCallback = PageLoadTimePerformanceCallback(
            NAV_PAGE_PERFORMANCE_MONITORING_PREPARE_METRICS,
            NAV_PAGE_PERFORMANCE_MONITORING_NETWORK_METRICS,
            NAV_PAGE_PERFORMANCE_MONITORING_RENDER_METRICS,
            0,
            0,
            0,
            0,
            navPerformanceMonitoring
    )

    companion object {
        private const val NAV_PAGE_PERFORMANCE_MONITORING_KEY = "mp_home"
        private const val NAV_PAGE_PERFORMANCE_MONITORING_PREPARE_METRICS = "home_plt_start_page_metrics"
        private const val NAV_PAGE_PERFORMANCE_MONITORING_NETWORK_METRICS = "home_plt_network_request_page_metrics"
        private const val NAV_PAGE_PERFORMANCE_MONITORING_RENDER_METRICS = "home_plt_render_page_metrics"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //PLT monitoring started
        navPerformanceCallback.startMonitoring(NAV_PAGE_PERFORMANCE_MONITORING_KEY)
        navPerformanceCallback.startPreparePagePerformanceMonitoring()

        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_top, R.anim.nav_fade_out)
        setContentView(R.layout.activity_main_nav)
        pageSource = intent.getStringExtra(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE)?:""
        findViewById<NavToolbar>(R.id.toolbar)?.let {
            it.setToolbarTitle(getString(R.string.title_main_nav))
            it.setupToolbarWithStatusBar(this, NavToolbar.Companion.StatusBar.STATUS_BAR_LIGHT, true)
        }
        setupNavigation()

        //PLT prepare finished
        navPerformanceCallback.stopPreparePagePerformanceMonitoring()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nav_fade_in, R.anim.slide_bottom)
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragment_container)
        toolbar.setOnBackButtonClickListener {
            navController.navigateUp()
        }
        navController.setGraph(R.navigation.nav_graph,
                MainNavFragmentArgs(StringMainNavArgsSourceKey = pageSource).toBundle())
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragment_container).navigateUp()
                || super.onSupportNavigateUp();
    }

    override fun getNavPerformanceInterface(): PageLoadTimePerformanceInterface {
        return navPerformanceCallback
    }
}