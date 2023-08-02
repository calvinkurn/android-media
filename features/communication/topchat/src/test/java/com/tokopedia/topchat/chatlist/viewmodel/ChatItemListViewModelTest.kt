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
import com.tokopedia.topchat.chatlist.domain.pojo.*
import com.tokopedia.topchat.chatlist.domain.pojo.chatblastseller.BlastSellerMetaDataResponse
import com.tokopedia.topchat.chatlist.domain.pojo.chatblastseller.ChatBlastSellerMetadata
import com.tokopedia.topchat.chatlist.domain.pojo.chatlistticker.ChatListTickerResponse
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatMetricResponse
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeature
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.chatlist.domain.usecase.ChatBanedSellerUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatBlastSellerMetaDataUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListTickerUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatWhitelistFeature
import com.tokopedia.topchat.chatlist.domain.usecase.GetOperationalInsightUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.MutationPinChatUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.MutationUnpinChatUseCase
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatItemListViewModel.Companion.BUBBLE_TICKER_PREF_NAME
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatItemListViewModel.Companion.OPERATIONAL_INSIGHT_NEXT_MONDAY
import com.tokopedia.topchat.chatlist.view.widget.BroadcastButtonLayout.Companion.BROADCAST_FAB_LABEL_PREF_NAME
import com.tokopedia.topchat.chatroom.view.uimodel.ReplyParcelableModel
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.util.Utils
import com.tokopedia.topchat.common.util.Utils.getBuildVersion
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatItemListViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val repository: GraphqlRepository = mockk(relaxed = true)

    private val chatWhitelistFeature: GetChatWhitelistFeature = mockk(relaxed = true)
    private val chatBannedSellerUseCase: ChatBanedSellerUseCase = mockk(relaxed = true)
    private val pinChatUseCase: MutationPinChatUseCase = mockk(relaxed = true)
    private val unpinChatUseCase: MutationUnpinChatUseCase = mockk(relaxed = true)
    private val getChatListUseCase: GetChatListMessageUseCase = mockk(relaxed = true)
    private val authorizeAccessUseCase: AuthorizeAccessUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val moveChatToTrashUseCase: MutationMoveChatToTrashUseCase = mockk(relaxed = true)
    private val operationalInsightUseCase: GetOperationalInsightUseCase = mockk(relaxed = true)
    private val getChatListTickerUseCase: GetChatListTickerUseCase = mockk(relaxed = true)

    @RelaxedMockK
    lateinit var getChatBlastSellerMetaDataUseCase: GetChatBlastSellerMetaDataUseCase

    @RelaxedMockK
    lateinit var cacheManager: TopchatCacheManager

    private val mutateChatListObserver: Observer<Result<ChatListPojo>> = mockk(relaxed = true)
    private val deleteChatObserver: Observer<Result<ChatDelete>> = mockk(relaxed = true)
    private val broadCastButtonVisibilityObserver: Observer<Boolean> = mockk(relaxed = true)
    private val broadCastButtonUrlObserver: Observer<String> = mockk(relaxed = true)
    private val chatBannedSellerStatusObserver: Observer<Result<Boolean>> = mockk(relaxed = true)
    private val isWhitelistTopBotObserver: Observer<Boolean> = mockk(relaxed = true)
    private val isChatAdminEligibleObserver: Observer<Result<Boolean>> = mockk(relaxed = true)

    private lateinit var viewModel: ChatItemListViewModel

    @Before fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ChatItemListViewModel(
            repository,
            chatWhitelistFeature,
            chatBannedSellerUseCase,
            pinChatUseCase,
            unpinChatUseCase,
            getChatListUseCase,
            authorizeAccessUseCase,
            moveChatToTrashUseCase,
            operationalInsightUseCase,
            getChatListTickerUseCase,
            getChatBlastSellerMetaDataUseCase,
            cacheManager,
            userSession,
            CoroutineTestDispatchersProvider
        )
        viewModel.mutateChatList.observeForever(mutateChatListObserver)
        viewModel.deleteChat.observeForever(deleteChatObserver)
        viewModel.broadCastButtonVisibility.observeForever(broadCastButtonVisibilityObserver)
        viewModel.broadCastButtonUrl.observeForever(broadCastButtonUrlObserver)
        viewModel.chatBannedSellerStatus.observeForever(chatBannedSellerStatusObserver)
        viewModel.isWhitelistTopBot.observeForever(isWhitelistTopBotObserver)
        viewModel.isChatAdminEligible.observeForever(isChatAdminEligibleObserver)

        mockkObject(ChatItemListViewModel)
    }

    @Test fun `getChatListMessage should return chat list of messages`() {
        // given
        val expectedValue = Success(getChatList)
        viewModel.filter = PARAM_FILTER_ALL

        coEvery {
            getChatListUseCase.invoke(any())
        } answers {
            ChatListResponse(getChatList, listOf(), listOf())
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

        coEvery {
            getChatListUseCase.invoke(any())
        } answers {
            ChatListResponse(getChatList, listOf(), listOf())
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

        coEvery {
            getChatListUseCase.invoke(any())
        } answers {
            ChatListResponse(getChatList, listOf(), listOf())
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

        coEvery {
            getChatListUseCase.invoke(any())
        } answers {
            ChatListResponse(getChatList, listOf(), listOf())
        }

        // when
        viewModel.getChatListMessage(0, -1)

        // then
        assertThat(viewModel.mutateChatList.value, `is`(expectedValue))
    }

    @Test fun `getChatListMessage should throw the Fail state`() {
        // given
        val expectedValue = Exception("")
        viewModel.filter = PARAM_FILTER_ALL

        coEvery {
            getChatListUseCase.invoke(any())
        } throws expectedValue

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
        coVerify(exactly = 1) {
            getChatListUseCase.invoke(any())
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
        coVerify(exactly = 1) {
            getChatListUseCase.invoke(any())
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
            isSuccess = 1,
            detailResponse = "",
            messageId = exMessageId
        )
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
            isSuccess = 0,
            detailResponse = "Error",
            messageId = exMessageId
        )
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
        // Given
        every {
            getChatListUseCase.hasNext
        } returns false

        // When
        val result = viewModel.chatListHasNext

        // Then
        assertFalse(result)
    }

    @Test
    fun should_get_true_has_next_when_use_case_false() {
        // Given
        every {
            getChatListUseCase.hasNext
        } returns true

        // When
        val result = viewModel.chatListHasNext

        // Then
        assertTrue(result)
    }

    @Test
    fun should_get_false_has_next_when_use_case_reset() {
        // When
        viewModel.resetState()
        val result = viewModel.chatListHasNext

        // Then
        assertFalse(result)
    }

    @Test
    fun should_invoke_onSuccess_when_success_unpin_chat() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            unpinChatUseCase.unpinChat(any(), any(), any())
        } answers {
            expectedSuccess.invoke(true)
        }

        // When
        viewModel.pinUnpinChat(exMessageId, false, expectedSuccess, expectedError)

        // Then
        verify(exactly = 1) {
            expectedSuccess.invoke(true)
        }
        verify(exactly = 0) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onSuccess_when_success_unpin_chat_but_response_false() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            unpinChatUseCase.unpinChat(any(), any(), any())
        } answers {
            expectedSuccess.invoke(false)
        }

        // When
        viewModel.pinUnpinChat(exMessageId, false, expectedSuccess, expectedError)

        // Then
        verify(exactly = 1) {
            expectedSuccess.invoke(false)
        }
        verify(exactly = 0) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onError_when_failed_to_unpin_chat() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            unpinChatUseCase.unpinChat(any(), any(), any())
        } answers {
            expectedError.invoke(expectedThrowable)
        }

        // When
        viewModel.pinUnpinChat(exMessageId, false, expectedSuccess, expectedError)

        // Then
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
        // Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            pinChatUseCase.pinChat(any(), any(), any())
        } answers {
            expectedSuccess.invoke(true)
        }

        // When
        viewModel.pinUnpinChat(exMessageId, true, expectedSuccess, expectedError)

        // Then
        verify(exactly = 1) {
            expectedSuccess.invoke(true)
        }
        verify(exactly = 0) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onSuccess_when_success_pin_chat_but_response_false() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            pinChatUseCase.pinChat(any(), any(), any())
        } answers {
            expectedSuccess.invoke(false)
        }

        // When
        viewModel.pinUnpinChat(exMessageId, true, expectedSuccess, expectedError)

        // Then
        verify(exactly = 1) {
            expectedSuccess.invoke(false)
        }
        verify(exactly = 0) {
            expectedError.invoke(expectedThrowable)
        }
    }

    @Test
    fun should_invoke_onError_when_failed_to_pin_chat() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        val expectedSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        val expectedError: (Throwable) -> Unit = mockk(relaxed = true)
        every {
            pinChatUseCase.pinChat(any(), any(), any())
        } answers {
            expectedError.invoke(expectedThrowable)
        }

        // When
        viewModel.pinUnpinChat(exMessageId, true, expectedSuccess, expectedError)

        // Then
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
        // Given
        viewModel.pinnedMsgId.add("Test123")
        viewModel.unpinnedMsgId.add("Test456")
        val sizeBeforePinnedMsgId = viewModel.pinnedMsgId.size
        val sizeBeforeUnpinnedMsgId = viewModel.unpinnedMsgId.size

        // When
        viewModel.clearPinUnpinData()
        val sizeAfterPinnedMsgId = viewModel.pinnedMsgId.size
        val sizeAfterUnpinnedMsgId = viewModel.unpinnedMsgId.size

        // Then
        assertEquals(1, sizeBeforePinnedMsgId)
        assertEquals(1, sizeBeforeUnpinnedMsgId)
        assertEquals(0, sizeAfterPinnedMsgId)
        assertEquals(0, sizeAfterUnpinnedMsgId)
    }

    @Test
    fun should_invoke_result_success_when_mark_chat_as_read() {
        // Given
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        val expectedData = ChatChangeStateResponse()
        val expectedResponse = GraphqlResponse(
            mapOf(Pair(ChatChangeStateResponse::class.java, expectedData)),
            mapOf(),
            false
        )
        coEvery {
            repository.response(any(), any())
        } returns expectedResponse

        // When
        viewModel.markChatAsRead(listOf(exMessageId), expectedResult)

        // Then
        verify(exactly = 1) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_invoke_result_fail_when_mark_chat_as_read() {
        // Given
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        val expectedError = Throwable("Oops!")
        coEvery {
            repository.response(any(), any())
        } throws expectedError

        // When
        viewModel.markChatAsRead(listOf(exMessageId), expectedResult)

        // Then
        verify(exactly = 1) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_invoke_result_success_when_mark_chat_as_unread() {
        // Given
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        val expectedData = ChatChangeStateResponse()
        val expectedResponse = GraphqlResponse(
            mapOf(Pair(ChatChangeStateResponse::class.java, expectedData)),
            mapOf(),
            false
        )
        coEvery {
            repository.response(any(), any())
        } returns expectedResponse

        // When
        viewModel.markChatAsUnread(listOf(exMessageId), expectedResult)

        // Then
        verify(exactly = 1) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_invoke_result_fail_when_mark_chat_as_unread() {
        // Given
        val expectedResult: (Result<ChatChangeStateResponse>) -> Unit = mockk(relaxed = true)
        val expectedError = Throwable("Oops!")
        coEvery {
            repository.response(any(), any())
        } throws expectedError

        // When
        viewModel.markChatAsUnread(listOf(exMessageId), expectedResult)

        // Then
        verify(exactly = 1) {
            expectedResult.invoke(any())
        }
    }

    @Test
    fun should_give_true_when_success_get_seller_banned_status() {
        // Given
        every {
            chatBannedSellerUseCase.getStatus(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(Boolean) -> Unit>()
            onSuccess.invoke(true)
        }

        // When
        viewModel.loadChatBannedSellerStatus()

        // Then
        assertEquals(
            true,
            (viewModel.chatBannedSellerStatus.value as Success).data
        )
    }

    @Test
    fun should_give_false_when_success_get_seller_banned_status() {
        // Given
        every {
            chatBannedSellerUseCase.getStatus(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(Boolean) -> Unit>()
            onSuccess.invoke(false)
        }

        // When
        viewModel.loadChatBannedSellerStatus()

        // Then
        assertEquals(
            false,
            (viewModel.chatBannedSellerStatus.value as Success).data
        )
    }

    @Test
    fun should_give_error_when_error_get_seller_banned_status() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        every {
            chatBannedSellerUseCase.getStatus(any(), captureLambda())
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(expectedThrowable)
        }

        // When
        viewModel.loadChatBannedSellerStatus()

        // Then
        assertEquals(
            expectedThrowable.message,
            (viewModel.chatBannedSellerStatus.value as Fail).throwable.message
        )
    }

    @Test
    fun should_get_data_when_load_success_get_chat_blast_seller_metadata() {
        // Given
        val testUrlBroadcast = "url broadcast 123"
        val expectedData = BlastSellerMetaDataResponse(
            ChatBlastSellerMetadata(urlBroadcast = testUrlBroadcast)
        )
        coEvery {
            getChatBlastSellerMetaDataUseCase(Unit)
        } returns flowOf(expectedData)

        every {
            userSession.isShopOwner
        } returns true

        // When
        viewModel.whenChatAdminAuthorized(PARAM_TAB_SELLER) {}
        viewModel.loadChatBlastSellerMetaData()

        // Then
        assertEquals(true, viewModel.isAdminHasAccess)
        assertEquals(
            testUrlBroadcast,
            viewModel.broadCastButtonUrl.value
        )
        assertEquals(
            true,
            viewModel.broadCastButtonVisibility.value
        )
    }

    @Test
    fun should_get_data_when_load_success_get_chat_blast_seller_metadata_but_no_access() {
        // Given
        val expectedData = BlastSellerMetaDataResponse()
        coEvery {
            getChatBlastSellerMetaDataUseCase(Unit)
        } returns flowOf(expectedData)

        // When
        viewModel.loadChatBlastSellerMetaData()

        // Then
        assertEquals(
            null,
            viewModel.broadCastButtonUrl.value
        )
        assertEquals(
            false,
            viewModel.broadCastButtonVisibility.value
        )
    }

    @Test
    fun should_get_data_when_load_error_get_chat_blast_seller_metadata() {
        // Given
        val expectedError = Throwable("Oops!")
        coEvery {
            getChatBlastSellerMetaDataUseCase(Unit)
        } throws expectedError

        // When
        viewModel.loadChatBlastSellerMetaData()

        // Then
        assertEquals(
            null,
            viewModel.broadCastButtonUrl.value
        )
        assertEquals(
            false,
            viewModel.broadCastButtonVisibility.value
        )
    }

    @Test
    fun should_give_the_same_reply_time() {
        // Given
        val testLastItem = ReplyParcelableModel(exMessageId, "msg", "1000")

        // When
        val result = viewModel.getReplyTimeStampFrom(testLastItem)

        // Then
        assertEquals(
            (testLastItem.replyTime.toLongOrZero() / 1_000_000L).toString(),
            result
        )
    }

    @Test
    fun should_invoke_onSuccess_when_success_load_top_bot_whitelist() {
        // Given
        val expectedResponse = ChatWhitelistFeatureResponse(
            ChatWhitelistFeature(isWhitelist = true)
        )
        every {
            chatWhitelistFeature.getWhiteList(any(), captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ChatWhitelistFeatureResponse) -> Unit>()
            onSuccess.invoke(expectedResponse)
        }

        // When
        viewModel.loadTopBotWhiteList()

        // Then
        assertTrue(viewModel.isWhitelistTopBot.value ?: false)
    }

    @Test
    fun should_invoke_onSuccess_when_success_load_top_bot_whitelist_but_false() {
        // Given
        val expectedResponse = ChatWhitelistFeatureResponse(
            ChatWhitelistFeature(isWhitelist = false)
        )
        every {
            chatWhitelistFeature.getWhiteList(any(), captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ChatWhitelistFeatureResponse) -> Unit>()
            onSuccess.invoke(expectedResponse)
        }

        // When
        viewModel.loadTopBotWhiteList()

        // Then
        assertFalse(viewModel.isWhitelistTopBot.value ?: true)
    }

    @Test
    fun should_invoke_onError_when_error_load_top_bot_whitelist() {
        // Given
        val expectedThrowable = Throwable("Oops!")
        every {
            chatWhitelistFeature.getWhiteList(any(), any(), captureLambda())
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(expectedThrowable)
        }

        // When
        viewModel.loadTopBotWhiteList()

        // Then
        assertEquals(null, viewModel.isWhitelistTopBot.value)
    }

    @Test
    fun should_give_2_filter_titles_when_buyer() {
        // Given
        val testContext: Context = mockk(relaxed = true)
        every {
            ChatItemListViewModel.arrayFilterParam.size
        } returns 3

        // When
        val result = viewModel.getFilterTitles(testContext, false)

        // Then
        assertEquals(2, result.size)
    }

    @Test
    fun should_give_3_filter_titles_when_seller_default() {
        // Given
        val testContext: Context = mockk(relaxed = true)
        every {
            ChatItemListViewModel.arrayFilterParam.size
        } returns 3

        // When
        val result = viewModel.getFilterTitles(testContext, true)

        // Then
        assertEquals(3, result.size)
    }

    @Test
    fun should_give_4_filter_titles_when_seller_whitelisted() {
        // Given
        val testContext: Context = mockk(relaxed = true)
        every {
            ChatItemListViewModel.arrayFilterParam.size
        } returns 4

        // When
        val result = viewModel.getFilterTitles(testContext, true)

        // Then
        assertEquals(4, result.size)
    }

    @Test
    fun does_have_filter() {
        // Give
        viewModel.filter = ChatListQueriesConstant.PARAM_FILTER_UNREAD

        // When
        val result = viewModel.hasFilter()

        // Then
        assertTrue(result)
    }

    @Test
    fun does_not_have_filter() {
        // When
        val result = viewModel.hasFilter()

        // Then
        assertFalse(result)
    }

    @Test
    fun does_not_have_filter_after_reset() {
        // Given
        viewModel.filter = ChatListQueriesConstant.PARAM_FILTER_UNREAD

        // When
        viewModel.reset()
        val result = viewModel.hasFilter()

        // Then
        assertFalse(result)
    }

    @Test
    fun should_get_ticker_with_true_show_when_get_operational_insight() {
        // Given
        val expectedResponse = ShopChatMetricResponse().apply {
            this.shopChatTicker?.showTicker = true
        }
        coEvery {
            operationalInsightUseCase(any())
        } returns expectedResponse
        every {
            cacheManager.getLongCache(any(), any())
        } returns 0

        // When
        viewModel.getOperationalInsight(shopId)

        // Then
        assertTrue((viewModel.chatOperationalInsight.value as Success).data.showTicker ?: false)
    }

    @Test
    fun should_get_ticker_with_false_show_when_get_operational_insight_but_not_next_monday() {
        // Given
        val expectedResponse = ShopChatMetricResponse().apply {
            this.shopChatTicker?.showTicker = true
        }
        coEvery {
            operationalInsightUseCase(any())
        } returns expectedResponse
        every {
            cacheManager.getLongCache(any(), any())
        } returns Long.MAX_VALUE

        // When
        viewModel.getOperationalInsight(shopId)

        // Then
        assertFalse((viewModel.chatOperationalInsight.value as Success).data.showTicker ?: true)
    }

    @Test
    fun should_get_ticker_with_false_show_when_get_operational_insight() {
        // Given
        val expectedResponse = ShopChatMetricResponse().apply {
            this.shopChatTicker?.showTicker = false
        }
        coEvery {
            operationalInsightUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getOperationalInsight(shopId)

        // Then
        assertFalse((viewModel.chatOperationalInsight.value as Success).data.showTicker ?: true)
    }

    @Test
    fun should_not_get_ticker_when_ticker_data_is_null() {
        // Given
        val expectedResponse = ShopChatMetricResponse().apply {
            this.shopChatTicker = null
        }
        coEvery {
            operationalInsightUseCase(any())
        } returns expectedResponse

        // When
        viewModel.getOperationalInsight(shopId)

        // Then
        assertEquals(
            null,
            viewModel.chatOperationalInsight.value
        )
    }

    @Test
    fun should_return_success_when_get_chat_list_ticker() {
        // Given
        val expectedResponse = ChatListTickerResponse(
            chatlistTicker = ChatListTickerResponse.ChatListTicker(
                tickerBuyer = ChatListTickerResponse.ChatListTicker.TickerBuyer(
                    message = "Ini adalah content dari ticker buyer",
                    enable = true,
                    tickerType = 1
                ),
                tickerSeller = ChatListTickerResponse.ChatListTicker.TickerSeller(
                    message = "Ini adalah content dari ticker seller",
                    enable = false,
                    tickerType = 1
                )
            )
        )
        coEvery {
            getChatListTickerUseCase(Unit)
        } returns expectedResponse

        // When
        viewModel.getChatListTicker()

        // Then
        val actualValue = (viewModel.chatListTicker.value as Success).data
        val expectedValue = expectedResponse.chatlistTicker
        assertEquals(expectedValue.tickerBuyer.message, actualValue.tickerBuyer.message)
        assertEquals(expectedValue.tickerBuyer.tickerType, actualValue.tickerBuyer.tickerType)
        assertEquals(expectedValue.tickerSeller.message, actualValue.tickerSeller.message)
        assertEquals(expectedValue.tickerSeller.tickerType, actualValue.tickerSeller.tickerType)
    }

    @Test
    fun should_get_throwable_when_error_get_chatlist_ticker() {
        // Given
        val expectedResult = Throwable("Oops!")
        coEvery {
            getChatListTickerUseCase(Unit)
        } throws expectedResult

        // When
        viewModel.getChatListTicker()

        // Then
        assertEquals(
            expectedResult.message,
            (viewModel.chatListTicker.value as Fail).throwable.message
        )
    }

    @Test
    fun should_get_throwable_when_error_get_operational_insight() {
        // Given
        val expectedResult = Throwable("Oops!")
        coEvery {
            operationalInsightUseCase(any())
        } throws expectedResult

        // When
        viewModel.getOperationalInsight(shopId)

        // Then
        assertEquals(
            expectedResult.message,
            (viewModel.chatOperationalInsight.value as Fail).throwable.message
        )
    }

    @Test
    fun should_save_next_monday_date_in_millis() {
        // Given
        mockkObject(Utils)
        val expectedTimeMillis: Long = 1
        every {
            cacheManager.getLongCache(any(), any())
        } returns expectedTimeMillis
        every {
            Utils.getNextParticularDay(any())
        } returns expectedTimeMillis

        // When
        viewModel.saveNextMondayDate()
        val result = cacheManager.getLongCache(OPERATIONAL_INSIGHT_NEXT_MONDAY, 0)

        // Then
        assertEquals(
            expectedTimeMillis,
            result
        )
    }

    @Test
    fun should_show_bubble_ticker_on_android_11_and_true_on_shared_pref() {
        // Given
        mockkObject(Utils)
        every {
            getBuildVersion()
        } returns 30
        every {
            cacheManager.getPreviousState(any(), any())
        } returns true

        // When
        val result = viewModel.shouldShowBubbleTicker()

        // Then
        assertEquals(result, true)
    }

    @Test
    fun should_not_show_bubble_ticker_on_android_below_11_and_true_on_shared_pref() {
        // Given
        mockkObject(Utils)
        every {
            getBuildVersion()
        } returns 29
        every {
            cacheManager.getPreviousState(any(), any())
        } returns true

        // When
        val result = viewModel.shouldShowBubbleTicker()

        // Then
        assertEquals(result, false)
    }

    @Test
    fun should_not_show_bubble_ticker_on_android_11_and_false_on_shared_pref() {
        // Given
        mockkObject(Utils)
        every {
            getBuildVersion()
        } returns 30
        every {
            cacheManager.getPreviousState(any(), any())
        } returns false

        // When
        val result = viewModel.shouldShowBubbleTicker()

        // Then
        assertEquals(result, false)
    }

    @Test
    fun should_not_show_bubble_ticker_on_android_bewlo_11_and_false_on_shared_pref() {
        // Given
        mockkObject(Utils)
        every {
            getBuildVersion()
        } returns 29
        every {
            cacheManager.getPreviousState(any(), any())
        } returns false

        // When
        val result = viewModel.shouldShowBubbleTicker()

        // Then
        assertEquals(result, false)
    }

    @Test
    fun test_save_ticker_pref() {
        // Given
        mockkObject(Utils)
        every {
            getBuildVersion()
        } returns 30
        every {
            cacheManager.getPreviousState(any(), any())
        } returns false

        // When
        viewModel.saveBooleanCache(BUBBLE_TICKER_PREF_NAME, false)
        val result = viewModel.shouldShowBubbleTicker()

        // Then
        assertEquals(result, false)
    }

    @Test
    fun should_show_broadcast_fab_new_label_when_cache_true() {
        // Given
        every {
            cacheManager.getPreviousState(any(), any())
        } returns true

        // When
        val result = viewModel.getBooleanCache("${BROADCAST_FAB_LABEL_PREF_NAME}_${userSession.userId}")

        // Then
        assertEquals(result, true)
    }

    @Test
    fun test_save_broadcast_fab() {
        // Given
        every {
            cacheManager.getPreviousState(any(), any())
        } returns true

        // When
        viewModel.saveBooleanCache(
            "${BROADCAST_FAB_LABEL_PREF_NAME}_${userSession.userId}",
            true
        )
        val result = viewModel.getBooleanCache("${BROADCAST_FAB_LABEL_PREF_NAME}_${userSession.userId}")

        // Then
        assertEquals(result, true)
    }

    companion object {
        private const val exMessageId = "190378584"
        private const val shopId = "testShopId1"
        private const val SDK_INT = "SDK_INT"
        private val getChatList: ChatListPojo = FileUtil.parse(
            "/success_get_chat_list.json",
            ChatListPojo::class.java
        )
    }
}
