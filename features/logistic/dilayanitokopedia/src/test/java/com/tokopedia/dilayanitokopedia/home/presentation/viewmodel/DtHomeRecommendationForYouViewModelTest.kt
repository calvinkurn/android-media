package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dilayanitokopedia.domain.mapper.recommendationforyou.HomeRecommendationMapper.TYPE_PRODUCT
import com.tokopedia.dilayanitokopedia.domain.model.GetDtHomeRecommendationResponse
import com.tokopedia.dilayanitokopedia.domain.model.GetHomeRecommendationProductV2
import com.tokopedia.dilayanitokopedia.domain.model.Position
import com.tokopedia.dilayanitokopedia.domain.model.Product
import com.tokopedia.dilayanitokopedia.domain.usecase.GetRecommendationForYouUseCase
import com.tokopedia.dilayanitokopedia.ui.recommendation.DtHomeRecommendationViewModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationLoading
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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

    lateinit var viewModel: DtHomeRecommendationViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = DtHomeRecommendationViewModel(
            dtGetRecommendationForYouUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.homeRecommendationLiveData.observeForever(homeRecommendationDataModelObserver)
    }

    @Test
    fun `verify when load initial page is empty`() {
        // Inject
        val mockResponse = spyk(
            GetDtHomeRecommendationResponse(
                GetHomeRecommendationProductV2(
                    products = arrayListOf()
                )
            )
        )

        // Given
        coEvery {
            dtGetRecommendationForYouUseCase(any())
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
                GetHomeRecommendationProductV2(
                    products = arrayListOf(
                        spyk(
                            Product()
                        )
                    ),
                    positions = arrayListOf(
                        Position(
                            type = TYPE_PRODUCT
                        )
                    )
                )
            )
        )

        // Given
        coEvery {
            dtGetRecommendationForYouUseCase(any())
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
            dtGetRecommendationForYouUseCase(any())
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

    @Test
    fun `verify when load next data is correct`() {
        // Inject
        val mockResponse = spyk(
            GetDtHomeRecommendationResponse(
                GetHomeRecommendationProductV2(
                    products = arrayListOf(
                        spyk(
                            Product()
                        )
                    ),
                    positions = arrayListOf(
                        Position(
                            type = TYPE_PRODUCT
                        )
                    )
                )
            )
        )

        // Given
        coEvery {
            dtGetRecommendationForYouUseCase(any())
        } returns mockResponse

        // When
        viewModel.loadNextData(2)

        // Then
        assertNotNull(
            viewModel.homeRecommendationLiveData.value?.homeRecommendations?.first()
        )
    }

    @Test
    fun `verify when load next data is error`() {
        // Given
        coEvery {
            dtGetRecommendationForYouUseCase(any())
        } throws mockThrowable

        // When
        viewModel.loadNextData(2)

        // Then
        assertNull(
            viewModel.homeRecommendationLiveData.value?.homeRecommendations?.first()
        )
    }

    @Test
    fun `verify when load next data after initial page is correct`() {
        // Inject
        val mockResponse = spyk(
            GetDtHomeRecommendationResponse(
                GetHomeRecommendationProductV2(
                    products = arrayListOf(
                        spyk(
                            Product()
                        )
                    ),
                    positions = arrayListOf(
                        Position(
                            type = TYPE_PRODUCT
                        )
                    )
                )
            )
        )

        // Given
        coEvery {
            dtGetRecommendationForYouUseCase(any())
        } returns mockResponse

        // When
        viewModel.loadInitialPage("")
        viewModel.loadNextData(2)

        // Then
        assertNotNull(
            viewModel.homeRecommendationLiveData.value?.homeRecommendations?.first()
        )
    }

    @Test
    fun `verify when load next data after initial page is error`() {
        // Given
        coEvery {
            dtGetRecommendationForYouUseCase(any())
        } throws mockThrowable
        viewModel.homeRecommendationLiveData.value = spyk(
            HomeRecommendationDataModel(
                homeRecommendations = listOf()
            )
        )

        // When
        viewModel.loadInitialPage("")
        viewModel.loadNextData(2)

        // Then
        assertNotNull(
            viewModel.homeRecommendationLiveData.value?.homeRecommendations?.first()
        )
    }
}
