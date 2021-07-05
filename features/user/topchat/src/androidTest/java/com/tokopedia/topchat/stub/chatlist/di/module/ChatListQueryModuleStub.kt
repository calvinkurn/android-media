package com.tokopedia.topchat.stub.chatlist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.chatlist.usecase.GetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.usecase.GetChatNotificationUseCase
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class ChatListQueryModuleStub(
        private val chatListUseCase: GetChatListMessageUseCase,
        private val chatNotificationUseCaseStub: GetChatNotificationUseCaseStub
) {

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.QUERY_DELETE_CHAT_MESSAGE)
    fun provideRawQueryDeleteChatListMessage(@TopchatContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_chat_delete)

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_READ)
    fun provideRawQueryMutationMarkChatAsRead(@TopchatContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_chat_mark_read)

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_UNREAD)
    fun provideRawQueryMutationMarkChatAsUnread(@TopchatContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_chat_mark_unread)

    @ChatListScope
    @Provides
    @IntoMap
    @StringKey(ChatListQueriesConstant.QUERY_BLAST_SELLER_METADATA)
    fun provideRawQueryChatBlastSellerMetaData(@TopchatContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_chat_blast_seller_metadata)

    @Provides
    @ChatListScope
    fun provideGetChatListMessageInfoUseCase(): GetChatListMessageUseCase = chatListUseCase

    @Provides
    @ChatListScope
    fun provideGetChatNotificationUseCase(): GetChatNotificationUseCase = chatNotificationUseCaseStub
}