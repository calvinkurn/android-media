package com.tokopedia.review.common.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

class ReviewSellerComponentBuilder {

    companion object {
        private var reviewSellerComponent: ReviewSellerComponent? = null

        fun getComponent(application: Application): ReviewSellerComponent {
            return reviewSellerComponent?.run { reviewSellerComponent }
                    ?: DaggerReviewSellerComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}