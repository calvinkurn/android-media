package com.tokopedia.topchat.stub.chatlist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@ChatListScope
@Module
class ChatListQueryModuleStub(
        private val chatListUseCase: GraphqlUseCase<ChatListPojo>
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
    fun provideTestDispatcher(): TopchatCoroutineContextProvider = TopchatAndroidTestCoroutineContextDispatcher()

    @Provides
    @ChatListScope
    fun provideGetChatListMessageInfoUseCase(): GraphqlUseCase<ChatListPojo> = chatListUseCase
}