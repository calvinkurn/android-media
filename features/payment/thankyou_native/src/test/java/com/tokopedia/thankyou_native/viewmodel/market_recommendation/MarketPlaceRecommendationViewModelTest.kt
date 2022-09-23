package com.tokopedia.thankyou_native.viewmodel.market_recommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.recommendation.data.ProductRecommendationData
import com.tokopedia.thankyou_native.recommendation.domain.TYPGetRecommendationUseCase
import com.tokopedia.thankyou_native.recommendation.presentation.viewmodel.MarketPlaceRecommendationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MarketPlaceRecommendationViewModelTest {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var typGetRecommendationUseCase: dagger.Lazy<TYPGetRecommendationUseCase>

    private lateinit var marketPlaceRecommendationViewModel: MarketPlaceRecommendationViewModel

    private val mockResponse = mockk<ProductRecommendationData>(relaxed = true)
    private val mockTypResponse = mockk<ThanksPageData>(relaxed = true)

    @ExperimentalCoroutinesApi
    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        marketPlaceRecommendationViewModel =
            MarketPlaceRecommendationViewModel(dispatcher, typGetRecommendationUseCase)
    }

    @Test
    fun getSuccessProductRecommendationData() {

        every {
            runBlocking {
                typGetRecommendationUseCase.get().getProductRecommendationData(mockTypResponse)
            }
        } answers {
            mockResponse
        }

        runBlocking {
            marketPlaceRecommendationViewModel.loadRecommendationData(mockTypResponse)
            Assert.assertEquals(
                marketPlaceRecommendationViewModel.recommendationMutableData.value,
                Success(data = mockResponse)
            )
        }


    }


    @Test
    fun getFailProductRecommendationData() {

        coEvery {
            runBlocking {
                typGetRecommendationUseCase.get().getProductRecommendationData(mockTypResponse)
            }
        } coAnswers  {
            null
        }

        runBlocking {
            marketPlaceRecommendationViewModel.loadRecommendationData(mockTypResponse)
            Assert.assertEquals(
                (marketPlaceRecommendationViewModel.recommendationMutableData.value as Fail).throwable.message,
                null
            )
        }


    }


}
