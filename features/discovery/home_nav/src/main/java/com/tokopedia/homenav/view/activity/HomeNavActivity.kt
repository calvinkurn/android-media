package com.tokopedia.homenav.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.ActivityMainNavBinding
import com.tokopedia.homenav.view.fragment.HomeNavFragment

@Suppress("LateinitUsage")
class HomeNavActivity : AppCompatActivity(), HomeNavPerformanceInterface {

    private lateinit var binding: ActivityMainNavBinding

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
        private const val TAG_FRAGMENT = "home_nav_fragment"

        private const val NAV_PAGE_PERFORMANCE_MONITORING_KEY = "mp_nav"
        private const val NAV_PAGE_PERFORMANCE_MONITORING_PREPARE_METRICS = "nav_page_plt_start_page_metrics"
        private const val NAV_PAGE_PERFORMANCE_MONITORING_NETWORK_METRICS = "nav_page_plt_network_request_page_metrics"
        private const val NAV_PAGE_PERFORMANCE_MONITORING_RENDER_METRICS = "nav_page_plt_render_page_metrics"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // PLT monitoring started
        navPerformanceCallback.startMonitoring(NAV_PAGE_PERFORMANCE_MONITORING_KEY)
        navPerformanceCallback.startPreparePagePerformanceMonitoring()

        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_top, R.anim.nav_fade_out)
        binding = ActivityMainNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportFragmentManager.findFragmentByTag(TAG_FRAGMENT) == null) {
            supportFragmentManager.commit {
                replace(binding.root.id, HomeNavFragment::class.java, intent.extras ?: Bundle(), TAG_FRAGMENT)
            }
        }

        // PLT prepare finished
        navPerformanceCallback.stopPreparePagePerformanceMonitoring()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nav_fade_in, R.anim.slide_bottom)
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT) as? HomeNavFragment ?: return super.onSupportNavigateUp()
        return fragment.onSupportNavigateUp(this) || super.onSupportNavigateUp()
    }

    override fun getNavPerformanceInterface(): PageLoadTimePerformanceInterface {
        return navPerformanceCallback
    }
}
