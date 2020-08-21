package com.tokopedia.review.feature.createreputation.presentation.activity

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity.CreateReviewActivityOld
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment.CreateReviewFragmentOld
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.ReviewTracking
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity

// ApplinkConstInternalMarketPlace.CREATE_REVIEW
class CreateReviewActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent> {

    companion object {
        const val PARAM_RATING = "rating"
        const val DEFAULT_PRODUCT_RATING = 5
    }

    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private var productId: String = ""
    private var createReviewFragment: CreateReviewFragment? = null
    private var createReviewOldFragment: CreateReviewFragmentOld? = null
    private var utmSource = CreateReviewActivityOld.DEFAULT_UTM_SOURCE
    private var rating = DEFAULT_PRODUCT_RATING
    private var isEditMode = false
    private var feedbackId = 0
    private var reputationId: String = ""

    override fun getNewFragment(): Fragment? {
        if(useNewPage()) {
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
        val bundle = intent.extras
        val uri = intent.data

        if (uri != null && uri.pathSegments.size > 0) {
            val uriSegment = uri.pathSegments
            productId = uri.lastPathSegment ?: ""
            reputationId = uriSegment[uriSegment.size - 2]
            rating = uri.getQueryParameter(CreateReviewActivityOld.PARAM_RATING)?.toIntOrNull() ?: CreateReviewActivityOld.DEFAULT_PRODUCT_RATING
            utmSource = uri.getQueryParameter(CreateReviewActivityOld.PARAM_UTM_SOURCE) ?: CreateReviewActivityOld.DEFAULT_UTM_SOURCE
        } else {
            productId = bundle?.getString(InboxReputationFormActivity.ARGS_PRODUCT_ID) ?: ""
            reputationId = bundle?.getString(InboxReputationFormActivity.ARGS_REPUTATION_ID) ?: ""
        }
        createReviewOldFragment = CreateReviewFragmentOld.createInstance(
                productId,
                reputationId,
                bundle?.getInt(CreateReviewFragmentOld.REVIEW_CLICK_AT, rating) ?: rating,
                utmSource
        )
        return createReviewOldFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromApplinkOrIntent()
        getAbTestPlatform()?.fetch(null)
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
        if(useNewPage()) {
            createReviewFragment?.let {
                CreateReviewTracking.reviewOnCloseTracker(it.getOrderId(), productId)
                it.showCancelDialog()
            }
        } else {
            createReviewOldFragment?.let {
                ReviewTracking.reviewOnCloseTracker(it.getOrderId, productId)
            }
            if (isTaskRoot) {
                val intent = RouteManager.getIntent(this, ApplinkConst.HOME)

                setResult(Activity.RESULT_OK, intent)
                startActivity(intent)
            } else {
                super.onBackPressed()
            }
            finish()
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(this.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun useNewPage(): Boolean {
        val abTestValue = getAbTestPlatform()?.getString(ReviewConstants.AB_TEST_KEY, "") ?: return true
        return abTestValue == ReviewConstants.NEW_REVIEW_FLOW
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