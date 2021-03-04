package com.tokopedia.review.feature.historydetails.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.historydetails.presentation.fragment.ReviewDetailFragment

class ReviewDetailActivity : BaseSimpleActivity(), ReviewPerformanceMonitoringListener {

    private var reputationId: String = ""
    private var feedbackId: Long = 0
    private var reviewDetailFragment: ReviewDetailFragment? = null
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromApplink()
        reviewDetailFragment = ReviewDetailFragment.createNewInstance(if(feedbackId != 0L) feedbackId else reputationId.toLongOrZero())
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
        supportActionBar?.hide()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ReviewConstants.REVIEW_DETAIL_PLT_PREPARE_METRICS,
                ReviewConstants.REVIEW_DETAIL_PLT_NETWORK_METRICS,
                ReviewConstants.REVIEW_DETAIL_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(ReviewConstants.REVIEW_DETAIL_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopMonitoring()
        }
        pageLoadTimePerformanceMonitoring = null
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.startPreparePagePerformanceMonitoring()
        }
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopPreparePagePerformanceMonitoring()
        }
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.startNetworkRequestPerformanceMonitoring()
        }
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopNetworkRequestPerformanceMonitoring()
        }
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.startRenderPerformanceMonitoring()
        }
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopRenderPerformanceMonitoring()
        }
    }

    override fun getNewFragment(): Fragment? {
        return reviewDetailFragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        reviewDetailFragment?.onBackPressed()
    }

    private fun getDataFromApplink() {
        val uri = intent.data ?: return
        reputationId = uri.lastPathSegment ?: ""
        feedbackId = uri.getQueryParameter(ReviewConstants.PARAM_FEEDBACK_ID).toLongOrZero()
    }
}