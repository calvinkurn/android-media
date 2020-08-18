package com.tokopedia.reviewseller.common

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.reviewseller.common.di.component.DaggerReviewSellerComponent
import com.tokopedia.reviewseller.common.di.component.ReviewSellerComponent

class ReviewSellerComponentBuilder {

    companion object {
        private var reviewSellerComponent: ReviewSellerComponent? = null

        fun getComponent(application: Application): ReviewSellerComponent {
            return reviewSellerComponent?.run { reviewSellerComponent }
                    ?: DaggerReviewSellerComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}