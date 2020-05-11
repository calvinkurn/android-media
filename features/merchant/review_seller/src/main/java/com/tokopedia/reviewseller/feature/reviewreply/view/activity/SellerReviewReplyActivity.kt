package com.tokopedia.reviewseller.feature.reviewreply.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerComponentBuilder
import com.tokopedia.reviewseller.feature.reviewreply.di.component.DaggerReviewReplyComponent
import com.tokopedia.reviewseller.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.reviewseller.feature.reviewreply.di.module.ReviewReplyModule
import com.tokopedia.reviewseller.feature.reviewreply.view.fragment.SellerReviewReplyFragment

class SellerReviewReplyActivity : BaseSimpleActivity(), HasComponent<ReviewReplyComponent> {

    override fun getNewFragment(): Fragment = SellerReviewReplyFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_review_reply
    }

    override fun getComponent(): ReviewReplyComponent {
        return DaggerReviewReplyComponent
                .builder()
                .reviewSellerComponent(ReviewSellerComponentBuilder.getComponent(application))
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
