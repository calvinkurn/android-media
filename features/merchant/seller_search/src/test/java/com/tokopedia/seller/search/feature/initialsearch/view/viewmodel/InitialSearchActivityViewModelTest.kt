package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import org.junit.Test

class InitialSearchActivityViewModelTest: InitialSearchActivityViewModelTestFixture() {

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
}