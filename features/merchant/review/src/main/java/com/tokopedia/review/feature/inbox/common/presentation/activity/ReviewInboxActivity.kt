package com.tokopedia.review.feature.inbox.common.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.container.presentation.fragment.ReviewInboxContainerFragment
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.activity.CreateReviewActivityOld

class ReviewInboxActivity : BaseSimpleActivity() {

    companion object {
        const val GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY"
        fun createNewInstance(context: Context): Intent {
            return Intent(context, ReviewInboxActivity::class.java)
        }
    }

    private var reviewInboxContainerFragment: ReviewInboxContainerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val goToReputationHistory = getArgumentsFromApplink() ?: false
        reviewInboxContainerFragment = ReviewInboxContainerFragment.createNewInstance(goToReputationHistory)
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return reviewInboxContainerFragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        reviewInboxContainerFragment?.onBackPressed()
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = ReviewInboxConstants.NO_SHADOW_ELEVATION
    }

    private fun getArgumentsFromApplink(): Boolean? {
        return intent?.getBooleanExtra(GO_TO_REPUTATION_HISTORY, false)
    }

}