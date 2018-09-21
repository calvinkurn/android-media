package com.tokopedia.broadcast.message.common.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@BroadcastMessageScope
@Module
class BroadcastMessageModule {

    @BroadcastMessageScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()
}