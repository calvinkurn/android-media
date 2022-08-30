package com.tokopedia.buyerorder.detail.revamp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.ActionButtonEventWrapper
import com.tokopedia.buyerorder.detail.data.ActionButtonList
import com.tokopedia.buyerorder.detail.data.DataEmail
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.data.SendEventEmail
import com.tokopedia.buyerorder.detail.domain.OmsDetailUseCase
import com.tokopedia.buyerorder.detail.domain.RevampActionButtonUseCase
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase
import com.tokopedia.common.network.data.model.RestResponse
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
import rx.Subscriber
import java.lang.reflect.Type

/**
 * created by @bayazidnasir on 19/8/2022
 */

class OrderDetailViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private val omsDetailUseCase: Lazy<OmsDetailUseCase> = mockk()
    private val actionButtonUseCase: Lazy<RevampActionButtonUseCase> = mockk()
    private val sendNotificationUseCase: Lazy<SendEventNotificationUseCase> = mockk(relaxed = true)

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

    @Test
    fun `request action button when called from adapter && flag is true should trigger event tapActionButton`(){
        //Given
        val mockData = ActionButtonList()

        every { actionButtonUseCase.get().setParams(any()) } answers { mapOf<String, Any>() }
        coEvery { actionButtonUseCase.get().executeOnBackground() } returns mockData

        //When
        viewModel.requestActionButton(emptyList(), 0, true, isCalledFromAdapter = true)

        //Then
        val result = viewModel.actionButton.value as ActionButtonEventWrapper.TapActionButton
        assertNotNull(result)
        assertEquals(mockData.actionButtonList, result.list)
        assertEquals(0, result.position)

        verify { actionButtonUseCase.get().setParams(any()) }
        coVerify { actionButtonUseCase.get().executeOnBackground() }
    }

    @Test
    fun `request action button when called from adapter && flag is true && control is refresh should trigger event tapActionButton`(){
        //Given
        val mockData = ActionButtonList(
            actionButtonList = listOf(ActionButton(control = "refresh"))
        )

        every { actionButtonUseCase.get().setParams(any()) } answers { mapOf<String, Any>() }
        coEvery { actionButtonUseCase.get().executeOnBackground() } returns mockData

        //When
        viewModel.requestActionButton(emptyList(), 0, true, isCalledFromAdapter = true)

        //Then
        val result = viewModel.actionButton.value as ActionButtonEventWrapper.TapActionButton
        assertNotNull(result)
        assertEquals(mockData.actionButtonList, result.list)
        assertEquals(0, result.position)

        verify { actionButtonUseCase.get().setParams(any()) }
        coVerify { actionButtonUseCase.get().executeOnBackground() }
    }

    @Test
    fun `request action button when called from adapter && flag is false should trigger event setActionButton`(){
        //Given
        val mockData = ActionButtonList()

        every { actionButtonUseCase.get().setParams(any()) } answers { mapOf<String, Any>() }
        coEvery { actionButtonUseCase.get().executeOnBackground() } returns mockData

        //When
        viewModel.requestActionButton(emptyList(), 0, false, isCalledFromAdapter = true)

        //Then
        val result = viewModel.actionButton.value as ActionButtonEventWrapper.SetActionButton
        assertNotNull(result)
        assertEquals(mockData.actionButtonList, result.list)
        assertEquals(0, result.position)

        verify { actionButtonUseCase.get().setParams(any()) }
        coVerify { actionButtonUseCase.get().executeOnBackground() }
    }

    @Test
    fun `request action button when called not from adapter should trigger event renderActionButton`(){
        //Given
        val mockData = ActionButtonList()

        every { actionButtonUseCase.get().setParams(any()) } answers { mapOf<String, Any>() }
        coEvery { actionButtonUseCase.get().executeOnBackground() } returns mockData

        //When
        viewModel.requestActionButton(emptyList(), 0, false, isCalledFromAdapter = false)

        //Then
        val result = viewModel.actionButton.value as ActionButtonEventWrapper.RenderActionButton
        assertNotNull(result)
        assertEquals(mockData.actionButtonList, result.list)

        verify { actionButtonUseCase.get().setParams(any()) }
        coVerify { actionButtonUseCase.get().executeOnBackground() }
    }

    @Test
    fun `send event notification should be success`(){
        //Given
        val response = RestResponse(SendEventEmail(data = DataEmail(message = "test1")), 200, false)
        val responseMap = mapOf<Type, RestResponse>(SendEventEmail::class.java to response)

        every { sendNotificationUseCase.get().execute(any()) } answers {
            firstArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            firstArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            firstArg<Subscriber<Map<Type, RestResponse>>>().onNext(responseMap)
        }

        //When
        viewModel.sendEventEmail(ActionButton(), "")

        //Then
        val result = viewModel.eventEmail.value as Success
        assertNotNull(result)
        assertEquals(response.getData<SendEventEmail>().data.message, result.data.data.message)

        verify { sendNotificationUseCase.get().execute(any()) }
    }

    @Test
    fun `send event notification should be failed`(){
        //Given
        val response = RestResponse(SendEventEmail(data = DataEmail(message = "test1")), 401, false)
        response.errorBody = """
            {
              "data": {
                "message": "failed to fetch"
              }
            }
        """.trimIndent()
        val responseMap = mapOf<Type, RestResponse>(SendEventEmail::class.java to response)

        every { sendNotificationUseCase.get().execute(any()) } answers {
            firstArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            firstArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            firstArg<Subscriber<Map<Type, RestResponse>>>().onNext(responseMap)
        }

        //When
        viewModel.sendEventEmail(ActionButton(), "")

        //Then
        val result = viewModel.eventEmail.value as Fail
        assertNotNull(result)
        assertEquals("failed to fetch", result.throwable.message)

        verify { sendNotificationUseCase.get().execute(any()) }
    }

    @Test
    fun `send event notification should be throw error`(){
        //Given

        every { sendNotificationUseCase.get().execute(any()) } answers {
            firstArg<Subscriber<Map<Type, RestResponse>>>().onError(MessageErrorException("failed to fetch"))
        }

        //When
        viewModel.sendEventEmail(ActionButton(), "")

        //Then
        val result = viewModel.eventEmail.value as Fail
        assertNotNull(result)
        assertEquals("failed to fetch", result.throwable.message)

        verify { sendNotificationUseCase.get().execute(any()) }
    }

}