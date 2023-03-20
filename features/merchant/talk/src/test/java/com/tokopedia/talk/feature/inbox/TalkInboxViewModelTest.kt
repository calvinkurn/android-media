package com.tokopedia.talk.feature.inbox

import com.tokopedia.config.GlobalConfig
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.DiscussionInbox
import com.tokopedia.talk.feature.inbox.data.DiscussionInboxResponseWrapper
import com.tokopedia.talk.feature.inbox.data.SmartReplyTalkDecommissionConfig
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.data.TalkInboxViewState
import com.tokopedia.talk.feature.inbox.data.TickerConfig
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test

class TalkInboxViewModelTest : TalkInboxViewModelTestFixture() {

    @Test
    fun `when setInboxType() should get data with first page & no filter`() {
        val expectedInboxType = TalkInboxTab.TalkBuyerInboxTab()
        val expectedData = DiscussionInboxResponseWrapper(discussionInbox = DiscussionInbox(buyerUnread = 1, sellerUnread = 10))
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.inboxList.observeForever { }
        viewModel.setInboxType(expectedInboxType.tabParam)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
        verifyUnreadCount(expectedData.discussionInbox.buyerUnread)
    }

    @Test
    fun `when setFilter() should get data with first page and expected filter`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxReadFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.inboxList.observeForever { }
        viewModel.setFilter(expectedFilter, isSellerView = true, shouldTrack = false)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
        verifyFilterEquals(expectedFilter.filterParam)
    }

    @Test
    fun `when setFilter() with existing filter should make filter blank`() {
        val expectedData = DiscussionInboxResponseWrapper(discussionInbox = DiscussionInbox(unrespondedTotal = 1))
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.inboxList.observeForever { }
        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter(), isSellerView = false, shouldTrack = true)
        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter(), isSellerView = true, shouldTrack = true)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
        verifyFilterEquals(expectedFilter.filterParam)
        verifyUnrespondedCount(expectedData.discussionInbox.unrespondedTotal)
    }

    @Test
    fun `when setFilter() but shouldTrack = false with existing filter should make filter blank`() {
        val expectedData = DiscussionInboxResponseWrapper(discussionInbox = DiscussionInbox(unrespondedTotal = 1))
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.inboxList.observeForever { }
        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter(), isSellerView = false, shouldTrack = false)
        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter(), isSellerView = true, shouldTrack = false)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
        verifyFilterEquals(expectedFilter.filterParam)
        verifyUnrespondedCount(expectedData.discussionInbox.unrespondedTotal)
    }

    @Test
    fun `when updatePage() should get data with expected page`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE + 1

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.inboxList.observeForever { }
        viewModel.updatePage(expectedPage)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    @Test
    fun `when updatePage() for old shop type should get data with expected page`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE + 1

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.inboxList.observeForever { }
        viewModel.setInboxType(TalkInboxTab.SHOP_OLD)
        viewModel.updatePage(expectedPage)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    @Test
    fun `when updatePage() error should return expected error`() {
        val expectedData = Throwable()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE + 1

        onGetInboxListError_thenReturn(expectedData)

        viewModel.inboxList.observeForever { }
        viewModel.updatePage(expectedPage)

        val expectedLiveDataValue = TalkInboxViewState.Fail<DiscussionInbox>(expectedData, expectedPage)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    @Test
    fun `when resetPage() should get data with expected page`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.inboxList.observeForever { }
        viewModel.resetPage()

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    @Test
    fun `getSmartReplyDecommissionConfig should success map config from remote config when on sellerapp`() {
        val expected = SmartReplyTalkDecommissionConfig.InboxPage(
            isSmartReviewDisabled = true,
            tickerConfig = TickerConfig(title = "Dummy Title", text = "Dummy text")
        )
        val partialRemoteConfigJson = """
            {
                "inbox_page": {
                    "is_smart_review_disabled": true,
                    "ticker_config": {
                        "title": "Dummy Title",
                        "text": "Dummy text"
                    }
                }
            }
        """.trimIndent()
        mockkStatic(GlobalConfig::class) {
            every {
                GlobalConfig.isSellerApp()
            } returns true

            every {
                firebaseRemoteConfig.getString("android_seller_app_smart_reply_talk_decommission_config")
            } returns partialRemoteConfigJson

            Assert.assertEquals(expected, viewModel.smartReplyDecommissionConfig.value)
        }
    }

    @Test
    fun `getSmartReplyDecommissionConfig should not map config from remote config when on customerapp`() {
        val expected = SmartReplyTalkDecommissionConfig.InboxPage(
            isSmartReviewDisabled = false,
            tickerConfig = TickerConfig(title = "", text = "")
        )
        mockkStatic(GlobalConfig::class) {
            every {
                GlobalConfig.isSellerApp()
            } returns false

            coVerify(inverse = true) {
                firebaseRemoteConfig.getString(any())
            }

            Assert.assertEquals(expected, viewModel.smartReplyDecommissionConfig.value)
        }
    }

    @Test
    fun `getSmartReplyDecommissionConfig should set smartReplyDecommissionConfig default config when JSON mapping is error`() {
        val expected = SmartReplyTalkDecommissionConfig.InboxPage(
            isSmartReviewDisabled = false,
            tickerConfig = TickerConfig(title = "", text = "")
        )
        val partialRemoteConfigJson = "this is invalid json"
        mockkStatic(GlobalConfig::class) {
            every {
                GlobalConfig.isSellerApp()
            } returns true

            every {
                firebaseRemoteConfig.getString("android_seller_app_smart_reply_talk_decommission_config")
            } returns partialRemoteConfigJson

            Assert.assertEquals(expected, viewModel.smartReplyDecommissionConfig.value)
        }
    }

    private fun verifyTalkInboxListUseCaseCalled() {
        coVerify { talkInboxListUseCase.executeOnBackground() }
    }

    private fun onGetInboxListSuccess_thenReturn(discussionInboxResponseWrapper: DiscussionInboxResponseWrapper) {
        coEvery { talkInboxListUseCase.executeOnBackground() } returns discussionInboxResponseWrapper
    }

    private fun onGetInboxListError_thenReturn(throwable: Throwable) {
        coEvery { talkInboxListUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyInboxListValueEquals(data: TalkInboxViewState<DiscussionInbox>) {
        viewModel.inboxList.verifyValueEquals(data)
    }

    private fun verifyFilterEquals(filter: String) {
        Assert.assertEquals(viewModel.getActiveFilter(), filter)
    }

    private fun verifyUnrespondedCount(unrespondedCount: Long) {
        Assert.assertEquals(viewModel.getUnrespondedCount(), unrespondedCount)
    }

    private fun verifyUnreadCount(unread: Long) {
        Assert.assertEquals(viewModel.getUnreadCount(), unread)
    }
}
