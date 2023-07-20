package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.BasicIdlingResource
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.RedefineRegisterInputPhoneViewModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.RegistrationPhoneState
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.local.RegisterPreferences
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.GetUserProfileUpdateUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.GetUserProfileValidateUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileUpdateModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileValidateModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.param.UserProfileUpdateParam
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.param.UserProfileValidateParam
import com.tokopedia.sessioncommon.data.RegisterCheckData
import com.tokopedia.sessioncommon.data.RegisterCheckModel
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.data.register.Register
import com.tokopedia.sessioncommon.data.register.RegisterV2Param
import com.tokopedia.sessioncommon.domain.usecase.GetRegisterCheckUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetRegisterV2AndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/*
* in this page, just need validation phone number, other valid value come from arguments
* */
class RedefineRegisterInputPhoneViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RedefineRegisterInputPhoneViewModel

    private val getRegisterCheckUseCase = mockk<GetRegisterCheckUseCase>(relaxed = true)

    private val getUserInfoAndSaveSessionUseCase =
        mockk<GetUserInfoAndSaveSessionUseCase>(relaxed = true)

    private val getRegisterV2AndSaveSessionUseCase =
        mockk<GetRegisterV2AndSaveSessionUseCase>(relaxed = true)

    private val getUserProfileUpdateUseCase = mockk<GetUserProfileUpdateUseCase>(relaxed = true)

    private val getUserProfileValidateUseCase = mockk<GetUserProfileValidateUseCase>(relaxed = true)

    private val registerPreferences = mockk<RegisterPreferences>(relaxed = true)

    private val idlingResource = mockk<BasicIdlingResource>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = RedefineRegisterInputPhoneViewModel(
            getRegisterCheckUseCase,
            getUserInfoAndSaveSessionUseCase,
            getRegisterV2AndSaveSessionUseCase,
            getUserProfileUpdateUseCase,
            getUserProfileValidateUseCase,
            registerPreferences,
            idlingResource,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `input empty phone number then field status invalid`() {
        // Given
        val phone = ""
        val expected = R.string.register_email_message_must_be_filled

        // When
        viewModel.validatePhone(phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `input to sort phone number then field status invalid`() {
        // Given
        val phone = "0812123"
        val expected = R.string.register_email_input_phone_min_length_error

        // When
        viewModel.validatePhone(phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `input to exceed phone number length then field status invalid`() {
        // Given
        val phone = "08121234567890123"
        val expected = R.string.register_email_input_phone_max_length_error

        // When
        viewModel.validatePhone(phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `input to valid phone number then field status valid`() {
        // Given
        val phone = "081212345678"
        val expected = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validatePhone(phone)

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `submit invalid phone number then field status invalid`() {
        // Given
        val email = "habibi@tokopedia.com"
        val phone = "0812"
        val isRequiredInputPhone = false // in this case, whatever the value behavior still same
        val validField = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validatePhone(phone)
        viewModel.submitForm(
            phone = phone,
            email = email,
            isRequiredInputPhone = isRequiredInputPhone
        )

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertTrue(result != validField)
    }

    @Test
    fun `submit valid phone number then field status valid`() {
        // Given
        val email = "habibi@tokopedia.com"
        val phone = "08121234567890"
        val isRequiredInputPhone = false // in this case, whatever the value behavior still same
        val validField = RedefineRegisterEmailConstants.EMPTY_RESOURCE

        // When
        viewModel.validatePhone(phone)
        viewModel.submitForm(
            phone = phone,
            email = email,
            isRequiredInputPhone = isRequiredInputPhone
        )

        // Then
        val result = viewModel.formState.getOrAwaitValue()
        assertEquals(validField, result)
    }

    @Test
    fun `get registerCheck then ineligible user`() {
        // Given
        val email = "habibi@tokopedia.com"
        val phone = "08121234567890"
        val isRequiredInputPhone = true
        val message = "Nomor anda tidak eligible untuk mendaftar"
        val response = RegisterCheckModel(
            data = RegisterCheckData(errors = listOf(message))
        )

        // When
        coEvery { getRegisterCheckUseCase(phone) } returns response
        viewModel.validatePhone(phone)
        viewModel.submitForm(
            phone = phone,
            email = email,
            isRequiredInputPhone = isRequiredInputPhone
        )

        // Then
        val result = viewModel.isRegisteredPhone.getOrAwaitValue()
        val isLoading = viewModel.submitPhoneLoading.getOrAwaitValue()
        assertTrue(result is RegistrationPhoneState.Ineligible)
        assertEquals(message, result.message)
        assertFalse(isLoading)
    }

    @Test
    fun `get registerCheck then registered user`() {
        // Given
        val email = "habibi@tokopedia.com"
        val phone = "08121234567890"
        val isRequiredInputPhone = true
        val response = RegisterCheckModel(
            data = RegisterCheckData(isExist = true)
        )

        // When
        coEvery { getRegisterCheckUseCase(phone) } returns response
        viewModel.validatePhone(phone)
        viewModel.submitForm(
            phone = phone,
            email = email,
            isRequiredInputPhone = isRequiredInputPhone
        )

        // Then
        val result = viewModel.isRegisteredPhone.getOrAwaitValue()
        val isLoading = viewModel.submitPhoneLoading.getOrAwaitValue()
        assertTrue(result is RegistrationPhoneState.Registered)
        assertEquals(phone, result.message)
        assertFalse(isLoading)
    }

    @Test
    fun `get registerCheck then unregistered user`() {
        // Given
        val email = "habibi@tokopedia.com"
        val phone = "08121234567890"
        val isRequiredInputPhone = true
        val response = RegisterCheckModel(
            data = RegisterCheckData(isExist = false)
        )

        // When
        coEvery { getRegisterCheckUseCase(phone) } returns response
        viewModel.validatePhone(phone)
        viewModel.submitForm(
            phone = phone,
            email = email,
            isRequiredInputPhone = isRequiredInputPhone
        )

        // Then
        val result = viewModel.isRegisteredPhone.getOrAwaitValue()
        val isLoading = viewModel.submitPhoneLoading.getOrAwaitValue()
        assertTrue(result is RegistrationPhoneState.Unregistered)
        assertEquals(phone, result.message)
        assertFalse(isLoading)
    }

    @Test
    fun `get registerCheck then return failed`() {
        // Given
        val email = "habibi@tokopedia.com"
        val phone = "08121234567890"
        val isRequiredInputPhone = true
        val response = Throwable()

        // When
        coEvery { getRegisterCheckUseCase(phone) } throws response
        viewModel.validatePhone(phone)
        viewModel.submitForm(
            phone = phone,
            email = email,
            isRequiredInputPhone = isRequiredInputPhone
        )

        // Then
        val result = viewModel.isRegisteredPhone.getOrAwaitValue()
        val isLoading = viewModel.submitPhoneLoading.getOrAwaitValue()
        assertTrue(result is RegistrationPhoneState.Failed)
        assertEquals(response, result.throwable)
        assertFalse(isLoading)
    }

    @Test
    fun `get userProfileValidate then success`() {
        // Given
        val email = "habibi@tokopedia.com"
        val phone = "08121234567890"
        val isRequiredInputPhone = false
        val response = UserProfileValidateModel()
        val param = UserProfileValidateParam(
            email = email,
            phone = phone
        )
        val expected = Success(response)

        // When
        coEvery { getUserProfileValidateUseCase(param) } returns response
        viewModel.validatePhone(phone)
        viewModel.submitForm(
            phone = phone,
            email = email,
            isRequiredInputPhone = isRequiredInputPhone
        )

        // Then
        val result = viewModel.userProfileValidate.getOrAwaitValue()
        val isLoading = viewModel.submitPhoneLoading.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get userProfileValidate then failed`() {
        // Given
        val email = "habibi@tokopedia.com"
        val phone = "08121234567890"
        val isRequiredInputPhone = false
        val response = Throwable()
        val param = UserProfileValidateParam(
            email = email,
            phone = phone
        )
        val expected = Fail(response)

        // When
        coEvery { getUserProfileValidateUseCase(param) } throws response
        viewModel.validatePhone(phone)
        viewModel.submitForm(
            phone = phone,
            email = email,
            isRequiredInputPhone = isRequiredInputPhone
        )

        // Then
        val result = viewModel.userProfileValidate.getOrAwaitValue()
        val isLoading = viewModel.submitPhoneLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get userProfileUpdate then success`() {
        // Given
        val response = UserProfileUpdateModel()
        val param = UserProfileUpdateParam()
        val expected = Success(response)

        // When
        coEvery { getUserProfileUpdateUseCase(param) } returns response
        viewModel.userProfileUpdate(param)

        // Then
        val result = viewModel.userPhoneUpdate.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(expected, result)
    }

    @Test
    fun `get userProfileUpdate then failed`() {
        // Given
        val response = Throwable()
        val param = UserProfileUpdateParam()
        val expected = Fail(response)

        // When
        coEvery { getUserProfileUpdateUseCase(param) } throws response
        viewModel.userProfileUpdate(param)

        // Then
        val result = viewModel.userPhoneUpdate.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(expected, result)
    }

    @Test
    fun `get registerV2 then success`() {
        // Given
        val response = Success(Register())
        val param = RegisterV2Param()

        // When
        coEvery { getRegisterV2AndSaveSessionUseCase(param) } returns response
        viewModel.registerV2(param)

        // Then
        val result = viewModel.registerV2.getOrAwaitValue()
        val isLoading = viewModel.submitRegisterLoading.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(response, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get registerV2 then get failed value from useCase`() {
        // Given
        val response = Fail(Throwable())
        val param = RegisterV2Param()

        // When
        coEvery { getRegisterV2AndSaveSessionUseCase(param) } returns response
        viewModel.registerV2(param)

        // Then
        val result = viewModel.registerV2.getOrAwaitValue()
        val isLoading = viewModel.submitRegisterLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(response, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get registerV2 then failed`() {
        // Given
        val response = Throwable()
        val param = RegisterV2Param()
        val expected = Fail(response)

        // When
        coEvery { getRegisterV2AndSaveSessionUseCase(param) } throws response
        viewModel.registerV2(param)

        // Then
        val result = viewModel.registerV2.getOrAwaitValue()
        val isLoading = viewModel.submitRegisterLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get userInfo then success`() {
        // Given
        val response = Success(ProfilePojo())

        // When
        coEvery { getUserInfoAndSaveSessionUseCase(Unit) } returns response
        viewModel.getUserInfo()

        // Then
        val result = viewModel.getUserInfo.getOrAwaitValue()
        val isLoading = viewModel.submitRegisterLoading.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(response, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get userInfo then get failed value from useCase`() {
        // Given
        val response = Fail(Throwable())

        // When
        coEvery { getUserInfoAndSaveSessionUseCase(Unit) } returns response
        viewModel.getUserInfo()

        // Then
        val result = viewModel.getUserInfo.getOrAwaitValue()
        val isLoading = viewModel.submitRegisterLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(response, result)
        assertFalse(isLoading)
    }

    @Test
    fun `get userInfo then failed`() {
        // Given
        val response = Throwable()
        val expected = Fail(response)

        // When
        coEvery { getUserInfoAndSaveSessionUseCase(Unit) } throws response
        viewModel.getUserInfo()

        // Then
        val result = viewModel.getUserInfo.getOrAwaitValue()
        val isLoading = viewModel.submitRegisterLoading.getOrAwaitValue()
        assertTrue(result is Fail)
        assertEquals(expected, result)
        assertFalse(isLoading)
    }

    @Test
    fun `set action after register then make sure function only called once`() {
        // When
        viewModel.saveFirstInstallTime()

        // Then
        coVerify(exactly = 1) {
            registerPreferences.saveFirstInstallTime()
        }
    }

    @Test
    fun `set action after register then failed`() {
        // Given
        val response = Throwable()

        // When
        coEvery { registerPreferences.saveFirstInstallTime() } throws response
        viewModel.saveFirstInstallTime()

        // Then
        coVerify(exactly = 1) {
            registerPreferences.saveFirstInstallTime()
        }
    }
}
