package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inboxcommon.RoleType.Companion.BUYER
import com.tokopedia.inboxcommon.RoleType.Companion.SELLER
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_ALL
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// TODO: Postponed temporarily, waiting @rifqimfahmi
class ChatItemListViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val repository: GraphqlRepository = mockk(relaxed = true)
    private val queries: MutableMap<String, String> = mutableMapOf()

    private val chatWhitelistFeature: GetChatWhitelistFeature = mockk(relaxed = true)
    private val chatBannedSellerUseCase: ChatBanedSellerUseCase = mockk(relaxed = true)
    private val pinChatUseCase: MutationPinChatUseCase = mockk(relaxed = true)
    private val unpinChatUseCase: MutationUnpinChatUseCase = mockk(relaxed = true)
    private val getChatListUseCase: GetChatListMessageUseCase = mockk(relaxed = true)

    private val mutateChatListObserver: Observer<Result<ChatListPojo>> = mockk(relaxed = true)
    private val deleteChatObserver: Observer<Result<ChatDelete>> = mockk(relaxed = true)
    private val broadCastButtonVisibilityObserver: Observer<Boolean> = mockk(relaxed = true)
    private val broadCastButtonUrlObserver: Observer<String> = mockk(relaxed = true)
    private val chatBannedSellerStatusObserver: Observer<Result<Boolean>> = mockk(relaxed = true)
    private val isWhitelistTopBotObserver: Observer<Boolean> = mockk(relaxed = true)

    private val viewModel = ChatItemListViewModel(
            repository,
            queries,
            chatWhitelistFeature,
            chatBannedSellerUseCase,
            pinChatUseCase,
            unpinChatUseCase,
            getChatListUseCase,
            Dispatchers.Unconfined
    )

    @Before fun setUp() {
        viewModel.mutateChatList.observeForever(mutateChatListObserver)
        viewModel.deleteChat.observeForever(deleteChatObserver)
        viewModel.broadCastButtonVisibility.observeForever(broadCastButtonVisibilityObserver)
        viewModel.broadCastButtonUrl.observeForever(broadCastButtonUrlObserver)
        viewModel.chatBannedSellerStatus.observeForever(chatBannedSellerStatusObserver)
        viewModel.isWhitelistTopBot.observeForever(isWhitelistTopBotObserver)
    }

    @Test fun `getChatListMessage should return chat list of messages`() {
        // given
        val expectedValue = Success(getChatList)
        viewModel.filter = PARAM_FILTER_ALL

        every {
            getChatListUseCase.getChatList(any(), viewModel.filter, any(), captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ChatListPojo, List<String>, List<String>) -> Unit>()
            onSuccess.invoke(getChatList, listOf(), listOf())
        }

        // when
        viewModel.getChatListMessage(0, 0, "")

        // then
        verify(exactly = 1) {
            mutateChatListObserver.onChanged(expectedValue)
            assertEquals(expectedValue, viewModel.mutateChatList.value)
        }
    }

    @Test fun `getChatListMessage as buyer should return chat list of messages`() {
        // given
        val expectedValue = Success(getChatList)
        viewModel.filter = PARAM_FILTER_ALL

        every {
            getChatListUseCase.getChatList(any(), viewModel.filter, any(), captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ChatListPojo, List<String>, List<String>) -> Unit>()
            onSuccess.invoke(getChatList, listOf(), listOf())
        }

        // when
        viewModel.getChatListMessage(0, BUYER)

        // then
        verify(exactly = 1) {
            mutateChatListObserver.onChanged(expectedValue)
            assertEquals(expectedValue, viewModel.mutateChatList.value)
        }
    }

    @Test fun `getChatListMessage as seller should return chat list of messages`() {
        // given
        val expectedValue = Success(getChatList)
        viewModel.filter = PARAM_FILTER_ALL

        every {
            getChatListUseCase.getChatList(any(), viewModel.filter, any(), captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ChatListPojo, List<String>, List<String>) -> Unit>()
            onSuccess.invoke(getChatList, listOf(), listOf())
        }

        // when
        viewModel.getChatListMessage(0, SELLER)

        // then
        verify(exactly = 1) {
            mutateChatListObserver.onChanged(expectedValue)
            assertEquals(expectedValue, viewModel.mutateChatList.value)
        }
    }

    @Test fun `getChatListMessage as undefined should return chat list of messages as buyer`() {
        // given
        val expectedValue = Success(getChatList)
        viewModel.filter = PARAM_FILTER_ALL

        every {
            getChatListUseCase.getChatList(any(), viewModel.filter, any(), captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ChatListPojo, List<String>, List<String>) -> Unit>()
            onSuccess.invoke(getChatList, listOf(), listOf())
        }

        // when
        viewModel.getChatListMessage(0, -1)

        // then
        verify(exactly = 1) {
            mutateChatListObserver.onChanged(expectedValue)
            assertEquals(expectedValue, viewModel.mutateChatList.value)
        }
    }

    @Test fun `getChatListMessage should throw the Fail state`() {
        // given
        val expectedValue = Throwable("")
        viewModel.filter = PARAM_FILTER_ALL

        every {
            getChatListUseCase.getChatList(any(), viewModel.filter, any(), any(), captureLambda())
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(expectedValue)
        }

        // when
        viewModel.getChatListMessage(0, 0, "")

        // then
        verify(exactly = 1) { mutateChatListObserver.onChanged(Fail(expectedValue)) }
    }

    @Test fun `chatMoveToTrash should able delete a message of topchat`() {
        // given
        queries["delete_chat_message"] = "mock"

    }

    //@Test fun ``() {}

    companion object {
        private val getChatList: ChatListPojo = FileUtil.parse(
                "/success_get_chat_list.json",
                ChatListPojo::class.java
        )
    }

}