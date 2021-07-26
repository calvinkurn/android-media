package com.tokopedia.tokopoints.di

import android.content.Context
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.view.recommwidget.DataMapper
import com.tokopedia.tokopoints.view.recommwidget.RewardsRecommUsecase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class RecomModule {

    @TokoPointScope
    @Provides
    fun getRewardRecommendationProductUseCase(context: Context,
                                              graphqlUseCase: GraphqlUseCase,
                                              userSession: UserSessionInterface,
                                              mapper: DataMapper): RewardsRecommUsecase {
        return RewardsRecommUsecase(context, graphqlUseCase, userSession, mapper)
    }

    @TokoPointScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @TokoPointScope
    @Provides
    fun provideMapper(): DataMapper {
        return DataMapper()
    }

}