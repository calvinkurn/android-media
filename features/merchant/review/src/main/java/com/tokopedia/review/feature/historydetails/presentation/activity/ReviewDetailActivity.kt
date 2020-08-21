package com.tokopedia.review.feature.historydetails.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.presentation.activity.CreateReviewActivity
import com.tokopedia.review.feature.historydetails.presentation.fragment.ReviewDetailFragment
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity.CreateReviewActivityOld
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationDetailActivity

class ReviewDetailActivity : BaseSimpleActivity() {

    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private var reputationId: String = ""
    private var feedbackId: Int = 0
    private var reviewDetailFragment: ReviewDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromApplink()
        reviewDetailFragment = ReviewDetailFragment.createNewInstance(feedbackId)
        getAbTestPlatform()?.fetch(null)
        super.onCreate(savedInstanceState)
        if(!useNewPage()) {
            val intent = InboxReputationDetailActivity.getCallingIntent(this, reputationId)
            startActivity(intent)
            finish()
            return
        }
        supportActionBar?.hide()
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
        feedbackId = uri.getQueryParameter(ReviewConstants.PARAM_FEEDBACK_ID).toIntOrZero()
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
}