package com.tokopedia.tokofood.search.searchbar

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class SearchContainerViewModelTest : SearchContainerViewModelTestFixture() {

    @Test
    fun `when typing search keyword should set live data success`() {
        val expectedKeyword = "makanan"
        val expectedKeywordList = listOf("ma", "maka", "makana", "makanan")
        var actualKeyword = ""
        runTest {
            val collectorJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.keywordResult.collectLatest {
                    actualKeyword = it
                }
            }
            for (keyword in expectedKeywordList) {
                viewModel.setKeyword(keyword)
            }
            advanceTimeBy(500L)

            collectorJob.cancel()
        }

        Assert.assertEquals(expectedKeyword, actualKeyword)
    }
}
