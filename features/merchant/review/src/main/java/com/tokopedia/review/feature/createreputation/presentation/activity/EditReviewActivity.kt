package com.tokopedia.review.feature.createreputation.presentation.activity

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment

class EditReviewActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent>,
    ReviewPerformanceMonitoringListener {

    companion object {
        const val PARAM_RATING = "rating"
        const val DEFAULT_PRODUCT_RATING = 5
    }

    private var productId: String = ""
    private var createReviewFragment: CreateReviewFragment? = null
    private var rating = DEFAULT_PRODUCT_RATING
    private var feedbackId = ""
    private var reputationId: String = ""
    private var utmSource: String = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
        setToolbar()
        setupFragment()
    }


    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun getNewFragment(): Fragment? {
        getDataFromApplinkOrIntent()
        setToolbar()
        createReviewFragment = CreateReviewFragment.createInstance(
            productId,
            reputationId,
            rating,
            feedbackId,
            utmSource
        )
        return createReviewFragment
    }

    override fun onBackPressed() {
        createReviewFragment?.let {
            CreateReviewTracking.reviewOnCloseTracker(
                orderId = "",
                productId = productId,
                isEligible = false
            )
            it.showCancelDialog()
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
            ReviewConstants.EDIT_REVIEW_PLT_PREPARE_METRICS,
            ReviewConstants.EDIT_REVIEW_PLT_NETWORK_METRICS,
            ReviewConstants.EDIT_REVIEW_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(ReviewConstants.EDIT_REVIEW_TRACE)
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

    private fun getDataFromApplinkOrIntent() {
        val bundle = intent.extras
        val uri = intent.data
        if (uri != null && uri.pathSegments.size > 0) {
            val uriSegment = uri.pathSegments
            productId = uri.lastPathSegment ?: ""
            reputationId = uriSegment[uriSegment.size - 2]
            rating = uri.getQueryParameter(PARAM_RATING)?.toIntOrNull() ?: DEFAULT_PRODUCT_RATING
            feedbackId = uri.getQueryParameter(ReviewConstants.PARAM_FEEDBACK_ID) ?: ""
            utmSource = uri.getQueryParameter(ReviewConstants.PARAM_SOURCE) ?: ""
        } else {
            productId = bundle?.getString(ReviewConstants.ARGS_PRODUCT_ID) ?: ""
            reputationId = bundle?.getString(ReviewConstants.ARGS_REPUTATION_ID) ?: ""
            rating = bundle?.getInt(CreateReviewFragment.REVIEW_CLICK_AT, rating)
                ?: DEFAULT_PRODUCT_RATING
        }
    }

    private fun setToolbar() {
        this.supportActionBar?.title = getString(R.string.review_create_activity_title)
    }

    private fun setupFragment() {
        intent.extras?.run {
            (applicationContext
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .cancel(getInt(CreateReviewFragment.REVIEW_NOTIFICATION_ID))
        }
        supportActionBar?.elevation = 0f
    }

}