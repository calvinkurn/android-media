package com.tokopedia.review.stub.reviewcommon

import android.app.Application
import com.tokopedia.review.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.review.stub.reviewcommon.di.component.DaggerReviewComponentStub
import com.tokopedia.review.stub.reviewcommon.di.component.ReviewComponentStub

object ReviewInstanceStub {
    private var reviewComponent: ReviewComponentStub? = null

    fun getComponent(application: Application): ReviewComponentStub {
        return reviewComponent?.run {
            reviewComponent
        } ?: DaggerReviewComponentStub.builder().baseAppComponentStub(
            BaseAppComponentStubInstance.getBaseAppComponentStub(application)
        ).build()
    }
}