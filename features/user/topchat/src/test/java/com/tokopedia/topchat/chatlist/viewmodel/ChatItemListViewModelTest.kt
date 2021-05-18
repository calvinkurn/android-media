package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inboxcommon.RoleType.Companion.BUYER
import com.tokopedia.inboxcommon.RoleType.Companion.SELLER
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_ALL
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_USER
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
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
    private val authorizeAccessUseCase: AuthorizeAccessUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val mutateChatListObserver: Observer<Result<ChatListPojo>> = mockk(relaxed = true)
    private val deleteChatObserver: Observer<Result<ChatDelete>> = mockk(relaxed = true)
    private val broadCastButtonVisibilityObserver: Observer<Boolean> = mockk(relaxed = true)
    private val broadCastButtonUrlObserver: Observer<String> = mockk(relaxed = true)
    private val chatBannedSellerStatusObserver: Observer<Result<Boolean>> = mockk(relaxed = true)
    private val isWhitelistTopBotObserver: Observer<Boolean> = mockk(relaxed = true)
    private val isChatAdminEligibleObserver: Observer<Result<Boolean>> = mockk(relaxed = true)

    private val viewModel = ChatItemListViewModel(
            repository,
            queries,
            chatWhitelistFeature,
            chatBannedSellerUseCase,
            pinChatUseCase,
            unpinChatUseCase,
            getChatListUseCase,
            authorizeAccessUseCase,
            userSession,
            Dispatchers.Unconfined
    )

    @Before fun setUp() {
        viewModel.mutateChatList.observeForever(mutateChatListObserver)
        viewModel.deleteChat.observeForever(deleteChatObserver)
        viewModel.broadCastButtonVisibility.observeForever(broadCastButtonVisibilityObserver)
        viewModel.broadCastButtonUrl.observeForever(broadCastButtonUrlObserver)
        viewModel.chatBannedSellerStatus.observeForever(chatBannedSellerStatusObserver)
        viewModel.isWhitelistTopBot.observeForever(isWhitelistTopBotObserver)
        viewModel.isChatAdminEligible.observeForever(isChatAdminEligibleObserver)
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
        viewModel.getChatListMessage(0, 0, PARAM_TAB_USER)

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
        coEvery {
            userSession.isShopOwner
        } answers {
            true
        }

        // when
        viewModel.getChatListMessage(0, SELLER)
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
        viewModel.getChatListMessage(0, 0, PARAM_TAB_USER)

        // then
        verify(exactly = 1) { mutateChatListObserver.onChanged(Fail(expectedValue)) }
    }

    @Test fun `chatMoveToTrash should able delete a message of topchat`() {
        // given
        queries["delete_chat_message"] = "mock"

    }

    @Test fun `buyer tab should be eligible to get chat list message`() {
        // when
        viewModel.filter = PARAM_FILTER_ALL
        viewModel.getChatListMessage(0, 0, PARAM_TAB_USER)

        // then
        verify(exactly = 0) {
            isChatAdminEligibleObserver.onChanged(any())
        }
        verify(exactly = 1) {
            getChatListUseCase.getChatList(any(), viewModel.filter, any(), any(), any())
        }
    }

    @Test fun `buyer role should be eligible to get chat list message`() {
        // when
        viewModel.filter = PARAM_FILTER_ALL
        viewModel.getChatListMessage(0, BUYER)

        // then
        verify(exactly = 0) {
            isChatAdminEligibleObserver.onChanged(any())
        }
        verify(exactly = 1) {
            getChatListUseCase.getChatList(any(), viewModel.filter, any(), any(), any())
        }
    }

    @Test fun `shop owner seller role should be eligible to get chat list message`() {
        // given
        val expectedResult = Success(true)

        every {
            userSession.isShopOwner
        } answers {
            true
        }

        // when
        viewModel.getChatListMessage(0, SELLER)

        // then
        verify(exactly = 1) {
            isChatAdminEligibleObserver.onChanged(expectedResult)
        }
        assertThat(viewModel.isChatAdminEligible.value, equalTo(expectedResult))
    }

    @Test fun `non-owner shop admin seller role should success check for eligibility first`() {
        // given
        val expectedEligiblity = true
        val expectedResult = Success(expectedEligiblity)
        every {
            userSession.isShopOwner
        } answers {
            false
        }
        every {
            userSession.isShopAdmin
        } answers {
            true
        }
        coEvery {
            authorizeAccessUseCase.execute(any())
        } answers {
            expectedEligiblity
        }

        // when
        viewModel.getChatListMessage(0, SELLER)

        // then
        verify(exactly = 1) {
            isChatAdminEligibleObserver.onChanged(expectedResult)
        }
        assertThat(viewModel.isChatAdminEligible.value, equalTo(expectedResult))
    }

    @Test fun `non-owner shop admin seller role should fail check for eligibility first`() {
        // given
        val expectedThrowable = MessageErrorException()
        val expectedResult = Fail(expectedThrowable)
        every {
            userSession.isShopOwner
        } answers {
            false
        }
        every {
            userSession.isShopAdmin
        } answers {
            true
        }
        coEvery {
            authorizeAccessUseCase.execute(any())
        } throws expectedThrowable

        // when
        viewModel.getChatListMessage(0, SELLER)

        // then
        assert(viewModel.isChatAdminEligible.value is Fail)
    }

    companion object {
        private val getChatList: ChatListPojo = FileUtil.parse(
                "/success_get_chat_list.json",
                ChatListPojo::class.java
        )
    }

}