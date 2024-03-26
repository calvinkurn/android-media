package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.ImpressionPredictionResponse
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionSearchUseCase
import com.tokopedia.topads.data.TopAdsGetBidSuggestionResponse
import com.tokopedia.topads.domain.usecase.TopAdsGetBidSuggestionByProductIDsUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertTrue
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
    private val topAdsGetBidSuggestionByProductIDsUseCase: TopAdsGetBidSuggestionByProductIDsUseCase =
        mockk(relaxed = true)
    private val topAdsImpressionPredictionUseCase: TopAdsImpressionPredictionSearchUseCase =
        mockk(relaxed = true)
    private val suggestionKeywordUseCase: SuggestionKeywordUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        viewModel = spyk(
            BudgetingAdsViewModel(
                rule.dispatchers,
                bidInfoUseCase,
                bidInfoUseCaseDefault,
                topAdsGetBidSuggestionByProductIDsUseCase,
                topAdsImpressionPredictionUseCase,
                suggestionKeywordUseCase
            )
        )
    }


    @Test
    fun `check onEmpty invocation in getBidInfo`() {
        val expected = "empty"
        var actual = ""
        val data = listOf<TopadsBidInfo.DataItem>()
        every {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(
                ResponseBidInfo.Result(TopadsBidInfo(data = data))
            )
        }
        viewModel.getBidInfo(
            suggestions = listOf(), onSuccess = {}, onEmpty = {
                actual = "empty"
            }
        )
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `check onSuccess invocation in getBidInfo`() {
        val expected = "1000"
        var actual = "0"
        val bidInfoData: ResponseBidInfo.Result = ResponseBidInfo.Result(
            TopadsBidInfo(
                data =
                listOf(TopadsBidInfo.DataItem(suggestionBid = expected))
            )
        )
        every {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(bidInfoData)
        }

        viewModel.getBidInfo(
            suggestions = listOf(), onSuccess = {
                actual = it[0].suggestionBid
            }, onEmpty = {}
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check exception in getBidInfo`() {
        every {
            bidInfoUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
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
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        var actual: List<KeywordData>? = null
        viewModel.getSuggestionKeyword("", 0, { actual = it }, {})
        Assert.assertEquals(null, actual)
    }

    @Test
    fun `check onSuccess invocation in getBidInfoDefault`() {
        val expected = "1000"
        var actual = "0"
        val bidInfoData: ResponseBidInfo.Result = ResponseBidInfo.Result(
            TopadsBidInfo(
                data =
                listOf(TopadsBidInfo.DataItem(suggestionBid = expected))
            )
        )
        val onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit = {
            actual = it[0].suggestionBid

        }
        every {
            bidInfoUseCaseDefault.executeQuerySafeMode(captureLambda(), any())
        } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(bidInfoData)
        }

        viewModel.getBidInfoDefault(
            suggestions = listOf(), onSuccess = onSuccess
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check onerror check  in getBidInfoDefault`() {

        every {
            bidInfoUseCaseDefault.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        var actual: List<TopadsBidInfo.DataItem>? = null
        viewModel.getBidInfoDefault(
            suggestions = listOf(), onSuccess = { actual = it }
        )

        Assert.assertEquals(null, actual)
    }

    @Test
    fun `getProductBid failure`() {
        coEvery {
            topAdsGetBidSuggestionByProductIDsUseCase.invoke(any(), any())
        } answers {
            throw Throwable()
        }
        viewModel.getProductBid(listOf("1"))
        assertTrue(viewModel.bidProductData.value is Fail)
    }

    @Test
    fun `getProductBid success`() {
        val data = Success(
            TopAdsGetBidSuggestionResponse(
                topAdsGetBidSuggestionByProductIDs = TopAdsGetBidSuggestionResponse.TopAdsGetBidSuggestionByProductIDs(
                    bidData = TopAdsGetBidSuggestionResponse.TopAdsGetBidSuggestionByProductIDs.BidData(
                        bidSuggestion = Int.ZERO
                    ),
                    error = TopAdsGetBidSuggestionResponse.TopAdsGetBidSuggestionByProductIDs.Error(
                        code = String.EMPTY,
                        detail = String.EMPTY,
                        title = String.EMPTY
                    )
                )
            )
        )
        coEvery {
            topAdsGetBidSuggestionByProductIDsUseCase.invoke(any(), any())
        } answers {
            data
        }
        viewModel.getProductBid(listOf())
        Assert.assertEquals(data, viewModel.bidProductData.value)
    }

    @Test
    fun `getPerformanceData failure`() {
        coEvery {
            topAdsImpressionPredictionUseCase.invoke(any(), any(), any(), any(), any())
        } answers {
            throw Throwable()
        }
        viewModel.getPerformanceData(listOf("1"), Float.ZERO, Float.ZERO, Float.ZERO)
        assertTrue(viewModel.performanceData.value is Fail)
    }

    @Test
    fun `getPerformanceData success`() {
        val data = Success(
            ImpressionPredictionResponse(
                umpGetImpressionPrediction = ImpressionPredictionResponse.UmpGetImpressionPrediction(
                    impressionPredictionData = ImpressionPredictionResponse.UmpGetImpressionPrediction.ImpressionPredictionData(
                        impression = ImpressionPredictionResponse.UmpGetImpressionPrediction.ImpressionPredictionData.Impression(
                            finalImpression = Int.ZERO,
                            increment = Int.ZERO,
                            oldImpression = Int.ZERO
                        )
                    ),
                    error = ImpressionPredictionResponse.UmpGetImpressionPrediction.Error(
                        title = String.EMPTY
                    )
                )
            )
        )
        coEvery {
            topAdsImpressionPredictionUseCase.invoke(any(), any(), any(), any(), any())
        } answers {
            data
        }
        viewModel.getPerformanceData(listOf(), Float.ZERO, Float.ZERO, Float.ZERO)
        Assert.assertEquals(data, viewModel.performanceData.value)
    }

}
