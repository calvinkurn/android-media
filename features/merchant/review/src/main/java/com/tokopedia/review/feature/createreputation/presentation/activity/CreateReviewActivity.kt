package com.tokopedia.review.feature.createreputation.presentation.activity

import android.app.NotificationManager
import android.content.Context
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
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import timber.log.Timber

// ApplinkConstInternalMarketPlace.CREATE_REVIEW
class CreateReviewActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent>, ReviewPerformanceMonitoringListener {

    companion object {
        const val PARAM_RATING = "rating"
        const val DEFAULT_PRODUCT_RATING = 5
        const val WRITE_FORM_EXPERIMENT_NAME = "ReviewForm_AB"
        const val WRITE_FORM_BOTTOM_SHEET_VARIANT = "variant_bottomsheet"
        const val WRITE_FORM_CONTROL_VARIANT = "control_page"
        const val CREATE_REVIEW_BOTTOM_SHEET_TAG = "CreateReviewBottomSheetTag"
    }


    private var productId: String = ""
    private var createReviewFragment: CreateReviewFragment? = null
    private var rating = DEFAULT_PRODUCT_RATING
    private var isEditMode = false
    private var feedbackId = ""
    private var reputationId: String = ""
    private var utmSource: String = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var createReviewBottomSheet: BottomSheetUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
        if (!isNewFormVariant() || isEditMode) {
            setWhiteTheme()
            setToolbar()
        }
        adjustOrientation()
        if (isNewView()) {
            handleDimming()
            hideToolbar()
            showWriteFormBottomSheet()
            return
        }
        setupOldFragment()
    }


    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun getNewFragment(): Fragment? {
        getDataFromApplinkOrIntent()
        if (isNewView()) {
            return null
        }
        setToolbar()
        createReviewFragment = CreateReviewFragment.createInstance(
                productId,
                reputationId,
                rating,
                isEditMode,
                feedbackId,
                utmSource
        )
        return createReviewFragment
    }

    override fun onBackPressed() {
        if (!isNewFormVariant() || isEditMode) {
            createReviewFragment?.let {
                CreateReviewTracking.reviewOnCloseTracker(it.getOrderId(), productId, it.createReviewViewModel.isUserEligible())
                it.showCancelDialog()
            }
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                if (isEditMode) ReviewConstants.EDIT_REVIEW_PLT_PREPARE_METRICS else ReviewConstants.CREATE_REVIEW_PLT_PREPARE_METRICS,
                if (isEditMode) ReviewConstants.EDIT_REVIEW_PLT_NETWORK_METRICS else ReviewConstants.CREATE_REVIEW_PLT_NETWORK_METRICS,
                if (isEditMode) ReviewConstants.EDIT_REVIEW_PLT_RENDER_METRICS else ReviewConstants.CREATE_REVIEW_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(if (isEditMode) ReviewConstants.EDIT_REVIEW_TRACE else ReviewConstants.CREATE_REVIEW_TRACE)
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
            isEditMode = uri.getQueryParameter(ReviewConstants.PARAM_IS_EDIT_MODE)?.toBoolean()
                    ?: false
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

    private fun handleDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    private fun setWhiteTheme() {
        setTheme(com.tokopedia.abstraction.R.style.Theme_WhiteUnify)
    }

    private fun hideToolbar() {
        findViewById<Toolbar>(com.tokopedia.abstraction.R.id.toolbar)?.hide()
    }

    private fun showWriteFormBottomSheet() {
        createReviewBottomSheet = CreateReviewBottomSheet.createInstance(rating, productId, reputationId, utmSource)
        createReviewBottomSheet?.apply {
            clearContentPadding = true
            show(supportFragmentManager, CREATE_REVIEW_BOTTOM_SHEET_TAG)
        }
    }

    private fun setupOldFragment() {
        intent.extras?.run {
            (applicationContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .cancel(getInt(CreateReviewFragment.REVIEW_NOTIFICATION_ID))
        }
        supportActionBar?.elevation = 0f
    }

    private fun isNewView(): Boolean {
        return isNewFormVariant() && !isEditMode
    }

    private fun isNewFormVariant(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                    WRITE_FORM_EXPERIMENT_NAME, WRITE_FORM_CONTROL_VARIANT
            ) == WRITE_FORM_BOTTOM_SHEET_VARIANT
        } catch (t: Throwable) {
            false
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}