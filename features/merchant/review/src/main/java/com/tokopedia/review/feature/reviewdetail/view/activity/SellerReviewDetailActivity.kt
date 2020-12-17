package com.tokopedia.review.feature.reviewdetail.view.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewSellerPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.reviewdetail.di.component.DaggerReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.review.feature.reviewdetail.view.fragment.SellerReviewDetailFragment

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewDetailActivity : BaseSimpleActivity(), HasComponent<ReviewProductDetailComponent>, ReviewSellerPerformanceMonitoringListener {

    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfMainApp()
        startPerformanceMonitoring()
    }

    override fun getNewFragment(): Fragment = SellerReviewDetailFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_review_detail
    }

    override fun getParentViewResourceID(): Int {
        return R.id.seller_review_detail_parent_view
    }

    override fun getComponent(): ReviewProductDetailComponent {
        return DaggerReviewProductDetailComponent
                .builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .reviewProductDetailModule(ReviewProductDetailModule())
                .build()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ReviewConstants.SELLER_REVIEW_DETAIL_PLT_PREPARE_METRICS,
                ReviewConstants.SELLER_REVIEW_DETAIL_PLT_NETWORK_METRICS,
                ReviewConstants.SELLER_REVIEW_DETAIL_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(ReviewConstants.SELLER_REVIEW_DETAIL_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun checkIfMainApp() {
        if (!GlobalConfig.isSellerApp()) {
            RouteManager.route(this, Uri.parse(ApplinkConst.REPUTATION).buildUpon().appendQueryParameter(ReviewInboxConstants.PARAM_TAB, ReviewInboxConstants.SELLER_TAB).build().toString())
        }
    }

}