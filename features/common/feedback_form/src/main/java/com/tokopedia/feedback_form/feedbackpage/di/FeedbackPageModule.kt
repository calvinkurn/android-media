package com.tokopedia.feedback_form.feedbackpage.di

import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.ScreenshotResultMapper
import com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage.FeedbackPagePresenter
import com.tokopedia.feedback_form.feedbackpage.domain.mapper.FeedbackDataMapper
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription

@Module
class FeedbackPageModule {

    @Provides
    @FeedbackPageScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @FeedbackPageScope
    fun provideFeedbackPagePresenter(compositeSubscription: CompositeSubscription, mapper: FeedbackDataMapper, imageMapper: ScreenshotResultMapper): FeedbackPagePresenter {
        return FeedbackPagePresenter(compositeSubscription, mapper, imageMapper)
    }
}