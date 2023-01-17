package com.tokopedia.review.feature.createreputation.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import timber.log.Timber

// ApplinkConstInternalMarketPlace.CREATE_REVIEW
class CreateReviewActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent>, ReviewPerformanceMonitoringListener {

    companion object {
        const val PARAM_RATING = "rating"
        const val DEFAULT_PRODUCT_RATING = 5
        const val CREATE_REVIEW_BOTTOM_SHEET_TAG = "CreateReviewBottomSheetTag"
    }

    private var productId: String = ""
    private var rating = DEFAULT_PRODUCT_RATING
    private var feedbackId = ""
    private var reputationId: String = ""
    private var utmSource: String = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
        adjustOrientation()
        handleDimming()
        hideToolbar()
        showWriteFormBottomSheet()
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun getNewFragment(): Fragment? {
        getDataFromApplinkOrIntent()
        return null
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
            ReviewConstants.CREATE_REVIEW_PLT_PREPARE_METRICS,
            ReviewConstants.CREATE_REVIEW_PLT_NETWORK_METRICS,
            ReviewConstants.CREATE_REVIEW_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(ReviewConstants.CREATE_REVIEW_TRACE)
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

    private fun handleDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    private fun hideToolbar() {
        findViewById<Toolbar>(com.tokopedia.abstraction.R.id.toolbar)?.hide()
    }

    private fun showWriteFormBottomSheet() {
        supportFragmentManager.findFragmentByTag(CREATE_REVIEW_BOTTOM_SHEET_TAG).let {
            if (it == null) {
                CreateReviewBottomSheet.createInstance(
                    rating,
                    productId,
                    reputationId,
                    utmSource
                ).run {
                    show(supportFragmentManager, CREATE_REVIEW_BOTTOM_SHEET_TAG)
                }
            }
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}