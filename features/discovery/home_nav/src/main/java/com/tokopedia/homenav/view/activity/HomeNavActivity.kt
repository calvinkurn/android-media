package com.tokopedia.homenav.view.activity

import android.content.res.TypedArray
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.ActivityMainNavBinding
import com.tokopedia.homenav.mainnav.view.fragment.MainNavFragmentArgs
import com.tokopedia.homenav.view.fragment.HomeNavFragment
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.utils.resources.isDarkMode

class HomeNavActivity: AppCompatActivity(), HomeNavPerformanceInterface {

    private lateinit var binding: ActivityMainNavBinding

    private var pageSource: String = ""
    private var pageSourcePath: String = ""
//    private var toolbar: NavToolbar? = null
//    private var fragmentContainer: View? = null
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
        //PLT monitoring started
        navPerformanceCallback.startMonitoring(NAV_PAGE_PERFORMANCE_MONITORING_KEY)
        navPerformanceCallback.startPreparePagePerformanceMonitoring()

        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_top, R.anim.nav_fade_out)
        binding = ActivityMainNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        pageSource = intent.getStringExtra(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE)?:""
//        pageSourcePath = intent.getStringExtra(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE_PATH)?:""
//        findViewById<NavToolbar>(R.id.toolbar)?.let {
//            it.setToolbarTitle(getString(R.string.title_main_nav))
//            it.setupToolbarWithStatusBar(
//                this,
//                if (applicationContext.isDarkMode()) NavToolbar.Companion.StatusBar.STATUS_BAR_DARK else NavToolbar.Companion.StatusBar.STATUS_BAR_LIGHT,
//                true
//            )
//            it.setShowShadowEnabled(true)
//        }
//        setupNavigation()
//        setupView()

        if (supportFragmentManager.findFragmentByTag(TAG_FRAGMENT) == null) {
            supportFragmentManager.commit {
                replace(binding.root.id, HomeNavFragment::class.java, intent.extras ?: Bundle(), TAG_FRAGMENT)
            }
        }

        //PLT prepare finished
        navPerformanceCallback.stopPreparePagePerformanceMonitoring()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nav_fade_in, R.anim.slide_bottom)
    }

    private fun setupNavigation() {
//        toolbar = findViewById(R.id.toolbar)
//        val navController = findNavController(R.id.fragment_container)
//        toolbar?.setOnBackButtonClickListener {
//            navController.navigateUp()
//        }
//        navController.setGraph(R.navigation.nav_graph,
//                MainNavFragmentArgs(StringMainNavArgsSourceKey = pageSource, StringMainNavArgsSourcePathKey = pageSourcePath).toBundle())
    }

    private fun setupView() {
//        fragmentContainer = findViewById(R.id.fragment_container)
//        try {
//            val styledAttributes: TypedArray = getTheme().obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
//            val mActionBarSize = styledAttributes.getDimension(0, 0f).toInt()
//            styledAttributes.recycle()
//
//            val layoutParams = fragmentContainer?.layoutParams as FrameLayout.LayoutParams
//            layoutParams.setMargins(
//                    layoutParams.leftMargin,
//                    16f.toDpInt() + mActionBarSize,
//                    layoutParams.rightMargin,
//                    layoutParams.bottomMargin
//            )
//        } catch (e: Exception) {
//            val layoutParams = fragmentContainer?.layoutParams as FrameLayout.LayoutParams
//            layoutParams.setMargins(
//                    layoutParams.leftMargin,
//                    200f.toDpInt(),
//                    layoutParams.rightMargin,
//                    layoutParams.bottomMargin
//            )
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT) as? HomeNavFragment ?: return super.onSupportNavigateUp()
        return fragment.onSupportNavigateUp(this) || super.onSupportNavigateUp()

    }

    override fun getNavPerformanceInterface(): PageLoadTimePerformanceInterface {
        return navPerformanceCallback
    }
}
