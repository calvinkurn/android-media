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

class ReviewInboxActivity : BaseSimpleActivity(), ReviewPerformanceMonitoringListener {

    companion object {
        fun createNewInstance(context: Context, tab: String?, source: String?): Intent {
            val intent = Intent(context, ReviewInboxActivity::class.java)
            intent.putExtra(ReviewInboxConstants.PARAM_TAB, tab ?: "")
            intent.putExtra(ReviewInboxConstants.PARAM_SOURCE, source ?: ReviewInboxConstants.DEFAULT_SOURCE)
            return intent
        }
    }

    private var reviewInboxContainerFragment: ReviewInboxContainerFragment? = null
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var tab: String = ""
    private var source: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        getArgumentsFromApplink()
        reviewInboxContainerFragment = ReviewInboxContainerFragment.createNewInstance(tab, source)
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
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

    private fun getArgumentsFromApplink() {
        tab = intent?.getStringExtra(ReviewInboxConstants.PARAM_TAB) ?: ""
        source = intent?.getStringExtra(ReviewInboxConstants.PARAM_SOURCE) ?: ReviewInboxConstants.DEFAULT_SOURCE
    }

}