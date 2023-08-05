package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class InitialSearchActivityViewModelTest : InitialSearchActivityViewModelTestFixture() {

    @Test
    fun `when get placeholder success should set live data success`() = runTest {
        val placeholder = "a placeholder"

        onGetSearchPlaceholder_thenReturn(placeholder)

        viewModel.getSearchPlaceholder()

        advanceUntilIdle()

        verifyGetSearchPlaceholderSuccess(expectedPlaceholder = placeholder)
    }

    @Test
    fun `when get placeholder error should set live data fail`() = runTest {
        val error = NullPointerException()

        onGetSearchPlaceholder_thenReturn(error)

        viewModel.getSearchPlaceholder()

        advanceUntilIdle()

        verifyGetSearchPlaceholderError(expectedError = error)
    }

    @Test
    fun `when get typing search success should set live data success`() {
        val expectedKeyword = "baju baru"
        val expectedKeywordList = listOf("ba", "baju", "baju ba", "baju baru")
        var actualKeyword = ""

        runTest {
            val scope = CoroutineScope(coroutineTestRule.dispatchers.coroutineDispatcher)
            val collectorJob = scope.launch {
                viewModel.queryChannel.collectLatest {
                    actualKeyword = it
                }
            }

            for (keyword in expectedKeywordList) {
                viewModel.getTypingSearch(keyword)
            }

            advanceUntilIdle()

            collectorJob.cancel()
        }

        verifyGetTypingSearchSuccess(expectedKeyword)
        Assert.assertEquals(expectedKeyword, actualKeyword)
    }
}
