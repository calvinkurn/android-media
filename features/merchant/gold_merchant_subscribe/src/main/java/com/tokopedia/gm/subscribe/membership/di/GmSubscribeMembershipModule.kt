package com.tokopedia.gm.subscribe.membership.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gm.subscribe.GmSubscribeModuleRouter
import com.tokopedia.gm.subscribe.membership.analytic.GmSubscribeMembershipTracking
import com.tokopedia.gm.subscribe.membership.domain.GetGmSubscribeMembershipUsecase
import com.tokopedia.gm.subscribe.membership.domain.SetGmSubscribeMembershipUsecase
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@Module
@GmSubscribeMembershipScope
class GmSubscribeMembershipModule {

    @GmSubscribeMembershipScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()

    @GmSubscribeMembershipScope
    @Provides
    fun provideGetRateEstimationUseCase(@ApplicationContext context: Context, graphqlUseCase: GraphqlUseCase): GetGmSubscribeMembershipUsecase {
        return GetGmSubscribeMembershipUsecase(context, graphqlUseCase)
    }

    @GmSubscribeMembershipScope
    @Provides
    fun provideSetGmSubscribeMembershipUsecase(@ApplicationContext context: Context, graphqlUseCase: GraphqlUseCase): SetGmSubscribeMembershipUsecase {
        return SetGmSubscribeMembershipUsecase(context, graphqlUseCase)
    }

    @GmSubscribeMembershipScope
    @Provides
    fun provideGmSubscribeMembershipTracking(@ApplicationContext context: Context): GmSubscribeMembershipTracking {
        return GmSubscribeMembershipTracking(context as GmSubscribeModuleRouter)
    }
}