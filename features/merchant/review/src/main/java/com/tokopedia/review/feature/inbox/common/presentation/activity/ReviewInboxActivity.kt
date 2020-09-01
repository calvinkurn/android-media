package com.tokopedia.review.feature.inbox.common.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.container.presentation.fragment.ReviewInboxContainerFragment
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity.CreateReviewActivityOld

class ReviewInboxActivity : BaseSimpleActivity(), ReviewPerformanceMonitoringListener {

    companion object {
        const val GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY"
        fun createNewInstance(context: Context): Intent {
            return Intent(context, ReviewInboxActivity::class.java)
        }
    }

    private var reviewInboxContainerFragment: ReviewInboxContainerFragment? = null
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val goToReputationHistory = getArgumentsFromApplink() ?: false
        reviewInboxContainerFragment = ReviewInboxContainerFragment.createNewInstance(goToReputationHistory)
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return reviewInboxContainerFragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        reviewInboxContainerFragment?.onBackPressed()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ReviewConstants.REVIEW_PENDING_PLT_PREPARE_METRICS,
                ReviewConstants.REVIEW_PENDING_PLT_NETWORK_METRICS,
                ReviewConstants.REVIEW_PENDING_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(ReviewConstants.REVIEW_PENDING_TRACE)
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

    private fun setUpToolBar() {
        supportActionBar?.elevation = ReviewInboxConstants.NO_SHADOW_ELEVATION
    }

    private fun getArgumentsFromApplink(): Boolean? {
        return intent?.getBooleanExtra(GO_TO_REPUTATION_HISTORY, false)
    }

}