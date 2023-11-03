package com.tokopedia.seller.search.initialsearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import org.junit.Assert.assertEquals
import org.junit.Test

class InitialSearchViewModelComposeTest : InitialSearchViewModelComposeTestFixture() {

    @Test
    fun `given onGetSellerSearch_thenReturn when fetchSellerSearch, then should return success`() {
        // given
        onGetSellerSearch_thenReturn()

        viewModel.fetchSellerSearch("baju", shopId = "123456")

        verifySuccessGetSellerSearchUseCaseCaseCalled()

        assertCollectingUiState {
            assertEquals(emptyList<BaseInitialSearchSeller>(), it.first().initialStateList)
            assertEquals(false, it.first().isDismissKeyboard)
            assertEquals(emptyList<String>(), it.first().titleList)
            assertEquals(null, it.first().throwable)
        }
    }

    @Test
    fun `given onGetSellerSearch_thenError when fetchSellerSearch, then should return failed`() {
        // when
        val illegalStateException = IllegalStateException("server error")
        onGetSellerSearch_thenError(illegalStateException)

        viewModel.fetchSellerSearch("baju", shopId = "123456")

        verifySuccessGetSellerSearchUseCaseCaseCalled()

        assertCollectingUiState {
            assertEquals(emptyList<BaseInitialSearchSeller>(), it.first().initialStateList)
            assertEquals(false, it.first().isDismissKeyboard)
            assertEquals(emptyList<String>(), it.first().titleList)
            assertEquals(
                illegalStateException.localizedMessage,
                it.first().throwable!!.localizedMessage
            )
        }
    }

    @Test
    fun `given onInsertSuccessSearch_thenReturn when insertSearchSeller, then should return success`() {
        // given
        onInsertSuccessSearch_thenReturn()

        viewModel.insertSearchSeller("baju", id = "123abc", title = "baju baru", index = 2)

        verifyInsertSuccessSearchUseCaseCalled()

        assertCollectingUiState {
            assertEquals(true, it.first().isDismissKeyboard)
            assertEquals(emptyList<String>(), it.first().titleList)
            assertEquals(null, it.first().throwable)
        }
    }

    @Test
    fun `given onGetSellerSearch_thenError when insertSearchSeller, then should return failed`() {
        // given
        val illegalStateException = IllegalStateException("server error")
        onInsertSuccessSearch_thenError(illegalStateException)

        viewModel.insertSearchSeller("baju", id = "123abc", title = "baju baru", index = 2)

        verifyInsertSuccessSearchUseCaseCalled()

        assertCollectingUiState {
            assertEquals(true, it.first().isDismissKeyboard)
            assertEquals(emptyList<String>(), it.first().titleList)
            assertEquals(
                illegalStateException.localizedMessage,
                it.first().throwable!!.localizedMessage
            )
        }
    }

    @Test
    fun `given onDeleteSuggestionHistory_thenReturn when all deleteSuggestion, then should return success`() {
        // given
        val keyword = listOf("baju", "bebas ongkir", "power merchant", "wawasan toko")
        onDeleteSuggestionHistory_thenReturn()

        viewModel.deleteSuggestionSearch(keyword, null)

        verifySuccessDeleteSuggestionHistoryUseCaseCalled()

        assertCollectingUiState {
            assertEquals(emptyList<String>(), it.first().titleList)
            assertEquals(false, it.first().isDismissKeyboard)
            assertEquals(null, it.first().throwable)
        }
    }

    @Test
    fun `given onDeleteSuggestionHistory_thenReturn when deleteSuggestion particular position, then should return success`() {
        // given
        onDeleteSuggestionHistory_thenReturn()

        viewModel.deleteSuggestionSearch(listOf("bebas ongkir"), 0)

        verifySuccessDeleteSuggestionHistoryUseCaseCalled()

        assertCollectingUiState {
            assertEquals(emptyList<String>(), it.first().titleList)
            assertEquals(false, it.first().isDismissKeyboard)
            assertEquals(null, it.first().throwable)
        }
    }

    @Test
    fun `given onDeleteSuggestionHistory_thenError when deleteSuggestionSearch, then should return failed`() {
        // given
        val illegalStateException = IllegalStateException("server error")
        onDeleteSuggestionHistory_thenError(illegalStateException)

        viewModel.deleteSuggestionSearch(listOf("baju"), itemPosition = 0)

        verifySuccessDeleteSuggestionHistoryUseCaseCalled()

        assertCollectingUiState {
            assertEquals(false, it.first().isDismissKeyboard)
            assertEquals(emptyList<String>(), it.first().titleList)
            assertEquals(
                illegalStateException.localizedMessage,
                it.first().throwable!!.localizedMessage
            )
        }
    }
}
