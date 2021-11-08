package com.tokopedia.shop.score.penalty.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.shop.score.common.plt.ShopPenaltyPerformanceMonitoringListener
import com.tokopedia.shop.score.common.plt.ShopPenaltyPltConstants
import com.tokopedia.shop.score.common.plt.ShopScorePltConstants
import com.tokopedia.shop.score.penalty.di.component.DaggerPenaltyComponent
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment

class ShopPenaltyPageActivity : BaseSimpleActivity(), HasComponent<PenaltyComponent>,
    ShopPenaltyPerformanceMonitoringListener {

    private var pageLoadTimePerformance: PageLoadTimePerformanceInterface? = null


    override fun getNewFragment(): Fragment = ShopPenaltyPageFragment.newInstance()

    override fun getComponent(): PenaltyComponent {
        return DaggerPenaltyComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformance = PageLoadTimePerformanceCallback(
            ShopPenaltyPltConstants.SHOP_PENALTY_PLT_PREPARE_METRICS,
            ShopPenaltyPltConstants.SHOP_PENALTY_PLT_NETWORK_METRICS,
            ShopPenaltyPltConstants.SHOP_PENALTY_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )
        pageLoadTimePerformance?.startMonitoring(ShopPenaltyPltConstants.SHOP_PENALTY_TRACE)
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