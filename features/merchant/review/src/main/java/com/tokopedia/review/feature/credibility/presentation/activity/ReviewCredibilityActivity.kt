package com.tokopedia.review.feature.credibility.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.credibility.di.DaggerReviewCredibilityComponent
import com.tokopedia.review.feature.credibility.di.ReviewCredibilityComponent
import com.tokopedia.review.feature.credibility.presentation.fragment.ReviewCredibilityBottomSheet
import timber.log.Timber

class ReviewCredibilityActivity : BaseSimpleActivity(), HasComponent<ReviewCredibilityComponent>,
    ReviewPerformanceMonitoringListener {

    companion object {
        const val REVIEW_CREDIBILITY_BOTTOM_SHEET_TAG = "ReviewCredibilityBottomSheetTag"
    }

    private var userId = ""
    private var source = ""
    private var productId = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromApplinkOrIntent()
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        adjustOrientation()
        handleDimming()
        hideToolbar()
        showReviewCredibilityBottomSheet()
    }

    override fun getComponent(): ReviewCredibilityComponent? {
        return DaggerReviewCredibilityComponent.builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
            ReviewConstants.REVIEW_CREDIBILITY_PLT_PREPARE_METRICS,
            ReviewConstants.REVIEW_CREDIBILITY_PLT_NETWORK_METRICS,
            ReviewConstants.REVIEW_CREDIBILITY_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(ReviewConstants.REVIEW_CREDIBILITY_TRACE)
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
        val uri = intent.data
        if (uri != null && uri.pathSegments.size > 0) {
            val uriSegment = uri.pathSegments
            userId = uriSegment.getOrNull(uriSegment.size - 2) ?: ""
            source = uriSegment.getOrNull(uriSegment.size - 1) ?: ""
            productId = uri.getQueryParameter(ReviewApplinkConst.PARAM_PRODUCT_ID) ?: ""
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

    private fun showReviewCredibilityBottomSheet() {
        supportFragmentManager.findFragmentByTag(
            REVIEW_CREDIBILITY_BOTTOM_SHEET_TAG
        ).let {
            if (it == null) {
                ReviewCredibilityBottomSheet.newInstance(userId, source, productId).run {
                    show(supportFragmentManager, REVIEW_CREDIBILITY_BOTTOM_SHEET_TAG)
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