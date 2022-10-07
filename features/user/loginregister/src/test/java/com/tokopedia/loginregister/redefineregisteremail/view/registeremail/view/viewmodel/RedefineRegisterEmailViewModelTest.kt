package com.tokopedia.loginregister.redefineregisteremail.view.registeremail.view.viewmodel

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.RedefineRegisterEmailViewModel
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.ValidateUserDataUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data.ValidateUserDataModel
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.domain.usecase.GenerateKeyUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RedefineRegisterEmailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RedefineRegisterEmailViewModel

    private val generateKeyUseCase = mockk<GenerateKeyUseCase>(relaxed = true)
    private val validateUserDataUseCase = mockk<ValidateUserDataUseCase>(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic(Base64::class)
        mockkObject(RsaUtils)
        viewModel = RedefineRegisterEmailViewModel(
            generateKeyUseCase,
            validateUserDataUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `input invalid email then form state email invalid`() {
        // Given
        val email = "habibi@tokopedia"
        val expected = R.string.register_email_message_email_not_valid

        // When
        viewModel.validateEmail(email)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.emailError)
    }

    @Test
    fun `input empty email then form state email invalid`() {
        // Given
        val email = ""
        val expected = R.string.register_email_message_must_be_filled

        // When
        viewModel.validateEmail(email)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.emailError)
    }

    @Test
    fun `input valid email then form state email valid`() {
        // Given
        val email = "habibi@tokopedia.com"
        val expected = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validateEmail(email)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.emailError)
    }

    @Test
    fun `input empty password then form state password invalid`() {
        // Given
        val password = ""
        val expected = R.string.register_email_message_must_be_filled

        // When
        viewModel.validatePassword(password)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.passwordError)
    }

    @Test
    fun `input too sort password then form state password invalid`() {
        // Given
        val password = "123"
        val expected = R.string.error_minimal_password

        // When
        viewModel.validatePassword(password)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.passwordError)
    }

    @Test
    fun `input exceed password length then form state password invalid`() {
        // Given
        val password = "12345678901234567890123456789012345"
        val expected = R.string.error_maximal_password

        // When
        viewModel.validatePassword(password)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.passwordError)
    }

    @Test
    fun `input valid password then form state password valid`() {
        // Given
        val password = "1234567890"
        val expected = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validatePassword(password)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.passwordError)
    }

    @Test
    fun `input empty name then form state name invalid`() {
        // Given
        val name = ""
        val expected = R.string.register_email_message_must_be_filled

        // When
        viewModel.validateName(name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.nameError)
    }

    @Test
    fun `input too sort name then form state name invalid`() {
        // Given
        val name = "Ha"
        val expected = R.string.register_email_message_name_min_length_error

        // When
        viewModel.validateName(name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.nameError)
    }

    @Test
    fun `input name with number then form state name invalid`() {
        // Given
        val name = "Habibi123"
        val expected = R.string.register_email_message_name_character_error

        // When
        viewModel.validateName(name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.nameError)
    }

    @Test
    fun `input exceed name length then form state name invalid`() {
        // Given
        val name = "AbcdefghijklmnopqrstuvwxyzAbcdefghijklmnopqrstuvwxyz"
        val expected = R.string.register_email_message_name_max_length_error

        // When
        viewModel.validateName(name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.nameError)
    }

    @Test
    fun `input valid name then form state name valid`() {
        // Given
        val name = "Habibi"
        val expected = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validateName(name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result.nameError)
    }

    @Test
    fun `submit invalid field then form state invalid`() {
        // Given
        val email = "habibi"
        val password = "12"
        val name = "H"
        val validField = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertTrue(result.emailError != validField)
        assertTrue(result.passwordError != validField)
        assertTrue(result.nameError != validField)
    }

    @Test
    fun `submit invalid field then current value not empty`() {
        // Given
        val email = "habibi"
        val password = "12"
        val name = "H"

        // When
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val resultEmail = viewModel.currentEmail
        val resultPassword = viewModel.currentPassword
        val resultName = viewModel.currentName
        assertEquals(email, resultEmail)
        assertEquals(password, resultPassword)
        assertEquals(name, resultName)
    }

    @Test
    fun `submit valid field then form state valid`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "123456789"
        val name = "Habibi"
        val validField = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val resultFieldState = viewModel.formState.getOrAwaitValue()
        assertTrue(resultFieldState.emailError == validField)
        assertTrue(resultFieldState.passwordError == validField)
        assertTrue(resultFieldState.nameError == validField)
    }

    @Test
    fun `submit valid field then field value all set`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "123456789"
        val name = "Habibi"

        // When
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val resultEmail = viewModel.currentEmail
        val resultPassword = viewModel.currentPassword
        val resultName = viewModel.currentName
        assertEquals(email, resultEmail)
        assertEquals(password, resultPassword)
        assertEquals(name, resultName)
    }

    @Test
    fun `submit valid field then form state email not valid`() {
        // Given
        val email = "habibi"
        val password = "123456789"
        val name = "Habibi"
        val validField = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertNotEquals(validField, result.emailError)
        assertEquals(validField, result.passwordError)
        assertEquals(validField, result.nameError)
    }

    @Test
    fun `submit valid field then form state password not valid`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "12"
        val name = "Habibi"
        val validField = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(validField, result.emailError)
        assertNotEquals(validField, result.passwordError)
        assertEquals(validField, result.nameError)
    }

    @Test
    fun `submit valid field then form state name not valid`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "1234567890"
        val name = "Ha"
        val validField = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(validField, result.emailError)
        assertEquals(validField, result.passwordError)
        assertNotEquals(validField, result.nameError)
    }

    @Test
    fun `submit valid field then success encrypt password and validate all of variable value`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "123456789"
        val name = "Habibi"
        val data = KeyData(key = "testkey", hash = "123")
        val key = ByteArray(10)
        val encryptedPassword = "qwerty"

        // When
        coEvery { Base64.decode(data.key, any()) } returns key
        coEvery { RsaUtils.encrypt(password, String(key), true) } returns encryptedPassword
        coEvery { generateKeyUseCase(Unit).keyData } returns data
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val resultHash = viewModel.currentHash
        val resultEncryptedPassword = viewModel.encryptedPassword
        assertTrue(resultHash.isNotEmpty())
        assertTrue(resultEncryptedPassword.isNotEmpty())
        assertEquals(data.hash, resultHash)
        assertEquals(encryptedPassword, resultEncryptedPassword)
    }

    @Test
    fun `submit valid field then failed get generateKeyUseCase`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "123456789"
        val name = "Habibi"
        val response = Throwable()
        val expected = Fail(response)

        // When
        coEvery { generateKeyUseCase(Unit).keyData } throws response
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val resultHash = viewModel.currentHash
        val resultEncryptedPassword = viewModel.encryptedPassword
        val resultValidateUserData = viewModel.validateUserData.getOrAwaitValue()
        assertTrue(resultHash.isEmpty())
        assertTrue(resultEncryptedPassword.isEmpty())
        assertTrue(resultValidateUserData is Fail)
        assertEquals(expected, resultValidateUserData)
    }

    @Test
    fun `submit valid field then success get validateUserData`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "123456789"
        val name = "Habibi"
        val data = KeyData(key = "testkey", hash = "123")
        val key = ByteArray(10)
        val encryptedPassword = "qwerty"
        val response = ValidateUserDataModel()
        val expected = Success(response.validateUserData)

        // When
        coEvery { Base64.decode(data.key, any()) } returns key
        coEvery { RsaUtils.encrypt(password, String(key), true) } returns encryptedPassword
        coEvery { generateKeyUseCase(Unit).keyData } returns data
        coEvery { validateUserDataUseCase(any()) } returns response
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val result = viewModel.validateUserData.getOrAwaitValue()
        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

    @Test
    fun `submit valid field then failed get validateUserData`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "123456789"
        val name = "Habibi"
        val data = KeyData(key = "testkey", hash = "123")
        val key = ByteArray(10)
        val encryptedPassword = "qwerty"
        val response = Throwable()
        val expected = Fail(response)

        // When
        coEvery { Base64.decode(data.key, any()) } returns key
        coEvery { RsaUtils.encrypt(password, String(key), true) } returns encryptedPassword
        coEvery { generateKeyUseCase(Unit).keyData } returns data
        coEvery { validateUserDataUseCase(any()) } throws response
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)

        // Then
        val result = viewModel.validateUserData.getOrAwaitValue()
        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

    @Test
    fun `submit valid field then failed and try again`() {
        // Given
        val email = "habibi@tokopedia.com"
        val password = "123456789"
        val name = "Habibi"
        val data = KeyData(key = "testkey", hash = "123")
        val key = ByteArray(10)
        val encryptedPassword = "qwerty"
        val responseFailed = Throwable()
        val responseSuccess = ValidateUserDataModel()
        val expected = Success(responseSuccess.validateUserData)

        // When
        coEvery { Base64.decode(data.key, any()) } returns key
        coEvery { RsaUtils.encrypt(password, String(key), true) } returns encryptedPassword
        coEvery { generateKeyUseCase(Unit).keyData } returns data
        // submit data and failed
        coEvery { validateUserDataUseCase(any()) } throws responseFailed
        viewModel.validateEmail(email)
        viewModel.validatePassword(password)
        viewModel.validateName(name)
        viewModel.submitForm(email, password, name)
        // try again submit data
        coEvery { validateUserDataUseCase(any()) } returns responseSuccess
        viewModel.submitForm(email, password, name)

        // Then
        val result = viewModel.validateUserData.getOrAwaitValue()
        val isLoading = viewModel.isLoading.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

}
