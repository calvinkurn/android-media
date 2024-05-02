package com.tokopedia.topads.edit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.ImpressionPredictionResponse
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionBrowseUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionSearchUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EditAdGroupDailyBudgetViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val topAdsImpressionPredictionSearchUseCase: TopAdsImpressionPredictionSearchUseCase =
        mockk(relaxed = true)
    private val topAdsImpressionPredictionBrowseUseCase: TopAdsImpressionPredictionBrowseUseCase =
        mockk(relaxed = true)
    private lateinit var viewModel: EditAdGroupDailyBudgetViewModel

    @Before
    fun setUp() {
        viewModel = EditAdGroupDailyBudgetViewModel(
            testDispatcher,
            topAdsImpressionPredictionSearchUseCase,
            topAdsImpressionPredictionBrowseUseCase
        )
    }

    @Test
    fun `getPerformanceData success`() {
        val adsImpression = Success(
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
                        title = ""
                    )
                )
            )
        )

        coEvery {
            topAdsImpressionPredictionSearchUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns adsImpression

        coEvery {
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns adsImpression

        viewModel.getPerformanceData(listOf("1", "2"), mutableListOf(0f, 1f, 2f), Float.ZERO)

        coVerify {
            topAdsImpressionPredictionSearchUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
        assertNotNull(viewModel.performanceData.value)
    }

    @Test
    fun `getPerformanceData failure`() {
        val result = Fail(Throwable())
        coEvery {
            topAdsImpressionPredictionSearchUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns result

        coEvery {
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns result

        viewModel.getPerformanceData(listOf("1", "2"), mutableListOf(0f, 1f, 2f), Float.ZERO)

        coVerify {
            topAdsImpressionPredictionSearchUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
    }
}
