package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.robot.*
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
import com.tokopedia.play.broadcaster.util.*
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by jegul on 25/09/20
 */

class PlaySearchSuggestionsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchers

    private val playBroadcastMapper = PlayBroadcastUiMapper(
            TestHtmlTextTransformer()
    )

    private val responseBuilder = PlayBroadcasterResponseBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given suggestions api success, when load suggestions from a keyword, then it should correct return suggestion list`() {
        val mockUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)
        val idNameList = listOf(
                "1" to "pizza tomat",
                "2" to "pizza keju",
                "3" to "pizza nanas"
        )
        val keyword = "pizza"

        val mockResponse = responseBuilder.buildGetProductsInEtalaseResponse(idNameList)

        givenPlaySearchSuggestionsViewModel(
                getProductsInEtalaseUseCase = mockUseCase
        ) {
            coEvery { mockUseCase.executeOnBackground() } returns mockResponse
        }.andThen {
            loadSuggestions(keyword)
        }.andWhen {
            getSuggestionList()
        }.thenVerify {
            it.assertWhenSuccess { actual ->
                actual.isEqualToIgnoringFields(playBroadcastMapper.mapSearchSuggestionList(keyword, mockResponse), SearchSuggestionUiModel::spannedSuggestion)
            }
        }
    }

    @Test
    fun `given suggestions api error, when load suggestions from a keyword, then it should return failure`() {
        val mockUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)
        val keyword = "pizza"

        val exception = IllegalArgumentException("Error get suggestion")

        givenPlaySearchSuggestionsViewModel(
                getProductsInEtalaseUseCase = mockUseCase
        ) {
            coEvery { mockUseCase.executeOnBackground() } throws exception
        }.andThen {
            loadSuggestions(keyword)
        }.andWhen {
            getSuggestionList()
        }.thenVerify {
            it.assertWhenFailed { actual ->
                actual.isEqualToComparingFieldByField(exception)
            }
        }
    }

    @Test
    fun `when not load suggestions from a keyword, then it should be error`() {
        givenPlaySearchSuggestionsViewModel(
        ).andMaybeWhen {
            getSuggestionList()
        }.thenExpectThrowable {
            it.isErrorType(TimeoutException::class.java)
        }
    }
}