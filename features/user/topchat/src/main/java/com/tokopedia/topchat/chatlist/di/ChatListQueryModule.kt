package com.tokopedia.topchat.chatlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * @author : Steven 2019-08-08
 */

@ChatListScope
@Module
class ChatListQueryModule {

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.QUERY_CHAT_LIST_MESSAGE)
    fun provideRawQueryGetChatListMessage(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_chat_list_message)


    @Provides
    fun provideGetChatListMessageInfoUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ChatListPojo> = GraphqlUseCase<ChatListPojo>(graphqlRepository).apply {
        setTypeClass(ChatListPojo::class.java)
    }
}