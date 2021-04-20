package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.seller.search.common.domain.GetSellerSearchPlaceholderUseCase
import com.tokopedia.seller.search.common.domain.model.SellerSearchPlaceholderResponse
import com.tokopedia.seller.search.common.domain.model.SellerSearchPlaceholderResponse.SellerSearchPlaceholder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule


open class InitialSearchActivityViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var getSearchPlaceholderUseCase: GetSellerSearchPlaceholderUseCase
    protected lateinit var viewModel: InitialSearchActivityViewModel

    @Before
    fun setUp() {
        getSearchPlaceholderUseCase = mockk()

        viewModel = InitialSearchActivityViewModel(
                getSearchPlaceholderUseCase,
                coroutineTestRule.dispatchers
        )
    }

    protected fun onGetSearchPlaceholder_thenReturn(placeholder: String) {
        val response = SellerSearchPlaceholderResponse(SellerSearchPlaceholder(placeholder))
        coEvery { getSearchPlaceholderUseCase.executeOnBackground() } returns response
    }

    protected fun onGetSearchPlaceholder_thenReturn(error: Throwable) {
        coEvery { getSearchPlaceholderUseCase.executeOnBackground() } throws error
    }

    protected fun verifyGetSearchPlaceholderSuccess(expectedPlaceholder: String) {
        val actualPlaceHolder = (viewModel.searchPlaceholder.value as? Success<String>)?.data
        assertEquals(expectedPlaceholder, actualPlaceHolder)
    }

    protected fun verifyGetSearchPlaceholderError(expectedError: Throwable) {
        val actualError = (viewModel.searchPlaceholder.value as? Fail)?.throwable
        assertEquals(expectedError::class.java.name, actualError?.let { it::class.java.name })
    }

    protected fun verifyGetTypingSearchSuccess(expectedKeyword: String) {
        val actualKeyword = viewModel.searchKeyword.value
        assertEquals(expectedKeyword, actualKeyword)
    }
}
