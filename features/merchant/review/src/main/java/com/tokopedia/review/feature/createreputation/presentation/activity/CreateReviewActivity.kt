package com.tokopedia.review.feature.createreputation.presentation.activity

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
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity.CreateReviewActivityOld

// ApplinkConstInternalMarketPlace.CREATE_REVIEW
class CreateReviewActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent> {

    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private var productId: String = ""
    private var createReviewFragment: CreateReviewFragment? = null
    private var rating = DEFAULT_PRODUCT_RATING
    private var isEditMode = false
    private var feedbackId = 0
    private var reputationId: String = ""

    companion object {
        const val PARAM_RATING = "rating"
        const val DEFAULT_PRODUCT_RATING = 5
        const val ARGS_REPUTATION_ID = "ARGS_REPUTATION_ID"
        const val ARGS_PRODUCT_ID = "ARGS_PRODUCT_ID"
        fun newInstance(context: Context) = Intent(context, CreateReviewActivity::class.java)
    }

    override fun getNewFragment(): Fragment? {
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
        getAbTestPlatform()?.fetch(null)
        super.onCreate(savedInstanceState)
        if(useOldPage()) {
            val intent = CreateReviewActivityOld.newInstance(context = this)
            intent.putExtra(ARGS_PRODUCT_ID, productId)
            intent.putExtra(ARGS_REPUTATION_ID, reputationId)
            startActivity(intent)
            finish()
            return
        }
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
            CreateReviewTracking.reviewOnCloseTracker(it.getOrderId(), productId)
            if(it.getIsEditMode) {
                it.showCancelDialog()
            } else {
                if (isTaskRoot) {
                    val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
                    startActivity(intent)
                } else {
                    super.onBackPressed()
                }
                finish()
            }
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(this.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun useOldPage(): Boolean {
        val abTestValue = getAbTestPlatform()?.getString(ReviewConstants.AB_TEST_KEY, "") ?: return true
        return abTestValue == ReviewConstants.OLD_REVIEW_FLOW
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
}