package com.tokopedia.developer_options.presentation.feedbackpage.di

import com.tokopedia.developer_options.presentation.feedbackpage.ui.FeedbackPagePresenter
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription

@Module
@FeedbackPageScope
class FeedbackPageModule {

    @Provides
    @FeedbackPageScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @FeedbackPageScope
    fun provideFeedbackPagePresenter(compositeSubscription: CompositeSubscription): FeedbackPagePresenter {
        return FeedbackPagePresenter(compositeSubscription)
    }
}