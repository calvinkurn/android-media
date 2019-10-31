package com.tokopedia.topchat.chatlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.pojo.ChatNotificationsPojo
import com.tokopedia.topchat.chatlist.pojo.NotificationsPojo
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

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.QUERY_DELETE_CHAT_MESSAGE)
    fun provideRawQueryDeleteChatListMessage(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_chat_delete)

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_READ)
    fun provideRawQueryMutationMarkChatAsRead(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_chat_mark_read)

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_UNREAD)
    fun provideRawQueryMutationMarkChatAsUnread(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_chat_mark_unread)

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.QUERY_BLAST_SELLER_METADATA)
    fun provideRawQueryChatBlastSellerMetaData(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_chat_blast_seller_metadata)

    @Provides
    fun provideGetChatListMessageInfoUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ChatListPojo> = GraphqlUseCase<ChatListPojo>(graphqlRepository).apply {
        setTypeClass(ChatListPojo::class.java)
    }

}