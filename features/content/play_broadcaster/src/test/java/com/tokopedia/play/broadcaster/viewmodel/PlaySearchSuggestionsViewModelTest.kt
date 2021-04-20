package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.model.ModelBuilder
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.PlaySearchSuggestionsViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.assertj.core.internal.RecursiveFieldByFieldComparator
import org.assertj.core.internal.TypeComparators
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 25/09/20
class PlaySearchSuggestionsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private val playBroadcastMapper = PlayBroadcastUiMapper()

    private val getProductInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)

    private val modelBuilder = ModelBuilder()
    private val mockProductsInEtalase by lazy { modelBuilder.buildProductsInEtalase() }

    private lateinit var viewModel: PlaySearchSuggestionsViewModel

    @Before
    fun setUp() {
        viewModel = PlaySearchSuggestionsViewModel(
                dispatcherProvider,
                getProductInEtalaseUseCase,
                mockk(relaxed = true),
                playBroadcastMapper
        )
    }

    @Test
    fun `when load suggestion is success, then it should return success`() = runBlockingTest(testDispatcher) {
        coEvery { getProductInEtalaseUseCase.executeOnBackground() } returns mockProductsInEtalase

        val keyword = "123"
        viewModel.loadSuggestionsFromKeyword(keyword)

        advanceUntilIdle()

        val result = viewModel.observableSuggestionList.getOrAwaitValue()

        val typeComparator = TypeComparators()
        typeComparator.put(CharSequence::class.java) { _, _ -> 0 }

        Assertions
                .assertThat(result)
                .usingComparatorForType(
                        RecursiveFieldByFieldComparator(emptyMap(), typeComparator) as Comparator<SearchSuggestionUiModel>,
                        SearchSuggestionUiModel::class.java
                )
                .isEqualToComparingFieldByFieldRecursively(
                        NetworkResult.Success(
                                playBroadcastMapper.mapSearchSuggestionList(keyword, mockProductsInEtalase)
                        )
                )
    }

    @Test
    fun `when load suggestion is failed, then it should return failed`() = runBlockingTest(testDispatcher) {
        val error = IllegalStateException()

        coEvery { getProductInEtalaseUseCase.executeOnBackground() } throws error

        val keyword = "123"
        viewModel.loadSuggestionsFromKeyword(keyword)

        advanceUntilIdle()

        val result = viewModel.observableSuggestionList.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
    }

}
 */