package com.tokopedia.digital_deals.di

import android.content.Context
import com.tokopedia.digital_deals.di.scope.DealsScope
import com.tokopedia.digital_deals.view.model.response.EventContentData
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class DealsModule (val context: Context) {

    @DealsScope
    @Provides
    fun provideDealsContext(): Context {
        return context
    }

    @DealsScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @DealsScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @DealsScope
    @Provides
    fun provideGqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<EventContentData> {
        return GraphqlUseCase(graphqlRepository)
    }
}