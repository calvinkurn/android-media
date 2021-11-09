package com.tokopedia.review

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.review.common.di.DaggerReviewInboxComponent
import com.tokopedia.review.common.di.ReviewInboxComponent

class ReviewInboxInstance {
    companion object {
        private var reviewInboxComponent: ReviewInboxComponent? = null

        fun getComponent(application: Application): ReviewInboxComponent {
            return reviewInboxComponent?.run {
                reviewInboxComponent
            } ?: DaggerReviewInboxComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
    }
}