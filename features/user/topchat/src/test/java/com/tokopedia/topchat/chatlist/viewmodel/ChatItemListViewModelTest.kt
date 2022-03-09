package com.tokopedia.topchat.chatlist.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.inboxcommon.RoleType.Companion.BUYER
import com.tokopedia.inboxcommon.RoleType.Companion.SELLER
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_ALL
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_SELLER
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_USER
import com.tokopedia.topchat.chatlist.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.pojo.chatblastseller.BlastSellerMetaDataResponse
import com.tokopedia.topchat.chatlist.pojo.chatblastseller.ChatBlastSellerMetadata
import com.tokopedia.topchat.chatlist.pojo.whitelist.ChatWhitelistFeature
import com.tokopedia.topchat.chatlist.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.chatlist.usecase.*
import com.tokopedia.topchat.chatroom.view.viewmodel.ReplyParcelableModel
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatItemListViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val repository: GraphqlRepository = mockk(relaxed = true)
    private var queries: MutableMap<String, String> = mockk(relaxed = true)

    private val chatWhitelistFeature: GetChatWhitelistFeature = mockk(relaxed = true)
    private val chatBannedSellerUseCase: ChatBanedSellerUseCase = mockk(relaxed = true)
    private val pinChatUseCase: MutationPinChatUseCase = mockk(relaxed = true)
    private val unpinChatUseCase: MutationUnpinChatUseCase = mockk(relaxed = true)
    private val getChatListUseCase: GetChatListMessageUseCase = mockk(relaxed = true)
    private val authorizeAccessUseCase: AuthorizeAccessUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val moveChatToTrashUseCase: MutationMoveChatToTrashUseCase = mockk(relaxed = true)

    private val mutateChatListObserver: Observer<Result<ChatListPojo>> = mockk(relaxed = true)
    private val deleteChatObserver: Observer<Result<ChatDelete>> = mockk(relaxed = true)
    private val broadCastButtonVisibilityObserver: Observer<Boolean> = mockk(relaxed = true)
    private val broadCastButtonUrlObserver: Observer<String> = mockk(relaxed = true)
    private val chatBannedSellerStatusObserver: Observer<Result<Boolean>> = mockk(relaxed = true)
    private val isWhitelistTopBotObserver: Observer<Boolean> = mockk(relaxed = true)
    private val isChatAdminEligibleObserver: Observer<Result<Boolean>> = mockk(relaxed = true)

    private lateinit var viewModel: ChatItemListViewModel

    @Before fun setUp() {
        viewModel = ChatItemListViewModel(
            repository,
            queries,
            chatWhitelistFeature,
            chatBannedSellerUseCase,
            pinChatUseCase,
            unpinChatUseCase,
            getChatListUseCase,
            authorizeAccessUseCase,
            moveChatToTrashUseCase,
            userSession,
            Dispatchers.Unconfined
        )
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
        assertThat(viewModel.mutateChatList.value, `is`(expectedValue))
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
        assertThat(viewModel.mutateChatList.value, `is`(expectedValue))
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
        assertThat(viewModel.mutateChatList.value, `is`(expectedValue))
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
        assertThat(viewModel.mutateChatList.value, `is`(expectedValue))
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

    @Test
    fun `on success delete chat`() {
        // Given
        val successDelete = ChatDelete(
            isSuccess = 1, detailResponse = "", messageId = exMessageId.toLong())
        val result = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = listOf(successDelete)
        }

        coEvery {
            moveChatToTrashUseCase(exMessageId)
        } returns result

        // When
        viewModel.chatMoveToTrash(exMessageId)

        // Then
        val actualResult = (viewModel.deleteChat.value as Success).data.isSuccess
        assertTrue(actualResult == successDelete.isSuccess)
    }

    @Test
    fun `on failed to delete chat`() {
        // Given
        val failedDelete = ChatDelete(
            isSuccess = 0, detailResponse = "Error", messageId = exMessageId.toLong())
        val result = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = listOf(failedDelete)
        }

        coEvery {
            moveChatToTrashUseCase(exMessageId)
        } returns result

        // When
        viewModel.chatMoveToTrash(exMessageId)

        // Then
        val actualResult = (viewModel.deleteChat.value as Fail).throwable.message
        assertTrue(actualResult == failedDelete.detailResponse)
    }

    @Test
    fun `on delete chat but empty`() {
        // Given
        val result = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = listOf()
        }

        coEvery {
            moveChatToTrashUseCase(exMessageId)
        } returns result

        // When
        viewModel.chatMoveToTrash(exMessageId)

        // Then
        assertEquals(null, viewModel.deleteChat.value)
    }

    @Test
    fun `on error delete chat`() {
        // Given
        val throwable = Throwable("Oops!")
        coEvery {
            moveChatToTrashUseCase(exMessageId)
        } throws throwable

        // When
        viewModel.chatMoveToTrash(exMessageId)

        // Then
        val actualResult = (viewModel.deleteChat.value as Fail).throwable.message
        assertTrue(actualResult == throwable.message)
    }

    @Test
    fun should_get_false_has_next_when_use_case_false() {
        //Given
        every {
            getChatListUseCase.hasNext
        } returns false

        //When
        val result = viewModel.chatListHasNext

        //Then
        assertFalse(result)
    }

    @Test
    fun should_get_true_has_next_when_use_case_false() {
        //Given
        every {
            getChatListUseCase.hasNext
        } returns true

        //When
        val result = viewModel.chatListHasNext

        //Then
        assertTrue(result)
    }

    @Test
    fun should_get_false_has_next_when_use_case_reset() {
        //When
        viewModel.resetState()
        val result = viewModel.chatListHasNext

        //Then
        assertFalse(result)
    }

    @Test
    fun should_invoke_onSuccess_when_success_unpin_chat() {
        //Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            unpinChatUseCase.unpinChat(any(), any(), any())
        } answers {
            expectedSuccess.invoke(true)
        }

        //When
        viewModel.pinUnpinChat(exMessageId, false, expectedSuccess, expectedError)

        //Then
        verify(exactly = 1) {
            expectedSuccess.invoke(true)
        }
        verify(exactly = 0) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onSuccess_when_success_unpin_chat_but_response_false() {
        //Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            unpinChatUseCase.unpinChat(any(), any(), any())
        } answers {
            expectedSuccess.invoke(false)
        }

        //When
        viewModel.pinUnpinChat(exMessageId, false, expectedSuccess, expectedError)

        //Then
        verify(exactly = 1) {
            expectedSuccess.invoke(false)
        }
        verify(exactly = 0) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onError_when_failed_to_unpin_chat() {
        //Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            unpinChatUseCase.unpinChat(any(), any(), any())
        } answers {
            expectedError.invoke(expectedThrowable)
        }

        //When
        viewModel.pinUnpinChat(exMessageId, false, expectedSuccess, expectedError)

        //Then
        verify(exactly = 0) {
            expectedSuccess.invoke(true)
        }
        verify(exactly = 0) {
            expectedSuccess.invoke(false)
        }
        verify(exactly = 1) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onSuccess_when_success_pin_chat() {
        //Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            pinChatUseCase.pinChat(any(), any(), any())
        } answers {
            expectedSuccess.invoke(true)
        }

        //When
        viewModel.pinUnpinChat(exMessageId, true, expectedSuccess, expectedError)

        //Then
        verify(exactly = 1) {
            expectedSuccess.invoke(true)
        }
        verify(exactly = 0) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onSuccess_when_success_pin_chat_but_response_false() {
        //Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            pinChatUseCase.pinChat(any(), any(), any())
        } answers {
            expectedSuccess.invoke(false)
        }

        //When
        viewModel.pinUnpinChat(exMessageId, true, expectedSuccess, expectedError)

        //Then
        verify(exactly = 1) {
            expectedSuccess.invoke(false)
        }
        verify(exactly = 0) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onError_when_failed_to_pin_chat() {
        //Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            pinChatUseCase.pinChat(any(), any(), any())
        } answers {
            expectedError.invoke(expectedThrowable)
        }

        //When
        viewModel.pinUnpinChat(exMessageId, true, expectedSuccess, expectedError)

        //Then
        verify(exactly = 0) {
            expectedSuccess.invoke(true)
        }
        verify(exactly = 0) {
            expectedSuccess.invoke(false)
        }
        verify(exactly = 1) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_empty_pinnedMsgId_and_unpinnedMsgId_when_cleared() {
        //Given
        viewModel.pinnedMsgId.add("Test123")
        viewModel.unpinnedMsgId.add("Test456")
        val sizeBeforePinnedMsgId = viewModel.pinnedMsgId.size
        val sizeBeforeUnpinnedMsgId = viewModel.unpinnedMsgId.size

        //When
        viewModel.clearPinUnpinData()
        val sizeAfterPinnedMsgId = viewModel.pinnedMsgId.size
        val sizeAfterUnpinnedMsgId = viewModel.unpinnedMsgId.size

        //Then
        assertEquals(1, sizeBeforePinnedMsgId)
        assertEquals(1, sizeBeforeUnpinnedMsgId)
        assertEquals(0, sizeAfterPinnedMsgId)
        assertEquals(0, sizeAfterUnpinnedMsgId)
    }

    @Test
    fun should_do_nothing_when_mark_chat_as_read_but_empty_query() {
        //Given
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        every {
            queries[ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_READ]
        } returns null

        //When
        viewModel.markChatAsRead(listOf(exMessageId), expectedResult)

        //Then
        verify (exactly = 0) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_do_nothing_when_mark_chat_as_unread_but_empty_query() {
        //Given
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        every {
            queries[ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_UNREAD]
        } returns null

        //When
        viewModel.markChatAsUnread(listOf(exMessageId), expectedResult)

        //Then
        verify (exactly = 0) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_invoke_result_success_when_mark_chat_as_read() {
        //Given
        val testQuery = "testQuery123"
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        val expectedData = ChatChangeStateResponse()
        val expectedResponse = GraphqlResponse(
            mapOf(Pair(ChatChangeStateResponse::class.java, expectedData)),
            mapOf(), false
        )
        every {
            queries[ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_READ]
        } returns testQuery
        coEvery {
            repository.response(any(), any())
        } returns expectedResponse

        //When
        viewModel.markChatAsRead(listOf(exMessageId), expectedResult)

        //Then
        verify (exactly = 1) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_invoke_result_fail_when_mark_chat_as_read() {
        //Given
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        val expectedError = Throwable("Oops!")
        val testQuery = "testQuery123"
        every {
            queries[ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_READ]
        } returns testQuery
        coEvery {
            repository.response(any(), any())
        } throws expectedError

        //When
        viewModel.markChatAsRead(listOf(exMessageId), expectedResult)

        //Then
        verify (exactly = 1) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_invoke_result_success_when_mark_chat_as_unread() {
        //Given
        val testQuery = "testQuery123"
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        val expectedData = ChatChangeStateResponse()
        val expectedResponse = GraphqlResponse(
            mapOf(Pair(ChatChangeStateResponse::class.java, expectedData)),
            mapOf(), false
        )
        every {
            queries[ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_UNREAD]
        } returns testQuery
        coEvery {
            repository.response(any(), any())
        } returns expectedResponse

        //When
        viewModel.markChatAsUnread(listOf(exMessageId), expectedResult)

        //Then
        verify (exactly = 1) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_invoke_result_fail_when_mark_chat_as_unread() {
        //Given
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        val expectedError = Throwable("Oops!")
        val testQuery = "testQuery123"
        every {
            queries[ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_UNREAD]
        } returns testQuery
        coEvery {
            repository.response(any(), any())
        } throws expectedError

        //When
        viewModel.markChatAsUnread(listOf(exMessageId), expectedResult)

        //Then
        verify (exactly = 1) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_give_true_when_success_get_seller_banned_status() {
        //Given
        every {
            chatBannedSellerUseCase.getStatus(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(Boolean) -> Unit>()
            onSuccess.invoke(true)
        }

        //When
        viewModel.loadChatBannedSellerStatus()

        //Then
        assertEquals(true,
            (viewModel.chatBannedSellerStatus.value as Success).data)
    }

    @Test
    fun should_give_false_when_success_get_seller_banned_status() {
        //Given
        every {
            chatBannedSellerUseCase.getStatus(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(Boolean) -> Unit>()
            onSuccess.invoke(false)
        }

        //When
        viewModel.loadChatBannedSellerStatus()

        //Then
        assertEquals(false,
            (viewModel.chatBannedSellerStatus.value as Success).data)
    }

    @Test
    fun should_give_error_when_error_get_seller_banned_status() {
        //Given
        val expectedThrowable = Throwable("Oops!")
        every {
            chatBannedSellerUseCase.getStatus(any(), captureLambda())
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(expectedThrowable)
        }

        //When
        viewModel.loadChatBannedSellerStatus()

        //Then
        assertEquals(expectedThrowable.message,
            (viewModel.chatBannedSellerStatus.value as Fail).throwable.message)
    }

    @Test
    fun should_not_update_anything_when_load_chat_blast_seller_metadata_but_query_null() {
        //Given
        every {
            queries[ChatListQueriesConstant.QUERY_BLAST_SELLER_METADATA]
        } returns null

        //When
        viewModel.loadChatBlastSellerMetaData()

        //Then
        assertEquals(null,
            viewModel.broadCastButtonUrl.value
        )
        assertEquals(null,
            viewModel.broadCastButtonVisibility.value
        )
    }

    @Test
    fun should_get_data_when_load_success_get_chat_blast_seller_metadata() {
        //Given
        val testQuery = "query123"
        val testUrlBroadcast = "url broadcast 123"
        val expectedData = BlastSellerMetaDataResponse(
            ChatBlastSellerMetadata(urlBroadcast = testUrlBroadcast)
        )
        val expectedResponse = GraphqlResponse(
            mapOf(Pair(BlastSellerMetaDataResponse::class.java, expectedData)),
            mapOf(), false
        )
        every {
            queries[ChatListQueriesConstant.QUERY_BLAST_SELLER_METADATA]
        } returns testQuery
        coEvery {
            repository.response(any(), any())
        } returns expectedResponse
        every {
            userSession.isShopOwner
        } returns true

        //When
        viewModel.whenChatAdminAuthorized(PARAM_TAB_SELLER) {}
        viewModel.loadChatBlastSellerMetaData()

        //Then
        assertEquals(true, viewModel.isAdminHasAccess)
        assertEquals(testUrlBroadcast,
            viewModel.broadCastButtonUrl.value
        )
        assertEquals(true,
            viewModel.broadCastButtonVisibility.value
        )
    }

    @Test
    fun should_get_data_when_load_success_get_chat_blast_seller_metadata_but_no_access() {
        //Given
        val testQuery = "query123"
        val expectedData = BlastSellerMetaDataResponse()
        val expectedResponse = GraphqlResponse(
            mapOf(Pair(BlastSellerMetaDataResponse::class.java, expectedData)),
            mapOf(), false
        )
        every {
            queries[ChatListQueriesConstant.QUERY_BLAST_SELLER_METADATA]
        } returns testQuery
        coEvery {
            repository.response(any(), any())
        } returns expectedResponse

        //When
        viewModel.loadChatBlastSellerMetaData()

        //Then
        assertEquals(null,
            viewModel.broadCastButtonUrl.value
        )
        assertEquals(false,
            viewModel.broadCastButtonVisibility.value
        )
    }

    @Test
    fun should_get_data_when_load_error_get_chat_blast_seller_metadata() {
        //Given
        val testQuery = "query123"
        val expectedError = Throwable("Oops!")
        every {
            queries[ChatListQueriesConstant.QUERY_BLAST_SELLER_METADATA]
        } returns testQuery
        coEvery {
            repository.response(any(), any())
        } throws expectedError

        //When
        viewModel.loadChatBlastSellerMetaData()

        //Then
        assertEquals(null,
            viewModel.broadCastButtonUrl.value
        )
        assertEquals(false,
            viewModel.broadCastButtonVisibility.value
        )
    }

    @Test
    fun should_give_the_same_reply_time() {
        //Given
        val testLastItem = ReplyParcelableModel(exMessageId,"msg", "1000")

        //When
        val result = viewModel.getReplyTimeStampFrom(testLastItem)

        //Then
        assertEquals(
            (testLastItem.replyTime.toLongOrZero() / 1_000_000L).toString(),
            result
        )
    }

    @Test
    fun should_invoke_onSuccess_when_success_load_top_bot_whitelist() {
        //Given
        val expectedResponse = ChatWhitelistFeatureResponse(
            ChatWhitelistFeature(isWhitelist = true))
        every {
            chatWhitelistFeature.getWhiteList(any(), captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ChatWhitelistFeatureResponse) -> Unit>()
            onSuccess.invoke(expectedResponse)
        }

        //When
        viewModel.loadTopBotWhiteList()

        //Then
        assertTrue(viewModel.isWhitelistTopBot.value?: false)
    }

    @Test
    fun should_invoke_onSuccess_when_success_load_top_bot_whitelist_but_false() {
        //Given
        val expectedResponse = ChatWhitelistFeatureResponse(
            ChatWhitelistFeature(isWhitelist = false))
        every {
            chatWhitelistFeature.getWhiteList(any(), captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ChatWhitelistFeatureResponse) -> Unit>()
            onSuccess.invoke(expectedResponse)
        }

        //When
        viewModel.loadTopBotWhiteList()

        //Then
        assertFalse(viewModel.isWhitelistTopBot.value?: true)
    }

    @Test
    fun should_invoke_onError_when_error_load_top_bot_whitelist() {
        //Given
        val expectedThrowable = Throwable("Oops!")
        every {
            chatWhitelistFeature.getWhiteList(any(), any(), captureLambda())
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(expectedThrowable)
        }

        //When
        viewModel.loadTopBotWhiteList()

        //Then
        assertEquals(null, viewModel.isWhitelistTopBot.value)
    }

    @Test
    fun should_give_3_filter_titles_when_buyer() {
        //Given
        val testContext: Context = mockk(relaxed = true)

        //When
        val result = viewModel.getFilterTitles(testContext, false)

        //Then
        assertEquals(3, result.size)
    }

    @Test
    fun should_give_4_filter_titles_when_seller() {
        //Given
        val testContext: Context = mockk(relaxed = true)

        //When
        val result = viewModel.getFilterTitles(testContext, true)

        //Then
        assertEquals(4, result.size)
    }

    @Test
    fun does_have_filter() {
        //Give
        viewModel.filter = ChatListQueriesConstant.PARAM_FILTER_UNREAD

        //When
        val result = viewModel.hasFilter()

        //Then
        assertTrue(result)
    }

    @Test
    fun does_not_have_filter() {
        //When
        val result = viewModel.hasFilter()

        //Then
        assertFalse(result)
    }

    @Test
    fun does_not_have_filter_after_reset() {
        //Given
        viewModel.filter = ChatListQueriesConstant.PARAM_FILTER_UNREAD

        //When
        viewModel.reset()
        val result = viewModel.hasFilter()

        //Then
        assertFalse(result)
    }

    companion object {
        private const val exMessageId = "190378584"
        private val getChatList: ChatListPojo = FileUtil.parse(
                "/success_get_chat_list.json",
                ChatListPojo::class.java
        )
    }

}