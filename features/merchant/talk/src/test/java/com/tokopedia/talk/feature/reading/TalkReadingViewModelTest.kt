package com.tokopedia.talk.feature.reading

import android.accounts.NetworkErrorException
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponseWrapper
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.data.model.TalkGoToWrite
import com.tokopedia.talk.feature.reading.data.model.TalkReadingCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Exception

class TalkReadingViewModelTest : TalkReadingViewModelTestFixture() {

    @Test
    fun `when getDiscussionAggregate should execute expected use case and get expected data`() {
        val response = DiscussionAggregateResponse()
        val productId = "15267029"
        val shopId = "480749"

        onGetDiscussionAggregate_thenReturn(response)

        viewModel.getDiscussionAggregate(productId, shopId)

        val expectedResponse = Success(response)

        verifyGetDiscussionAggregateUseCaseExecuted()
        verifyDiscussionAggregateEquals(expectedResponse)
    }

    @Test
    fun `when getDiscussionAggregate fails should execute expected use case and fail with expected exception`() {
        val productId = "15267029"
        val shopId = "480749"
        val exception = NetworkErrorException()

        onGetDiscussionAggregateFail_thenReturn(exception)

        viewModel.getDiscussionAggregate(productId, shopId)

        verifyGetDiscussionAggregateUseCaseExecuted()
        verifyDiscussionAggregateErrorEquals(Fail(exception))
    }

    @Test
    fun `when getDiscussionData should execute expected use case and get expected data`() {
        val response = DiscussionDataResponseWrapper()
        val productId = "15267029"
        val shopId = "480749"
        val page = 0
        val limit = 10
        val sortOption = ""
        val categories = ""

        onGetDiscussionData_thenReturn(response)

        viewModel.getDiscussionData(productId, shopId, page, limit, sortOption, categories)

        val expectedResponse = Success(response)

        verifyGetDiscussionDataUseCaseExecuted()
        verifyDiscussionDataEquals(expectedResponse)
    }

    @Test
    fun `when getDiscussionData fail should execute expected use case and fail with expected exception`() {
        val productId = "15267029"
        val shopId = "480749"
        val page = 0
        val limit = 10
        val sortOption = ""
        val categories = ""
        val exception = NetworkErrorException()

        onGetDiscussionDataFail_thenReturn(exception)

        viewModel.getDiscussionData(productId, shopId, page, limit, sortOption, categories)

        verifyGetDiscussionDataUseCaseExecuted()
        verifyDiscussionDataErrorEquals(Fail(exception))
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
    fun `when updateSelectedCategory for missing category should do nothing`() {
        val categories = listOf(
                TalkReadingCategory("stok", "Stok", false),
                TalkReadingCategory("lainnya", "Lainnya", true) )

        viewModel.updateCategories(categories)
        viewModel.updateSelectedCategory("some category", false)

        verifyCategoriesEqual(categories)
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
        val sortOptions = listOf(SortOption.SortByInformativeness(), SortOption.SortByTime())

        viewModel.updateSortOptions(sortOptions)

        verifySortOptionsEquals(sortOptions)
    }

    @Test
    fun `when updateSelectedSort should update element in _sortOptions`() {
        val sortOptions = listOf(SortOption.SortByInformativeness(), SortOption.SortByTime())

        viewModel.updateSortOptions(sortOptions)
        viewModel.updateSelectedSort(sortOptions.last())

        val expectedSortOptions = listOf(SortOption.SortByInformativeness(isSelected = false), SortOption.SortByTime(isSelected = true))
        verifySortOptionsEquals(expectedSortOptions)
    }

    @Test
    fun `when data is null should do nothing`() {
        viewModel.updateSelectedSort(SortOption.SortByTime())
        viewModel.unselectAllCategories()
    }

    @Test
    fun `when data is null updateSelectedCategory should do nothing`() {
        viewModel.updateSelectedCategory("some category", false)
    }

    @Test
    fun `when resetSortOptions should set sortOptions to default value`() {
        val sortOptions = listOf(SortOption.SortByInformativeness(), SortOption.SortByTime())

        viewModel.updateSortOptions(sortOptions)
        viewModel.updateSelectedSort(sortOptions.last())
        viewModel.resetSortOptions()

        verifySortOptionsEquals(sortOptions)
    }

    @Test
    fun `when data is null resetSortOptions should do nothing`() {
        viewModel.resetSortOptions()
    }

    @Test
    fun `when updateLastAction should set to expected value`() {
        val lastAction = TalkGoToWrite
        viewModel.updateLastAction(lastAction)
        assertEquals(lastAction, viewModel.talkLastAction)
    }

    @Test
    fun `when setSuccess should set to expected value`() {

    }


    private fun verifyDiscussionAggregateEquals(expectedResponse: Success<DiscussionAggregateResponse>) {
        viewModel.discussionAggregate.verifySuccessEquals(expectedResponse)
    }

    private fun verifyDiscussionDataEquals(expectedResponse: Success<DiscussionDataResponseWrapper>) {
        viewModel.discussionData.verifySuccessEquals(expectedResponse)
    }

    private fun verifyDiscussionAggregateErrorEquals(expectedResponse: Fail) {
        viewModel.discussionAggregate.verifyErrorEquals(expectedResponse)
    }

    private fun verifyDiscussionDataErrorEquals(expectedResponse: Fail) {
        viewModel.discussionData.verifyErrorEquals(expectedResponse)
    }

    private fun verifyCategoriesEqual(categories: List<TalkReadingCategory>) {
        viewModel.filterCategories.verifyValueEquals(categories)
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
        viewModel.sortOptions.verifyValueEquals(sortOptions)
    }

    private fun onGetDiscussionAggregate_thenReturn(discussionAggregateResponse: DiscussionAggregateResponse) {
        coEvery { getDiscussionAggregateUseCase.executeOnBackground() } returns discussionAggregateResponse
    }

    private fun onGetDiscussionData_thenReturn(discussionDataResponseWrapper: DiscussionDataResponseWrapper) {
        coEvery { getDiscussionDataUseCase.executeOnBackground() } returns discussionDataResponseWrapper
    }

    private fun onGetDiscussionAggregateFail_thenReturn(exception: Exception) {
        coEvery { getDiscussionAggregateUseCase.executeOnBackground() } throws exception
    }

    private fun onGetDiscussionDataFail_thenReturn(exception: Exception) {
        coEvery { getDiscussionDataUseCase.executeOnBackground() } throws exception
    }

    private fun verifyGetDiscussionAggregateUseCaseExecuted() {
        coVerify { getDiscussionAggregateUseCase.executeOnBackground() }
    }

    private fun verifyGetDiscussionDataUseCaseExecuted() {
        coVerify { getDiscussionDataUseCase.executeOnBackground() }
    }
}