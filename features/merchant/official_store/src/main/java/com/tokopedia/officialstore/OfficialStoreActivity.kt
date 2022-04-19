package com.tokopedia.officialstore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.navigation_common.listener.OfficialStorePerformanceMonitoringListener
import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment

/**
 * Created by Lukas on 27/10/20.
 */
class OfficialStoreActivity : AppCompatActivity(), OfficialStorePerformanceMonitoringListener {
    private var officialStorePageLoadTimePerformanceCallback : PageLoadTimePerformanceCallback? = null

    companion object {
        private const val OFFICIAL_STORE_PERFORMANCE_MONITORING_KEY = "mp_official_store_activity"
        private const val OFFICIAL_STORE_PERFORMANCE_MONITORING_PREPARE_METRICS =
            "official_store_activity_plt_start_page_metrics"
        private const val OFFICIAL_STORE_PERFORMANCE_MONITORING_NETWORK_METRICS =
            "official_store_activity_plt_network_request_page_metrics"
        private const val OFFICIAL_STORE_PERFORMANCE_MONITORING_RENDER_METRICS =
            "official_store_activity_plt_render_page_metrics"
        private const val PERFORMANCE_MONITORING_CACHE_ATTRIBUTION = "dataSource"
        private const val PERFORMANCE_MONITORING_CACHE_VALUE = "Cache"
        private const val PERFORMANCE_MONITORING_NETWORK_VALUE = "Network"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startOfficialStorePerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_official_store)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, OfficialHomeContainerFragment.newInstance(null), "wishlist")
                .commit()
    }

    override fun startOfficialStorePerformanceMonitoring() {
        if (officialStorePageLoadTimePerformanceCallback == null) {
            officialStorePageLoadTimePerformanceCallback = PageLoadTimePerformanceCallback(
                OFFICIAL_STORE_PERFORMANCE_MONITORING_PREPARE_METRICS,
                OFFICIAL_STORE_PERFORMANCE_MONITORING_NETWORK_METRICS,
                OFFICIAL_STORE_PERFORMANCE_MONITORING_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
            )
            officialStorePageLoadTimePerformanceCallback?.startMonitoring(OFFICIAL_STORE_PERFORMANCE_MONITORING_KEY)
            officialStorePageLoadTimePerformanceCallback?.startPreparePagePerformanceMonitoring()
        }
    }

    override fun stopOfficialStorePerformanceMonitoring(isCache: Boolean) {
        if (officialStorePageLoadTimePerformanceCallback != null) {
            if (isCache) {
                officialStorePageLoadTimePerformanceCallback?.addAttribution(
                    PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
                    PERFORMANCE_MONITORING_CACHE_VALUE
                )
            } else {
                officialStorePageLoadTimePerformanceCallback?.addAttribution(
                    PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
                    PERFORMANCE_MONITORING_NETWORK_VALUE
                )
            }
            officialStorePageLoadTimePerformanceCallback?.stopRenderPerformanceMonitoring()
            officialStorePageLoadTimePerformanceCallback?.stopMonitoring()
            officialStorePageLoadTimePerformanceCallback = null
        }
    }

    override fun getOfficialStorePageLoadTimePerformanceInterface(): PageLoadTimePerformanceInterface? {
        return officialStorePageLoadTimePerformanceCallback
    }
}