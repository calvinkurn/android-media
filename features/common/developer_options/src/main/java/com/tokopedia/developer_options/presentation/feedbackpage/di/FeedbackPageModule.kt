package com.tokopedia.developer_options.presentation.feedbackpage.di

import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.CategoriesMapper
import com.tokopedia.developer_options.presentation.feedbackpage.domain.model.ScreenshotResultMapper
import com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage.FeedbackPagePresenter
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
    fun provideFeedbackPagePresenter(compositeSubscription: CompositeSubscription, mapper: CategoriesMapper, imageMapper: ScreenshotResultMapper): FeedbackPagePresenter {
        return FeedbackPagePresenter(compositeSubscription, mapper, imageMapper)
    }
}