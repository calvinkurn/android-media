package com.tokopedia.topchat.chatsearch.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class ChatSearchModule {

    @ChatSearchScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ChatSearchScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ChatSearchScope
    @Provides
    fun provideGqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetChatSearchResponse> {
        return GraphqlUseCase(graphqlRepository)
    }
}