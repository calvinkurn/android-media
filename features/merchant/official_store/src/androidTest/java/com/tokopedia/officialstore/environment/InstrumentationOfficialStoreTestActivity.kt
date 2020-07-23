package com.tokopedia.officialstore.environment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.analytics.performance.util.*
import com.tokopedia.navigation_common.listener.OfficialStorePerformanceMonitoringListener
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment

class InstrumentationOfficialStoreTestActivity : AppCompatActivity(),
        EspressoPerformanceActivity,
        OfficialStorePerformanceMonitoringListener {

    private var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startOfficialStorePerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumentation_official_store_home_test)

        val officialHomeFragment: Fragment = OfficialHomeFragment()
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

    override fun stopOfficialStorePerformanceMonitoring() {
        pageLoadTimePerformanceInterface?.stopRenderPerformanceMonitoring()
        pageLoadTimePerformanceInterface?.stopMonitoring()
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
