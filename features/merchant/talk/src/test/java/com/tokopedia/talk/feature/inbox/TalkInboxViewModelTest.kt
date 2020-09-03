package com.tokopedia.talk.feature.inbox

import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.DiscussionInboxResponseWrapper
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.data.TalkInboxViewState
import com.tokopedia.talk.feature.inbox.presentation.activity.TalkInboxActivity
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import java.lang.Exception

class TalkInboxViewModelTest : TalkInboxViewModelTestFixture() {

    @Test
    fun `when setInboxType() should get data with first page, no filter and specified inbox type`() {
        val expectedInboxType = TalkInboxTab.TalkBuyerInboxTab()
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxNoFilter()

        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.setInboxType(expectedInboxType.tabParam)

        verifyTalkInboxListUseCaseCalled()


    }

    @Test
    fun `when setFilter() should get data with first page and expected filter`() {
        val expectedData = DiscussionInboxResponseWrapper()
        val expectedFilter = TalkInboxFilter.TalkInboxReadFilter()
        val expectedPage = TalkConstants.DEFAULT_INITIAL_PAGE
        onGetInboxListSuccess_thenReturn(expectedData)

        viewModel.setFilter(expectedFilter)

        val expectedLiveDataValue = TalkInboxViewState.Success(expectedData, expectedPage, expectedFilter, expectedData.discussionInbox.hasNext)
        verifyTalkInboxListUseCaseCalled()

    }

    @Test
    fun `when setFilter() with existing filter should make filter blank`() {
        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter())
        viewModel.setFilter(TalkInboxFilter.TalkInboxReadFilter())
    }

    private fun verifyTalkInboxListUseCaseCalled() {
        coVerify { talkInboxListUseCase.executeOnBackground() }
    }

    private fun onGetInboxListSuccess_thenReturn(discussionInboxResponseWrapper: DiscussionInboxResponseWrapper) {
        coEvery { talkInboxListUseCase.executeOnBackground() } returns discussionInboxResponseWrapper
    }

    private fun onGetInboxListFail_thenReturn(exception: Exception) {
        coEvery { talkInboxListUseCase.executeOnBackground() } throws exception
    }

    private fun verify() {

    }



}