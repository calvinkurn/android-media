package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEvent
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class InitialSearchActivityViewModelComposeTest :
    InitialSearchActivityViewModelComposeTestFixture() {

    @Test
    fun `given onGetSearchPlaceholder_thenReturn when getSearchPlaceholder, then should return success`() {
        // given
        val placeholder = "ketik prouk apa yang kamu cari"
        onGetSearchPlaceholder_thenReturn(placeholder)

        // when
        viewModel.getSearchPlaceholder()

        // then
        verifyGetSearchPlaceholderUseCaseCalled()
        assertCollectingUiState {
            Assert.assertEquals(placeholder, it.first().searchBarPlaceholder)
            Assert.assertEquals("", it.first().searchBarKeyword)
        }
    }

    @Test
    fun `given onGetSearchPlaceholder_thenReturn when getSearchPlaceholder, then should return fail`() {
        // given
        val illegalStateException = IllegalStateException("server error")
        onGetSearchPlaceholder_thenReturn(illegalStateException)

        // when
        viewModel.getSearchPlaceholder()

        // then
        verifyGetSearchPlaceholderUseCaseCalled()
        assertCollectingUiState {
            Assert.assertEquals("", it.first().searchBarPlaceholder)
            Assert.assertEquals("", it.first().searchBarKeyword)
        }
    }

    @Test
    fun `given OnKeywordTextChanged when onUiEffect success should return success`() {
        val resultKeyword = "baju baru"
        val expectedKeywordList = listOf("ba", "baju", "baju ba", "baju baru")

        runTest {
            for (keyword in expectedKeywordList) {
                viewModel.onUiEffect(GlobalSearchUiEvent.OnKeywordTextChanged(keyword))
            }

            advanceUntilIdle()

            assertCollectingUiState {
                Assert.assertEquals(resultKeyword, it.first().searchBarKeyword)
                Assert.assertEquals("", it.first().searchBarPlaceholder)
            }

            assertCollectingUiEvent {
                Assert.assertEquals(
                    GlobalSearchUiEvent.OnSearchResultKeyword(resultKeyword),
                    it.first()
                )
            }
        }
    }

    @Test
    fun `given OnKeyboardSearchSubmit when onUiEffect success should return success`() {
        val resultKeyword = "baju baru"

        viewModel.onUiEffect(GlobalSearchUiEvent.OnKeyboardSearchSubmit(resultKeyword))

        assertCollectingUiEvent {
            Assert.assertTrue(it.first() is GlobalSearchUiEvent.OnKeyboardSearchSubmit)
        }
    }
}
