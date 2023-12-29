package com.tokopedia.managepassword.viewmodel

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordData
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordResponseModel
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordV2Response
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordUseCase
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordV2UseCase
import com.tokopedia.managepassword.addpassword.view.viewmodel.AddPasswordViewModel
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 27/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class AddPasswordViewModelTestJunit {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val addPasswordUseCase = mockk<AddPasswordUseCase>(relaxed = true)
    val addPasswordv2UseCase = mockk<AddPasswordV2UseCase>(relaxed = true)
    val getProfileCompletionUseCase = mockk<GetProfileCompletionUseCase>(relaxed = true)
    val generatePublicKeyUseCase = mockk<GeneratePublicKeyUseCase>(relaxed = true)

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    private var observer = mockk<Observer<Result<AddPasswordData>>>(relaxed = true)
    private var validatePasswordObserver = mockk<Observer<Result<String>>>(relaxed = true)
    private var validateConfirmPasswordObserver = mockk<Observer<Result<String>>>(relaxed = true)
    private var hasPasswordObserver = mockk<Observer<Result<ProfileDataModel>>>(relaxed = true)

    lateinit var viewModel: AddPasswordViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())

        viewModel = AddPasswordViewModel(
            addPasswordUseCase,
            addPasswordv2UseCase,
            getProfileCompletionUseCase,
            generatePublicKeyUseCase,
            dispatcherProviderTest
        )
        viewModel.response.observeForever(observer)
        viewModel.validatePassword.observeForever(validatePasswordObserver)
        viewModel.validatePasswordConfirmation.observeForever(validateConfirmPasswordObserver)
        viewModel.profileDataModel.observeForever(hasPasswordObserver)
    }

    var password = "abc1234567"
    var confirmPassword = "abc1234567"
    val mockThrowable = Throwable("Opps!")

    @Test
    fun `createPassword Success`() {
        val mockAddPassword = AddPasswordData(isSuccess = true)
        val mockAddPasswordResponse = AddPasswordResponseModel(mockAddPassword)

        coEvery { addPasswordUseCase(any()) } returns mockAddPasswordResponse

        viewModel.createPassword(password, confirmPassword)

        /* Then */
        verify { observer.onChanged(Success(mockAddPasswordResponse.addPassword)) }
    }

    @Test
    fun `createPassword isSuccess = false`() {
        val mockAddPassword = AddPasswordData(isSuccess = false, errorMessage = "Error")
        val mockAddPasswordResponse = AddPasswordResponseModel(mockAddPassword)

        coEvery { addPasswordUseCase(any()) } returns mockAddPasswordResponse
        viewModel.createPassword(password, confirmPassword)

        /* Then */
        assertEquals((viewModel.response.value as Fail).throwable.message, mockAddPasswordResponse.addPassword.errorMessage)
    }

    @Test
    fun `createPassword - fail`() {
        coEvery { addPasswordUseCase(any()) }.throws(mockThrowable)
        viewModel.createPassword(password, confirmPassword)

        coVerify { observer.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `createPassword v2 Success`() {
        val keyData = KeyData(key = "cGFkZGluZw==", hash = "zzzz")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)
        val mockAddPasswordV2Response = AddPasswordV2Response(
            AddPasswordData(
                isSuccess = true,
                errorMessage = ""
            )
        )

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)
        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "encrypted"

        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo
        coEvery { addPasswordv2UseCase(any()) } returns mockAddPasswordV2Response

        viewModel.createPasswordV2(password, confirmPassword)

        /* Then */
        coEvery { observer.onChanged(Success(mockAddPasswordV2Response.addPassword)) }
    }

    @Test
    fun `createPassword v2 isSuccess = false`() {
        val keyData = KeyData(key = "cGFkZGluZw==", hash = "zzzz")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)
        val mockAddPasswordV2Response = AddPasswordV2Response(
            AddPasswordData(
                isSuccess = false,
                errorMessage = "Opps!"
            )
        )

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)
        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "encrypted"

        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo
        coEvery { addPasswordv2UseCase(any()) } returns mockAddPasswordV2Response

        viewModel.createPasswordV2(password, confirmPassword)

        /* Then */
        coEvery {
            observer.onChanged(Fail(Throwable(mockAddPasswordV2Response.addPassword.errorMessage)))
        }
    }

    @Test
    fun `createPassword v2 - hash empty`() {
        val generateKeyPojo = GenerateKeyPojo()

        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo

        viewModel.createPasswordV2(password, confirmPassword)

        observer.onChanged(Fail(Throwable("")))
    }

    @Test
    fun `createPassword v2 - fail`() {
        coEvery { generatePublicKeyUseCase() }.throws(mockThrowable)
        coEvery { addPasswordv2UseCase(any()) }.throws(mockThrowable)

        viewModel.createPasswordV2(password, confirmPassword)

        coVerify { observer.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `Execute validatePassword Success`() {
        password = "12345678"
        viewModel.validatePassword(password)
        /* Then */
        verify {
            validatePasswordObserver.onChanged(Success(""))
        }
    }

    @Test
    fun `Execute validatePassword (Empty password)`() {
        password = ""
        viewModel.validatePassword(password)
        /* Then */
        assertEquals((viewModel.validatePassword.value as Fail).throwable.message, "Harus diisi")
    }

    @Test
    fun `Execute validatePassword (Pass less than 8 char)`() {
        password = "111111"
        viewModel.validatePassword(password)
        /* Then */
        assertEquals((viewModel.validatePassword.value as Fail).throwable.message, "Minimum 8 karakter")
    }

    @Test
    fun `Execute validatePassword (Pass more than 32 char)`() {
        password = "11111111111111111111111111111111111"
        viewModel.validatePassword(password)
        /* Then */
        assertEquals((viewModel.validatePassword.value as Fail).throwable.message, "Maksimum 32 karakter")
    }

    @Test
    fun `Execute confirmValidatePassword Success`() {
        confirmPassword = "12345678"
        viewModel.validatePasswordConfirmation(confirmPassword)
        /* Then */

        verify {
            validateConfirmPasswordObserver.onChanged(Success(""))
        }
    }

    @Test
    fun `Execute confirmValidatePassword (Empty password)`() {
        confirmPassword = ""
        viewModel.validatePasswordConfirmation(confirmPassword)

        /* Then */
        assertEquals((viewModel.validatePasswordConfirmation.value as Fail).throwable.message, "Harus diisi")
    }

    @Test
    fun `Execute confirmValidatePassword (Pass less than 8 char)`() {
        confirmPassword = "111111"
        viewModel.validatePasswordConfirmation(confirmPassword)
        /* Then */
        assertEquals((viewModel.validatePasswordConfirmation.value as Fail).throwable.message, "Minimum 8 karakter")
    }

    @Test
    fun `Execute confirmValidatePassword (Pass more than 32 char)`() {
        confirmPassword = "11111111111111111111111111111111111"
        viewModel.validatePasswordConfirmation(confirmPassword)
        /* Then */
        assertEquals((viewModel.validatePasswordConfirmation.value as Fail).throwable.message, "Maksimum 32 karakter")
    }

    @Test
    fun checkPassword() {
        val profile = ProfileDataModel.Profile(isCreatedPassword = true)
        val mockResponse = ProfileDataModel(profile)

        coEvery { getProfileCompletionUseCase(Unit) } returns mockResponse
        viewModel.checkPassword()

        /* Then */
        coVerify { hasPasswordObserver.onChanged(Success(mockResponse)) }
    }

    @Test
    fun `checkPassword - fail`() {
        coEvery { getProfileCompletionUseCase(Unit) }.throws(mockThrowable)

        viewModel.checkPassword()

        coVerify { hasPasswordObserver.onChanged(Fail(mockThrowable)) }
    }
}
