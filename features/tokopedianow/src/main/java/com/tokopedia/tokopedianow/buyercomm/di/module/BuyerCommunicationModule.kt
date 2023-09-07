package com.tokopedia.tokopedianow.buyercomm.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.buyercomm.di.scope.BuyerCommunicationScope
import dagger.Module
import dagger.Provides

@Module
class BuyerCommunicationModule {

    @BuyerCommunicationScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}
