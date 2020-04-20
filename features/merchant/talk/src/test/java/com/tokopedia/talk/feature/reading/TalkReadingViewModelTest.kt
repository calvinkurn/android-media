package com.tokopedia.talk.feature.reading

import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.DiscussionDataResponseWrapper
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.data.model.TalkReadingCategory
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
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
        val categories = listOf(
                TalkReadingCategory("stok", "Stok", false),
                TalkReadingCategory("lainnya", "Lainnya", true) )

        viewModel.updateCategories(categories)

        verifyCategoriesEqual(categories)
    }

    @Test
    fun `when updateSelectedCategory should set element in _filterCategories with expected value`() {
        val categories = listOf(
                TalkReadingCategory("stok", "Stok", false),
                TalkReadingCategory("lainnya", "Lainnya", true) )

        viewModel.updateCategories(categories)
        viewModel.updateSelectedCategory("Lainnya (5)", false)

        val expectedCategory = TalkReadingCategory("lainnya", "Lainnya", false)
        verifyCategoryEquals(expectedCategory)
    }

    @Test
    fun `when unselectAllCategories should update all elements in _filterCategories to unselected`() {
        val categories = listOf(
                TalkReadingCategory("stok", "Stok", false),
                TalkReadingCategory("lainnya", "Lainnya", true) )

        viewModel.updateCategories(categories)
        viewModel.unselectAllCategories()

        verifyAllCategoriesAreUnselected()
    }

    @Test
    fun `when updateSortOptions should update value in _sortOptions`() {
        val sortOptions = listOf(SortOption.SortByInformativeness(), SortOption.SortByLike(), SortOption.SortByTime())

        viewModel.updateSortOptions(sortOptions)

        verifySortOptionsEquals(sortOptions)
    }

    @Test
    fun `when updateSelectedSort should update element in _sortOptions`() {
        val sortOptions = listOf(SortOption.SortByInformativeness(), SortOption.SortByLike(), SortOption.SortByTime())

        viewModel.updateSortOptions(sortOptions)
        viewModel.updateSelectedSort(sortOptions.last())

        val expectedSortOptions = listOf(SortOption.SortByInformativeness(isSelected = false), SortOption.SortByLike(), SortOption.SortByTime(isSelected = true))
        verifySortOptionsEquals(expectedSortOptions)
    }

    private fun verifyCategoriesEqual(categories: List<TalkReadingCategory>) {
        viewModel.filterCategories.value?.forEachIndexed { index, talkReadingCategory ->
            assertEquals(categories[index], talkReadingCategory)
        }
    }

    private fun verifyAllCategoriesAreUnselected() {
        viewModel.filterCategories.value?.forEach {
            assertEquals(false, it.isSelected)
        }
    }

    private fun verifyCategoryEquals(expectedCategory: TalkReadingCategory) {
        val actualCategory = viewModel.filterCategories.value?.last()
        assertEquals(expectedCategory, actualCategory)
    }

    private fun verifySortOptionsEquals(sortOptions: List<SortOption>) {
        viewModel.sortOptions.value?.forEachIndexed { index, sortOption ->
            assertEquals(sortOptions[index], sortOption)
        }
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