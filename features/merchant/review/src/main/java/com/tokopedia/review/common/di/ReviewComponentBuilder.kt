package com.tokopedia.review.common.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

class ReviewComponentBuilder {

    companion object {
        private var reviewComponent: ReviewComponent? = null

        fun getComponent(application: Application?): ReviewComponent {
            return reviewComponent?.run { reviewComponent }
                    ?: DaggerReviewComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}