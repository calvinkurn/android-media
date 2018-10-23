package com.tokopedia.flashsale.management.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@Module
@CampaignScope
class CampaignModule {

    @CampaignScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()
}