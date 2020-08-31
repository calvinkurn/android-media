package com.tokopedia.review

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.review.common.di.DaggerReviewComponent
import com.tokopedia.review.common.di.ReviewComponent

class ReviewInstance {
    companion object {
        private var reviewComponent: ReviewComponent? = null

        fun getComponent(application: Application): ReviewComponent {
            return reviewComponent?.run {
                reviewComponent
            } ?: DaggerReviewComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
    }
}