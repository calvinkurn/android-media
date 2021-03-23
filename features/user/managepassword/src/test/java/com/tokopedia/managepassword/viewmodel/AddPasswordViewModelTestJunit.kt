package com.tokopedia.managepassword.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordData
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordResponseModel
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordUseCase
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordV2UseCase
import com.tokopedia.managepassword.addpassword.view.viewmodel.AddPasswordViewModel
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
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

    val dispatcher = TestCoroutineDispatcher()
    private var observer = mockk<Observer<Result<AddPasswordData>>>(relaxed = true)
    private var validatePasswordObserver = mockk<Observer<Result<String>>>(relaxed = true)
    private var validateConfirmPasswordObserver = mockk<Observer<Result<String>>>(relaxed = true)
    private var hasPasswordObserver = mockk<Observer<Result<ProfileDataModel>>>(relaxed = true)

    lateinit var viewModel: AddPasswordViewModel

    @Before
    fun setUp() {
        viewModel = AddPasswordViewModel(
                addPasswordUseCase,
                addPasswordv2UseCase,
                getProfileCompletionUseCase,
                generatePublicKeyUseCase,
                dispatcher
        )
        viewModel.response.observeForever(observer)
        viewModel.validatePassword.observeForever(validatePasswordObserver)
        viewModel.validatePasswordConfirmation.observeForever(validateConfirmPasswordObserver)
        viewModel.profileDataModel.observeForever(hasPasswordObserver)

    }

    var password = "abc1234567"
    var confirmPassword = "abc1234567"
    val mockThrowable = mockk<Throwable>(relaxed = true)

    @Test
    fun `Execute createPassword`() {
        viewModel.createPassword(password, confirmPassword)
        /* Then */
        verify {
            AddPasswordViewModel.createRequestParams(password, confirmPassword)
            addPasswordUseCase.submit(any(), any())
        }
    }

    @Test
    fun `createPassword Success`() {
        val mockAddPassword = AddPasswordData(isSuccess = true)
        val mockAddPasswordResponse = AddPasswordResponseModel(mockAddPassword)

        every { addPasswordUseCase.submit(any(), any()) } answers {
            firstArg<(AddPasswordResponseModel) -> Unit>().invoke(mockAddPasswordResponse)
        }
        viewModel.createPassword(password, confirmPassword)

        /* Then */
        verify { observer.onChanged(Success(mockAddPasswordResponse.addPassword)) }
    }

    @Test
    fun `createPassword isSuccess = false`() {
        val mockAddPassword = AddPasswordData(isSuccess = false, errorMessage = "Error")
        val mockAddPasswordResponse = AddPasswordResponseModel(mockAddPassword)

        every { addPasswordUseCase.submit(any(), any()) } answers {
            firstArg<(AddPasswordResponseModel) -> Unit>().invoke(mockAddPasswordResponse)
        }
        viewModel.createPassword(password, confirmPassword)

        /* Then */
        assertEquals((viewModel.response.value as Fail).throwable.message, mockAddPasswordResponse.addPassword.errorMessage)
    }

    @Test
    fun `createPassword Error`() {
        /* When */
        every { addPasswordUseCase.submit(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.createPassword(password, confirmPassword)

        /* Then */
        verify { observer.onChanged(Fail(mockThrowable)) }
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
        assertEquals((viewModel.validatePasswordConfirmation.value as Fail).throwable.message, "Minimum 8 karakter") }

    @Test
    fun `Execute confirmValidatePassword (Pass more than 32 char)`() {
        confirmPassword = "11111111111111111111111111111111111"
        viewModel.validatePasswordConfirmation(confirmPassword)
        /* Then */
        assertEquals((viewModel.validatePasswordConfirmation.value as Fail).throwable.message, "Maksimum 32 karakter")
    }

    @Test
    fun `checkPassword Success`() {
        val profile = ProfileDataModel.Profile()
        val mockResponse = ProfileDataModel(profile)

        every { getProfileCompletionUseCase.getData(any(), any()) } answers {
            firstArg<(ProfileDataModel) -> Unit>().invoke(mockResponse)
        }
        viewModel.checkPassword()

        /* Then */
        verify { hasPasswordObserver.onChanged(Success(mockResponse)) }
    }

    @Test
    fun `checkPassword Error`() {
        val throwable = Throwable(message = "Error")

        every { getProfileCompletionUseCase.getData(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.checkPassword()

        /* Then */
        verify { hasPasswordObserver.onChanged(Fail(throwable)) }
    }
 }