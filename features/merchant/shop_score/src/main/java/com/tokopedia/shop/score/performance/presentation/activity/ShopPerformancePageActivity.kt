package com.tokopedia.shop.score.performance.presentation.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.shop.score.common.plt.ShopScorePerformanceMonitoringListener
import com.tokopedia.shop.score.common.plt.ShopScorePltConstants
import com.tokopedia.shop.score.performance.di.component.DaggerShopPerformanceComponent
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.presentation.fragment.ShopPerformancePageFragment

class ShopPerformancePageActivity : BaseSimpleActivity(), HasComponent<ShopPerformanceComponent>,
    ShopScorePerformanceMonitoringListener {

    private var pageLoadTimePerformance: PageLoadTimePerformanceInterface? = null

    override fun getNewFragment(): Fragment = ShopPerformancePageFragment.newInstance()

    override fun getComponent(): ShopPerformanceComponent {
        return DaggerShopPerformanceComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformance = PageLoadTimePerformanceCallback(
            ShopScorePltConstants.SHOP_PERFORMANCE_PLT_PREPARE_METRICS,
            ShopScorePltConstants.SHOP_PERFORMANCE_PLT_NETWORK_METRICS,
            ShopScorePltConstants.SHOP_PERFORMANCE_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )
        pageLoadTimePerformance?.startMonitoring(ShopScorePltConstants.SHOP_PERFORMANCE_TRACE)
        pageLoadTimePerformance?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformance?.stopMonitoring()
        pageLoadTimePerformance = null
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformance?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformance?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformance?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformance?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformance?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformance?.stopRenderPerformanceMonitoring()
    }
}