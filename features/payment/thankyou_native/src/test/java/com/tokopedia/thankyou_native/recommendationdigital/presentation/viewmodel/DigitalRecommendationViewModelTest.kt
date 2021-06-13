package com.tokopedia.thankyou_native.recommendationdigital.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.thankyou_native.recommendationdigital.domain.usecase.DigitalRecommendationUseCase
import com.tokopedia.thankyou_native.recommendationdigital.model.RechargeRecommendationDigiPersoItem
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationDigiPersoResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalRecommendationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var digitalRecommendationUseCase: DigitalRecommendationUseCase

    lateinit var digitalRecommendationViewModel: DigitalRecommendationViewModel

    @ExperimentalCoroutinesApi
    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        digitalRecommendationViewModel = DigitalRecommendationViewModel(digitalRecommendationUseCase, dispatcher)
    }

    @Test
    fun getDigitalRecommendationData_SuccessGetRecomData() {
        // given
        val mockResponse = RechargeRecommendationDigiPersoItem(
                appLink = "",
                bannerAppLink = "",
                bannerWebLink = "",
                recommendationItems = listOf(),
                mediaURL = "",
                option1 = "",
                option2 = "",
                option3 = "",
                textLink = "",
                title = "Doner Kebab",
                tracking = listOf(),
                webLink = ""
        )

        every { digitalRecommendationUseCase.cancelJobs() } returns Unit
        every { digitalRecommendationUseCase.getDigitalRecommendationData(any(), any(), any(), any()) } answers {
            firstArg<(RechargeRecommendationDigiPersoItem) -> Unit>().invoke(mockResponse)
        }

        // when
        digitalRecommendationViewModel.getDigitalRecommendationData("", listOf())

        // then
        val actualData = digitalRecommendationViewModel.digitalRecommendationLiveData
        assert(actualData.value is Success)

        val result = actualData.value as? Success<RechargeRecommendationDigiPersoItem>
        assertEquals(result?.data?.title, mockResponse.title)
    }

    @Test
    fun getDigitalRecommendationData_FailedGetRecomData() {
        // given
        val mockResponse = MessageErrorException("Recommendation Error Dummy")

        every { digitalRecommendationUseCase.cancelJobs() } returns Unit
        every { digitalRecommendationUseCase.getDigitalRecommendationData(any(), any(), any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockResponse)
        }

        // when
        digitalRecommendationViewModel.getDigitalRecommendationData("", listOf())

        // then
        val actualData = digitalRecommendationViewModel.digitalRecommendationLiveData
        assert(actualData.value is Fail)

        val result = actualData.value as? Fail
        assertEquals(result?.throwable?.message, mockResponse.message)
    }
}