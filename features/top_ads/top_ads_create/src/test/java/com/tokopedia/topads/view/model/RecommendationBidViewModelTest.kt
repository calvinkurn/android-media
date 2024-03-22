@file:Suppress("DEPRECATION")

package com.tokopedia.topads.view.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.ImpressionPredictionResponse
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionBrowseUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecommendationBidViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: RecommendationBidViewModel
    private val topAdsImpressionPredictionUseCase: TopAdsImpressionPredictionBrowseUseCase =
        mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = RecommendationBidViewModel(rule.dispatchers, topAdsImpressionPredictionUseCase)
    }

    @Test
    fun `getPerformanceData success`() {
        val data: Result<ImpressionPredictionResponse> = Success(
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
            topAdsImpressionPredictionUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } answers {
            data
        }
        viewModel.getPerformanceData(listOf(), Float.ZERO, Float.ZERO, Float.ZERO)
        assertEquals(data, viewModel.performanceData.value)
    }

    @Test
    fun `getPerformanceData failure`() {
        val throwable = Throwable()
        coEvery {
            topAdsImpressionPredictionUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws throwable
        viewModel.getPerformanceData(listOf(), Float.ZERO, Float.ZERO, Float.ZERO)
        assertEquals(Fail(throwable), viewModel.performanceData.value)

    }

}
