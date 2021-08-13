package com.tokopedia.thankyou_native.recommendation.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.recommendation.di.qualifier.RecommendationApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class RecommendationModule(val context: Context) {

    @Provides
    @RecommendationApplicationContext
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository


    @Provides
    fun provideUserSession(@RecommendationApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}