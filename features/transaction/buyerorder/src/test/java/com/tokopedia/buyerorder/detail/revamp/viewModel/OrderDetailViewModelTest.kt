package com.tokopedia.buyerorder.detail.revamp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.ActionButtonList
import com.tokopedia.buyerorder.detail.data.Body
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.domain.DigiPersoUseCase
import com.tokopedia.buyerorder.detail.domain.OmsDetailUseCase
import com.tokopedia.buyerorder.detail.domain.RevampActionButtonUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
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
    private val omsDetailUseCase: Lazy<OmsDetailUseCase> = mockk()
    private val digiPersoUseCase: Lazy<DigiPersoUseCase> = mockk()
    private val actionButtonUseCase: Lazy<RevampActionButtonUseCase> = mockk()

    private lateinit var viewModel: OrderDetailViewModel

    @Before
    fun setup(){
        viewModel = OrderDetailViewModel(
            omsDetailUseCase,
            digiPersoUseCase,
            actionButtonUseCase,
            dispatcher.io,
        )
    }

    @Test
    fun `fetch oms details should be success`(){
        //Given
        val mockData = DetailsData(OrderDetails())

        every { omsDetailUseCase.get().createParams(any(), any(), any()) } answers { mapOf<String, Any>() }

        coEvery {
            omsDetailUseCase.get().executeOnBackground()
        } returns mockData

        //When
        viewModel.requestOmsDetail("", "", "")

        //Then
        val result = viewModel.omsDetail.value as Success
        assertNotNull(result)
        assertEquals(mockData, result.data)

        verify { omsDetailUseCase.get().createParams(any(), any(), any()) }
        coVerify { omsDetailUseCase.get().executeOnBackground() }
    }

    @Test
    fun `fetch oms details should be fail`(){
        //Given
        val error = MessageErrorException("failed to fetch")
        every { omsDetailUseCase.get().createParams(any(), any(), any()) } answers { mapOf<String, Any>() }

        coEvery {
            omsDetailUseCase.get().executeOnBackground()
        } throws error

        //when
        viewModel.requestOmsDetail("", "", "")

        //then
        val result = viewModel.omsDetail.value as Fail
        assertNotNull(result)
        assertEquals(error.message, result.throwable.message)

        verify { omsDetailUseCase.get().createParams(any(), any(), any()) }
        coVerify { omsDetailUseCase.get().executeOnBackground() }
    }

    @Test
    fun `fetch digiPerso should be success`(){
        //given
        val mockData = RecommendationDigiPersoResponse(null)
        every { digiPersoUseCase.get().createParams() } answers { mapOf<String, Any>() }
        coEvery { digiPersoUseCase.get().executeOnBackground() } returns mockData

        //when
        viewModel.requestDigiPerso()

        //then
        val result = viewModel.digiPerso.value as Success
        assertNotNull(result)
        assertEquals(mockData, result.data)

        verify { digiPersoUseCase.get().createParams() }
        coVerify { digiPersoUseCase.get().executeOnBackground() }
    }

    @Test
    fun `fetch digiPerso should be fail`(){
        //given
        val error = MessageErrorException("failed to fetch")
        every { digiPersoUseCase.get().createParams() } answers { mapOf<String, Any>() }
        coEvery { digiPersoUseCase.get().executeOnBackground() } throws  error

        //when
        viewModel.requestDigiPerso()

        //then
        val result = viewModel.digiPerso.value as Fail
        assertNotNull(result)
        assertEquals(error.message, result.throwable.message)

        verify { digiPersoUseCase.get().createParams() }
        coVerify { digiPersoUseCase.get().executeOnBackground() }
    }

    @Test
    fun `fetch actionButton when flag is false should be success`(){
        //given
        val mockData = ActionButtonList()
        every { actionButtonUseCase.get().setParams(any()) } answers { mapOf<String, Any>() }
        coEvery { actionButtonUseCase.get().executeOnBackground() } returns mockData

        //when
        viewModel.requestActionButton(listOf(), 1, false)

        //then
        val result = viewModel.actionButton.value as Success
        assertNotNull(result)
        assertEquals(1, result.data.first)
        assertEquals(mockData.actionButtonList, result.data.second)

        verify { actionButtonUseCase.get().setParams(any()) }
        coVerify { actionButtonUseCase.get().executeOnBackground() }
    }

    @Test
    fun `fetch action button when flag is true should be success`(){
        //given
        val mockData = ActionButtonList(
            listOf(ActionButton(
                body = Body(
                    appURL = "https://tokopedia.com"
                ),
                control = "refresh"
            ))
        )
        every { actionButtonUseCase.get().setParams(any()) } answers { mapOf<String, Any>() }
        coEvery { actionButtonUseCase.get().executeOnBackground() } returns mockData

        //when
        viewModel.requestActionButton(listOf(ActionButton()), 1, true)

        //then
        val result = viewModel.actionButton.value as Success
        assertNotNull(result)
        assertEquals(1, result.data.first)
        assertEquals(mockData.actionButtonList, result.data.second)

        verify { actionButtonUseCase.get().setParams(any()) }
        coVerify { actionButtonUseCase.get().executeOnBackground() }
    }

    @Test
    fun `fetch action button should be fail`(){
        //given
        val error = MessageErrorException("failed to fetch")
        every { actionButtonUseCase.get().setParams(any()) } answers { mapOf<String, Any>() }
        coEvery { actionButtonUseCase.get().executeOnBackground() } throws error

        //when
        viewModel.requestActionButton(listOf(), 1, false)

        //then
        val result = viewModel.actionButton.value as Fail
        assertNotNull(result)
        assertEquals(error.message, result.throwable.message)

        verify { actionButtonUseCase.get().setParams(any()) }
        coVerify { actionButtonUseCase.get().executeOnBackground() }
    }
}