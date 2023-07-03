package com.tokopedia.inbox.universalinbox.stub.di.recommendationwidget

import com.tokopedia.inbox.universalinbox.stub.di.base.UniversalInboxFakeBaseAppComponent
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationComponent
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationWidgetScope
import dagger.Component

@RecommendationWidgetScope
@Component(
    modules = [
        UniversalInboxRecommendationWidgetModuleStub::class,
        UniversalInboxRecommendationWidgetViewModelModuleStub::class
    ],
    dependencies = [UniversalInboxFakeBaseAppComponent::class]
)
interface UniversalInboxRecommendationWidgetComponentStub : RecommendationComponent
