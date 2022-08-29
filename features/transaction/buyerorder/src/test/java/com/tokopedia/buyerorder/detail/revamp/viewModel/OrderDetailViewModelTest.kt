package com.tokopedia.buyerorder.detail.revamp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.domain.OmsDetailUseCase
import com.tokopedia.buyerorder.detail.domain.RevampActionButtonUseCase
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase
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
    private val actionButtonUseCase: Lazy<RevampActionButtonUseCase> = mockk()
    private val sendNotificationUseCase: Lazy<SendEventNotificationUseCase> = mockk()

    private lateinit var viewModel: OrderDetailViewModel

    @Before
    fun setup(){
        viewModel = OrderDetailViewModel(
            omsDetailUseCase,
            actionButtonUseCase,
            sendNotificationUseCase,
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
}