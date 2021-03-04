package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import org.junit.Test

class InitialSearchActivityViewModelTest : InitialSearchActivityViewModelTestFixture() {

    @Test
    fun `when get placeholder success should set live data success`() {
        val placeholder = "a placeholder"

        onGetSearchPlaceholder_thenReturn(placeholder)

        viewModel.getSearchPlaceholder()

        verifyGetSearchPlaceholderSuccess(expectedPlaceholder = placeholder)
    }

    @Test
    fun `when get placeholder error should set live data fail`() {
        val error = NullPointerException()

        onGetSearchPlaceholder_thenReturn(error)

        viewModel.getSearchPlaceholder()

        verifyGetSearchPlaceholderError(expectedError = error)
    }

    @Test
    fun `when get typing search success should set live data success`() {
        coroutineTestRule.runBlockingTest {
            val resultKeyword = "baju baru"
            val expectedKeywordList = listOf("ba", "baju", "baju ba", "baju baru")

            for (keyword in expectedKeywordList) {
                viewModel.getTypingSearch(keyword)
            }

            advanceTimeBy(300)

            verifyGetTypingSearchSuccess(resultKeyword)
        }
    }

}