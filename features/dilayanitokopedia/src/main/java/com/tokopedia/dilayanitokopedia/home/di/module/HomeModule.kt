package com.tokopedia.dilayanitokopedia.home.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.dilayanitokopedia.home.di.scope.HomeScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module()
class HomeModule {


        @HomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @HomeScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }


//
//    @HomeScope
//    @Provides
//    fun provideGetRecommendationUseCase(
//        @ApplicationContext context: Context,
//        coroutineGqlRepository: GraphqlRepository
//    ): GetRecommendationUseCase = GetRecommendationUseCase(context, coroutineGqlRepository)

}
