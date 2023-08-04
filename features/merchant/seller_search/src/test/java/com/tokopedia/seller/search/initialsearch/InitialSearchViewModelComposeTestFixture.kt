package com.tokopedia.seller.search.initialsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.domain.model.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.domain.usecase.DeleteSuggestionHistoryUseCase
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiState
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchComposeViewModel
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.seller.search.feature.suggestion.domain.usecase.InsertSuccessSearchUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
abstract class InitialSearchViewModelComposeTestFixture {

    @RelaxedMockK
    lateinit var getSellerSearchUseCase: GetSellerSearchUseCase

    @RelaxedMockK
    lateinit var deleteSuggestionHistoryUseCase: DeleteSuggestionHistoryUseCase

    @RelaxedMockK
    lateinit var insertSellerSearchUseCase: InsertSuccessSearchUseCase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = UnconfinedTestRule()

    lateinit var viewModel: InitialSearchComposeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = InitialSearchComposeViewModel(
            CoroutineTestDispatchersProvider,
            getSellerSearchUseCase,
            deleteSuggestionHistoryUseCase,
            insertSellerSearchUseCase
        )
    }

    protected fun assertCollectingUiState(block: (List<InitialSearchUiState>) -> Unit) {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val testCollectorList = mutableListOf<InitialSearchUiState>()
        val uiStateCollectorJob = scope.launch {
            viewModel.uiState.toList(testCollectorList)
        }
        block.invoke(testCollectorList)
        uiStateCollectorJob.cancel()
    }

    protected fun onDeleteSuggestionHistory_thenError(exception: Exception) {
        coEvery { deleteSuggestionHistoryUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    protected fun onGetSellerSearch_thenError(exception: Exception) {
        coEvery { getSellerSearchUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    protected fun onDeleteSuggestionHistory_thenReturn() {
        coEvery { deleteSuggestionHistoryUseCase.executeOnBackground() } returns DeleteHistoryResponse.DeleteHistory()
    }

    protected fun onGetSellerSearch_thenReturn() {
        coEvery { getSellerSearchUseCase.executeOnBackground() } returns SellerSearchResponse.SellerSearch()
    }

    protected fun verifySuccessDeleteSuggestionHistoryUseCaseCalled() {
        coVerify { deleteSuggestionHistoryUseCase.executeOnBackground() }
    }

    protected fun onInsertSuccessSearch_thenReturn() {
        coEvery { insertSellerSearchUseCase.executeOnBackground() } returns SuccessSearchResponse.SuccessSearch()
    }

    protected fun onInsertSuccessSearch_thenError(exception: Exception) {
        coEvery { insertSellerSearchUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    protected fun verifySuccessGetSellerSearchUseCaseCaseCalled() {
        coVerify { getSellerSearchUseCase.executeOnBackground() }
    }

    protected fun verifyInsertSuccessSearchUseCaseCalled() {
        coVerify { insertSellerSearchUseCase.executeOnBackground() }
    }
}
