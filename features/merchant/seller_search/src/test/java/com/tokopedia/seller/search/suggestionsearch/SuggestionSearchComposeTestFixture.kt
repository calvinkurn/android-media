package com.tokopedia.seller.search.suggestionsearch

import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.seller.search.feature.suggestion.domain.usecase.InsertSuccessSearchUseCase
import com.tokopedia.seller.search.feature.suggestion.view.model.compose.SuggestionSearchUiState
import com.tokopedia.seller.search.feature.suggestion.view.viewmodel.SuggestionSearchComposeViewModel
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
abstract class SuggestionSearchComposeTestFixture {

    @RelaxedMockK
    lateinit var getSellerSearchUseCase: GetSellerSearchUseCase

    @RelaxedMockK
    lateinit var insertSellerSearchUseCase: InsertSuccessSearchUseCase

    lateinit var viewModel: SuggestionSearchComposeViewModel

    @get:Rule
    val rule = UnconfinedTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SuggestionSearchComposeViewModel(
            CoroutineTestDispatchersProvider,
            getSellerSearchUseCase,
            insertSellerSearchUseCase
        )
    }

    protected fun assertCollectingUiState(block: (List<SuggestionSearchUiState>) -> Unit) {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val testCollectorList = mutableListOf<SuggestionSearchUiState>()
        val uiStateCollectorJob = scope.launch {
            viewModel.uiState.toList(testCollectorList)
        }
        block.invoke(testCollectorList)
        uiStateCollectorJob.cancel()
    }

    protected fun onGetSellerSearch_thenError(exception: Exception) {
        coEvery { getSellerSearchUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    protected fun onGetSellerSearch_thenReturn() {
        coEvery { getSellerSearchUseCase.executeOnBackground() } returns SellerSearchResponse.SellerSearch()
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
