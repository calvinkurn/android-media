package com.tokopedia.talk.feature.reading

import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.DiscussionDataResponseWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class TalkReadingViewModelTest : TalkReadingViewModelTestFixture() {

    @Test
    fun `when getDiscussionAggregate should execute expected use case and get expected data`() {

    }

    @Test
    fun `when getDiscussionAggregate should execute expected use case and fail with expected exception`() {

    }

    @Test
    fun `when getDiscussionData should execute expected use case and get expected data`() {

    }

    @Test
    fun `when getDiscussionData should execute expected use case and fail with expected exception`() {

    }

    @Test
    fun `when updateCategories should set _filterCategories with expected value`() {

    }

    @Test
    fun `when updateSelectedCategory should set element in _filterCategories with expected value`() {

    }

    @Test
    fun `when unselectAllCategories should update all elements in _filterCategories to unselected`() {

    }

    @Test
    fun `when updateSortOptions should update value in _sortOptions`() {

    }

    @Test
    fun `when updateSelectedSort should update element in _sortOptions`() {

    }

    private fun onGetDiscussionAggregate_thenReturn(discussionAggregateResponse: DiscussionAggregateResponse) {
        coEvery { getDiscussionAggregateUseCase.executeOnBackground() } returns discussionAggregateResponse
    }

    private fun onGetDiscussionData_thenReturn(discussionDataResponseWrapper: DiscussionDataResponseWrapper) {
        coEvery { getDiscussionDataUseCase.executeOnBackground() } returns discussionDataResponseWrapper
    }

    private fun verifyGetDiscussionAggregateUseCaseExecuted() {
        coVerify { getDiscussionAggregateUseCase.executeOnBackground() }
    }

    private fun verifyGetDiscussionDataUseCaseExecuted() {
        coVerify { getDiscussionDataUseCase.executeOnBackground() }
    }
}