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
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment

// ApplinkConstInternalMarketPlace.CREATE_REVIEW
class CreateReviewActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent>, ReviewPerformanceMonitoringListener {

    companion object {
        const val PARAM_RATING = "rating"
        const val DEFAULT_PRODUCT_RATING = 5
    }


    private var productId: String = ""
    private var createReviewFragment: CreateReviewFragment? = null
    private var rating = DEFAULT_PRODUCT_RATING
    private var isEditMode = false
    private var feedbackId = 0
    private var reputationId: String = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun getNewFragment(): Fragment? {
        setToolbar()
        createReviewFragment = CreateReviewFragment.createInstance(
                productId,
                reputationId,
                rating,
                isEditMode,
                feedbackId
        )
        return createReviewFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromApplinkOrIntent()
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        intent.extras?.run {
            (applicationContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .cancel(getInt(CreateReviewFragment.REVIEW_NOTIFICATION_ID))
        }
        supportActionBar?.elevation = 0f
    }


    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun onBackPressed() {
        createReviewFragment?.let {
            CreateReviewTracking.reviewOnCloseTracker(it.getOrderId(), productId, it.createReviewViewModel.isUserEligible())
            it.showCancelDialog()
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                if(isEditMode) ReviewConstants.EDIT_REVIEW_PLT_PREPARE_METRICS else ReviewConstants.CREATE_REVIEW_PLT_PREPARE_METRICS,
                if(isEditMode) ReviewConstants.EDIT_REVIEW_PLT_NETWORK_METRICS else ReviewConstants.CREATE_REVIEW_PLT_NETWORK_METRICS,
                if(isEditMode) ReviewConstants.EDIT_REVIEW_PLT_RENDER_METRICS else ReviewConstants.CREATE_REVIEW_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(if(isEditMode) ReviewConstants.EDIT_REVIEW_TRACE else ReviewConstants.CREATE_REVIEW_TRACE)
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

    private fun getDataFromApplinkOrIntent() {
        val bundle = intent.extras
        val uri = intent.data
        if (uri != null && uri.pathSegments.size > 0) {
            val uriSegment = uri.pathSegments
            productId = uri.lastPathSegment ?: ""
            reputationId = uriSegment[uriSegment.size - 2]
            rating = uri.getQueryParameter(PARAM_RATING)?.toIntOrNull() ?: DEFAULT_PRODUCT_RATING
            isEditMode = uri.getQueryParameter(ReviewConstants.PARAM_IS_EDIT_MODE)?.toBoolean() ?: false
            feedbackId = uri.getQueryParameter(ReviewConstants.PARAM_FEEDBACK_ID)?.toIntOrZero() ?: 0
        } else {
            productId = bundle?.getString(ReviewConstants.ARGS_PRODUCT_ID) ?: ""
            reputationId = bundle?.getString(ReviewConstants.ARGS_REPUTATION_ID) ?: ""
            rating = bundle?.getInt(CreateReviewFragment.REVIEW_CLICK_AT, rating) ?: DEFAULT_PRODUCT_RATING
        }
    }

    private fun setToolbar() {
        this.supportActionBar?.setTitle(getString(R.string.review_create_activity_title))
    }
}