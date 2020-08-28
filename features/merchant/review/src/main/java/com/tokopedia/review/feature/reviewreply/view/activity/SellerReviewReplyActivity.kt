package com.tokopedia.review.feature.reviewreply.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.reviewreply.di.component.DaggerReviewReplyComponent
import com.tokopedia.review.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.review.feature.reviewreply.di.module.ReviewReplyModule
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment

class SellerReviewReplyActivity : BaseSimpleActivity(), HasComponent<ReviewReplyComponent> {

    override fun getNewFragment(): Fragment = SellerReviewReplyFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_review_reply
    }

    override fun getComponent(): ReviewReplyComponent {
        return DaggerReviewReplyComponent
                .builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .reviewReplyModule(ReviewReplyModule())
                .build()
    }

    override fun onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this)
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

}
