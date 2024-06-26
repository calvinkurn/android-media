package com.tokopedia.inbox.universalinbox.stub.di.recommendationwidget

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.universalinbox.stub.common.UserSessionStub
import com.tokopedia.inbox.universalinbox.stub.data.repository.GraphqlRepositoryStub
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationWidgetScope
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetState
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object UniversalInboxRecommendationWidgetModuleStub {

    @Provides
    @RecommendationWidgetScope
    fun provideUserSessionInterface(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSessionStub(context)
    }

    @Provides
    fun provideRecommendationWidgetState(): RecommendationWidgetState =
        RecommendationWidgetState()

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlRepositoryStub()
    }

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context.applicationContext
    }
}
