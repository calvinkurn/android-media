package com.tokopedia.profilecompletion.addphone

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.UserProfileUpdatePhone
import com.tokopedia.profilecompletion.addphone.data.UserProfileValidate
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.addphone.domain.UserProfileUpdateUseCase
import com.tokopedia.profilecompletion.addphone.domain.UserProfileValidateUseCase
import com.tokopedia.profilecompletion.addphone.domain.param.UserProfileUpdateParam
import com.tokopedia.profilecompletion.addphone.viewmodel.NewAddPhoneViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class NewAddPhoneViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NewAddPhoneViewModel

    private val userProfileValidateUseCase = mockk<UserProfileValidateUseCase>(relaxed = true)
    private val userProfileUpdateUseCase = mockk<UserProfileUpdateUseCase>(relaxed = true)
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = NewAddPhoneViewModel(
            userProfileValidateUseCase,
            userProfileUpdateUseCase,
            userSessionInterface,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `input empty phone number then field status invalid`() {
        // Given
        val phone = ""
        val expected = R.string.new_add_phone_must_be_filled

        // When
        viewModel.validatePhone(phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `input sort phone number then field status invalid`() {
        // Given
        val phone = "0812123"
        val expected = R.string.new_add_phone_min_length_error

        // When
        viewModel.validatePhone(phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `input exceed phone number length then field status invalid`() {
        // Given
        val phone = "08121234567890123"
        val expected = R.string.new_add_phone_max_length_error

        // When
        viewModel.validatePhone(phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `input valid phone number then field status valid`() {
        // Given
        val phone = "081212345678"
        val expected = NewAddPhoneViewModel.EMPTY_RESOURCE

        // When
        viewModel.validatePhone(phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `submit invalid phone number then field status invalid`() {
        // Given
        val phone = "0812"
        val validField = NewAddPhoneViewModel.EMPTY_RESOURCE

        // When
        viewModel.validatePhone(phone)
        viewModel.submitForm(phone = phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertTrue(result != validField)
    }

    @Test
    fun `submit valid phone number then field status valid`() {
        // Given
        val phone = "08121234567890"
        val validField = NewAddPhoneViewModel.EMPTY_RESOURCE

        // When
        viewModel.validatePhone(phone)
        viewModel.submitForm(phone = phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(validField, result)
    }

    @Test
    fun `get userProfileValidate then success`() {
        // Given
        val phone = "08121234567890"
        val response = UserValidatePojo(
            UserProfileValidate(
                isValid = true,
                message = ""
            )
        )
        val expected = Success(Unit)

        // When
        coEvery { userProfileValidateUseCase(phone) } returns response
        viewModel.validatePhone(phone)
        viewModel.submitForm(phone = phone)

        // Then
        val result = viewModel.userProfileValidate.getOrAwaitValue()
        val isLoading = viewModel.userValidateLoading.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get userProfileValidate then not eligible`() {
        // Given
        val phone = "08121234567890"
        val response = UserValidatePojo(
            UserProfileValidate(
                isValid = false,
                message = "Nomor anda sudah banyak digunakan di akun lain!"
            )
        )
        val expected = Fail(MessageErrorException(response.userProfileValidate.message))

        // When
        coEvery { userProfileValidateUseCase(phone) } returns response
        viewModel.validatePhone(phone)
        viewModel.submitForm(phone = phone)

        // Then
        val result = viewModel.userProfileValidate.getOrAwaitValue()
        val isLoading = viewModel.userValidateLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(expected.throwable.message, result.throwable.message)
        assertFalse(isLoading)
    }

    @Test
    fun `get userProfileValidate then failed`() {
        // Given
        val phone = "08121234567890"
        val response = Throwable()
        val expected = Fail(response)

        // When
        coEvery { userProfileValidateUseCase(phone) } throws response
        viewModel.validatePhone(phone)
        viewModel.submitForm(phone = phone)

        // Then
        val result = viewModel.userProfileValidate.getOrAwaitValue()
        val isLoading = viewModel.userValidateLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get userProfileUpdate then success`() {
        // Given
        val phone = "08121234567890"
        val validateToken = "qwerty"
        val response = AddPhonePojo(
            UserProfileUpdatePhone(
                isSuccess = 1,
                completionScore = 70,
                errors = arrayListOf()
            )
        )
        val param = UserProfileUpdateParam(
            phone = phone,
            currentValidateToken = validateToken
        )
        val expected = Success(Pair(phone, response.data))

        // When
        coEvery { userProfileUpdateUseCase(param) } returns response
        viewModel.userProfileUpdate(phone, validateToken)

        // Then
        val result = viewModel.userPhoneUpdate.getOrAwaitValue()
        val isLoading = viewModel.userUpdateLoading.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(expected, result)
        assertFalse(isLoading)
        coVerify(exactly = 1) {
            userSessionInterface.setIsMSISDNVerified(true)
            userSessionInterface.phoneNumber = phone
        }
    }

    @Test
    fun `get userProfileUpdate then get message failed`() {
        // Given
        val phone = "08121234567890"
        val validateToken = "qwerty"
        val response = AddPhonePojo(
            UserProfileUpdatePhone(
                isSuccess = 0,
                completionScore = 70,
                errors = arrayListOf("Gagal!")
            )
        )
        val param = UserProfileUpdateParam(
            phone = phone,
            currentValidateToken = validateToken
        )
        val expected = Fail(MessageErrorException(response.data.errors.first()))

        // When
        coEvery { userProfileUpdateUseCase(param) } returns response
        viewModel.userProfileUpdate(phone, validateToken)

        // Then
        val result = viewModel.userPhoneUpdate.getOrAwaitValue()
        val isLoading = viewModel.userUpdateLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertTrue(result.throwable is MessageErrorException)
        assertSame(expected.throwable.message, result.throwable.message)
        assertFalse(isLoading)
    }

    @Test
    fun `get userProfileUpdate then failed`() {
        // Given
        val phone = "08121234567890"
        val validateToken = "qwerty"
        val response = Throwable()
        val param = UserProfileUpdateParam(
            phone = phone,
            currentValidateToken = validateToken
        )
        val expected = Fail(response)

        // When
        coEvery { userProfileUpdateUseCase(param) } throws response
        viewModel.userProfileUpdate(phone, validateToken)

        // Then
        val result = viewModel.userPhoneUpdate.getOrAwaitValue()
        val isLoading = viewModel.userUpdateLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

}
