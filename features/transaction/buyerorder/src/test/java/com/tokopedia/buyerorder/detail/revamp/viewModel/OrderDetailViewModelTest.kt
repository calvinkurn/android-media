package com.tokopedia.buyerorder.detail.revamp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.domain.DigiPersoUseCase
import com.tokopedia.buyerorder.detail.domain.OmsDetailUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * created by @bayazidnasir on 19/8/2022
 */

class OrderDetailViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private val omsDetailUseCase: OmsDetailUseCase = mockk()
    private val digiPersoUseCase: DigiPersoUseCase = mockk()

    private lateinit var viewModel: OrderDetailViewModel

    @Before
    fun setup(){
        viewModel = OrderDetailViewModel(
            omsDetailUseCase,
            digiPersoUseCase,
            dispatcher.io,
        )
    }

    @Test
    fun `fetch oms details should be success`(){
        //Given
        val mockData = DetailsData(OrderDetails())

        every { omsDetailUseCase.createParams(any(), any(), any()) } answers { mapOf<String, Any>() }

        coEvery {
            omsDetailUseCase.executeOnBackground()
        } returns mockData

        //When
        viewModel.requestOmsDetail("", "", "")

        //Then
        val result = viewModel.omsDetail.value as Success
        assertNotNull(result)
        assertEquals(mockData, result.data)

        verify { omsDetailUseCase.createParams(any(), any(), any()) }
        coVerify { omsDetailUseCase.executeOnBackground() }
    }

    @Test
    fun `fetch oms details should be fail`(){
        //Given
        val error = MessageErrorException("failed to fetch")
        every { omsDetailUseCase.createParams(any(), any(), any()) } answers { mapOf<String, Any>() }

        coEvery {
            omsDetailUseCase.executeOnBackground()
        } throws error

        //when
        viewModel.requestOmsDetail("", "", "")

        //then
        val result = viewModel.omsDetail.value as Fail
        assertNotNull(result)
        assertEquals(error.message, result.throwable.message)

        verify { omsDetailUseCase.createParams(any(), any(), any()) }
        coVerify { omsDetailUseCase.executeOnBackground() }
    }

    @Test
    fun `fetch digiPerso should be success`(){
        //given
        val mockData = RecommendationDigiPersoResponse(null)
        every { digiPersoUseCase.createParams() } answers { mapOf<String, Any>() }
        coEvery { digiPersoUseCase.executeOnBackground() } returns mockData

        //when
        viewModel.requestDigiPerso()

        //then
        val result = viewModel.digiPerso.value as Success
        assertNotNull(result)
        assertEquals(mockData, result.data)

        verify { digiPersoUseCase.createParams() }
        coVerify { digiPersoUseCase.executeOnBackground() }
    }

    @Test
    fun `fetch digiPerso should be fail`(){
        //given
        val error = MessageErrorException("failed to fetch")
        every { digiPersoUseCase.createParams() } answers { mapOf<String, Any>() }
        coEvery { digiPersoUseCase.executeOnBackground() } throws  error

        //when
        viewModel.requestDigiPerso()

        //then
        val result = viewModel.digiPerso.value as Fail
        assertNotNull(result)
        assertEquals(error.message, result.throwable.message)

        verify { digiPersoUseCase.createParams() }
        coVerify { digiPersoUseCase.executeOnBackground() }
    }
}