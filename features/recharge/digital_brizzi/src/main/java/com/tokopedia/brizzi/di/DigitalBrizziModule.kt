package com.tokopedia.brizzi.di

import com.tokopedia.brizzi.mapper.BrizziCardObjectMapper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import id.co.bri.sdk.Brizzi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class DigitalBrizziModule {

    @DigitalBrizziScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @DigitalBrizziScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @DigitalBrizziScope
    @Provides
    fun provideBrizziInstance(): Brizzi {
        return Brizzi.getInstance()
    }

    @DigitalBrizziScope
    @Provides
    fun provideBrizziMapper(): BrizziCardObjectMapper {
        return BrizziCardObjectMapper()
    }
}