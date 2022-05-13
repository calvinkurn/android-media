package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.updateinactivephone.domain.data.RegisterCheckData
import com.tokopedia.updateinactivephone.domain.data.RegisterCheckModel
import com.tokopedia.updateinactivephone.domain.usecase.InputOldPhoneNumberUseCase
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.model.PhoneFormState
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.viewmodel.InputOldPhoneNumberViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coVerify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class InputOldPhoneNumberViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var useCase: InputOldPhoneNumberUseCase

    @Mock
    lateinit var coroutineDispatcher: CoroutineDispatchers

    private lateinit var viewModel: InputOldPhoneNumberViewModel

    @Before
    fun setUp() {
        viewModel = InputOldPhoneNumberViewModel(useCase, coroutineDispatcher)
    }

    @Test
    fun `number less than 9 character - error`() {
        val phoneNumber = "08218765"
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.formState.value
        assertTrue(result?.isDataValid == isDataValid)
        assertFalse(result?.numberError == defaultNumberError)
    }

    @Test
    fun `number between 9 until 15 character - not error`() {
        val phoneNumber = "0821876590"
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.formState.value
        assertFalse(result?.isDataValid == isDataValid)
        assertTrue(result?.numberError == defaultNumberError)
    }

    @Test
    fun `number between more than 15 character - error`() {
        val phoneNumber = "08218765909876445634"
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.formState.value
        assertFalse(result?.isDataValid == isDataValid)
        assertTrue(result?.numberError == defaultNumberError)
    }

    @Test
    fun `click submit then loading`(){
        val phoneNumber = "0821876590"

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.isLoading.value
        assertTrue(result == true)
    }

    @Test
    fun `click submit after fetch data then not loading`() = coVerify {
        val phoneNumber = "0821876590"
        val data = RegisterCheckModel()

        `when`(useCase(phoneNumber)).thenReturn(data)
        viewModel.submitNumber(phoneNumber)

        val result = viewModel.isLoading.value
        assertFalse(result == true)
    }

    @Test
    fun `click submit then set current number`(){
        val phoneNumber = "0821876590"

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.currentNumber
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `double click submit with different phone number then verify only called once`(){
        val phoneNumber = "0821876590"

        viewModel.submitNumber(phoneNumber)
        viewModel.submitNumber(phoneNumber)

        verify(viewModel.currentNumber)
    }

    @Test
    fun `submit phone number then exist`() = coVerify {
        val phoneNumber = "0821876590"
        val data = RegisterCheckModel(RegisterCheckData(InputOldPhoneNumberViewModel.STATUS_USER_ACTIVE, emptyList()))

        `when`(useCase(phoneNumber)).thenReturn(data)

        val result = viewModel.statusPhoneNumber.value
        assertEquals(Success(phoneNumber), result)
    }

    @Test
    fun `submit phone number then error`() = coVerify {
        val phoneNumber = "0821876590"
        val message = "message"
        val data = RegisterCheckModel(RegisterCheckData(12, listOf(message)))

        `when`(useCase(phoneNumber)).thenReturn(data)

        val result = viewModel.statusPhoneNumber.value
        assertEquals(Fail(MessageErrorException(message)), result)
    }

    @Test
    fun `submit phone number then not exist`() = coVerify {
        val phoneNumber = "0821876590"
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false
        val data = RegisterCheckModel(RegisterCheckData(12, emptyList()))

        `when`(useCase(phoneNumber)).thenReturn(data)

        val result = viewModel.formState.value
        assertFalse(result?.isDataValid == isDataValid)
        assertTrue(result?.numberError == defaultNumberError)
    }

    @Test
    fun `submit phone number then trowable`() = coVerify {
        val phoneNumber = "0821876590"
        val data = Throwable()

        `when`(useCase(phoneNumber)).thenThrow(data)

        val result = viewModel.statusPhoneNumber.value
        assertEquals(Fail(data), result)
    }
}