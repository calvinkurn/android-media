package com.tokopedia.flashsale.management.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flashsale.management.domain.GetCampaignListUsecase
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@Module
@CampaignScope
class CampaignModule {

    @CampaignScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()

    @CampaignScope
    @Provides
    fun provideGetRateEstimationUseCase(@ApplicationContext context: Context, graphqlUseCase: GraphqlUseCase): GetCampaignListUsecase {
        return GetCampaignListUsecase(context, graphqlUseCase)
    }
}