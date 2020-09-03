package com.tokopedia.talk.feature.inbox

import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.DiscussionInboxResponseWrapper
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.data.TalkInboxViewState
import com.tokopedia.talk.feature.inbox.presentation.activity.TalkInboxActivity
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.util.verifyValueEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import java.lang.Exception

class TalkInboxViewModelTest : TalkInboxViewModelTestFixture() {

    @Test
    fun `when setInboxType() should get data with first page & no filter`() {
        val expectedInboxType = TalkInboxTab.TalkBuyerInboxTab()
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.setInboxType(expectedInboxType.tabParam)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox.inbox.map { TalkInboxUiModel(it) }, expectedPage, expectedFilter, expectedData.discussionInbox.hasNext)

        verifyTalkInboxListUseCaseCalled()
        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    @Test
    fun `when setFilter() should get data with first page and expected filter`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxReadFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.setFilter(expectedFilter)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox.inbox.map { TalkInboxUiModel(it) }, expectedPage, expectedFilter, expectedData.discussionInbox.hasNext)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    @Test
    fun `when setFilter() with existing filter should make filter blank`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter())
        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter())

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox.inbox.map { TalkInboxUiModel(it) }, expectedPage, expectedFilter, expectedData.discussionInbox.hasNext)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    @Test
    fun `when updatePage() should get data with expected page`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE + 1

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.updatePage(expectedPage)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox.inbox.map { TalkInboxUiModel(it) }, expectedPage, expectedFilter, expectedData.discussionInbox.hasNext)

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

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData.discussionInbox.inbox.map { TalkInboxUiModel(it) }, expectedPage, expectedFilter, expectedData.discussionInbox.hasNext)

        verifyTalkInboxListUseCaseCalled()
        verifyInboxListValueEquals(expectedLiveDataValue)
    }

    private fun verifyTalkInboxListUseCaseCalled() {
        coVerify { talkInboxListUseCase.executeOnBackground() }
    }

    private fun onGetInboxListSuccess_thenReturn(discussionInboxResponseWrapper: DiscussionInboxResponseWrapper) {
        coEvery { talkInboxListUseCase.executeOnBackground() } returns discussionInboxResponseWrapper
    }

    private fun verifyInboxListValueEquals(data: TalkInboxViewState.Success<List<TalkInboxUiModel>>) {
        viewModel.inboxList.verifyValueEquals(data)
    }



}