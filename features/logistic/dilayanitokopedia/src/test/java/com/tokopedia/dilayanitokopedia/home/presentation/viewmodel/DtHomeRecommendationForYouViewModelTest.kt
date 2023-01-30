package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dilayanitokopedia.home.domain.model.GetDtHomeRecommendationResponse
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeRecommendationProductV2
import com.tokopedia.dilayanitokopedia.home.domain.model.Product
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetRecommendationForYouUseCase
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoading
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DtHomeRecommendationForYouViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dtGetRecommendationForYouUseCase =
        mockk<GetRecommendationForYouUseCase>(relaxed = true)
    private val homeRecommendationDataModelObserver =
        mockk<Observer<HomeRecommendationDataModel>>(relaxed = true)

    lateinit var viewModel: DtHomeRecommendationForYouViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = DtHomeRecommendationForYouViewModel(
            dtGetRecommendationForYouUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.homeRecommendationLiveData.observeForever(homeRecommendationDataModelObserver)
    }

    @Test
    fun `verify when load initial page is empty`() {
        // Inject
        val mockResponse = spyk(
            GetDtHomeRecommendationResponse()
        )

        // Given
        coEvery {
            dtGetRecommendationForYouUseCase.executeOnBackground()
        } returns mockResponse

        // When
        viewModel.loadInitialPage("")

        // Then
        assertTrue(
            viewModel.homeRecommendationLiveData.value?.homeRecommendations?.first() != null
        )
    }

    @Test
    fun `verify when load initial page have product`() {
        // Inject
        val mockResponse = spyk(
            GetDtHomeRecommendationResponse(
                response = spyk(
                    GetHomeRecommendationProductV2(
                        products = arrayListOf(
                            spyk(
                                Product()
                            )
                        )
                    )
                )
            )
        )

        // Given
        coEvery {
            dtGetRecommendationForYouUseCase.executeOnBackground()
        } returns mockResponse

        // When
        viewModel.loadInitialPage("")

        // Then
        assertTrue(
            viewModel.homeRecommendationLiveData.value?.homeRecommendations?.first() != null
        )
    }

    @Test
    fun `verify when load initial page is error`() {
        // Given
        coEvery {
            dtGetRecommendationForYouUseCase.executeOnBackground()
        } throws mockThrowable

        // When
        viewModel.loadInitialPage("")

        // Then
        verify(exactly = 0) {
            homeRecommendationDataModelObserver.onChanged(
                HomeRecommendationDataModel(
                    homeRecommendations = listOf(
                        HomeRecommendationError()
                    )
                )
            )
        }
    }

    @Test
    fun `verify when loadLoading is correctly`() {
        // When
        viewModel.loadLoading()

        // Then
        assertTrue(
            viewModel.homeRecommendationLiveData.value?.homeRecommendations?.first() is HomeRecommendationLoading
        )
    }
}
