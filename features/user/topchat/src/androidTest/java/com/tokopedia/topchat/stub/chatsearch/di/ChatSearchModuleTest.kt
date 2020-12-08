package com.tokopedia.topchat.stub.chatsearch.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.di.ChatSearchScope
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@ChatSearchScope
class ChatSearchModuleTest {

    @ChatSearchScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    @ChatSearchScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ChatSearchScope
    @Provides
    fun provideGqlUseCase(): GraphqlUseCaseStub<GetChatSearchResponse> {
        return GraphqlUseCaseStub()
    }

}