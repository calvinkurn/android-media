package com.tokopedia.officialstore.environment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.analytics.performance.util.*
import com.tokopedia.navigation_common.listener.OfficialStorePerformanceMonitoringListener
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment

class InstrumentationOfficialStoreTestFullActivity : AppCompatActivity(),
        EspressoPerformanceActivity,
        OfficialStorePerformanceMonitoringListener {

    private val PERFORMANCE_MONITORING_CACHE_ATTRIBUTION = "dataSource"
    private val PERFORMANCE_MONITORING_CACHE_VALUE = "Cache"
    private val PERFORMANCE_MONITORING_NETWORK_VALUE = "Network"

    private var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startOfficialStorePerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumentation_official_store_home_test)

        val officialHomeFragment: Fragment = OfficialHomeContainerFragment()
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container, officialHomeFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun getPltPerformanceResultData(): PltPerformanceData? {
        return pageLoadTimePerformanceInterface?.getPltPerformanceData()
    }

    override fun stopOfficialStorePerformanceMonitoring(isCache: Boolean) {
        if (pageLoadTimePerformanceInterface != null) {
            if (isCache) {
                pageLoadTimePerformanceInterface?.addAttribution(
                    PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
                    PERFORMANCE_MONITORING_CACHE_VALUE
                )
            } else {
                pageLoadTimePerformanceInterface?.addAttribution(
                    PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
                    PERFORMANCE_MONITORING_NETWORK_VALUE
                )
            }
            pageLoadTimePerformanceInterface?.stopRenderPerformanceMonitoring()
            pageLoadTimePerformanceInterface?.stopMonitoring()
            pageLoadTimePerformanceInterface = null
        }
    }

    override fun getOfficialStorePageLoadTimePerformanceInterface(): PageLoadTimePerformanceInterface? {
        return pageLoadTimePerformanceInterface
    }

    override fun startOfficialStorePerformanceMonitoring() {
        pageLoadTimePerformanceInterface = PageLoadTimePerformanceCallback(
                tagPrepareDuration = "prepare",
                tagNetworkRequestDuration = "network",
                tagRenderDuration = "render"
        )
        pageLoadTimePerformanceInterface?.startMonitoring("start monitoring")
        pageLoadTimePerformanceInterface?.startPreparePagePerformanceMonitoring()
    }

}
