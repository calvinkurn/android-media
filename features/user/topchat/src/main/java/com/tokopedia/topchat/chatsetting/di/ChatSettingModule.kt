package com.tokopedia.topchat.chatsetting.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatsetting.data.GetChatSettingResponse
import com.tokopedia.topchat.chatsetting.usecase.GetChatSettingUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@ChatSettingScope
class ChatSettingModule {

    @ChatSettingScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ChatSettingScope
    @Provides
    @Named(ChatListQueriesConstant.QUERY_GET_CHAT_SETTING)
    fun provideGqlQueryShopReputation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_get_chat_settings)
    }

    @ChatSettingScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ChatSettingScope
    @Provides
    fun provideGqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetChatSettingResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @ChatSettingScope
    @Provides
    fun provideFavorite(
            @Named(ChatListQueriesConstant.QUERY_GET_CHAT_SETTING)
            gqlQuery: String,
            gqlUseCase: GraphqlUseCase<GetChatSettingResponse>
    ): GetChatSettingUseCase {
        return GetChatSettingUseCase(gqlQuery, gqlUseCase)
    }
}