package com.tokopedia.review.feature.createreputation.di

import android.app.Application
import android.content.Context
import com.tokopedia.review.ReviewInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
object CreateReviewDaggerInstance {
    private var INSTANCE: CreateReviewComponent? = null

    fun getInstance(context: Context): CreateReviewComponent {
        return INSTANCE ?: DaggerCreateReviewComponent.builder()
            .reviewComponent(ReviewInstance.getComponent(context.applicationContext as Application))
            .build().also { INSTANCE = it }
    }
}