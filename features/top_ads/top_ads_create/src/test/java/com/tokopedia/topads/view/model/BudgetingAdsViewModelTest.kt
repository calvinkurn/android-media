package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionSearchUseCase
import com.tokopedia.topads.domain.usecase.TopAdsGetBidSuggestionByProductIDsUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class BudgetingAdsViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: BudgetingAdsViewModel

    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val bidInfoUseCaseDefault: BidInfoUseCase = mockk(relaxed = true)
    private val topAdsGetBidSuggestionByProductIDsUseCase: TopAdsGetBidSuggestionByProductIDsUseCase = mockk(relaxed = true)
    private val topAdsImpressionPredictionUseCase: TopAdsImpressionPredictionSearchUseCase = mockk(relaxed = true)
    private val suggestionKeywordUseCase: SuggestionKeywordUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        viewModel = spyk(BudgetingAdsViewModel(rule.dispatchers,
            bidInfoUseCase, bidInfoUseCaseDefault, topAdsGetBidSuggestionByProductIDsUseCase, topAdsImpressionPredictionUseCase, suggestionKeywordUseCase))
    }


    @Test
    fun `check onEmpty invocation in getBidInfo`() {
        val expected = "empty"
        var actual = ""
        val data = listOf<TopadsBidInfo.DataItem>()
        val onEmpty: () -> Unit = {
            actual = "empty"
        }
        val onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit = {
            if (it.isEmpty()) {
                onEmpty()
            }
        }
        every {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            onSuccess.invoke(data)
        }
        viewModel.getBidInfo(
            suggestions = listOf(), onSuccess = onSuccess, onEmpty = onEmpty
        )
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `check onSuccess invocation in getBidInfo`() {
        val expected = "1000"
        var actual = "0"
        val bidInfoData: ResponseBidInfo.Result = ResponseBidInfo.Result(TopadsBidInfo(data =
        listOf(TopadsBidInfo.DataItem(suggestionBid = expected))))
        val onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit = {
            actual = it[0].suggestionBid

        }
        every {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            onSuccess.invoke(bidInfoData.topadsBidInfo.data)
        }

        viewModel.getBidInfo(
            suggestions = listOf(), onSuccess = onSuccess, onEmpty = {}
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check exception in getBidInfo`() {
        every {
            bidInfoUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockk())
        }

        var actual: List<TopadsBidInfo.DataItem>? = null
        viewModel.getBidInfo(
            suggestions = listOf(),
            onSuccess = { actual = it }, {}
        )
        Assert.assertEquals(null, actual)
    }

    @Test
    fun `getSuggestionKeyword invoke success is not empty data`() {
        val expected = spyk(KeywordSuggestionResponse.Result())

        every { expected.topAdsGetKeywordSuggestionV3.data } returns listOf(KeywordData())
        every { suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(KeywordSuggestionResponse.Result) -> Unit>().invoke(expected)
        }

        var actual: List<KeywordData>? = null
        viewModel.getSuggestionKeyword("", 0, { actual = it }, {})
        Assert.assertEquals(expected.topAdsGetKeywordSuggestionV3.data, actual)
    }

    @Test
    fun `getSuggestionKeyword on empty data should invoke onEmpty`() {
        every { suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(KeywordSuggestionResponse.Result) -> Unit>().invoke(KeywordSuggestionResponse.Result())
        }
        var onEmptyCalled = false
        viewModel.getSuggestionKeyword("", 0, {}, { onEmptyCalled = true })
        Assert.assertTrue(onEmptyCalled)
    }

    @Test
    fun `getSuggestionKeyword on empty data should not invoke onsuccess`() {
        every { suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(KeywordSuggestionResponse.Result) -> Unit>().invoke(KeywordSuggestionResponse.Result())
        }
        var actual: List<KeywordData>? = null
        viewModel.getSuggestionKeyword("", 0, { actual = it }, {})
        Assert.assertEquals(null, actual)
    }

    @Test
    fun `getSuggestionKeyword on error should not invoke onsuccess`() {
        every {
            suggestionKeywordUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockk())
        }

        var actual: List<KeywordData>? = null
        viewModel.getSuggestionKeyword("", 0, { actual = it }, {})
        Assert.assertEquals(null, actual)
    }

    @Test
    fun `check onSuccess invocation in getBidInfoDefault`() {
        val expected = "1000"
        var actual = "0"
        val bidInfoData: ResponseBidInfo.Result = ResponseBidInfo.Result(TopadsBidInfo(data =
        listOf(TopadsBidInfo.DataItem(suggestionBid = expected))))
        val onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit = {
            actual = it[0].suggestionBid

        }
        every {
            bidInfoUseCaseDefault.executeQuerySafeMode(captureLambda(), any())
        } answers {
            onSuccess.invoke(bidInfoData.topadsBidInfo.data)
        }

        viewModel.getBidInfoDefault(
            suggestions = listOf(), onSuccess = onSuccess
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check onerror check`() {

        every {
            bidInfoUseCaseDefault.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockk())
        }

        var actual: List<TopadsBidInfo.DataItem>? = null
        viewModel.getBidInfoDefault(
            suggestions = listOf(), onSuccess = { actual = it }
        )

        Assert.assertEquals(null, actual)
    }


}
