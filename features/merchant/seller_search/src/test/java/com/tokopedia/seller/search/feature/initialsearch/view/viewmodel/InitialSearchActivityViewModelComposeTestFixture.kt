package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.seller.search.common.domain.GetSellerSearchPlaceholderUseCase
import com.tokopedia.seller.search.common.domain.model.SellerSearchPlaceholderResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule

abstract class InitialSearchActivityViewModelComposeTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private lateinit var getSearchPlaceholderUseCase: GetSellerSearchPlaceholderUseCase
    protected lateinit var viewModel: InitialSearchActivityComposeViewModel

    @Before
    fun setUp() {
        getSearchPlaceholderUseCase = mockk()

        viewModel = InitialSearchActivityComposeViewModel(
            getSearchPlaceholderUseCase,
            coroutineTestRule.dispatchers
        )
    }

    protected fun assertCollectingUiState(block: (List<GlobalSearchUiState>) -> Unit) {
        val scope = CoroutineScope(coroutineTestRule.dispatchers.coroutineDispatcher)
        val testCollectorList = mutableListOf<GlobalSearchUiState>()
        val uiStateCollectorJob = scope.launch {
            viewModel.globalSearchUiState.toList(testCollectorList)
        }
        block.invoke(testCollectorList)
        uiStateCollectorJob.cancel()
    }

    protected fun assertCollectingUiEvent(block: (List<GlobalSearchUiEvent>) -> Unit) {
        val scope = CoroutineScope(coroutineTestRule.dispatchers.coroutineDispatcher)
        val testCollectorList = mutableListOf<GlobalSearchUiEvent>()
        val uiStateCollectorJob = scope.launch {
            viewModel.uiEffect.toList(testCollectorList)
        }
        block.invoke(testCollectorList)
        uiStateCollectorJob.cancel()
    }

    protected fun onGetSearchPlaceholder_thenReturn(placeholder: String) {
        val response = SellerSearchPlaceholderResponse(
            SellerSearchPlaceholderResponse.SellerSearchPlaceholder(placeholder)
        )
        coEvery { getSearchPlaceholderUseCase.executeOnBackground() } returns response
    }

    protected fun onGetSearchPlaceholder_thenReturn(error: Throwable) {
        coEvery { getSearchPlaceholderUseCase.executeOnBackground() } throws error
    }

    protected fun verifyGetSearchPlaceholderUseCaseCalled() {
        coVerify { getSearchPlaceholderUseCase.executeOnBackground() }
    }
}
