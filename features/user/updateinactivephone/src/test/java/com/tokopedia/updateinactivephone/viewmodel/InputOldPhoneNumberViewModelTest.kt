package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.updateinactivephone.domain.data.RegisterCheckData
import com.tokopedia.updateinactivephone.domain.data.RegisterCheckModel
import com.tokopedia.updateinactivephone.domain.usecase.InputOldPhoneNumberUseCase
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.model.PhoneFormState
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.viewmodel.InputOldPhoneNumberViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class InputOldPhoneNumberViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val useCase = mockk<InputOldPhoneNumberUseCase>(relaxed = true)
    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private lateinit var viewModel: InputOldPhoneNumberViewModel

    @Before
    fun setUp() {
        viewModel = InputOldPhoneNumberViewModel(useCase, dispatcherProviderTest)
    }

    @Test
    fun `number is empty - error`() {
        val phoneNumber = ""
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.formState.getOrAwaitValue()
        assertTrue(result.isDataValid == isDataValid)
        assertFalse(result.numberError == defaultNumberError)
    }

    @Test
    fun `number less than 9 character - error`() {
        val phoneNumber = "08218765"
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.formState.getOrAwaitValue()
        assertTrue(result.isDataValid == isDataValid)
        assertFalse(result.numberError == defaultNumberError)
    }

    @Test
    fun `number between 9 until 15 character - not error`() {
        val phoneNumber = "0821876590"
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.formState.getOrAwaitValue()
        assertFalse(result.isDataValid == isDataValid)
        assertTrue(result.numberError == defaultNumberError)
    }

    @Test
    fun `number between more than 15 character - error`() {
        val phoneNumber = "08218765909876445634"
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false

        viewModel.submitNumber(phoneNumber)

        val result = viewModel.formState.getOrAwaitValue()
        assertTrue(result.isDataValid == isDataValid)
        assertFalse(result.numberError == defaultNumberError)
    }

    @Test
    fun `click submit after fetch data then not loading`() {
        val phoneNumber = "0821876590"
        val data = RegisterCheckModel()

        coEvery {
            useCase(phoneNumber)
        } returns data
        viewModel.submitNumber(phoneNumber)

        val result = viewModel.isLoading.getOrAwaitValue()
        assertFalse(result)
    }

    @Test
    fun `submit phone number then exist`() {
        val phoneNumber = "0821876590"
        val data = RegisterCheckModel(RegisterCheckData(InputOldPhoneNumberViewModel.STATUS_USER_ACTIVE, emptyList()))

        coEvery {
            useCase(phoneNumber)
        } returns data
        viewModel.submitNumber(phoneNumber)

        val result = viewModel.statusPhoneNumber.getOrAwaitValue()
        assertEquals(Success(phoneNumber), result)
    }

    @Test
    fun `submit phone number then error`() {
        val phoneNumber = "0821876590"
        val message = "message"
        val data = RegisterCheckModel(RegisterCheckData(12, listOf(message)))

        coEvery {
            useCase(phoneNumber)
        } returns data
        viewModel.submitNumber(phoneNumber)

        val result = viewModel.statusPhoneNumber.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `submit phone number then not exist`() {
        val phoneNumber = "0821876590"
        val defaultNumberError = PhoneFormState.DEFAULT_NUMBER_ERROR
        val isDataValid = false
        val data = RegisterCheckModel(RegisterCheckData(12, emptyList()))

        coEvery {
            useCase(phoneNumber)
        } returns data
        viewModel.submitNumber(phoneNumber)

        val result = viewModel.formState.getOrAwaitValue()
        assertTrue(result.isDataValid == isDataValid)
        assertFalse(result.numberError == defaultNumberError)
    }

    @Test
    fun `submit phone number then throwable`() {
        val phoneNumber = "0821876590"
        val data = Throwable()

        coEvery {
            useCase(phoneNumber)
        } throws data
        viewModel.submitNumber(phoneNumber)

        val result = viewModel.statusPhoneNumber.getOrAwaitValue()
        assertEquals(Fail(data), result)
    }
}