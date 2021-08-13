package com.tokopedia.talk.feature.inbox

import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.*
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.coEvery
import io.mockk.coVerify
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

        viewModel.setFilter(expectedFilter, isSellerView = true, shouldTrack = true)

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

        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter(), isSellerView = false, shouldTrack = true)
        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter(), isSellerView = true, shouldTrack = true)

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

        viewModel.updatePage(expectedPage)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    @Test
    fun `when resetPage() should get data with expected page`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.resetPage()

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox, expectedPage, expectedFilter)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    private fun verifyTalkInboxListUseCaseCalled() {
        coVerify { talkInboxListUseCase.executeOnBackground() }
    }

    private fun onGetInboxListSuccess_thenReturn(discussionInboxResponseWrapper: DiscussionInboxResponseWrapper) {
        coEvery { talkInboxListUseCase.executeOnBackground() } returns discussionInboxResponseWrapper
    }

    private fun verifyInboxListValueEquals(data: TalkInboxViewState.Success<DiscussionInbox>) {
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