package com.tokopedia.seller.search.suggestionsearch

import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers

class SuggestionSearchComposeTest : SuggestionSearchComposeTestFixture() {

    @Test
    fun `when showLoading, then should isLoadingState is success`() {
        viewModel.showLoading()

        assertCollectingUiState {
            Assert.assertEquals(true, it.first().isLoadingState)
        }
    }

    @Test
    fun `given onGetSellerSearch_thenReturn when fetchSellerSearch, then should return success`() {
        onGetSellerSearch_thenReturn()

        viewModel.fetchSellerSearch(
            keyword = "baju",
            shopId = "1243"
        )

        verifySuccessGetSellerSearchUseCaseCaseCalled()

        assertCollectingUiState {
            Assert.assertEquals(
                emptyList<BaseSuggestionSearchSeller>(),
                it.first().suggestionSellerSearchList
            )
            Assert.assertEquals(false, it.first().isDismissedKeyboard)
            Assert.assertEquals(false, it.first().isLoadingState)
            Assert.assertEquals(null, it.first().throwable)
        }
    }

    @Test
    fun `when onGetSellerSearch_thenReturn when fetchSellerSearch, then should return fail`() {
        val illegalStateException = IllegalStateException("server error")

        onGetSellerSearch_thenError(illegalStateException)

        viewModel.fetchSellerSearch(
            keyword = ArgumentMatchers.anyString(),
            shopId = ArgumentMatchers.anyString()
        )
        assertCollectingUiState {
            Assert.assertEquals(
                emptyList<BaseSuggestionSearchSeller>(),
                it.first().suggestionSellerSearchList
            )
            Assert.assertEquals(false, it.first().isDismissedKeyboard)
            Assert.assertEquals(false, it.first().isLoadingState)
            Assert.assertEquals(
                illegalStateException.localizedMessage,
                it.first().throwable!!.localizedMessage
            )
        }
    }

    @Test
    fun `given onInsertSuccessSearch_thenReturn when insertSearchSeller should return success`() {
        onInsertSuccessSearch_thenReturn()

        viewModel.insertSearchSeller(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt()
        )

        verifyInsertSuccessSearchUseCaseCalled()
        assertCollectingUiState {
            Assert.assertEquals(
                emptyList<BaseSuggestionSearchSeller>(),
                it.first().suggestionSellerSearchList
            )
            Assert.assertEquals(true, it.first().isDismissedKeyboard)
            Assert.assertEquals(false, it.first().isLoadingState)
            Assert.assertEquals(
                null,
                it.first().throwable
            )
        }
    }

    @Test
    fun `given onInsertSuccessSearch_thenError when insertSearchSeller should return failed`() {
        val illegalStateException = IllegalStateException("server error")
        onInsertSuccessSearch_thenError(illegalStateException)

        viewModel.insertSearchSeller(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt()
        )

        assertCollectingUiState {
            Assert.assertEquals(
                emptyList<BaseSuggestionSearchSeller>(),
                it.first().suggestionSellerSearchList
            )
            Assert.assertEquals(true, it.first().isDismissedKeyboard)
            Assert.assertEquals(false, it.first().isLoadingState)
            Assert.assertEquals(
                illegalStateException.localizedMessage,
                it.first().throwable!!.message
            )
        }
    }
}
